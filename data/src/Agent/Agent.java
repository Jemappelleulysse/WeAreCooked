package Agent;

import Ingredient.Ingredient;
import Meuble.*;
import Recipe.Recipe;
import Utils.Pair;
import Model.Model;


import java.util.ArrayList;
import java.util.Objects;

import static Utils.Util.pathFinding;

public class Agent {

    private Model model;
    private Ingredient heldIngredient = null;
    private final ArrayList<Ingredient> currentIngredients = new ArrayList<Ingredient>();
    private ArrayList<Ingredient> missingIngredients;
    private ArrayList<Pair> actionsToDo;
    private float timeBeforeNextAction = 0;
    private float timeBetweenActions = 0;

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public Agent(Model model, Recipe recipe) {
        this.model = model;
        this.actionsToDo = new ArrayList<>();
        this.missingIngredients = new ArrayList<Ingredient>(recipe.getIngredients());
    }

    /// /////// ///
    /// GETTERS ///
    /// /////// ///
    private boolean isPlayerHandEmpty() {
        return (heldIngredient == null);
    }

    private boolean isRecipeFinished() {
        return (missingIngredients.isEmpty());
    }

    private boolean isHoldingNeededIngredient() {
        return (missingIngredients.contains(heldIngredient));
    }

    private Ingredient getNextIngredient() {
        return missingIngredients.getFirst();
    }


    /// /////// ///
    /// SETTERS ///
    /// /////// ///
    private void refreshHand() {
        heldIngredient = model.player.getIngredientHeld();
        //this.currentIngredients = currentIngredients;
    }

    private void reset(Recipe recipe) {
        heldIngredient = null;
        currentIngredients.clear();
        missingIngredients = new ArrayList<Ingredient>(recipe.getIngredients());
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
    public void doNextAction() {
        if (isPlayerHandEmpty()) {
            if (isRecipeFinished()) {
                goValidateRecipe();
            }
            goGrab(getNextIngredient());
        }
        else {
            if(isHoldingNeededIngredient()) {
                goPlaceHeldIngredientOnPlate();
            }
            else {
                goPrepareHeldIngredient();
            }
        }
    }

    public void update(float dt) {
        if (true) return;

        if (actionsToDo.isEmpty()) {
            System.out.println("oui");
            doNextAction();
        } else {
            timeBeforeNextAction -= dt;
            if (timeBeforeNextAction <= 0) {
                timeBeforeNextAction += timeBetweenActions;
                Pair action = actionsToDo.getFirst();
                actionsToDo.removeFirst();
                model.move(action);
            }
        }
        return ;
    }

    /// ///////////////////// ///
    /// PLAYER ACTION METHODS ///
    /// ///////////////////// ///

    public void goGrab(Ingredient ingredient) {
        Coffre coffreIng = null;
        for (Meuble meuble : model.meubles) {
            if (meuble.getClass() == Coffre.class && ((Coffre) meuble).getIngredient().equals(ingredient)) {
                coffreIng = (Coffre) meuble;
            }
        }
        if (coffreIng == null) {
            System.out.print("PAS DE MEUBLE A " + ingredient + "TROUVE");
            return;
        }
        Pair destination = videProcheMeuble(new Pair(coffreIng.getPosX(),coffreIng.getPosY()));
        if (destination == null) {
            System.out.print("PAS D'EMPLACEMENT VIDE TROUVE");
            return;
        }

        ArrayList<Pair> actions = Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(coffreIng.getPosX(),coffreIng.getPosY())));

        refreshHand();
    }

    public Pair videProcheMeuble(Pair pos) {
        Pair[] posAutour = new Pair[4];
        for (int i = 0; i < posAutour.length; i++) {
            int k = (i == 0) ? 1 : ((i == 1) ? -1 : 0);
            int l = (i == 2) ? 1 : ((i == 3) ? -1 : 0);
            posAutour[i] = new Pair(pos.i + k, pos.j + l);
            System.out.println(posAutour[i]);
        }
        Pair destination = null;
        for (int i = 0; i < posAutour.length; i++) {
            if (posAutour[i].i < 0 || posAutour[i].i >= model.board.length || posAutour[i].j < 0 || posAutour[i].j >= model.board.length) {
                continue;
            }
            System.out.println(posAutour[i].i + " | " + posAutour[i].j + " [" + Pair.getTableau(new Pair(posAutour[i]),model.board)+"]");
            if (Pair.getTableau(new Pair(posAutour[i]),model.board) == -1) {
                destination = new Pair(posAutour[i]);
                break;
            }
        }
        return destination;
    }

    public void goValidateRecipe() {
        Pair destination = null;
        Meuble meubleTravail = null;
        for (Meuble meuble : model.meubles) {
            if (meuble.getClass() == Comptoir.class) {
                meubleTravail = meuble;
                break;
            }
        }
        if (meubleTravail == null) {
            System.out.print("PAS DE COMPTOIR TROUVE");
            return;
        }
        destination = videProcheMeuble(new Pair(meubleTravail.getPosX(),meubleTravail.getPosY()));

        if (destination == null) {
            System.out.print("PAS D'EMPLACEMENT VIDE TROUVE");
            return;
        }
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));;
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(meubleTravail.getPosX(),meubleTravail.getPosY())));


        // Aller valider la recette

        //Recipe nextRecipe = world.getNextRecipe();
        //reset(nextRecipe);
    }

    public void goPrepareHeldIngredient() {
        Pair destination = null;
        Meuble meubleTravail = null;
        switch (heldIngredient) {
            case Ingredient.TOMATE :
                for (Meuble meuble : model.meubles) {
                    if (meuble.getClass() == PlancheADecoupe.class && ((PlancheADecoupe) meuble).getIngredientOn() == null) {
                        meubleTravail = meuble;
                        break;
                    }
                }
                if (meubleTravail == null) {
                    System.out.print("PAS DE PLANCHE VIDE TROUVE");
                    return;
                }
                destination = videProcheMeuble(new Pair(meubleTravail.getPosX(),meubleTravail.getPosY()));
                break;
            default :
                System.out.println("Pas d'ingredient dans la main");

        }
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(meubleTravail.getPosX(),meubleTravail.getPosY())));
        actionsToDo.add(destination.sub(new Pair(meubleTravail.getPosX(),meubleTravail.getPosY())));
        actionsToDo.add(destination.sub(new Pair(meubleTravail.getPosX(),meubleTravail.getPosY())));

        refreshHand();
    }

    public void goPlaceHeldIngredientOnPlate() {
        // Place l'ingrédient tenu sur l'assiette du comptoir
        Pair destination = null;
        Meuble meubleTravail = null;
        for (Meuble meuble : model.meubles) {
            if (meuble.getClass() == Comptoir.class) {
                meubleTravail = meuble;
                break;
            }
        }
        if (meubleTravail == null) {
            System.out.print("PAS DE COMPTOIR TROUVE");
            return;
        }
        System.out.println("\n-----------------------------\n"+meubleTravail.getPosX() +" " + meubleTravail.getPosY()+"\n---------------------------------");
        destination = videProcheMeuble(new Pair(meubleTravail.getPosX(),meubleTravail.getPosY()));

        if (destination == null) {
            System.out.print("PAS D'EMPLACEMENT VIDE TROUVE");
            return;
        }
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(meubleTravail.getPosX(),meubleTravail.getPosY())));


        currentIngredients.add(heldIngredient);
        missingIngredients.remove(heldIngredient);


        refreshHand();
    }
}
