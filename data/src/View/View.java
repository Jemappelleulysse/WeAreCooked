package View;

import javax.swing.JPanel;
import java.awt.*;

import Ingredient.Ingredient;
import Model.Model;
import Meuble.*;

import static View.PixelArts.*;


public class View extends JPanel {

        private Model model;

        public View() {
            this.setBackground(Color.LIGHT_GRAY);
            this.setBounds(0, 0, 900,900);
            setDoubleBuffered(true);
        }

        public void setModel(Model model) {
            this.model = model;
        }

        void drawIngredient(Graphics g, int centerX, int centerY, int diameter, Ingredient ingredient) {
            int smallDiameter = diameter / 3;
            int smallX = centerX + diameter - smallDiameter - 2;
            int smallY = centerY + diameter - smallDiameter - 2;
            switch (ingredient) {
                case TOMATE :
                    drawWholeTomato( g, smallX, smallY );
                    break;
                case VIANDE :
                    g.setColor(Color.PINK);
                    break;
                case TOMATE_COUPE :
                    drawSlicedTomato(g, smallX, smallY);
                    break;
                case PATES :
                    drawRawPasta(g, smallX, smallY);
                    break;
                case PATES_CUITES:
                    drawCookedPasta(g, smallX, smallY);
                    break;
                case POT :
                    drawPotWithWater(g, centerX, centerY);
                    break;
            }

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
            int diameter =(Math.min(cellWidth, cellHeight)); // marge de 2px de chaque côté
            int diameterPlayer = (int)(diameter * 2./3.);
            int centerX = model.player.getPos().i * cellWidth + cellWidth / 2 - diameter / 2;
            int centerXPlayer = model.player.getPos().i * cellWidth + cellWidth / 2 - diameterPlayer / 2;
            int centerY = model.player.getPos().j * cellHeight + cellHeight / 2 - diameter / 2;
            int centerYPlayer = model.player.getPos().j * cellHeight + cellHeight / 2 - diameterPlayer / 2+10;
            g.setColor(Color.YELLOW);
            g.fillOval(centerXPlayer, centerYPlayer, diameterPlayer, diameterPlayer);
            drawChefHat(g,model.player.getPos().i*cellHeight+26,model.player.getPos().j * cellHeight);
            drawMustache(g,centerXPlayer-3,model.player.getPos().j * cellHeight+70);

            if (model.player.getIngredientHolded()!=null) {
                drawIngredient(g, centerX, centerY, diameter, model.player.getIngredientHolded());
            } else {
                g.setColor(Color.RED);
            }
            for (int i = 0 ; i < 8 ; i++) {
                for (int j = 0 ; j < 8 ; j++) {
                    //drawSlicedTomato(g, i * cellWidth + cellWidth / 2 - diameter / 2, j * cellHeight + cellHeight / 2 - diameter / 2);
                    //drawTopViewPot(g, i * cellWidth + cellWidth / 2 - diameter / 2, j * cellHeight + cellHeight / 2 - diameter / 2);
                    //drawSinkWithDirtyPlate(g, i * cellWidth + cellWidth / 2 - diameter / 2, j * cellHeight + cellHeight / 2 - diameter / 2);
                    //drawSink(g, i * cellWidth + cellWidth / 2 - diameter / 2, j * cellHeight + cellHeight / 2 - diameter / 2);
                    //drawMustache(g, i * cellWidth + cellWidth / 2 - diameter / 2, j * cellHeight + cellHeight / 2 - diameter / 2);
                    //drawMinimalStove( g, i * cellWidth + cellWidth / 2 - diameter / 2, j * cellHeight + cellHeight / 2 - diameter / 2 );
                }
            }



            // Dessine les meubles en bleu
            for (Meuble meuble : model.meubles) {
                int meubleX = meuble.getPosX() * cellWidth;
                int meubleY = meuble.getPosY() * cellHeight;


                switch (meuble.getClass().getSimpleName()) {
                    case "PlanDeTravail":
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(meubleX, meubleY, cellWidth, cellHeight);
                        if (((PlanDeTravail)meuble).hasSomethingOn()) {
                            drawIngredient(g,meubleX,meubleY,diameter,((PlanDeTravail)meuble).getIngredientOn());
                        }
                        break;
                    case "Coffre":
                        g.setColor(new Color(81, 0,0));
                        g.fillRect(meubleX, meubleY, cellWidth, cellHeight);
                        drawIngredient(g,meubleX,meubleY,diameter,((Coffre)meuble).getIngredient());
                        break;
                    case "PlancheADecoupe":
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(meubleX, meubleY, cellWidth, cellHeight);
                        g.setColor(Color.lightGray);
                        g.fillRect(meubleX+cellWidth/8 + 8, meubleY+cellHeight/8, cellWidth-cellWidth/4, cellHeight-cellHeight/4);
                        if (((PlancheADecoupe)meuble).hasSomethingOn()) {
                            drawIngredient(g,meubleX,meubleY,diameter,((PlancheADecoupe)meuble).getIngredientOn());
                        }
                        drawKnife(g,meubleX+3,meubleY+10);
                        //g.setColor(Color.darkGray);
                        break;
                    case "Comptoir" :
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(meubleX, meubleY, cellWidth, cellHeight);
                        g.setColor(Color.lightGray);
                        g.fillOval(meubleX+cellWidth/8, meubleY+cellHeight/8, cellWidth-cellWidth/4, cellHeight-cellHeight/4);
                        int dec = -5;
                        for (Ingredient ingredient : ((Comptoir)meuble).currentIngredients) {
                            drawIngredient(g,meubleX+dec,meubleY+dec,diameter,ingredient);
                            dec+=10;
                        }
                        break;
                    case "Gaziniere" :
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(meubleX, meubleY, cellWidth, cellHeight);
                        drawMinimalStove(g, meubleX + cellWidth / 2 - diameter / 2, meubleY + cellHeight / 2 - diameter / 2 );
                        if (((Gaziniere)meuble).isHasAPot() && ((Gaziniere)meuble).hasSomethingOn()) {
                            drawPotWithPasta(g, meubleX + cellWidth / 2 - diameter / 2, meubleY + cellHeight / 2 - diameter / 2 );
                        } else if (((Gaziniere)meuble).isHasAPot()) {
                            drawPotWithWater(g, meubleX + cellWidth / 2 - diameter / 2, meubleY + cellHeight / 2 - diameter / 2 );
                        }
                        break;

                    default:
                        g.setColor(Color.GREEN);
                        break;
                }

            }


    }



    public void update(float dt) {
            repaint();
    }











    
}