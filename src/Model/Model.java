package Model;

import Agent.Agent;
import HoldableObjects.Ingredient;
import Furnitures.*;
import Player.Player;
import Recipes.BolognesePasta;
import Utils.Pair;
import View.View;

import javax.swing.*;
import java.util.ArrayList;

public class Model {

    public int[][] board;
    public ArrayList<Player> players;
    public ArrayList<Furniture> furnitures = new ArrayList<>();

    public void add(Furniture furniture) {
        furnitures.add(furniture);
        board[furniture.getPosX()][furniture.getPosY()] = furnitures.indexOf(furniture)+1;
    }

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

    public boolean move(int direction, int ID) {
        Player player = this.getPlayer(ID);
        int k = (direction == 0) ? 1 : (direction == 1) ? -1 : 0;
        int l =  (direction == 2) ? 1 : (direction == 3) ? -1 : 0;
        if (board[player.getPosX()+k][player.getPosY()+l] != -1) {
            if (board[player.getPosX()+k][player.getPosY()+l] <= furnitures.size()) {
                player.setObjectHeld(furnitures.get(board[player.getPosX()+k][player.getPosY()+l]-1).interact(player.getObjectHeld()));
                return false;
            }
        }
        board[player.getPosX()][player.getPosY()] = -1;
        player.setPosX(player.getPosX()+k);
        player.setPosY(player.getPosY()+l);
        board[player.getPosX()][player.getPosY()] = 0;

        return true;
    }

    public boolean move(Pair p, int ID) {
        if (p.i == 1) {
            return move(1, ID);
        }else if (p.i == -1) {
            return move(0, ID);
        } else if (p.j == 1) {
            return move(3, ID);
        } else {
            return move(2, ID);
        }
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

        add(new WorkSurface(3, 3));
        add(new WorkSurface(3, 3));
        add(new WorkSurface(3, 4));
        add(new WorkSurface(4, 3));
        add(new WorkSurface(4, 4));
        add(new IngredientChest(0, 1, Ingredient.PASTA));
        add(new IngredientChest(0, 2, Ingredient.TOMATO));
        add(new CuttingBoard(6, 0));
        add(new CuttingBoard(5, 0));
        add(new GasStove(2,7));
        add(new GasStove(3,7));
        add(new Counter(6, 7, new BolognesePasta()));
        add(new Sink(7,3));
        add(new Sink(7,4));
        for (int x = 0; x < 8; x++) {

            if (!(x == 6 || x == 5)) add(new WorkSurface(x, 0));  // top row
            if (!(x == 6 || x ==2 || x == 3)) add(new WorkSurface(x, 7));   // bottom row
        }
        for (int y = 1; y < 7; y++) { // avoid duplicating corners

            if (!(y == 1 || y == 2)) add(new WorkSurface(0, y));   // left column
            if (!(y ==3 || y ==4)) add(new WorkSurface(7, y));   // right column
        }

    }

    public void update(float dt){
        for (Furniture furniture : furnitures) {
            furniture.update(dt);
        }
    }

    //TODO : Modifer pour éviter que les players nouvellements créer
    //      ne se chevauche
    public void addPlayer(int ID) {
        players.add(new Player(2,3, ID));
    }

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
