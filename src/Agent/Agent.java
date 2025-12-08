package Agent;

import Furnitures.*;
import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;
import HoldableObjects.KitchenUstensils;
import Utils.Vec2;
import Model.Model;

import java.util.ArrayList;
import java.util.Objects;

import static Utils.Util.pathFinding;


public class Agent {

    private final Model model;
    private final int id;

    private final ArrayList<Vec2> nextMoves =  new ArrayList<>();

    private float timeBeforeNextAction = 0f;
    private final float timeBetweenActions;


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
        return (!model.getPlayer(id).isHoldingSomething());
    }

    private boolean isRecipeFinished() {
        return (model.isRecipeFinished());
    }

    private boolean isHoldingNeededIngredient() {
        HoldableObject heldObject = model.getPlayer(id).getObjectHeld();

        if (heldObject != null) {
            if (heldObject.getClass() == Ingredient.class) {
                if (model.getRecipeIngredients(0).contains(heldObject)) {
                    return (!model.getValidIngredients().contains(heldObject));
                }
            }
        }
        return false;
    }

    private Ingredient getNextIngredient() {
        Ingredient nextIngredient = null;

        for (Ingredient ingredient : model.getRecipeIngredients(0)) {
            if (!model.getValidIngredients().contains(ingredient)) {
                nextIngredient = ingredient;
            }
        }

        return nextIngredient;
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

            // On demande au model de bouger le joueur
            Vec2 nextMove = nextMoves.removeFirst();
            if (nextMove.isNotNull()) {
                model.movePlayer(id, nextMove);
            }
        }
    }

    private Furniture getFurnitureWithIngredientOn(HoldableObject neededObject) {
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == neededObject ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getObjectOn() == neededObject ||
                    (furniture.getClass() == GasStove.class && ((GasStove)furniture).getPot() == neededObject && ((GasStove)furniture).getIngredientInPot() == null) ||
                    (furniture.getClass() == GasStove.class && ((GasStove)furniture).getIngredientInPot() == neededObject && (!(neededObject== Ingredient.PASTA || neededObject == Ingredient.SLICED_POTATO || neededObject == Ingredient.RAW_MEAT))
            ))
                    {
                return furniture;
            }
        }
        return null;
    }

    private int checkEmptyPot() {
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == GasStove.class && ((GasStove)furniture).getPot() == KitchenUstensils.EMPTY_POT && ((GasStove)furniture).getIngredientInPot() == null) {
                System.out.print("BOZO");
                return 2;
            }
        }
        return 0;
    }

    private int checkPotFull() {
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getPot() == KitchenUstensils.WATER_POT && ((GasStove) furniture).getIngredientInPot() == null ) {
                return 2;
            } else if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == KitchenUstensils.WATER_POT ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getObjectOn() == KitchenUstensils.WATER_POT) {
                return 1;
            }
        }
        return 0;
    }
    private int checkPotFullOIL() {
        for (Furniture furniture : model.furnitures) {
            if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getPot() == KitchenUstensils.OIL_POT && ((GasStove) furniture).getIngredientInPot() == null ) {
                return 2;
            } else if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == KitchenUstensils.OIL_POT ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getObjectOn() == KitchenUstensils.OIL_POT) {
                return 1;
            }
        }
        return 0;
    }

    public void doNextAction() {
        if (isPlayerHandEmpty()) {
            if (isRecipeFinished()) {
                goValidateRecipe();
                return;
            }
            Ingredient nextIngredient = getNextIngredient();

            if (nextIngredient == Ingredient.SLICED_TOMATO) {
            //System.out.println(",next ingredient is " + nextIngredient);
            if (getFurnitureWithIngredientOn(nextIngredient) != null) {
                System.out.println("OUI + "+nextIngredient);
                goGrab((nextIngredient));
            } else if (nextIngredient == Ingredient.SLICED_TOMATO) {
                goGrab(Ingredient.TOMATO);
            } else if (nextIngredient == Ingredient.COOKED_PASTA) {
                if (getFurnitureWithIngredientOn(Ingredient.COOKED_PASTA) != null) {
                    goGrab(Ingredient.COOKED_PASTA);
                } else if (checkPotFull() == 2) {
                if (checkPotFull() == 2) {
                    goGrab(Ingredient.PASTA);
                } else if (checkPotFull() == 1) {
                    goGrab(KitchenUstensils.FULL_POT);
                } else if (getFurnitureWithIngredientOn(KitchenUstensils.EMPTY_POT) != null){

                    goGrab(KitchenUstensils.WATER_POT);
                } else if (getFurnitureWithIngredientOn(KitchenUstensils.EMPTY_POT) != null){
                    goGrab(KitchenUstensils.EMPTY_POT);
                } else {
                    actionsToDo.add(new Pair(0,0));
                }
            } else if (nextIngredient == Ingredient.WASHED_SALAD) {
                goGrab(Ingredient.SALAD);
            } else if (nextIngredient == Ingredient.SLICED_BREAD) {
                goGrab(Ingredient.BREAD);
            } else if (nextIngredient == Ingredient.COOKED_MEAT) {



                if (checkEmptyPot() ==2) {
                    goGrab(Ingredient.RAW_MEAT);
                } else if (getFurnitureWithIngredientOn(KitchenUstensils.EMPTY_POT) != null) {
                    goGrab(KitchenUstensils.EMPTY_POT);
                } else {
                    nextMoves.add(new Vec2(0,0));
                    actionsToDo.add(new Pair(0,0));
                }


            } else if (nextIngredient == Ingredient.COOKED_POTATO) {
                System.out.println(checkPotFull());
                if (checkPotFull() == 2) {
                    if (getFurnitureWithIngredientOn(Ingredient.SLICED_POTATO) != null) {
                        System.out.println("hein?");
                        goGrab(Ingredient.SLICED_POTATO);
                    } else {
                        goGrab(Ingredient.POTATO);
                    }
                } else if (checkPotFull() == 1) {
                    goGrab((KitchenUstensils.WATER_POT));
                } else if (getFurnitureWithIngredientOn(KitchenUstensils.EMPTY_POT) != null) {
                    goGrab(KitchenUstensils.EMPTY_POT);
                } else {
                    actionsToDo.add(new Pair(0,0));
                }
            } else if (nextIngredient == Ingredient.FRIED_POTATO) {
                if (checkPotFullOIL() == 2) {
                    if (getFurnitureWithIngredientOn(Ingredient.SLICED_POTATO) != null) {
                        goGrab(Ingredient.SLICED_POTATO);
                    } else {
                        goGrab(Ingredient.POTATO);
                    }
                } else if (checkPotFull() == 1) {
                    goGrab((KitchenUstensils.OIL_POT));
                } else if (getFurnitureWithIngredientOn(KitchenUstensils.EMPTY_POT) != null) {
                    goGrab(KitchenUstensils.EMPTY_POT);
                } else {
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

        if (furniture != null) {
            Vec2 furniturePos = new Vec2(furniture.getPosX(),furniture.getPosY());
            destination = nextEmptyCase(furniturePos);
        } else {
            return;
        }

        ArrayList<Vec2> actions = Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(id).getPos(), destination, model.board, new int[8][8])));
        nextMoves.addAll(actions);
        nextMoves.add(destination.sub(new Vec2(furniture.getPosX(),furniture.getPosY())));
    }

    public Vec2 nextEmptyCase(Vec2 pos) {
        Vec2[] posAutour = new Vec2[4];
        for (int i = 0; i < posAutour.length; i++) {
            int k = (i == 0) ? 1 : ((i == 1) ? -1 : 0);
            int l = (i == 2) ? 1 : ((i == 3) ? -1 : 0);
            posAutour[i] = new Vec2(pos.getX() + k, pos.getY() + l);
        }
        Vec2 destination = null;
        for (int i = 0; i < posAutour.length; i++) {
            if (posAutour[i].getX() < 0 || posAutour[i].getX() >= model.board.length || posAutour[i].getY() < 0 || posAutour[i].getY() >= model.board.length) {
                continue;
            }
            if (Vec2.getTableau(new Vec2(posAutour[i]),model.board) == -1) {
                destination = new Vec2(posAutour[i]);
            if (Pair.getTableau(new Pair(posAutour[i]),model.board) == -1 || Pair.getTableau(new Pair(posAutour[i]),model.board) == 0) {
                destination = new Pair(posAutour[i]);
                break;
            }
        }
        return destination;
    }

    public void goValidateRecipe() {
        Vec2 destination;
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
        ArrayList<Vec2> actions =  Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(id).getPos(), destination, model.board, new int[8][8])));;
        nextMoves.addAll(actions);
        nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));
        nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));
    }

    public void goPrepareHeldIngredient() {
        Vec2 destination = null;
        Furniture workSurface = null;
        HoldableObject heldObject = model.getPlayer(id).getObjectHeld();

        switch (heldObject) {
            case Ingredient.TOMATO, Ingredient.POTATO, Ingredient.BREAD:
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == null) {
                        workSurface = furniture;
                        break;
                    }
                }

                break;
            case Ingredient.PASTA, Ingredient.RAW_MEAT, Ingredient.SLICED_POTATO:
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getIngredientInPot() == null) {
                        workSurface = furniture;
                        break;
                    }
                }
                break;
            case Ingredient.SALAD:
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == Sink.class) {
                        workSurface = furniture;
                        break;
                    }
                }
                break;
            case KitchenUstensils.EMPTY_POT:
                boolean b = (getNextIngredient() == Ingredient.FRIED_POTATO);
                for (Furniture furniture : model.furnitures) {
                    if (b && furniture.getClass() == OilSink.class) {
                        workSurface = furniture;
                        break;
                    } else if (!b && furniture.getClass() == Sink.class) {
                        workSurface = furniture;
                        break;
                    }
                }
                break;
            case KitchenUstensils.WATER_POT, KitchenUstensils.OIL_POT:
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
        ArrayList<Vec2> actions = Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(id).getPos(), destination, model.board, new int[8][8])));

        nextMoves.addAll(actions);
        Vec2 useFurniture = destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY()));
        if (workSurface.getClass() == CuttingBoard.class) {
            nextMoves.add(useFurniture);
            nextMoves.add(useFurniture);

            nextMoves.add(useFurniture);

            nextMoves.add(useFurniture);


        }
        nextMoves.add(useFurniture);
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
        ArrayList<Vec2> actions =  Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(id).getPos(), destination, model.board, new int[8][8])));
        nextMoves.addAll(actions);
        nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));

        // TODO: update la liste des ingrédients valides
    }
}
