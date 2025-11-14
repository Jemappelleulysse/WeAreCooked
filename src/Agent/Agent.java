package Agent;

import Furnitures.*;
import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;
import HoldableObjects.KitchenUstensils;
import Recipes.BolognesePasta;
import Recipes.Recipe;
import Utils.Vec2;
import Model.Model;
import Utils.Vec2;

import java.util.ArrayList;
import java.util.Objects;

import static Utils.Util.pathFinding;


public class Agent {

    private final Model model;
    private final int id;

    private final ArrayList<Vec2> nextMoves =  new ArrayList<>();

    private float timeBeforeNextAction = 0f;
    private final float timeBetweenActions;

    private HoldableObject heldObject = null;
    private final ArrayList<Ingredient> currentIngredients = new ArrayList<>();
    private ArrayList<Ingredient> missingIngredients;

    /// //////////// ///
    /// CONSTRUCTORS ///
    /// //////////// ///

    public Agent(Model model, int id) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null.");
        }
        this.model = model;

        if (id < 0) {
            throw new IllegalArgumentException("Agent id cannot be negative.");
        }
        this.id = id;

        this.timeBetweenActions = 0.1f;
    }

    public Agent(Model model, int id, float timeBetweenActions) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null.");
        }
        this.model = model;

        if (id < 0) {
            throw new IllegalArgumentException("Agent id cannot be negative.");
        }
        this.id = id;

        if (timeBetweenActions <= 0f) {
            throw new IllegalArgumentException("Agent time cannot be negative or null.");
        }
        this.timeBetweenActions = timeBetweenActions;
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


    /// /////// ///
    /// METHODS ///
    /// /////// ///

    public void update(float dt) {

        timeBeforeNextAction -= dt;

        // Si le temps entre chaque action est écoulé
        if (timeBeforeNextAction <=0) {
            timeBeforeNextAction = timeBetweenActions;

            // S'il n'y a plus de mouvement à faire → détermination de la prochaine action logique et des prochains mouvements
            if (nextMoves.isEmpty()) {
                doNextAction();
            }

            Vec2 nextMove = nextMoves.removeFirst();
            if (nextMove.isNotNull())
                model.move(nextMove);
            refreshHand();
        }
    }

    private Furniture getFurnitureWithIngredientOn(HoldableObject neededObject) {
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == neededObject ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getObjectOn() == neededObject ||
                    furniture.getClass() == GasStove.class && ((GasStove)furniture).getPot() == neededObject ||
                    (furniture.getClass() == GasStove.class && ((GasStove)furniture).getIngredientInPot() == neededObject && neededObject!= Ingredient.PASTA)
                    ) {
                return furniture;
            }
        }
        return null;
    }

    private int checkPotFull() {
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getPot() == KitchenUstensils.FULL_POT && ((GasStove) furniture).getIngredientInPot() == null ) {
                return 2;
            } else if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == KitchenUstensils.FULL_POT ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getObjectOn() == KitchenUstensils.FULL_POT) {
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
                } else if (getFurnitureWithIngredientOn(KitchenUstensils.EMPTY_POT) != null){
                    goGrab(KitchenUstensils.EMPTY_POT);
                    //System.out.println("Je suis un Bozo4");
                } else {
                    //System.out.println("Je suis un Bozo5");
                    nextMoves.add(new Vec2(0,0));
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


    /// ///////////////////// ///
    /// PLAYER ACTION METHODS ///
    /// ///////////////////// ///

    public void goGrab(HoldableObject object) {
        Furniture furniture = getFurnitureWithIngredientOn(object);
        Vec2 destination = null;
        if (furniture == null) {
            for (Furniture fur : model.furnitures) {
                if (fur.getClass() == IngredientChest.class && ((IngredientChest) fur).getIngredient().equals(object)) {
                    furniture = fur;
                }
            }
        }

        destination = nextEmptyCase(new Vec2(furniture.getPosX(),furniture.getPosY()));

        if (destination == null) {
            return;
        }

        ArrayList<Vec2> actions = Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        nextMoves.addAll(actions);
        nextMoves.add(destination.sub(new Vec2(furniture.getPosX(),furniture.getPosY())));
        refreshHand();
    }

    public Vec2 nextEmptyCase(Vec2 pos) {
        Vec2[] posAutour = new Vec2[4];
        for (int i = 0; i < posAutour.length; i++) {
            int k = (i == 0) ? 1 : ((i == 1) ? -1 : 0);
            int l = (i == 2) ? 1 : ((i == 3) ? -1 : 0);
            posAutour[i] = new Vec2(pos.i + k, pos.j + l);
        }
        Vec2 destination = null;
        for (int i = 0; i < posAutour.length; i++) {
            if (posAutour[i].i < 0 || posAutour[i].i >= model.board.length || posAutour[i].j < 0 || posAutour[i].j >= model.board.length) {
                continue;
            }
            if (Vec2.getTableau(new Vec2(posAutour[i]),model.board) == -1) {
                destination = new Vec2(posAutour[i]);
                break;
            }
        }
        return destination;
    }

    public void goValidateRecipe() {
        Vec2 destination = null;
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
        destination = nextEmptyCase(new Vec2(workSurface.getPosX(),workSurface.getPosY()));

        if (destination == null) {
            nextMoves.add(new Vec2(0,-1));
            return;
        }
        ArrayList<Vec2> actions =  Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));;
        nextMoves.addAll(actions);
        nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));
        nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));


        // Aller valider la recette

        //Recipe nextRecipe = world.getNextRecipe();
        //reset(nextRecipe);
    }

    public void goPrepareHeldIngredient() {
        Vec2 destination = null;
        Furniture workSurface = null;
        switch (heldObject) {
            case Ingredient.TOMATO :
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == null) {
                        workSurface = furniture;
                        break;
                    }
                }

                break;
            case Ingredient.PASTA:
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getIngredientInPot() == null) {
                        workSurface = furniture;
                        break;
                    }
                }
                break;
            case KitchenUstensils.EMPTY_POT:
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == Sink.class) {
                        workSurface = furniture;
                        break;
                    }
                }
                break;
            case KitchenUstensils.FULL_POT:
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
        destination = nextEmptyCase(new Vec2(workSurface.getPosX(),workSurface.getPosY()));
        //System.out.println("\n-----------------------------\n"+workSurface.getPosX() +" " + workSurface.getPosY()+"\n---------------------------------");
        ArrayList<Vec2> actions =  Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        nextMoves.addAll(actions);
        if (workSurface.getClass() == CuttingBoard.class) {
            nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));
            nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));
            nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));
            nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));

        }
        nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));


        refreshHand();
    }

    public void goPlaceHeldIngredientOnPlate() {
        // Place l'ingrédient tenu sur l'assiette du comptoir
        Vec2 destination = null;
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
        destination = nextEmptyCase(new Vec2(workSurface.getPosX(),workSurface.getPosY()));

        if (destination == null) {
            return;
        }
        ArrayList<Vec2> actions =  Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.player.getPos(), destination, model.board, new int[8][8])));
        nextMoves.addAll(actions);
        nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));


        currentIngredients.add((Ingredient) heldObject);
        missingIngredients.remove(heldObject);


        refreshHand();
    }
}
