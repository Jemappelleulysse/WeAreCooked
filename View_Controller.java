import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;


public class View_Controller {

    public int[][] board;
    public int posX;
    public int posY; 
    public GridPanel panelBoard;

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
            int centerX = posX * cellWidth + cellWidth / 2 - diameter / 2;
            int centerY = posY * cellHeight + cellHeight / 2 - diameter / 2;
            g.setColor(Color.RED);
            g.fillOval(centerX, centerY, diameter, diameter);
        }
    }


    public View_Controller() {
        board = new int[8][8];
        posX = 0;
        posY = 0;
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
            if (key == KeyEvent.VK_UP && posY > 0) {
                posY--;
            } else if (key == KeyEvent.VK_DOWN && posY < 7) {
                posY++;
            } else if (key == KeyEvent.VK_LEFT && posX > 0) {
                posX--;
            } else if (key == KeyEvent.VK_RIGHT && posX < 7) {
                posX++;
            }
            panelBoard.repaint();
        }
    });
    // SUPPRIME la boucle infinie ici !
}

    public static void main(String[] args) {
        View_Controller vc = new View_Controller();
        vc.display();
    }


    public void updatePosition(int x, int y) {
        this.posX = x;
        this.posY = y;
        panelBoard.repaint(); // Redessine le panneau pour refléter la nouvelle position
    }



    
}