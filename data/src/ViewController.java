import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Color;
import Meuble.*;
import Player.Player;


public class ViewController {

    public int[][] board;
    public Player player;
    public GridPanel panelBoard;
    public ArrayList<Meuble> meubles = new ArrayList<>();

    class GridPanel extends JPanel {
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
            if (player.isHoldingSomething()) {
                switch (player.getIngredientHolded()) {    
                    default:
                        g.setColor(Color.RED);
                        int smallDiameter = diameter / 3;
                        int smallX = centerX + diameter - smallDiameter - 2;
                        int smallY = centerY + diameter - smallDiameter - 2;
                        g.fillOval(smallX, smallY, smallDiameter, smallDiameter);
                        break;
                }
            } else {
                g.setColor(Color.RED);
            }


            // Dessine les meubles en bleu
            for (Meuble meuble : meubles) {
                switch (getClass().getSimpleName()) {
                    case "PlanDeTravail":
                        g.setColor(Color.BLUE);
                        break;    
                    default:
                        g.setColor(Color.GREEN);
                        break;
                }
                int meubleX = meuble.getPosX() * cellWidth;
                int meubleY = meuble.getPosY() * cellHeight;
                g.fillRect(meubleX, meubleY, cellWidth, cellHeight);    
            }
        }
    }

    public boolean move(int direction) {
        int k = (direction == 0) ? 1 : (direction == 1) ? -1 : 0;
        int l =  (direction == 2) ? 1 : (direction == 3) ? -1 : 0;
        boolean isThereSomething = false;
        for (Meuble meuble : meubles) {
            System.out.println(meuble.getClass().getSimpleName() + " : " + meuble.getPosX() + " : " + meuble.getPosY());
            if (meuble.getPosX() == player.getPosX()+k && meuble.getPosY() == player.getPosY()+l) {
                isThereSomething = true;
                System.out.println("oui");
                switch (meuble.getClass().getSimpleName()) {
                    case "Coffre" :
                        if (!player.isHoldingSomething()) {
                            player.setIngredientHolded(((Coffre) meuble).getIngredient());
                            player.setHoldingSomething(true);
                        }
                        break;
                    case "PlanDeTravail" :
                        PlanDeTravail planDeTravail = (PlanDeTravail) meuble;
                        if (player.isHoldingSomething()) {
                            if (!planDeTravail.hasSomethingOn()) {
                                player.setHoldingSomething(false);
                                planDeTravail.setHasSomethingOn(true);
                                planDeTravail.setIngredientOn(player.getIngredientHolded());
                                player.setIngredientHolded(null);
                            }
                        } else {
                            if (planDeTravail.hasSomethingOn()) {
                                player.setHoldingSomething(true);
                                player.setIngredientHolded(planDeTravail.getIngredientOn());
                                planDeTravail.setHasSomethingOn(false);
                                planDeTravail.setIngredientOn(null);
                            }
                        }
                        break;
                }
            }
        }
        if (isThereSomething) {
            return true;
        }
        player.setPosX(player.getPosX()+k);
        player.setPosY(player.getPosY()+l);
        return true;
    }


    public ViewController() {
        board = new int[8][8];
        player = new Player(0, 0);
        panelBoard = new GridPanel();
        panelBoard.setBackground(Color.LIGHT_GRAY);
        panelBoard.setBounds(0, 0, 400, 400);
        // Initialize board with zeros
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }
    }



    public void display() {
        System.out.println("Displaying view...");
        JFrame frame = new JFrame("MVC Example");
        frame.setSize(1024, 700);
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
                }
                panelBoard.repaint();
            }
        });
    }




    
}