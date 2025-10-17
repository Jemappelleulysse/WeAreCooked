package Furnitures;

import Ingredient.Ingredient;

public class CuttingBoard extends Furniture {

    private Ingredient ingredientOn = null;
    public int cutNb = 3;
    public int currNb = 0;

    /// CONSTRUCTOR ///
    public CuttingBoard(int posX, int posY) {
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

        if(hasSomethingOn()) {      // QUELQUE CHOSE SUR LA PLANCHE
            if(getIngredientOn().equals(Ingredient.TOMATO)) {
                if (currNb < cutNb-1) {
                    currNb++;
                    returnedIngredient = ingredientInHand;
                } else {
                    setIngredientOn(Ingredient.SLICED_TOMATO);
                    returnedIngredient = ingredientInHand;
                    currNb = 0;
                }

            } else {
                if(ingredientInHand == null) {
                    returnedIngredient = getIngredientOn();
                    setIngredientOn(null);
                } else {
                    returnedIngredient = ingredientInHand;
                }
            }
        } else {    // RIEN SUR LA PLANCHE
            if(ingredientInHand != null) {
                setIngredientOn(ingredientInHand);
            }
        }
        return returnedIngredient;
    }
}
