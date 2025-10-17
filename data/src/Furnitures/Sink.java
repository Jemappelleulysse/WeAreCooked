package Furnitures;

import Ingredient.Ingredient;

public class Sink extends Furniture {

    private Ingredient ingredientOn = null;

    /// CONSTRUCTOR ///
    public Sink(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }


    /// GETTER ///
    public boolean hasSomethingOn() {
        return ingredientOn != null;
    }


    public Ingredient getIngredientOn() {
        return ingredientOn;
    }


    /// SETTER ///
    public void setIngredientOn(Ingredient ingredientOn) {
        this.ingredientOn = ingredientOn;
    }


    /// METHODS ///
    @Override
    public Ingredient interact(Ingredient ingredientInHand) {

        Ingredient returnedIngredient = null;

        if (ingredientInHand == Ingredient.EMPTY_POT) {
            returnedIngredient = Ingredient.FULL_POT;
        } else {
            returnedIngredient = ingredientInHand;
        }
        return returnedIngredient;
    }
}
