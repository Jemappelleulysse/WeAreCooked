package View;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

import HoldableObjects.*;
import Furnitures.*;
import Player.Player;
import Recipes.BolognesePasta;
import Recipes.Salad;
import Recipes.Sandwich;
import Recipes.SteakAndFries;

import static View.PixelArts.*;

public class View extends JPanel {

        private ArrayList<Player> players;
        private ArrayList<Furniture> furnitures;

        public View() {
            this.setBackground(Color.LIGHT_GRAY);
            this.setBounds(0, 0, 900,1015);
            setDoubleBuffered(true);
        }

        void drawHoldableObject(Graphics g, int centerX, int centerY, int diameter, HoldableObject object) {
            int smallDiameter = diameter / 3;
            int smallX = centerX + diameter - smallDiameter - 2;
            int smallY = centerY + diameter - smallDiameter - 2;

            switch (object) {
                case Ingredient.BREAD:
                    drawBread(g, smallX, smallY);
                    break;
                case Ingredient.SLICED_BREAD:
                    drawSlicedBread(g, smallX, smallY);
                    break;
                case Ingredient.TOMATO:
                    drawWholeTomato( g, smallX, smallY );
                    break;
                case Ingredient.SLICED_TOMATO:
                    drawSlicedTomato(g, smallX, smallY);
                    break;
                case Ingredient.SALAD:
                    drawSalad(g, smallX, smallY);
                    break;
                case Ingredient.WASHED_SALAD:
                    drawWashedSalad(g, smallX, smallY);
                    break;
                case Ingredient.RAW_MEAT :
                    drawRawMeat(g, smallX, smallY);
                    break;
                case Ingredient.COOKED_MEAT:
                    drawCookedMeat(g, smallX, smallY);
                    break;
                case Ingredient.PASTA :
                    drawRawPasta(g, smallX, smallY);
                    break;
                case Ingredient.COOKED_PASTA:
                    drawCookedPasta(g, smallX, smallY);
                    break;
                case KitchenUstensils.WATER_POT:
                    drawPotWithWater(g, centerX, centerY);
                    break;
                case KitchenUstensils.OIL_POT:
                    drawPotWithOil(g,centerX,centerY);
                    break;
                case KitchenUstensils.EMPTY_POT:
                    drawTopViewPot(g, centerX, centerY);
                    break;
                case Ingredient.COOKED_POTATO:
                    drawCookedPotato(g,smallX,smallY);
                    break;
                case Ingredient.POTATO:
                    drawPotato(g,smallX,smallY);
                    break;
                case Ingredient.SLICED_POTATO:
                    drawSlicedPotato(g,smallX,smallY);
                    break;
                case Ingredient.FRIED_POTATO:
                    drawFriedPotato(g,smallX,smallY);
                    break;
                default:
                    throw new IllegalArgumentException("Objet inconnu : " + object.toString());
            }

        }

        @Override
        protected void paintComponent(Graphics g) {
            int offsety = 115;
            super.paintComponent(g);
            int rows = 8;
            int cols = 8;
            int cellWidth = (getWidth()) / cols;
            int cellHeight = (getHeight()-offsety) / rows;
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
            int diameter =(Math.min(cellWidth, cellHeight)); // marge de 2px de chaque côté

            // Dessine le personnage en rouge
            for(Player p : players) {
                int diameterPlayer = (int)(diameter * 2./3.);
                int centerX = p.getPos().i * cellWidth + cellWidth / 2 - diameter / 2;
                int centerXPlayer = p.getPos().i * cellWidth + cellWidth / 2 - diameterPlayer / 2;
                int centerY = p.getPos().j * cellHeight + cellHeight / 2 - diameter / 2;
                int centerYPlayer = p.getPos().j * cellHeight + cellHeight / 2 - diameterPlayer / 2+10;
                g.setColor(Color.YELLOW);
                g.fillOval(centerXPlayer, centerYPlayer, diameterPlayer, diameterPlayer);
                drawChefHat(g,p.getPos().i*cellHeight+26,p.getPos().j * cellHeight);
                drawMustache(g,centerXPlayer-3,p.getPos().j * cellHeight+70);

                if (p.getObjectHeld()!=null) {
                    drawHoldableObject(g, centerX, centerY, diameter, p.getObjectHeld());
                } else {
                    g.setColor(Color.RED);
                }
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
            for (Furniture furniture : furnitures) {
                int furnitureX = furniture.getPosX() * cellWidth;
                int furnitureY = furniture.getPosY() * cellHeight;


                switch (furniture.getClass().getSimpleName()) {
                    case "WorkSurface":
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        if (((WorkSurface)furniture).hasSomethingOn()) {
                            drawHoldableObject(g,furnitureX,furnitureY,diameter,((WorkSurface)furniture).getObjectOn());
                        }
                        break;
                    case "IngredientChest":
                        g.setColor(new Color(81, 0,0));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        drawHoldableObject(g,furnitureX,furnitureY,diameter,((IngredientChest)furniture).getIngredient());
                        break;
                    case "CuttingBoard":
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        g.setColor(Color.lightGray);
                        g.fillRect(furnitureX+cellWidth/8 + 8, furnitureY+cellHeight/8, cellWidth-cellWidth/4, cellHeight-cellHeight/4);
                        if (((CuttingBoard)furniture).hasSomethingOn()) {
                            drawHoldableObject(g,furnitureX,furnitureY,diameter,((CuttingBoard)furniture).getObjectOn());
                        }
                        drawKnife(g,furnitureX+3,furnitureY+10);
                        if (((CuttingBoard)furniture).hasSomethingOn() && (((CuttingBoard)furniture).getObjectOn() == Ingredient.TOMATO ||
                                                                           ((CuttingBoard)furniture).getObjectOn() == Ingredient.BREAD ||
                                                                            ((CuttingBoard)furniture).getObjectOn() == Ingredient.POTATO)) {
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
                            drawHoldableObject(g,furnitureX+dec,furnitureY+dec,diameter,ingredient);
                            dec+=10;
                        }
                        break;
                    case "GasStove" :
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        drawMinimalStove(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                        if (((GasStove)furniture).getPot() == KitchenUstensils.WATER_POT ) {
                            if (((GasStove) furniture).hasIngredientInPot()) {
                                if (((GasStove) furniture).getIngredientInPot() == Ingredient.PASTA) {
                                    drawPotWithPasta(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                                } else {
                                    drawPotWithPotatoWater(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                                }
                                drawProgressBar(g,furnitureX+30,furnitureY,(int)((((GasStove)furniture).currTime*100)/((GasStove)furniture).cookingTime));
                            } else {
                                drawPotWithWater(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                            }
                        } else if (((GasStove)furniture).getPot() == KitchenUstensils.OIL_POT ) {
                            if (((GasStove) furniture).hasIngredientInPot()) {
                                drawPotWithPotatoOil(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                                drawProgressBar(g,furnitureX+30,furnitureY,(int)((((GasStove)furniture).currTime*100)/((GasStove)furniture).cookingTime));
                            } else {
                                drawPotWithOil(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                            }
                        } else if (((GasStove)furniture).hasAPot()) {
                            if (((GasStove) furniture).getIngredientInPot() == Ingredient.RAW_MEAT) {
                                drawPotWithMeat(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                                drawProgressBar(g,furnitureX+30,furnitureY,(int)((((GasStove)furniture).currTime*100)/((GasStove)furniture).cookingTime));
                            } else if (((GasStove) furniture).hasIngredientInPot()) {
                                drawTopViewPot(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2);
                                drawIngredientInMiddle(g,furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2,((GasStove)furniture).getIngredientInPot());
                            }
                            else {
                                drawTopViewPot(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2);
                            }
                        }
                        break;
                    case "Sink" :
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        drawSink(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                        break;
                    case "OilSink" :
                        g.setColor(new Color(221, 147, 62));
                        g.fillRect(furnitureX, furnitureY, cellWidth, cellHeight);
                        drawOilSink(g, furnitureX + cellWidth / 2 - diameter / 2, furnitureY + cellHeight / 2 - diameter / 2 );
                        break;
                    default:
                        g.setColor(Color.GREEN);
                        break;
                }
            }
            g.setColor(Color.BLACK);
            g.fillRect(0, 896, 900, 130);
            drawRecipeProgress(g, 0,900,new BolognesePasta());
    }

    public void update(float dt, ArrayList<Player> players, ArrayList<Furniture> furnitures) {
            this.players = players;
            this.furnitures = furnitures;
            repaint();
    }

}