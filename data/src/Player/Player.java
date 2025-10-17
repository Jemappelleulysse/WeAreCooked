package Player;

import Ingredient.Ingredient;
import Utils.Pair;
import View.View;

import java.util.ArrayList;

public class Player {

    private int posX;
    private int posY;
    private boolean isHoldingSomething =  false;
    private Ingredient ingredientHeld = null;

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public Player(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /// /////// ///
    /// GETTERS ///
    /// /////// ///
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Pair getPos() {
        return new Pair(posX, posY);
    }

    public boolean isHoldingSomething() {
        return isHoldingSomething;
    }

    public Ingredient getIngredientHeld() {
        return ingredientHeld;
    }

    /// /////// ///
    /// SETTERS ///
    /// /////// ///
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setIngredientHeld(Ingredient ingredientHeld) {
        this.ingredientHeld = ingredientHeld;
        isHoldingSomething = ingredientHeld != null;
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
    public void takePath(ArrayList<Pair> path) {
        ArrayList<Pair> actions = Pair.coordsToDirections(path);
        // Timer delay in milliseconds
        int delay = 300;

        ActionListener taskPerformer = new ActionListener() {
            private int counter = 0;

            public void actionPerformed(ActionEvent evt) {
                if (counter >= actions.size()) {
                    ((Timer)evt.getSource()).stop();
                } else {
                    Pair action = actions.get(counter++);
                    ViewController.instance.move(action);
                }
            }
        };

        new Timer(delay, taskPerformer).start();
    }

}