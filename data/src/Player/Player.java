package Player;

import Ingredient.Ingredient;
import Utils.Pair;
import View.View;

import java.util.ArrayList;

public class Player {

    private int posX;
    private int posY;
    private boolean isHoldingSomething =  false;
    private Ingredient ingredientHolded = null;

    public Player(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    //// GETTERS ////
    public int getPosX() {
        return posX;
    }

    public int getPosY() { return posY; }

    public Pair getPos() { return new Pair(posX, posY);}

    public boolean isHoldingSomething() {
        return isHoldingSomething;
    }

    public Ingredient getIngredientHolded() {
        return ingredientHolded;
    }

    //// SETTERS ////
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setHoldingSomething(boolean holdingSomething) {
        isHoldingSomething = holdingSomething;
    }

    public void setIngredientHolded(Ingredient ingredientHolded) {
        this.ingredientHolded = ingredientHolded;
    }




}