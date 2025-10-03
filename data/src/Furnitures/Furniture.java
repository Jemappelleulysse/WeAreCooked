package Furnitures;

import Ingredient.Ingredient;

public abstract class Furniture {

    private int posX;
    private int posY;

    //// GETTERS ////
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }


    //// SETTERS ////
    protected void setPosX(int posX) {
        this.posX = posX;
    }

    protected void setPosY(int posY) {
        this.posY = posY;
    }


    /// METHODS ///
    public abstract Ingredient interact(Ingredient ingredient);
}