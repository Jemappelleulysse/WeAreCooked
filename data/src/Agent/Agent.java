package Agent;

import Furnitures.*;
import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;
import HoldableObjects.KitchenUstensils;
import Recipes.BolognesePasta;
import Recipes.Recipe;
import Utils.Pair;
import Model.Model;

import java.util.ArrayList;
import java.util.Objects;

import static Utils.Util.pathFinding;


public class Agent {

    private Model model;
    private HoldableObject heldObject = null;
    private final ArrayList<Ingredient> currentIngredients = new ArrayList<Ingredient>();
    private ArrayList<Ingredient> missingIngredients;
    private ArrayList<Pair> actionsToDo;
    private float timeBeforeNextAction = 0f;
    private float timeBetweenActions = 0.07f;
    private boolean dostart = false;

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
        return (heldObject == null);
    }

    private boolean isRecipeFinished() {
        return (missingIngredients.isEmpty());
    }

    private boolean isHoldingNeededIngredient() {
        return (missingIngredients.contains(heldObject));
    }

    private Ingredient getNextIngredient() {
        return missingIngredients.getFirst();
    }

    /// /////// ///
    /// SETTERS ///
    /// /////// ///
    private void refreshHand() {
        heldObject = model.player.getObjectHeld();
        //this.currentIngredients = currentIngredients;
    }

    private void reset(Recipe recipe) {
        heldObject = null;
        currentIngredients.clear();
        missingIngredients = new ArrayList<Ingredient>(recipe.getIngredients());
    }

    public void start() {
        this.dostart = !dostart;
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
    private Furniture getFurnitureWithIngredientOn(Ingredient ingredient) {
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getIngredientOn() == ingredient ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getIngredientOn() == ingredient ||
                    furniture.getClass() == GasStove.class && ((GasStove)furniture).getPot() == ingredient ||
                    (furniture.getClass() == GasStove.class && ((GasStove)furniture).getIngredientOn() == ingredient && ingredient!= Ingredient.PASTA)
                    ) {
                return furniture;
            }
        }
        return null;
    }

    private int checkPotFull() {
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getPot() == Ingredient.FULL_POT && ((GasStove) furniture).getIngredientOn() == null ) {
                return 2;
            } else if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getIngredientOn() == Ingredient.FULL_POT ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getIngredientOn() == Ingredient.FULL_POT) {
                return 1;
            }
        }
        return 0;
    }

    public void doNextAction() {
        if (isPlayerHandEmpty()) {
            if (isRecipeFinished()) {

                goValidateRecipe();
                currentIngredients.clear();
                missingIngredients.clear();
                missingIngredients.addAll(new BolognesePasta().getIngredients());
                return;
            }
            Ingredient nextIngredient = getNextIngredient();
            //System.out.println(",next ingredient is " + nextIngredient);
            if (nextIngredient == Ingredient.SLICED_TOMATO) {
                goGrab(Ingredient.TOMATO);
            } else if (nextIngredient == Ingredient.COOKED_PASTA) {
                if (getFurnitureWithIngredientOn(Ingredient.COOKED_PASTA) != null) {
                    goGrab(Ingredient.COOKED_PASTA);
                    //System.out.println("Je suis un Bozo1");

                } else if (checkPotFull() == 2) {
                    //System.out.println("Je suis un Bozo2");

                    goGrab(Ingredient.PASTA);
                } else if (checkPotFull() == 1) {
                    //System.out.println("Je suis un Bozo3");

                    goGrab(KitchenUstensils.FULL_POT);
                } else if (getFurnitureWithIngredientOn(Ingredient.EMPTY_POT) != null){
                    goGrab(KitchenUstensils.EMPTY_POT);
                    //System.out.println("Je suis un Bozo4");
                } else {
                    //System.out.println("Je suis un Bozo5");
                    actionsToDo.add(new Pair(0,0));
                }

            }
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
        if(!dostart) return;
        //if (true) return;
        timeBeforeNextAction -= dt;
        //System.out.println(dt);
        if (timeBeforeNextAction <=0) {
            timeBeforeNextAction = timeBetweenActions;
            if (actionsToDo.isEmpty()) {
                doNextAction();
            }
            Pair action = actionsToDo.getFirst();
            actionsToDo.removeFirst();
            if (!(action.i ==0  && action.j == 0))
                model.move(action);
            refreshHand();
        }
    }

    /// ///////////////////// ///
    /// PLAYER ACTION METHODS ///
    /// ///////////////////// ///

    public void goGrab(Ingredient ingredient) {
        Furniture furniture = getFurnitureWithIngredientOn(ingredient);
        Pair destination = null;
        if (furniture == null) {
            for (Furniture fur : model.furnitures) {
                if (fur.getClass() == IngredientChest.class && ((IngredientChest) fur).getIngredient().equals(ingredient)) {
                    furniture = fur;
                }
            }
        }

        destination = nextEmptyCase(new Pair(furniture.getPosX(),furniture.getPosY()));

        if (destination == null) {
            return;
        }

        ArrayList<Pair> actions = Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(furniture.getPosX(),furniture.getPosY())));
        refreshHand();
    }

    public Pair nextEmptyCase(Pair pos) {
        Pair[] posAutour = new Pair[4];
        for (int i = 0; i < posAutour.length; i++) {
            int k = (i == 0) ? 1 : ((i == 1) ? -1 : 0);
            int l = (i == 2) ? 1 : ((i == 3) ? -1 : 0);
            posAutour[i] = new Pair(pos.i + k, pos.j + l);
        }
        Pair destination = null;
        for (int i = 0; i < posAutour.length; i++) {
            if (posAutour[i].i < 0 || posAutour[i].i >= model.board.length || posAutour[i].j < 0 || posAutour[i].j >= model.board.length) {
                continue;
            }
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
            return;
        }
        destination = nextEmptyCase(new Pair(workSurface.getPosX(),workSurface.getPosY()));

        if (destination == null) {
            actionsToDo.add(new Pair(0,-1));
            return;
        }
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));;
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));


        // Aller valider la recette

        //Recipe nextRecipe = world.getNextRecipe();
        //reset(nextRecipe);
    }

    public void goPrepareHeldIngredient() {
        Pair destination = null;
        Furniture workSurface = null;
        switch (heldObject) {
            case Ingredient.TOMATO :
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getIngredientOn() == null) {
                        workSurface = furniture;
                        break;
                    }
                }

                break;
            case Ingredient.PASTA:
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getIngredientOn() == null) {
                        workSurface = furniture;
                        break;
                    }
                }
                break;
            case Ingredient.EMPTY_POT:
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == Sink.class) {
                        workSurface = furniture;
                        break;
                    }
                }
                break;
            case Ingredient.FULL_POT:
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getPot() == null) {
                        workSurface = furniture;
                        break;
                    }
                }
                break;
            default :
                //System.out.println("Pas d'ingredient dans la main");

        }
        if (workSurface == null) {
            //System.out.print("PAS DE PLANCHE VIDE TROUVE");
            return;
        }
        destination = nextEmptyCase(new Pair(workSurface.getPosX(),workSurface.getPosY()));
        //System.out.println("\n-----------------------------\n"+workSurface.getPosX() +" " + workSurface.getPosY()+"\n---------------------------------");
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        if (workSurface.getClass() == CuttingBoard.class) {
            actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));
            actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));
            actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));
            actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));

        }
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
            return;
        }
        destination = nextEmptyCase(new Pair(workSurface.getPosX(),workSurface.getPosY()));

        if (destination == null) {
            return;
        }
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));


        currentIngredients.add((Ingredient) heldObject);
        missingIngredients.remove(heldObject);


        refreshHand();
    }
}
