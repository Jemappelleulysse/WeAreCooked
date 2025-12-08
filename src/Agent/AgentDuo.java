package Agent;

import Furnitures.*;
import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;
import HoldableObjects.KitchenUstensils;
import Model.Model;
import Player.Player;
import Utils.Util;
import Utils.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static Utils.Util.pathFinding;

public class AgentDuo {

    private final Model model;
    private final int id;
    private AgentDuo mate;

    private AgentState state = AgentState.WAITING_TO_START;
    private int preparingIngredientId = -1;
    private final ArrayList<Vec2> nextMoves =  new ArrayList<>();
    private int recipeId = 0;

    private float timeBeforeNextAction = 0f;
    private final float timeBetweenActions;


    /// //////////// ///
    /// CONSTRUCTORS ///
    /// //////////// ///

    public AgentDuo(Model model, int id) {
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

    public AgentDuo(Model model, int id, AgentDuo mate, float timeBetweenActions) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null.");
        }
        this.model = model;

        if (id < 0) {
            throw new IllegalArgumentException("Agent id cannot be negative.");
        }
        this.id = id;

        if (mate == null) {
            throw new IllegalArgumentException("Mate cannot be null.");
        }
        this.mate = mate;

        if (timeBetweenActions <= 0f) {
            throw new IllegalArgumentException("Agent time cannot be negative or null.");
        }
        this.timeBetweenActions = timeBetweenActions;
    }

    /// /////// ///
    /// SETTERS ///
    /// /////// ///

    public void setMate(AgentDuo mate) {
        if (mate == null) {
            throw new IllegalArgumentException("Mate cannot be null.");
        }
        this.mate = mate;
    }

    /// /////// ///
    /// GETTERS ///
    /// /////// ///

    public AgentState getState() {
        return state;
    }

    public int getPreparingIngredientId() {
        return preparingIngredientId;
    }


    /// ////////////////////// ///
    /// PLAYER INFOS RETRIEVER ///
    /// ////////////////////// ///

    private boolean isPlayerHandEmpty() {
        return (!model.getPlayer(id).isHoldingSomething());
    }

    private boolean isHoldingNeededIngredient() {

        HoldableObject heldObject = model.getPlayer(id).getObjectHeld();

        if (heldObject != null) {
            if (heldObject.getClass() == Ingredient.class) {
                if (model.getRecipeIngredients(recipeId).contains(heldObject)) {
                    return (!model.getValidIngredients().contains(heldObject));
                }
            }
        }
        return false;
    }


    /// ////////////////////// ///
    /// RECIPE INFOS RETRIEVER ///
    /// ////////////////////// ///

    private Ingredient getRecipeIngredient(int ingredientId) {

        ArrayList<Ingredient> recipeIngredients = model.getRecipeIngredients(recipeId);

        if (recipeIngredients.size() <= ingredientId ) {
            throw new IllegalArgumentException("Recipe id " + ingredientId + " is invalid.");
        }

        return recipeIngredients.get(ingredientId);
    }

    private int getNextIngredientId() {
        int nextIngredientId = preparingIngredientId + 1;

        // Si le mate prépare l'ingrédient suivant, on prend l'ingrédient d'après
        if (mate.getPreparingIngredientId() == nextIngredientId && mate.recipeId == this.recipeId) {
            nextIngredientId++;
        }

        // Si l'ingrédient d'après n'existe pas
        if (nextIngredientId >= model.getRecipeIngredients(recipeId).size()) {
            return -1;
        }

        return nextIngredientId;
    }

    private boolean isRecipeFinished() {
        return (model.isRecipeFinished());
    }

    // Vérifie si l'ingrédient en cours de préparation a été placé sur le comptoir (liste des ingrédients validés)
    private boolean isIngredientValidated() {
        Ingredient test = getRecipeIngredient(preparingIngredientId);
        return (model.getValidIngredients().contains(test));
    }


    /// //////////////////// ///
    /// MATE INFOS RETRIEVER ///
    /// //////////////////// ///

    private AgentState getMateState() {
        return mate.getState();
    }


    /// /////////////////// ///
    /// MAP INFOS RETRIEVER ///
    /// /////////////////// ///

    private int checkFullPot() {
        for (Furniture furniture : model.furnitures) {
            // S'il y a une casserole pleine sur une gazinière
            if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getPot() == KitchenUstensils.FULL_POT && ((GasStove) furniture).getIngredientInPot() == null ) {
                return 2;
            }
            // S'il y a une casserole pleine sur un autre meuble
            else if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == KitchenUstensils.FULL_POT ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getObjectOn() == KitchenUstensils.FULL_POT) {
                return 1;
            }
        }
        // S'il n'y a pas de casserole pleine
        return 0;
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

    private Vec2 nextEmptyCase(Vec2 pos) {
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
                break;
            }
        }
        return destination;
    }


    /// /////// ///
    /// METHODS ///
    /// /////// ///

    public void update(float dt) {
//        System.out.println("Agent : " + this.id + " | x = "
//                + this.model.getPlayer(this.id).getPosX()
//                + " y = " + this.model.getPlayer(this.id).getPosY());
        timeBeforeNextAction -= dt;

        // Si le temps entre chaque action est écoulé
        if (timeBeforeNextAction <=0) {
            timeBeforeNextAction = timeBetweenActions;

            // S'il n'y a plus de mouvement à faire → détermination de la prochaine action logique et des prochains mouvements
            if (nextMoves.isEmpty()) {
                updateState();
            }

            if (!nextMoves.isEmpty()) {
                // On demande au model de bouger le joueur
                Vec2 nextMove = nextMoves.removeFirst();
                if (nextMove.isNotNull()) {
                    if(!model.movePlayer(id, nextMove)){
                        nextMoves.clear();
                    }
                }
            }
        }
    }

    // Ici on part du principe que les recettes contiennent au moins 2 ingrédients
    public void updateState() {
        //System.out.print("State change from " + this.state);
        switch (state) {

            case WAITING_TO_START:
                AgentState mateState = getMateState();
//                switch (mateState) {
//                    case WAITING_TO_START, WAITING_FOR_NEXT_RECIPE:
//                        this.state = AgentState.PREPARING_INGREDIENT;
//                        this.preparingIngredientId = 0;
//                        break;
//                    case PREPARING_INGREDIENT:
//                        this.state = AgentState.PREPARING_INGREDIENT;
//                        this.preparingIngredientId = 1;
//                        break;
//                    default:
//                        throw new IllegalStateException("Unknown state : { ownState: " + state + ", mateState: " + mateState + "}");
//                }
                this.state = AgentState.PREPARING_INGREDIENT;
                this.preparingIngredientId = getNextIngredientId();
                break;

            case PREPARING_INGREDIENT:
                prepare(getRecipeIngredient(preparingIngredientId));
                break;

            case VALIDATING_INGREDIENT:
                if (isIngredientValidated()) {
                    // Si la recette est finie
                    if (isRecipeFinished()) {
                        // Si personne n'est entrain de valider, on valide
                        if (getMateState() != AgentState.VALIDATING_RECIPE) {
                            state = AgentState.VALIDATING_RECIPE;
                            preparingIngredientId = -1;
                            goValidateRecipe();
                            if (nextMoves.isEmpty()) {
                                mate.moveFromCase();
                            }
                            break;
                        } // Sinon on attend
                        else {
                            state = AgentState.WAITING_FOR_NEXT_RECIPE;
                            preparingIngredientId = -1;
                            break;
                        }
                    }
                    // Si la recette n'est pas finie
                    else {
                        state = AgentState.PREPARING_INGREDIENT;
                        preparingIngredientId = getNextIngredientId();
                        if (preparingIngredientId == -1){
                            recipeId = 1;
                            preparingIngredientId = getNextIngredientId();
                        }
                        break;
                    }
                } else {
                    goPlaceHeldIngredientOnPlate();
                    if (nextMoves.isEmpty()) {
                        mate.moveFromCase();
                    }
                }
                break;

            case VALIDATING_RECIPE:
                // TODO : Vérifier si la recette a été validée et si oui passer au prochain ingrédient
                this.recipeId = 0;
                mate.recipeId = 0;
                state = AgentState.WAITING_FOR_NEXT_RECIPE;
                break;

            case WAITING_FOR_COOKING:
                if(isPlayerHandEmpty()){Ingredient cookedIngredient = getRecipeIngredient(preparingIngredientId);
                    // Si l'ingrédient est cuit
                    if (getFurnitureWithIngredientOn(cookedIngredient) != null) {
                        goGrab(cookedIngredient);
                        state = AgentState.PREPARING_INGREDIENT;
                    }
                } else { //Il c'est bloqué pendant le trajet
                    goPrepareHeldIngredient();
                }
                break;

            case WAITING_FOR_NEXT_RECIPE:
                if (model.validIngredients.isEmpty()){
                    state = AgentState.WAITING_TO_START;
                }
                break;

            default:
                throw new IllegalStateException("Unknown state : " + state);
        }
        //System.out.println(" to " + this.state);
    }

    void prepare(Ingredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient to prepare cannot be null.");
        }

        // SI LA MAIN EST VIDE
        if (isPlayerHandEmpty()) {

            switch (ingredient) {
                case Ingredient.SLICED_TOMATO:
                    goGrab(Ingredient.TOMATO);
                    break;

                case Ingredient.COOKED_PASTA:
                    int potSituation = checkFullPot();
                    if (potSituation == 2) {
                        // Il y a une casserole pleine sur le feu
                        goGrab(Ingredient.PASTA);
                    } else if (potSituation == 1) {
                        // Il y a une casserole pleine ailleurs que sur le feu
                        goGrab(KitchenUstensils.FULL_POT);
                    } else if (getFurnitureWithIngredientOn(KitchenUstensils.EMPTY_POT) != null) {
                        // Il n'y a pas de casserole pleine
                        goGrab(KitchenUstensils.EMPTY_POT);
                    }
                    break;

                default:
                    throw new IllegalStateException("Unknown ingredient : " + ingredient);
            }
        }

        // SI LA MAIN N'EST PAS VIDE
        else {
            if (isHoldingNeededIngredient()) {
                if(recipeId == 0){
                    goPlaceHeldIngredientOnPlate();
                    state = AgentState.VALIDATING_INGREDIENT;
                }
            } else {
                goPrepareHeldIngredient();
            }
        }
    }


    /// ///////////////////// ///
    /// PLAYER ACTION METHODS ///
    /// ///////////////////// ///

    private void goGrab(HoldableObject object) {
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

        if (destination != null) {
            ArrayList<Vec2> actions = Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(id).getPos(), destination, model.board, new int[8][8])));
            nextMoves.addAll(actions);
            nextMoves.add(destination.sub(new Vec2(furniture.getPosX(),furniture.getPosY())));
        } else {
            Player p = model.getPlayer(id);
            nextMoves.add(new Vec2(p.getPosX(),p.getPosY()).sub(new Vec2(furniture.getPosX(),furniture.getPosY())));
        }
    }

    private void goValidateRecipe() {
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

    private void goPrepareHeldIngredient() {
        Vec2 destination = null;
        Furniture workSurface = null;
        HoldableObject heldObject = model.getPlayer(id).getObjectHeld();

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
                        //TODO pas très propre d'être en waiting for cooked
                        //     avant d'être arrivé ça force un doublon en gestion de cas spé
                        //     comme la colision avec l'autre joueur
                        state = AgentState.WAITING_FOR_COOKING;
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

    private void moveFromCase(){
        Player p = model.getPlayer(id);
        ArrayList<Vec2> neighboors = Util.getNeighbors(p.getPos());
        for(Vec2 v : neighboors){
            if (model.board[v.getX()][v.getY()] == -1){
                nextMoves.add(p.getPos().sub(new Vec2(v.getX(),v.getY())));
                return;
            }
        }
        throw new ArrayStoreException("Aucune case libre");
    }

    private void goPlaceHeldIngredientOnPlate() {
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
