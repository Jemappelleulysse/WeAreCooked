package View;

import HoldableObjects.Ingredient;
import Recipes.Recipe;

import java.awt.*;
import java.util.Random;

public class PixelArts {
    public static void drawKnife(Graphics g, int i, int j) {
        int scale = 2;
        int bladeLength = 32 * scale;       // Longueur de la lame (verticale)
        int bladeWidth = 4 * scale;         // Largeur de la lame
        int handleLength = 12 * scale;      // Longueur du manche
        int handleWidth = 6 * scale;        // Largeur du manche

        // Lame (gris clair)
        g.setColor(new Color(200, 200, 200));
        g.fillRect(i, j, bladeWidth, bladeLength);

        // Tranchant (gris foncé)
        g.setColor(new Color(150, 150, 150));
        g.fillRect(i + bladeWidth, j + 1, 1, bladeLength - 2);

        // Manche (brun)
        g.setColor(new Color(102, 51, 0));
        g.fillRect(i - 1, j + bladeLength, handleWidth, handleLength);

        // Rivets (blancs)
        g.setColor(Color.WHITE);
        g.fillRect(i + 3, j + bladeLength + 2 * scale, scale, scale);
        g.fillRect(i + 3, j + bladeLength + 5 * scale, scale, scale);
        g.fillRect(i + 3, j + bladeLength + 8 * scale, scale, scale);

    }

    public static void drawChefHat(Graphics g, int i, int j) {
        int scale = 2; // facteur d'agrandissement

        int hatWidth = 30 * scale;
        int hatHeight = 20 * scale;
        int bandHeight = 6 * scale;

        // Partie supérieure bombée (blanc clair)
        g.setColor(new Color(245, 245, 245));
        g.fillOval(i, j, hatWidth, hatHeight);

        // Contour supérieur (gris clair)
        g.setColor(new Color(200, 200, 200));
        g.drawOval(i, j, hatWidth, hatHeight);

        // Ombres internes pour effet de volume
        g.setColor(new Color(220, 220, 220));
        g.fillOval(i + scale, j + scale, hatWidth - 2 * scale, hatHeight - 2 * scale);

        // Bande de base (blanc plus foncé)
        g.setColor(new Color(230, 230, 230));
        g.fillRect(i + 4 * scale, j + hatHeight - bandHeight / 2, hatWidth - 8 * scale, bandHeight);

        // Ombre sous la bande (gris foncé)
        g.setColor(new Color(160, 160, 160));
        g.drawLine(i + 4 * scale, j + hatHeight - bandHeight / 2 + bandHeight,
                i + hatWidth - 4 * scale, j + hatHeight - bandHeight / 2 + bandHeight);

        // Petits plis verticaux pour texture
        g.setColor(new Color(180, 180, 180));
        for (int k = 0; k < 5; k++) {
            int x = i + (hatWidth / 5) * k + scale;
            g.drawLine(x, j + scale, x, j + hatHeight - bandHeight);
        }
    }

    public static void drawMustache(Graphics g, int i, int j) {
        int scale = 2; // pixel scaling factor

        // Colors
        Color darkBrown = new Color(60, 40, 20);
        Color highlight = new Color(100, 80, 60);

        // Left wing
        g.setColor(darkBrown);
        g.fillArc(i + 12, j, 12 * scale, 8 * scale, 0, 180);

        // Right wing
        g.fillArc(i - 12 + 28 * scale, j, 12 * scale, 8 * scale, 0, 180);

        // Slim center bridge (1/3 of original width)
        int bridgeWidth = 16 * scale / 3; // previously 16 * scale
        g.fillRect(i + 12 * scale + (16 * scale - bridgeWidth) / 2, j + 3 * scale, bridgeWidth, 2 * scale);

        // Highlights
        g.setColor(highlight);
        //g.fillRect(i + 14 * scale, j + 4 * scale, 4 * scale, scale);
        //g.fillRect(i + 22 * scale, j + 4 * scale, 4 * scale, scale);
    }

    public static void drawTopViewPot(Graphics g, int i, int j) {
        int offsety = 30;
        int offsetx = 30;
        int diameter = 60;
        int rimThickness = 6;
        int handleWidth = 10;
        int handleHeight = 20;

        // Corps extérieur (gris foncé)
        g.setColor(new Color(100, 100, 100));
        g.fillOval(offsetx+i, j+offsety, diameter, diameter);

        // Intérieur (gris clair)
        g.setColor(new Color(180, 180, 180));
        g.fillOval(offsetx+ i + rimThickness, j + rimThickness + offsety, diameter - 2 * rimThickness, diameter - 2 * rimThickness);

        // Ombre centrale (gris moyen)
        g.setColor(new Color(140, 140, 140));
        g.fillOval(offsetx+i + diameter / 4, j + diameter / 4+offsety, diameter / 2, diameter / 2);

        // Poignée gauche
        g.setColor(new Color(80, 80, 80));
        g.fillRect(offsetx+i - handleWidth, j + diameter / 2 - handleHeight / 2+offsety, handleWidth, handleHeight);

        // Poignée droite
        g.fillRect(offsetx+i + diameter, j + diameter / 2 - handleHeight / 2+offsety, handleWidth, handleHeight);
    }

    public static void drawPotWithPasta(Graphics g, int i, int j) {
        drawPotWithWater(g, i, j);
        int offsety = 30;
        int offsetx = 30;
        int diameter = 60;
        int rim = 6;


        // Pâtes (formes jaunes désordonnées)
        g.setColor(new Color(255, 220, 100)); // jaune pâle
        Random rand = new Random(0);
        for (int k = 0; k < 10; k++) {
            int px = i + rim + 8 + rand.nextInt(diameter - 2 * rim - 16);
            int py = j + rim + 8 + rand.nextInt(diameter - 2 * rim - 16);
            int w = 5 + rand.nextInt(3); // largeur aléatoire
            int h = 2 + rand.nextInt(2); // hauteur aléatoire
            g.fillRect(px+offsetx, py+offsety, w, h);
        }
    }
    public static void drawPotWithMeat(Graphics g, int i, int j) {
        drawTopViewPot(g,i,j);
        drawIngredientInMiddle(g,i,j, Ingredient.RAW_MEAT);
    }
    public static void drawPotWithCookedMeat(Graphics g, int i, int j) {
        drawTopViewPot(g,i,j);
        drawIngredientInMiddle(g,i,j, Ingredient.COOKED_MEAT);
    }

    public static void drawPotWithPotatoWater(Graphics g, int i, int j) {
        drawPotWithWater(g,i,j);
        drawIngredientInMiddle(g,i,j,Ingredient.SLICED_POTATO);
    }

    public static void drawIngredientInMiddle(Graphics g, int i, int j, Ingredient ingredient) {
        int offsetx = 45;
        int offsety = 45;
        switch(ingredient) {
            case COOKED_PASTA :
                drawCookedPasta(g,i+offsetx,j+offsety);
                break;
            case RAW_MEAT :
                drawRawMeat(g,i+offsetx,j+offsety);
                break;
            case COOKED_MEAT:
                drawCookedMeat(g,i+offsetx,j+offsety);
                break;
            case SLICED_POTATO:
                drawSlicedPotato(g,i+offsetx,j+offsety);
                break;
            case COOKED_POTATO:
                drawCookedPotato(g,i+offsetx,j+offsety);
                break;
            case FRIED_POTATO:
                drawFriedPotato(g,i+offsetx,j+offsety);
                break;
        }
    }

    public static void drawPotWithPotatoOil(Graphics g, int i, int j) {
        drawPotWithOil(g,i,j);
        drawIngredientInMiddle(g,i,j,Ingredient.SLICED_POTATO);
    }

    public static void drawPotWithWater(Graphics g, int i, int j) {
        int offsety = 30;
        int offsetx = 30;
        int diameter = 60;
        int rim = 6;
        int handleWidth = 10;
        int handleHeight = 20;

        // Corps extérieur
        g.setColor(new Color(80, 80, 80)); // gris foncé
        g.fillOval(i + offsetx, j + offsety, diameter, diameter);

        // Intérieur (eau)
        g.setColor(new Color(100, 150, 200)); // bleu clair
        g.fillOval(i + rim + offsetx, j + rim+offsety, diameter - 2 * rim, diameter - 2 * rim);


        // Poignée gauche
        g.setColor(new Color(60, 60, 60));
        g.fillRect(i - handleWidth+offsetx, j + diameter / 2 - handleHeight / 2+offsety, handleWidth, handleHeight);

        // Poignée droite
        g.fillRect(i + diameter+offsetx, j + diameter / 2 - handleHeight / 2+offsety, handleWidth, handleHeight);
    }

    public static void drawPotWithOil(Graphics g, int i, int j) {
        int offsety = 30;
        int offsetx = 30;
        int diameter = 60;
        int rim = 6;
        int handleWidth = 10;
        int handleHeight = 20;

        // Corps extérieur
        g.setColor(new Color(80, 80, 80)); // gris foncé
        g.fillOval(i + offsetx, j + offsety, diameter, diameter);

        // Intérieur (huile)
        g.setColor(new Color(97, 91, 25)); // bleu clair
        g.fillOval(i + rim + offsetx, j + rim+offsety, diameter - 2 * rim, diameter - 2 * rim);


        // Poignée gauche
        g.setColor(new Color(60, 60, 60));
        g.fillRect(i - handleWidth+offsetx, j + diameter / 2 - handleHeight / 2+offsety, handleWidth, handleHeight);

        // Poignée droite
        g.fillRect(i + diameter+offsetx, j + diameter / 2 - handleHeight / 2+offsety, handleWidth, handleHeight);

    }

    public static void drawSink(Graphics g, int i, int j) {
        int sinkSize = 100;
        int rim = 6;

        // Évier (gris foncé)
        g.setColor(new Color(90, 90, 90));
        g.fillRect(i+7, j+4, sinkSize, sinkSize);

        // Eau (bleu clair)
        g.setColor(new Color(150, 200, 230));
        g.fillRect(i + rim+7, j + rim+4, sinkSize - 2 * rim, sinkSize - 2 * rim);

        // Robinet (gris métal)
        g.setColor(new Color(160, 160, 160));
        int faucetWidth = 10;
        int faucetHeight = 40; // allongé (au lieu de 20)
        int faucetX = i + sinkSize / 2 - faucetWidth / 2;
        int faucetY = j - faucetHeight;

        // Tuyau vertical
        g.fillRect(faucetX, faucetY, faucetWidth, faucetHeight);

        // Courbure horizontale
        g.fillRect(faucetX + faucetWidth, faucetY + 4, 12, 6);

        // Bout du robinet
        g.setColor(new Color(120, 120, 120));
        g.fillOval(faucetX + faucetWidth + 10, faucetY + 3, 6, 6);

    }

    public static void drawOilSink(Graphics g, int i, int j) {
        int sinkSize = 100;
        int rim = 6;

        // Évier (gris foncé)
        g.setColor(new Color(90, 90, 90));
        g.fillRect(i+7, j+4, sinkSize, sinkSize);

        // Huile (jaune foncé)
        g.setColor(new Color(83, 79, 18));
        g.fillRect(i + rim+7, j + rim+4, sinkSize - 2 * rim, sinkSize - 2 * rim);

        // Robinet (gris métal)
        g.setColor(new Color(160, 160, 160));
        int faucetWidth = 10;
        int faucetHeight = 40; // allongé (au lieu de 20)
        int faucetX = i + sinkSize / 2 - faucetWidth / 2;
        int faucetY = j - faucetHeight;

        // Tuyau vertical
        g.fillRect(faucetX, faucetY, faucetWidth, faucetHeight);

        // Courbure horizontale
        g.fillRect(faucetX + faucetWidth, faucetY + 4, 12, 6);

        // Bout du robinet
        g.setColor(new Color(120, 120, 120));
        g.fillOval(faucetX + faucetWidth + 10, faucetY + 3, 6, 6);
    }

    public static void drawDirtyPlate(Graphics g, int i, int j) {

        int sinkSize = 100;
        // Assiette agrandie de 33 %
        int basePlateSize = 40;
        int plateSize = (int) (basePlateSize * 1.5); // ≈53

        int plateX = i + sinkSize / 2 - plateSize / 2;
        int plateY = j + sinkSize / 2 - plateSize / 2;

        // Assiette (blanc cassé)
        g.setColor(new Color(240, 240, 240));
        g.fillOval(plateX, plateY, plateSize, plateSize);

        // Contour de l’assiette
        g.setColor(new Color(200, 200, 200));
        g.drawOval(plateX, plateY, plateSize, plateSize);

        // Taches de saleté (x4)
        Random rand = new Random(0);
        for (int k = 0; k < 16; k++) {
            int dirtX = plateX + 10 + rand.nextInt(plateSize - 20);
            int dirtY = plateY + 10 + rand.nextInt(plateSize - 20);
            int dirtW = 6 + rand.nextInt(4);
            int dirtH = 4 + rand.nextInt(3);

            // Couleur aléatoire : brun ou vert sale
            if (rand.nextBoolean()) {
                g.setColor(new Color(160, 100, 60)); // brun
            } else {
                g.setColor(new Color(120, 160, 100)); // vert sale
            }
            g.fillOval(dirtX, dirtY, dirtW, dirtH);
        }
    }

    public static void drawWholeTomato(Graphics g, int i, int j) {
        int w = 30, h = 25;

        // Corps rouge
        g.setColor(new Color(220, 30, 30));
        g.fillOval(i, j, w, h);

        // Ombre
        g.setColor(new Color(180, 20, 20));
        g.fillOval(i + 4, j + 4, w - 8, h - 8);

        // Tige verte
        g.setColor(new Color(40, 120, 40));
        g.fillRect(i + w / 2 - 2, j - 4, 4, 6);
        g.drawLine(i + w / 2, j, i + w / 2 - 6, j - 6);
        g.drawLine(i + w / 2, j, i + w / 2 + 6, j - 6);
    }

    public static void drawHalfTomato(Graphics g, int i, int j) {
        int w = 30, h = 25;

        // Demi-tomate rouge
        g.setColor(new Color(220, 30, 30));
        g.fillArc(i, j, w, h, 0, 180);

        // Intérieur (chair)
        g.setColor(new Color(255, 150, 150));
        g.fillArc(i + 4, j + 2, w - 8, h / 2, 0, 180);

        // Graines (jaune pâle)
        g.setColor(new Color(255, 230, 100));
        g.fillOval(i + 8, j + 6, 4, 4);
        g.fillOval(i + 18, j + 6, 4, 4);
    }
    public static void drawSlicedTomato(Graphics g, int i, int j) {
        drawHalfTomato(g,i,j);
        drawHalfTomato(g,i,j+10);
    }


    public static void drawRawMeat(Graphics g, int i, int j) {
        int w = 30, h = 20;

        // Contour de la viande (rouge foncé)
        g.setColor(new Color(150, 30, 30));
        g.fillRoundRect(i, j, w, h, 8, 8);

        // Chair intérieure (rose)
        g.setColor(new Color(255, 120, 120));
        g.fillRoundRect(i + 3, j + 3, w - 6, h - 6, 6, 6);

        // Os/blanc au centre
        g.setColor(new Color(240, 240, 200));
        g.fillOval(i + w/2 - 4, j + h/2 - 4, 8, 8);

        // Petites fibres (traits plus clairs)
        g.setColor(new Color(255, 180, 180));
        g.fillRect(i + 8, j + 6, 3, 2);
        g.fillRect(i + 14, j + 10, 4, 2);
        g.fillRect(i + 20, j + 7, 3, 2);
    }

    public static void drawCookedMeat(Graphics g, int i, int j) {
        int w = 30, h = 20;

        // Contour de la viande (marron foncé)
        g.setColor(new Color(50, 23, 2));
        g.fillRoundRect(i, j, w, h, 8, 8);

        // Chair intérieure (marron)
        g.setColor(new Color(59, 28, 28));
        g.fillRoundRect(i + 3, j + 3, w - 6, h - 6, 6, 6);

        // Os/blanc au centre
        g.setColor(new Color(240, 240, 200));
        g.fillOval(i + w/2 - 4, j + h/2 - 4, 8, 8);

        // Petites fibres (traits plus clairs)
        g.setColor(new Color(255, 180, 180));
        g.fillRect(i + 8, j + 6, 3, 2);
        g.fillRect(i + 14, j + 10, 4, 2);
        g.fillRect(i + 20, j + 7, 3, 2);
    }

    public static void drawBread(Graphics g, int i, int j) {
        int w = 40, h = 25;

        // Croûte (brun doré)
        g.setColor(new Color(200, 150, 80));
        g.fillRoundRect(i, j, w, h, 12, 12);

        // Intérieur (mie, plus clair)
        g.setColor(new Color(245, 220, 160));
        g.fillRoundRect(i + 4, j + 4, w - 8, h - 8, 10, 10);

        // Petits détails de texture
        g.setColor(new Color(220, 190, 130));
        g.fillOval(i + 10, j + 10, 3, 3);
        g.fillOval(i + 20, j + 12, 2, 2);
        g.fillOval(i + 26, j + 8, 2, 2);
    }

    public static void drawSlicedBread(Graphics g, int i, int j) {
        int w = 40, h = 25;

        // Croûte (brun doré)
        g.setColor(new Color(200, 150, 80));
        g.fillArc(i, j, w, h, 0, 180); // moitié supérieure
        g.fillArc(i, j, w, h, 180, 180); // moitié inférieure

        // Mie (intérieur clair)
        g.setColor(new Color(245, 220, 160));
        g.fillArc(i + 4, j + 2, w - 8, h/2, 0, 180); // haut
        g.fillArc(i + 4, j + h/2, w - 8, h/2 - 2, 180, 180); // bas

        // Petits détails de texture
        g.setColor(new Color(220, 190, 130));
        g.fillOval(i + 12, j + 6, 3, 3);
        g.fillOval(i + 22, j + h/2 + 4, 2, 2);
    }

    // Salade (feuille verte)
    public static void drawSalad(Graphics g, int i, int j) {
        int w = 40, h = 30;

        // Vert principal
        g.setColor(new Color(80, 180, 80));
        g.fillOval(i, j, w, h);

        // Nervures plus foncées
        g.setColor(new Color(40, 120, 40));
        g.drawLine(i + w/2, j + 5, i + w/2, j + h - 5);
        g.drawLine(i + 10, j + 10, i + w - 10, j + h - 10);
        g.drawLine(i + 10, j + h - 10, i + w - 10, j + 10);

        // Petites irrégularités (bord dentelé)
        g.setColor(new Color(60, 160, 60));
        g.fillOval(i - 4, j + 8, 8, 8);
        g.fillOval(i + w - 4, j + 12, 8, 8);
    }

    public static void drawWashedSalad(Graphics g, int i, int j) {
        int w = 40, h = 30;

        // Vert plus clair/brillant
        g.setColor(new Color(120, 220, 120));
        g.fillOval(i, j, w, h);

        // Nervures
        g.setColor(new Color(60, 160, 60));
        g.drawLine(i + w/2, j + 5, i + w/2, j + h - 5);
        g.drawLine(i + 12, j + 8, i + w - 12, j + h - 8);

        // Gouttelettes d’eau (bleu clair)
        g.setColor(new Color(180, 220, 255));
        g.fillOval(i + 8, j + 6, 4, 4);
        g.fillOval(i + 20, j + 12, 3, 3);
        g.fillOval(i + 28, j + 18, 5, 5);

        // Reflets (blanc semi-transparent)
        g.setColor(new Color(255, 255, 255, 180));
        g.fillOval(i + 14, j + 10, 6, 3);
    }

    // Pomme de terre entière
    public static void drawPotato(Graphics g, int i, int j) {
        int w = 35, h = 25;

        // Peau (brun clair)
        g.setColor(new Color(180, 140, 90));
        g.fillOval(i, j, w, h);

        // Petites taches
        g.setColor(new Color(150, 110, 70));
        g.fillOval(i + 8, j + 10, 3, 3);
        g.fillOval(i + 18, j + 6, 2, 2);
        g.fillOval(i + 22, j + 14, 3, 3);
    }

    // Pomme de terre coupée en tranches
    public static void drawSlicedPotato(Graphics g, int i, int j) {
        int w = 35, h = 25;

        g.setColor(new Color(240, 220, 160));
        g.fillOval(i, j, w, h);

    }


    // Pomme de terre frite (style frites)
    public static void drawFriedPotato(Graphics g, int i, int j) {
        // Plusieurs bâtonnets jaunes dorés
        g.setColor(new Color(255, 210, 80));
        g.fillRect(i, j, 6, 18);
        g.fillRect(i + 8, j + 2, 6, 16);
        g.fillRect(i + 16, j + 4, 6, 14);
        g.fillRect(i + 24, j + 1, 6, 17);

        // Ombres plus foncées
        g.setColor(new Color(220, 170, 60));
        g.fillRect(i, j + 14, 6, 4);
        g.fillRect(i + 8, j + 12, 6, 4);
        g.fillRect(i + 16, j + 10, 6, 4);
        g.fillRect(i + 24, j + 13, 6, 4);
    }

    // Pomme de terre cuite (chair visible)
    public static void drawCookedPotato(Graphics g, int i, int j) {
        int w = 35, h = 25;

        // Chair ouverte (jaune clair)
        g.setColor(new Color(250, 230, 150));
        g.fillArc(i + 5, j + 5, w - 10, h - 10, 30, 120);
        g.fillArc(i + 5, j + 5, w - 10, h - 10, 210, 120);
    }







    public static void drawRawPasta(Graphics g, int i, int j) {
        drawCompactPasta(g, i, j, 10);
    }

    public static void drawCookedPasta(Graphics g, int i, int j) {
        drawCompactPasta(g, i, j, 0);
    }

    public static void drawCompactPasta(Graphics g, int i, int j,int seed) {
        int w = 40, h = 35;
        Random rand = new Random(seed);

        // Fond transparent ou assiette (facultatif)
        g.setColor(new Color(255, 245, 200)); // beige clair
        g.fillOval(i, j, w, h);

        // Pâtes (jaune pâle)
        g.setColor(new Color(209, 162, 18));

        // Spaghettis en courbes
        for (int k = 0; k < 24; k++) {
            int arcX = i + 4 + rand.nextInt(17);
            int arcY = j + 4 + rand.nextInt(17);
            int arcW = 12 + rand.nextInt(13);
            int arcH = 6 + rand.nextInt(9);
            int startAngle = rand.nextInt(360);
            int arcAngle = 180 + rand.nextInt(90);
            g.drawArc(arcX, arcY, arcW, arcH, startAngle, arcAngle);
        }
    }



    public static void drawMinimalStove(Graphics g, int i, int j) {

        int offsetx = 6;
        int offsety = 25;
        int width = 100;
        int height = 60;
        int burnerSize = 24;

        // Corps de la gazinière
        g.setColor(new Color(180, 180, 180)); // gris clair
        g.fillRect(i+offsetx, j+offsety, width, height);

        // Brûleur central
        int burnerX = i + width / 2 - burnerSize / 2 + offsetx;
        int burnerY = j + height / 2 - burnerSize / 2 + offsety;
        g.setColor(Color.BLACK);
        g.fillOval(burnerX, burnerY, burnerSize, burnerSize);

        // Grille en croix
        g.setColor(new Color(100, 100, 100));
        g.drawLine(burnerX + burnerSize / 2, burnerY, burnerX + burnerSize / 2, burnerY + burnerSize);
        g.drawLine(burnerX, burnerY + burnerSize / 2, burnerX + burnerSize, burnerY + burnerSize / 2);

        // Bouton de commande à droite
        g.setColor(new Color(60, 60, 60));
        g.fillOval(i + width - 15+offsetx, j + height / 2 - 5+offsety, 10, 10);
    }


    public static void drawProgressBar(Graphics g, int x, int y, int percent) {
        int width = 60;
        int height = 8;

        // Encadré blanc
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);

        // Bord noir (facultatif pour contraste)
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // Remplissage vert selon le pourcentage
        int filledWidth = (int)(width * percent / 100.0);
        g.setColor(new Color(0, 180, 0)); // vert vif
        g.fillRect(x, y, filledWidth, height);
    }

    public static void drawRecipeProgress(Graphics g, int x, int y, Recipe recipe) {

        int offsetx = 5+x;
        int offsety = 5+y;

        int width = 120+44;
        int height = 50;

        // Encadré blanc
        g.setColor(Color.WHITE);
        g.fillRect(offsetx, offsety, width, height);

        // Bord Gris
        g.setColor(Color.GRAY);
        g.drawRect(offsetx, offsety, width, height);

        // Image Recette
        for (int i = 0 ; i < recipe.getIngredients().size() ; i++) {
            switch (recipe.getIngredients().get(i)) {
                case Ingredient.COOKED_PASTA -> drawCookedPasta(g,offsetx+62+(i*9),offsety+8+(i*9));
                case Ingredient.COOKED_MEAT -> drawCookedMeat(g,offsetx+62+(i*9),offsety+8+(i*9));
                case Ingredient.SLICED_TOMATO -> drawSlicedTomato(g,offsetx+62+(i*9),offsety+8+(i*9));
                case Ingredient.SLICED_BREAD -> drawSlicedBread(g,offsetx+62+(i*9),offsety+8+(i*9));
                case Ingredient.WASHED_SALAD -> drawWashedSalad(g,offsetx+62+(i*9),offsety+8+(i*9));
                case Ingredient.FRIED_POTATO -> drawFriedPotato(g,offsetx+62+(i*9),offsety+8+(i*9));
                case Ingredient.COOKED_POTATO -> drawCookedPotato(g,offsetx+62+(i*9),offsety+8+(i*9));
            }
        }

        // Encadré Gris détail
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(offsetx, offsety+height, width, height);

        // Bord Noir
        g.setColor(Color.BLACK);
        g.drawRect(offsetx, offsety+height, width, height);

        // Image ingrédient
        for (int i = 0 ; i < recipe.getIngredients().size() ; i++) {
            switch (recipe.getIngredients().get(i)) {
                case Ingredient.COOKED_PASTA -> drawRawPasta(g,offsetx+(i*44),offsety+height+8);
                case Ingredient.COOKED_MEAT -> drawRawMeat(g,offsetx+18+(i*44),offsety+height+8);
                case Ingredient.SLICED_TOMATO -> drawWholeTomato(g,offsetx+(i*44),offsety+height+15);
                case Ingredient.SLICED_BREAD -> drawBread(g,offsetx+18+(i*44),offsety+height+8);
                case Ingredient.WASHED_SALAD -> drawSalad(g,offsetx+18+(i*44),offsety+height+8);
                case Ingredient.FRIED_POTATO -> drawPotato(g,offsetx+18+(i*44),offsety+height+8);
                case Ingredient.COOKED_POTATO -> drawPotato(g,offsetx+18+(i*44),offsety+height+8);
            }
        }
        //drawWholeTomato(g, offsetx+18, offsety+height+15);
        //drawRawPasta(g, offsetx+62, offsety+height+8);
    }
}
