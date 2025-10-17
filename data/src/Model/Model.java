package Model;

import Agent.Agent;
import Ingredient.Ingredient;
import Furnitures.*;
import Player.Player;
import Recipes.BolognesePasta;
import Utils.Pair;
import View.View;

import javax.swing.*;
import java.util.ArrayList;

public class Model {


    long lastTime = System.nanoTime();
    long currentTime = System.nanoTime();
    private float timer = 16;

    private boolean doContinue = true;
    public Agent agent;
    public int[][] board;
    public Player player;
    private View view;
    public ArrayList<Furniture> furnitures = new ArrayList<>();

    public void add(Furniture furniture) {
        furnitures.add(furniture);
        board[furniture.getPosX()][furniture.getPosY()] = furnitures.indexOf(furniture)+1;
    }


    public boolean move(int direction) {
        int k = (direction == 0) ? 1 : (direction == 1) ? -1 : 0;
        int l =  (direction == 2) ? 1 : (direction == 3) ? -1 : 0;
        if (board[player.getPosX()+k][player.getPosY()+l] != -1) {
            if (board[player.getPosX()+k][player.getPosY()+l] <= furnitures.size()) {
                player.setIngredientHeld(furnitures.get(board[player.getPosX()+k][player.getPosY()+l]-1).interact(player.getIngredientHeld()));
                return false;
            }
        }
        board[player.getPosX()][player.getPosY()] = -1;
        player.setPosX(player.getPosX()+k);
        player.setPosY(player.getPosY()+l);
        board[player.getPosX()][player.getPosY()] = 0;

        return true;
    }

    public void setView(View view) {this.view = view;}

    public boolean move(Pair p) {
        if (p.i == 1) {
            return move(1);
        }else if (p.i == -1) {
            return move(0);
        } else if (p.j == 1) {
            return move(3);
        } else {
            return move(2);
        }
    }

    public Model() {
        board = new int[8][8];
        player = new Player(2,3);
        // Initialize board with zeros
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                    board[i][j] = -1;
            }
        }


        agent = new Agent(this, new BolognesePasta());
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


    public void start() {
        //
        // Des trucs à initialiser?
        //

        while(doContinue) {

            float dt = (System.nanoTime() - lastTime) / 1000000000.0f;

            lastTime = System.nanoTime();

            for (Furniture furniture : furnitures) {
                furniture.update(dt);
            }
            SwingUtilities.invokeLater(() -> view.update(dt));
            //System.out.println(System.currentTimeMillis() - dt + " ms needed for the view Update");
            agent.update(dt);

            //System.out.println(System.currentTimeMillis() - dt + " ms needed for the agent Update");
        }


    }






}
