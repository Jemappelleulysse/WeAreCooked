package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

import Ingredient.Ingredient;
import Furnitures.*;
import Player.Player;
import Utils.Pair;


public class ViewController {


    public static ViewController instance;
    public int[][] board;
    public Player player;
    public GridPanel panelBoard;
    public ArrayList<Furniture> furnitures = new ArrayList<>();


    public void add(Furniture furniture) {
        furnitures.add(furniture);
        board[furniture.getPosX()][furniture.getPosY()] = furnitures.indexOf(furniture)+1;
    }

    public class GridPanel extends JPanel {

        void drawIngredient(Graphics g, int centerX, int centerY, int diameter, Ingredient ingredient) {
            switch (ingredient) {
                case TOMATO ->  g.setColor(Color.RED);
                case MEAT -> g.setColor(Color.PINK);
                case SLICED_TOMATO -> g.setColor(Color.MAGENTA);
                case PASTA ->  g.setColor(Color.ORANGE);
            }
            int smallDiameter = diameter / 3;
            int smallX = centerX + diameter - smallDiameter - 2;
            int smallY = centerY + diameter - smallDiameter - 2;
            g.fillOval(smallX, smallY, smallDiameter, smallDiameter);
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int rows = 8;
            int cols = 8;
            int cellWidth = (getWidth()) / cols;
            int cellHeight = (getHeight()) / rows;
            g.setColor(Color.BLACK);
            // Dessine les lignes horizontales
            for (int i = 0; i <= rows; i++) {
            int y = (i == rows) ? getHeight() - 1 : i * cellHeight;
            g.drawLine(0, y, getWidth() - 1, y);
            }
            // Dessine les lignes verticales
            for (int j = 0; j <= cols; j++) {
                int x = (j == cols) ? getWidth() - 1 : j * cellWidth;
                g.drawLine(x, 0, x, getHeight() - 1);
            }
            // Dessine le personnage en rouge
            int diameter = Math.min(cellWidth, cellHeight) - 4; // marge de 2px de chaque côté
            int centerX = player.getPosX() * cellWidth + cellWidth / 2 - diameter / 2;
            int centerY = player.getPosY() * cellHeight + cellHeight / 2 - diameter / 2;
            g.setColor(Color.YELLOW);
            g.fillOval(centerX, centerY, diameter, diameter);
            if (player.getIngredientHeld()!=null) {
                drawIngredient(g, centerX, centerY, diameter, player.getIngredientHeld());
            } else {
                g.setColor(Color.RED);
            }



            // Dessine les furnitures en bleu
            for (Furniture furniture : furnitures) {
                int furnitureX = furniture.getPosX() * cellWidth;
                int furnitureY = furniture.getPosY() * cellHeight;

                switch (furniture.getClass().getSimpleName()) {
                    case "WorkSurface":
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        if (((WorkSurface)furniture).hasSomethingOn()) {
                            drawIngredient(g,furnitureX,furnitureY,diameter,((WorkSurface)furniture).getIngredientOn());
                        }
                        break;
                    case "IngredientChest":
                        g.setColor(new Color(81, 0,0));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        drawIngredient(g,furnitureX,furnitureY,diameter,((IngredientChest)furniture).getIngredient());
                        break;
                    case "CuttingBoard":
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        g.setColor(Color.lightGray);
                        g.fillRect(furnitureX+cellWidth/8, furnitureY+cellHeight/8, cellWidth-cellWidth/4, cellHeight-cellHeight/4);
                        if (((CuttingBoard)furniture).hasSomethingOn()) {
                            drawIngredient(g,furnitureX,furnitureY,diameter,((CuttingBoard)furniture).getIngredientOn());
                        }
                        //g.setColor(Color.darkGray);
                        break;
                    case "Counter" :
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        g.setColor(Color.lightGray);
                        g.fillOval(furnitureX+cellWidth/8, furnitureY+cellHeight/8, cellWidth-cellWidth/4, cellHeight-cellHeight/4);
                        int dec = -5;
                        for (Ingredient ingredient : ((Counter)furniture).currentIngredients) {
                            drawIngredient(g,furnitureX+dec,furnitureY+dec,diameter,ingredient);
                            dec+=10;
                        }
                    default:
                        g.setColor(Color.GREEN);
                        break;
                }


            }
        }
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
        System.out.print(player.getPosX() + " " + player.getPosY());
        //DEBUG
        /*System.out.println(this);
        int[][] seen = new int[board.length][board[0].length];
        ArrayList<Pair> path = pathFinding(new Pair(player.getPosX(),player.getPosY()),new Pair(6, 6),seen);
        if (path != null) {
            for (Pair p : path) {
                System.out.println(p);
            }
        } else {
            System.out.println("PATH NOT FOUND");
        }*/
        panelBoard.repaint();
        return true;
    }
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

    public ArrayList<Pair> pathFinding(Pair posDepart, Pair posArrive, int[][] seen) {
        Queue<ArrayList<Pair>> queue = new LinkedList<>();
        ArrayList<Pair> initialPath = new ArrayList<>();
        initialPath.add(posDepart);
        queue.add(initialPath);

        while (!queue.isEmpty()) {
            ArrayList<Pair> currentPath = queue.poll();
            Pair current = currentPath.getLast();

            // Vérification des limites et obstacles
            if (current.i < 0 || current.i >= board.length || current.j < 0 || current.j >= board[0].length) {
                continue;
            }
            if (!(getBoard(current) == -1 || getBoard(current) == 0) || seen[current.i][current.j] == 1) {
                continue;
            }

            // Marquer comme vu
            seen[current.i][current.j] = 1;

            // Si on est arrivé
            if (current.equals(posArrive)) {
                for (Pair p : currentPath) {
                    System.out.print(p);
                }
                return currentPath;
            }

            // Ajouter les voisins à explorer
            for (Pair neighbor : getNeighbors(current)) {
                ArrayList<Pair> newPath = new ArrayList<>(currentPath);
                newPath.add(neighbor);
                queue.add(newPath);
            }
        }

        return null; // Aucun chemin trouvé
    }

    private ArrayList<Pair> getNeighbors(Pair p) {
        ArrayList<Pair> neighbors = new ArrayList<>();
        neighbors.add(new Pair(p.i - 1, p.j)); // haut
        neighbors.add(new Pair(p.i + 1, p.j)); // bas
        neighbors.add(new Pair(p.i, p.j - 1)); // gauche
        neighbors.add(new Pair(p.i, p.j + 1)); // droite
        return neighbors;
    }


     public int getBoard(Pair ij) {
        return board[ij.i][ij.j];
    }

    public ViewController() {
        board = new int[8][8];

        panelBoard = new GridPanel();
        panelBoard.setBackground(Color.LIGHT_GRAY);
        panelBoard.setBounds(0, 0, 900,900);
        // Initialize board with zeros
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = -1;
            }
        }
        player = new Player(2,3);
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



    public void display() {
        System.out.println("Displaying view...");
        JFrame frame = new JFrame("MVC Example");
        frame.setSize(900, 930);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.add(panelBoard);
        panelBoard.setVisible(true);
        frame.setResizable(false);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP && player.getPosY() > 0) {
                    move(3);
                } else if (key == KeyEvent.VK_DOWN && player.getPosY() < 7) {
                    move(2);
                } else if (key == KeyEvent.VK_LEFT && player.getPosX() > 0) {
                    move(1);
                } else if (key == KeyEvent.VK_RIGHT && player.getPosX() < 7) {
                    move(0);
                } else if (key == KeyEvent.VK_Y) {
                    player.takePath(pathFinding(new Pair(player.getPosX(), player.getPosY()),new Pair(6,6),new int[8][8]));
                }
                panelBoard.repaint();
            }
        });
    }




    
}