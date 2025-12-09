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
                // Si la recette contient l'ingrédient tenu en main
                if (model.getRecipeIngredients(recipeId).contains(heldObject)) {
                    // Si la recette est d'indice 0, on vérifie si l'objet n'est pas déjà sur l'assiette
                    if(recipeId == 0) {
                        return (!model.getValidIngredients().contains(heldObject));
                    }
                    // Sinon l'objet est nécessaire pour une recette future
                    else {
                        return true;
                    }
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

        if(recipeIngredients == null || recipeIngredients.isEmpty()) {
            return null;
        }

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

    private int checkPot(KitchenUstensils pot) {
        for (Furniture furniture : model.furnitures) {
            // S'il y a la casserole voulue sur une gazinière
            if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getPot() == pot && ((GasStove) furniture).getIngredientInPot() == null ) {
                return 2;
            }
            // S'il y a la casserole voulue sur un autre meuble
            else if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == pot ||
                    furniture.getClass() == WorkSurface.class && ((WorkSurface) furniture).getObjectOn() == pot) {
                return 1;
            }
        }
        // S'il n'y a pas la casserole voulue
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

    // Ici, on part du principe que les recettes contiennent au moins 2 ingrédients
    public void updateState() {

        switch (state) {

            case WAITING_TO_START:
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
                            this.recipeId = 0;
                            mate.recipeId = 0;
                            goValidateRecipe();
                            if (nextMoves.isEmpty()) {
                                mate.moveFromCase();
                            }
                            break;
                        } // Sinon, on attend
                        else {
                            state = AgentState.WAITING_FOR_NEXT_RECIPE;
                            preparingIngredientId = -1;
                            break;
                        }
                    }
                    // Si la recette n'est pas finie
                    else {
                        preparingIngredientId = getNextIngredientId();
                        // S'il n'y a pas de prochain ingrédient
                        if (preparingIngredientId == -1){
                            state = AgentState.WAITING_FOR_NEXT_RECIPE;
                        } // S'il y a un prochain ingrédient
                        else {
                            state = AgentState.PREPARING_INGREDIENT;
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
                this.recipeId = 0;
                mate.recipeId = 0;
                if(model.recipes.isEmpty()) {
                    state = AgentState.END;
                } else {
                    state = AgentState.PREPARING_INGREDIENT;
                    preparingIngredientId = getNextIngredientId();
                }
                break;

            case WAITING_FOR_COOKING:
                if(isPlayerHandEmpty()){
                    Ingredient cookedIngredient = getRecipeIngredient(preparingIngredientId);
                    // Si l'ingrédient est cuit
                    if (getFurnitureWithIngredientOn(cookedIngredient) != null) {
                        goGrab(cookedIngredient);
                        state = AgentState.PREPARING_INGREDIENT;
                    }
                } else { //Il s'est bloqué pendant le trajet
                    goPrepareHeldIngredient();
                }
                break;

            case WAITING_FOR_NEXT_RECIPE:
                if (model.recipes.size() <= 1) {
                    state = AgentState.END;
                    recipeId = -1;
                    preparingIngredientId = -1;
                } else {
                    recipeId = 1;
                    // Vérifie que le premier ingrédient de la recette est accessible
                    if (getRecipeIngredient(0) != null){
                        state = AgentState.PREPARING_INGREDIENT;
                        preparingIngredientId = 0;
                    } else {
                        throw new IllegalStateException("Next recipe trouvée mais premier ingrédient inaccessible.");
                    }
                }
                break;

            case END:
                break;

            default:
                throw new IllegalStateException("Unknown state : " + state);
        }
    }

    void prepare(Ingredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient to prepare cannot be null.");
        }

        // SI LA MAIN EST VIDE
        if (isPlayerHandEmpty()) {

            // Si l'ingrédient fini est posé sur un plan de travail
            if (getFurnitureWithIngredientOn(ingredient) != null) {
                goGrab(ingredient);
            }
            else {
                int potSituation;

                switch (ingredient) {

                    case Ingredient.SLICED_TOMATO:
                        goGrab(Ingredient.TOMATO);
                        break;

                    case Ingredient.WASHED_SALAD:
                        goGrab(Ingredient.SALAD);
                        break;

                    case Ingredient.SLICED_BREAD:
                        goGrab(Ingredient.BREAD);
                        break;

                    case Ingredient.COOKED_PASTA:
                        potSituation = checkPot(KitchenUstensils.WATER_POT);
                        if (potSituation == 2) {
                            // Il y a une casserole d'eau vide sur le feu
                            goGrab(Ingredient.PASTA);
                        } else if (potSituation == 1) {
                            // Il y a une casserole d'eau ailleurs
                            goGrab(KitchenUstensils.WATER_POT);
                        } else {
                            // Il n'y a pas de casserole d'eau
                            potSituation = checkPot(KitchenUstensils.EMPTY_POT);
                            // S'il y a une casserole vide de dispo on va la chercher
                            if (potSituation != 0) {
                                goGrab(KitchenUstensils.EMPTY_POT);
                            } // Sinon, on attend
                        }
                        break;

                    case Ingredient.COOKED_MEAT:
                        potSituation = checkPot(KitchenUstensils.EMPTY_POT);
                        if (potSituation == 2) {
                            // Il y a une casserole vide sur le feu
                            goGrab(Ingredient.RAW_MEAT);
                        } else if (potSituation == 1) {
                            // Il y a une casserole vide ailleurs
                            goGrab(KitchenUstensils.WATER_POT);
                        } // Sinon, on attend
                        break;

                    case Ingredient.COOKED_POTATO:
                        potSituation = checkPot(KitchenUstensils.WATER_POT);
                        if (potSituation == 2) {
                            // Il y a une casserole d'eau vide sur le feu
                            goGrab(Ingredient.POTATO);
                        } else if (potSituation == 1) {
                            // Il y a une casserole d'eau ailleurs
                            goGrab(KitchenUstensils.WATER_POT);
                        } else {
                            // Il n'y a pas de casserole d'eau
                            potSituation = checkPot(KitchenUstensils.EMPTY_POT);
                            // S'il y a une casserole vide de dispo on va la chercher
                            if (potSituation != 0) {
                                goGrab(KitchenUstensils.EMPTY_POT);
                            } // Sinon, on attend
                        }
                        break;

                    case Ingredient.FRIED_POTATO:
                        potSituation = checkPot(KitchenUstensils.OIL_POT);
                        if (potSituation == 2) {
                            // Il y a une casserole d'huile vide sur le feu
                            goGrab(Ingredient.POTATO);
                        } else if (potSituation == 1) {
                            // Il y a une casserole d'huile ailleurs
                            goGrab(KitchenUstensils.OIL_POT);
                        } else {
                            // Il n'y a pas de casserole d'huile
                            potSituation = checkPot(KitchenUstensils.EMPTY_POT);
                            // S'il y a une casserole vide de dispo on va la chercher
                            if (potSituation != 0) {
                                goGrab(KitchenUstensils.EMPTY_POT);
                            } // Sinon, on attend
                        }
                        break;

                    default:
                        throw new IllegalStateException("Unknown ingredient : " + ingredient);
                }
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
        Vec2 destination;

        // Si l'objet voulu n'est pas sur un meuble, on cherche un coffre
        if (furniture == null) {
            for (Furniture fur : model.furnitures) {
                if (fur.getClass() == IngredientChest.class && ((IngredientChest) fur).getIngredient().equals(object)) {
                    furniture = fur;
                }
            }
        }

        // Si un coffre est trouvé, on cherche la case vide la plus proche
        if (furniture != null) {
            Vec2 furniturePos = new Vec2(furniture.getPosX(),furniture.getPosY());
            destination = nextEmptyCase(furniturePos);
        } else {
            return;
        }

        // Si la case vide est trouvé, on ajoute les déplacements du pathfinding à la liste de mouvements
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
        Ingredient preparedIngredient = getRecipeIngredient(preparingIngredientId);

        switch (heldObject) {

            case Ingredient.TOMATO, Ingredient.POTATO, Ingredient.BREAD :
                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == CuttingBoard.class && ((CuttingBoard) furniture).getObjectOn() == null) {
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

            case Ingredient.PASTA, Ingredient.SLICED_POTATO, Ingredient.RAW_MEAT:
                KitchenUstensils wantedPot;
                if(preparedIngredient == Ingredient.FRIED_POTATO) {
                    wantedPot = KitchenUstensils.OIL_POT;
                } else if (preparedIngredient == Ingredient.COOKED_MEAT) {
                    wantedPot = KitchenUstensils.EMPTY_POT;
                } else {
                    wantedPot = KitchenUstensils.WATER_POT;
                }

                for (Furniture furniture : model.furnitures) {
                    if (furniture.getClass() == GasStove.class &&
                            ((GasStove) furniture).getPot() == wantedPot &&
                            ((GasStove) furniture).getIngredientInPot() == null)
                    {
                        workSurface = furniture;
                        state = AgentState.WAITING_FOR_COOKING;
                        break;
                    }
                }
                break;

            case KitchenUstensils.EMPTY_POT:
                Object wantedFurniture;
                switch (preparedIngredient) {
                    case RAW_MEAT:
                        wantedFurniture = GasStove.class;
                        break;

                    case COOKED_POTATO, COOKED_PASTA:
                        wantedFurniture = Sink.class;
                        break;

                    case FRIED_POTATO:
                        wantedFurniture = OilSink.class;
                        break;

                    case null, default:
                        throw new IllegalStateException("Holding an empty pot while preparing ingredient : " + preparedIngredient);
                }

                if (wantedFurniture == GasStove.class) {
                    for (Furniture furniture : model.furnitures) {
                        if (furniture.getClass() == GasStove.class && ((GasStove) furniture).getPot() == null) {
                            workSurface = furniture;
                            break;
                        }
                    }
                } else {
                    for (Furniture furniture : model.furnitures) {
                        if (furniture.getClass() == wantedFurniture) {
                            workSurface = furniture;
                            break;
                        }
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
                throw new IllegalStateException("L'objet tenu en main n'a pas de logique attribuée");
        }

        if (workSurface == null) {
            // Aucune surface de travail trouvée
            return;
        }

        // Déplacement jusqu'au meuble
        destination = nextEmptyCase(new Vec2(workSurface.getPosX(),workSurface.getPosY()));
        if (destination == null) {
            return;
        }

        ArrayList<Vec2> actions = Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(id).getPos(), destination, model.board, new int[8][8])));
        nextMoves.addAll(actions);

        // Interaction avec le meuble
        Vec2 useFurniture = destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY()));
        if (workSurface.getClass() == CuttingBoard.class) {
            nextMoves.add(useFurniture);
            nextMoves.add(useFurniture);
            nextMoves.add(useFurniture);
            nextMoves.add(useFurniture);
        }
        nextMoves.add(useFurniture);
    }

    private void goPlaceHeldIngredientOnPlate() {
        // Place l'ingrédient tenu sur l'assiette du comptoir
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
            return;
        }
        ArrayList<Vec2> actions =  Vec2.coordsToDirections(Objects.requireNonNull(pathFinding(model.getPlayer(id).getPos(), destination, model.board, new int[8][8])));
        nextMoves.addAll(actions);
        nextMoves.add(destination.sub(new Vec2(workSurface.getPosX(),workSurface.getPosY())));
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
}
