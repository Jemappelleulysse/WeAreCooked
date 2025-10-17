package View;

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
}
