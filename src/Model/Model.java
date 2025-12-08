package Model;

import HoldableObjects.Ingredient;
import Furnitures.*;
import Player.Player;
import Recipes.BolognesePasta;
import Recipes.Recipe;
import Utils.Vec2;

import javax.swing.*;
import java.util.ArrayList;

public class Model {

    public int[][] board;
    public ArrayList<Player> players;
    public ArrayList<Furniture> furnitures = new ArrayList<>();
    public Counter counter;
    public Recipe currentRecipe;
    public ArrayList<Ingredient> validIngredients;


    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///

    private void addToBoard(Furniture furniture) {
        furnitures.add(furniture);
        board[furniture.getPosX()][furniture.getPosY()] = furnitures.indexOf(furniture)+1;
    }

    public Model() {
        board = new int[8][8];
        this.players = new ArrayList<>();
        // Initialize board with zeros
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = -1;
            }
        }

        this.currentRecipe = new BolognesePasta();
        this.validIngredients = new ArrayList<>();

        addToBoard(new WorkSurface(3, 3));
        addToBoard(new WorkSurface(3, 3));
        addToBoard(new WorkSurface(3, 4));
        addToBoard(new WorkSurface(4, 3));
        addToBoard(new WorkSurface(4, 4));
        addToBoard(new IngredientChest(0, 1, Ingredient.PASTA));
        addToBoard(new IngredientChest(0, 2, Ingredient.TOMATO));
        addToBoard(new CuttingBoard(6, 0));
        addToBoard(new CuttingBoard(5, 0));
        addToBoard(new GasStove(2,7));
        addToBoard(new GasStove(3,7));
        counter = new Counter(6, 7, this);
        addToBoard(counter);
        addToBoard(new Sink(7,3));
        addToBoard(new Sink(7,4));

        for (int x = 0; x < 8; x++) {

            if (!(x == 6 || x == 5)) addToBoard(new WorkSurface(x, 0));  // top row
            if (!(x == 6 || x ==2 || x == 3)) addToBoard(new WorkSurface(x, 7));   // bottom row
        }
        for (int y = 1; y < 7; y++) { // avoid duplicating corners

            if (!(y == 1 || y == 2)) addToBoard(new WorkSurface(0, y));   // left column
            if (!(y ==3 || y ==4)) addToBoard(new WorkSurface(7, y));   // right column
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

    public ArrayList<Ingredient> getRecipeIngredients() {
        return currentRecipe.getIngredients();
    }

    public ArrayList<Ingredient> getValidIngredients() {
        return validIngredients;
    }

    public boolean isRecipeFinished() {
        return currentRecipe.isComplete(validIngredients);
    }


    /// ////// ///
    /// CONFIG ///
    /// ////// ///

    public Vec2 getNewPlayerPos(){
        for (int i = 0; i < 8; ++i){
            for (int j = 0; j < 8; ++j){
                if (board[i][j] == -1){
                    return new Vec2(i,j);
                }
            }
        }
        throw new ArrayStoreException("Can't get new player position, " +
                "all case of the board are full");
    }

    //TODO : Modifier pour éviter que les players nouvellement créés ne se chevauchent
    public void addPlayer(int ID) {
//        Vec2 pos = getNewPlayerPos();
//        board[pos.getX()][pos.getY()] = 0;
//        players.add(new Player(pos.getX(), pos.getY(), ID));
        if(ID == 0){
            board[2][3] = 0;
            players.add(new Player(2, 3, ID));
        } else if(ID == 1){
            board[5][4] = 0;
            players.add(new Player(5, 4, ID));
        }
    }

    public void addValidIngredient(Ingredient ingredient) {
        if (!currentRecipe.getIngredients().contains(ingredient)) {
            throw new  IllegalArgumentException("Ingredient " + ingredient + " isn't in the current recipe: " + currentRecipe);
        }

        validIngredients.add(ingredient);
    }

    //TODO: Faire en sorte que la nouvelle recette soit aléatoire
    public void updateRecipe() {
        this.currentRecipe = new BolognesePasta();
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

        int score = 0;

        // Update du timer des meubles
        for (Furniture furniture : furnitures) {
            furniture.update(dt);
        }

        // Update de la recette du comptoir

        // TODO: Calcul du score
//        if (isRecipeFinished()) {
//            updateRecipe();
//            score = 50;
//        }
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
