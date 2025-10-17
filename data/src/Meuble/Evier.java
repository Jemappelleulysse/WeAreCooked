package Meuble;

import Ingredient.Ingredient;

public class Evier extends Meuble {

    private Ingredient ingredientOn = null;

    /// CONSTRUCTOR ///
    public Evier(int posX, int posY) {
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

        if (ingredientInHand == Ingredient.POT_VIDE) {
            returnedIngredient = Ingredient.POT_REMPLI;
        } else {
            returnedIngredient = ingredientInHand;
        }
        return returnedIngredient;
    }
}
