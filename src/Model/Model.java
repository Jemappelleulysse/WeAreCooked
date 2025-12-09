package Model;

import HoldableObjects.Ingredient;
import Furnitures.*;
import Player.Player;
import Recipes.*;
import Utils.Vec2;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Model {

    public int[][] board;
    public ArrayList<Player> players;
    public ArrayList<Furniture> furnitures = new ArrayList<>();
    public Counter counter;
    public ArrayList<Recipe> recipes;
    public ArrayList<Ingredient> validIngredients;
    private Recipe recipeToCashIn;


    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///

    private void addToBoard(Furniture furniture) {
        furnitures.add(furniture);
        board[furniture.getPosX()][furniture.getPosY()] = furnitures.indexOf(furniture)+1;
    }

    public Model() {
        board = new int[8][8];
        this.recipes = new ArrayList<>();
        this.players = new ArrayList<>();
        // Initialize board with zeros
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = -1;
            }
        }

        this.validIngredients = new ArrayList<>();

        addToBoard(new IngredientChest(3, 3, Ingredient.RAW_MEAT));
        addToBoard(new IngredientChest(4, 3, Ingredient.BREAD));
        addToBoard(new WorkSurface(3, 4));
        addToBoard(new WorkSurface(4, 4));
        addToBoard(new IngredientChest(0, 1, Ingredient.SALAD));
        addToBoard(new IngredientChest(0, 2, Ingredient.TOMATO));
        addToBoard(new IngredientChest(0, 4, Ingredient.PASTA));
        addToBoard(new IngredientChest(0, 5, Ingredient.POTATO));

        addToBoard(new CuttingBoard(6, 0));
        addToBoard(new CuttingBoard(5, 0));
        addToBoard(new GasStove(3,7));
        addToBoard(new GasStove(4,7));
        counter = new Counter(6, 7, this);
        addToBoard(counter);
        addToBoard(new OilSink(3,0));
        addToBoard(new Sink(7,3));
        addToBoard(new Sink(7,4));

        for (int x = 0; x < 8; x++) {
            if (x!=3 && x!=5 && x!=6) addToBoard(new WorkSurface(x, 0));  // top row
            if (x!=3 && x!=4 && x!=6) addToBoard(new WorkSurface(x, 7));   // bottom row
        }
        for (int y = 1; y < 7; y++) { // avoid duplicating corners
            if (y!=1 && y!=2 && y!=4 && y!=5) addToBoard(new WorkSurface(0, y));   // left column
            if (y!=3 && y!=4) addToBoard(new WorkSurface(7, y));   // right column
        }

    }


    /// ////// ///
    /// GETTER ///
    /// ////// ///

    public Player getPlayer(int ID) {
        Player player = null;
        for  (Player p : players) {
            if (p.getID() == ID) {
                player = p;
                break;
            }
        }
        if (player == null) {
            throw new IllegalArgumentException("Player with ID " + ID + " not found");
        }
        return player;
    }

    public ArrayList<Ingredient> getRecipeIngredients(int id) {

        if (recipes.size() <= id) {
            return null;
        }

        return recipes.get(id).getIngredients();
    }

    public ArrayList<Ingredient> getValidIngredients() {
        return validIngredients;
    }

    public boolean isRecipeFinished() {
        return recipes.getFirst().isComplete(validIngredients);
    }

    public Recipe getFirstRecipe() {
        if (!recipes.isEmpty()){
            return recipes.getFirst();
        }
        return null;
    }

    public Recipe getNextRecipe() {
        if (recipes.size() >= 2){
            return recipes.get(1);
        }
        return null;
    }

    /// ////// ///
    /// CONFIG ///
    /// ////// ///

    public void generateRecipes(int nbrRecipes) {
        recipes.clear();
        RecipeEnum[] allRecipes = RecipeEnum.values();
        Random rand = new Random();
        for (int i = 0; i < nbrRecipes; i++) {
            switch (allRecipes[rand.nextInt(allRecipes.length)]){
                case BOLOGNESE_PASTA:
                    recipes.add(new BolognesePasta());
                    break;
                case SANDWICH:
                    recipes.add(new Sandwich());
                    break;
                case CAESAR_SALAD:
                    recipes.add(new CaesarSalad());
                    break;
                case STEAK_AND_FRIES:
                    recipes.add(new SteakAndFries());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid recipe type");
            }
        }
    }

    public Vec2 getNewPlayerPos(){
        for (int i = 0; i < 8; ++i){
            for (int j = 0; j < 8; ++j){
                if (board[i][j] == -1){
                    return new Vec2(i,j);
                }
            }
        }
        throw new ArrayStoreException("Can't get new player position, all case of the board are full");
    }

    public void addPlayer(int ID) {
        Vec2 pos = getNewPlayerPos();
        board[pos.getX()][pos.getY()] = 0;
        players.add(new Player(pos.getX(), pos.getY(), ID));
    }

    public void addValidIngredient(Ingredient ingredient) {
        if (!recipes.getFirst().getIngredients().contains(ingredient)) {
            throw new  IllegalArgumentException("Ingredient " + ingredient + " isn't in the current recipe: " + recipes.getFirst());
        }

        validIngredients.add(ingredient);
    }

    public void updateRecipes() {
        recipeToCashIn = this.recipes.removeFirst();
        this.validIngredients.clear();
    }


    /// //////////// ///
    /// INTERACTIONS ///
    /// //////////// ///

    public boolean movePlayer(int id, Vec2 nextMove) {

        boolean isInteracting = false;
        Player player = this.getPlayer(id);

        int nextX = player.getPosX() - nextMove.getX();
        int nextY = player.getPosY() - nextMove.getY();

        // Si la case n'est pas vide
        if (board[nextX][nextY] != -1) {
            // Si le meuble est dans la liste de meubles
            if (board[nextX][nextY] == 0) {
                return false; //Bloqué par un joueur
            }
            else if (board[nextX][nextY] <= furnitures.size()) {
                // On interagit avec un meuble
                Furniture furniture = furnitures.get(board[nextX][nextY]-1);
                player.setObjectHeld(furniture.interact(player.getObjectHeld()));
                isInteracting = true;
            }
        }
        if (!isInteracting) {
            board[player.getPosX()][player.getPosY()] = -1;
            player.setPosX(nextX);
            player.setPosY(nextY);
            board[player.getPosX()][player.getPosY()] = 0;
        }
        return true;
    }

    public int update(float dt){

        // Update du timer des meubles
        for (Furniture furniture : furnitures) {
            furniture.update(dt);
        }

        // Mise à jour du score
        int score = 0;
        if(recipeToCashIn != null) {
            // Attribuer un score par recette
        }
        return score;
    }


    /// ///////// ///
    /// TO STRING ///
    /// ///////// ///

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < board.length ; i++) {
            for (int j = 0; j < board.length; j++) {
                s = s + "[" + board[j][i] + "] ";
            }
            s += "\n";
        }
        return s;
    }
}
