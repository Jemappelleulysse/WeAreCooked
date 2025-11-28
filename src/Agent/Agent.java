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

    private int ID;
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
    public Agent(Model model, Recipe recipe, int id) {
        this.ID = id;
        this.model = model;
        model.addPlayer(this.ID);
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
        heldObject = model.getPlayer(this.ID).getObjectHeld();
        //this.currentIngredients = currentIngredients;
    }

    private void reset(Recipe recipe) {
        heldObject = null;
        currentIngredients.clear();
        missingIngredients = new ArrayList<Ingredient>(recipe.getIngredients());
    }

    public void start() {
        this.dostart = !dostart;
        actionsToDo.clear();
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
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
                currentIngredients.clear();
                missingIngredients.clear();
                missingIngredients.addAll(new BolognesePasta().getIngredients());
                return;
            }
            Ingredient nextIngredient = getNextIngredient();
            //System.out.println(",next ingredient is " + nextIngredient);
            if (getFurnitureWithIngredientOn(nextIngredient) != null) {
                System.out.println("OUI + "+nextIngredient);
                goGrab((nextIngredient));
            } else if (nextIngredient == Ingredient.SLICED_TOMATO) {
                goGrab(Ingredient.TOMATO);
            } else if (nextIngredient == Ingredient.COOKED_PASTA) {
                if (checkPotFull() == 2) {
                    goGrab(Ingredient.PASTA);
                } else if (checkPotFull() == 1) {

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
                model.move(action, this.ID);
            refreshHand();
        }
    }

    /// ///////////////////// ///
    /// PLAYER ACTION METHODS ///
    /// ///////////////////// ///

    public void goGrab(HoldableObject object) {
        Furniture furniture = getFurnitureWithIngredientOn(object);
        Pair destination = null;
        if (furniture == null) {
            for (Furniture fur : model.furnitures) {
                if (fur.getClass() == IngredientChest.class && ((IngredientChest) fur).getIngredient().equals(object)) {
                    furniture = fur;
                }
            }
        }

        destination = nextEmptyCase(new Pair(furniture.getPosX(),furniture.getPosY()));

        if (destination == null) {
            return;
        }

        ArrayList<Pair> actions = Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(this.ID).getPos(), destination, model.board, new int[8][8])));
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
            if (Pair.getTableau(new Pair(posAutour[i]),model.board) == -1 || Pair.getTableau(new Pair(posAutour[i]),model.board) == 0) {
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
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(this.ID).getPos(), destination, model.board, new int[8][8])));;
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
        destination = nextEmptyCase(new Pair(workSurface.getPosX(),workSurface.getPosY()));
        //System.out.println("\n-----------------------------\n"+workSurface.getPosX() +" " + workSurface.getPosY()+"\n---------------------------------");
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(this.ID).getPos(), destination, model.board, new int[8][8])));
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
        ArrayList<Pair> actions =  Pair.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(this.ID).getPos(), destination, model.board, new int[8][8])));
        actionsToDo.addAll(actions);
        actionsToDo.add(destination.sub(new Pair(workSurface.getPosX(),workSurface.getPosY())));


        currentIngredients.add((Ingredient) heldObject);
        missingIngredients.remove(heldObject);


        refreshHand();
    }
}
