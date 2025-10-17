package View;

import javax.swing.JPanel;
import java.awt.*;

import Ingredient.Ingredient;
import Model.Model;
import Furnitures.*;

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
                case TOMATO :
                    drawWholeTomato( g, smallX, smallY );
                    break;
                case MEAT :
                    g.setColor(Color.PINK);
                    break;
                case SLICED_TOMATO :
                    drawSlicedTomato(g, smallX, smallY);
                    break;
                case PASTA :
                    drawRawPasta(g, smallX, smallY);
                    break;
                case COOKED_PASTA:
                    drawCookedPasta(g, smallX, smallY);
                    break;
                case FULL_POT:
                    drawPotWithWater(g, centerX, centerY);
                    break;
                case EMPTY_POT:
                    drawTopViewPot(g, centerX, centerY);
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

            if (model.player.getIngredientHeld()!=null) {
                drawIngredient(g, centerX, centerY, diameter, model.player.getIngredientHeld());
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



            // Dessine les furnitures en bleu
            for (Furniture furniture : model.furnitures) {
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
                        g.fillRect(furnitureX+cellWidth/8 + 8, furnitureY+cellHeight/8, cellWidth-cellWidth/4, cellHeight-cellHeight/4);
                        if (((CuttingBoard)furniture).hasSomethingOn()) {
                            drawIngredient(g,furnitureX,furnitureY,diameter,((CuttingBoard)furniture).getIngredientOn());
                        }
                        drawKnife(g,furnitureX+3,furnitureY+10);
                        if (((CuttingBoard)furniture).hasSomethingOn() && ((CuttingBoard)furniture).getIngredientOn() == Ingredient.TOMATO) {
                            drawProgressBar(g,furnitureX+30,furnitureY,(int)(((CuttingBoard)furniture).currNb*100/(((CuttingBoard)furniture).cutNb)));
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
                        break;
                    case "GasStove" :
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        drawMinimalStove(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                        if (((GasStove)furniture).hasAPot() && ((GasStove)furniture).hasSomethingOn()) {
                            drawPotWithPasta(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                            drawProgressBar(g,furnitureX+30,furnitureY,(int)((((GasStove)furniture).tempsActuel*100)/((GasStove)furniture).tempsCuisson));
                            System.out.println(((GasStove)furniture).tempsActuel);
                        } else if (((GasStove)furniture).hasAPot() && ((GasStove)furniture).getPot() == Ingredient.FULL_POT) {
                            drawPotWithWater(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                        } else if (((GasStove)furniture).hasAPot()) {
                            drawTopViewPot(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                        }
                        break;
                    case "Sink" :
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        drawSink(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
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