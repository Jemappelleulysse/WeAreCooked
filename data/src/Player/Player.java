package Player;

import Ingredient.Ingredient;
import Utils.Pair;
import View.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
}