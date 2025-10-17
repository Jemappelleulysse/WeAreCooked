package Agent;

import Furnitures.*;
import Ingredient.Ingredient;
import Recipes.Recipe;
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
        IngredientChest chest = null;
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == IngredientChest.class && ((IngredientChest) furniture).getIngredient().equals(ingredient)) {
                chest = (IngredientChest) furniture;
            }
        }
        if (chest == null) {
            System.out.print("PAS DE furniture A " + ingredient + "TROUVE");
            return;
        }
        Pair destination = nextEmptyCase(new Pair(chest.getPosX(),chest.getPosY()));
        if (destination == null) {
            System.out.print("PAS D'EMPLACEMENT VIDE TROUVE");
            return;
        }

        ArrayList<Pair> actions = Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(chest.getPosX(),chest.getPosY())));

        refreshHand();
    }

    public Pair nextEmptyCase(Pair pos) {
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
        Furniture workSurface = null;
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == Counter.class) {
                workSurface = furniture;
                break;
            }
        }
        if (workSurface == null) {
            System.out.print("PAS DE COMPTOIR TROUVE");
            return;
        }
        destination = nextEmptyCase(new Pair(workSurface.getPosX(),workSurface.getPosY()));

        if (destination == null) {
            System.out.print("PAS D'EMPLACEMENT VIDE TROUVE");
            return;
        }
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));;
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));


        // Aller valider la recette

        //Recipe nextRecipe = world.getNextRecipe();
        //reset(nextRecipe);
    }

    public void goPrepareHeldIngredient() {
        Pair destination = null;
        Furniture workSurface = null;
        switch (heldIngredient) {
            case Ingredient.TOMATO :
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getIngredientOn() == null) {
                        workSurface = furniture;
                        break;
                    }
                }
                if (workSurface == null) {
                    System.out.print("PAS DE PLANCHE VIDE TROUVE");
                    return;
                }
                destination = nextEmptyCase(new Pair(workSurface.getPosX(),workSurface.getPosY()));
                break;
            default :
                System.out.println("Pas d'ingredient dans la main");

        }
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));

        refreshHand();
    }

    public void goPlaceHeldIngredientOnPlate() {
        // Place l'ingrédient tenu sur l'assiette du comptoir
        Pair destination = null;
        Furniture workSurface = null;
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == Counter.class) {
                workSurface = furniture;
                break;
            }
        }
        if (workSurface == null) {
            System.out.print("PAS DE COMPTOIR TROUVE");
            return;
        }
        System.out.println("\n-----------------------------\n"+workSurface.getPosX() +" " + workSurface.getPosY()+"\n---------------------------------");
        destination = nextEmptyCase(new Pair(workSurface.getPosX(),workSurface.getPosY()));

        if (destination == null) {
            System.out.print("PAS D'EMPLACEMENT VIDE TROUVE");
            return;
        }
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));


        currentIngredients.add(heldIngredient);
        missingIngredients.remove(heldIngredient);


        refreshHand();
    }
}
