package Meuble;

import Ingredient.Ingredient;

public class Gaziniere extends Meuble {

    private boolean hasAPot = true;
    private Ingredient ingredientOn = null;

    /// CONSTRUCTOR ///
    public Gaziniere(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }


    /// GETTER ///
    public boolean hasSomethingOn() {
        return ingredientOn != null;
    }

    public boolean isHasAPot() {
        return hasAPot;
    }

    public void setHasAPot(boolean hasAPot) {
        this.hasAPot = hasAPot;
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

        if (!hasAPot) {
            if (ingredientInHand == Ingredient.POT) {
                hasAPot = true;
            }
        } else if (hasSomethingOn()) {
            if (getIngredientOn().equals(Ingredient.PATES)) {
                setIngredientOn(Ingredient.PATES_CUITES);
                returnedIngredient = ingredientInHand;
            } else {
                if (ingredientInHand == null) {
                    returnedIngredient = getIngredientOn();
                    setIngredientOn(null);
                } else {
                    returnedIngredient = ingredientInHand;
                }
            }
        } else {
            if (ingredientInHand == Ingredient.PATES) {
                setIngredientOn(ingredientInHand);
            } else if (ingredientInHand == null) {
                returnedIngredient = Ingredient.POT;
                hasAPot = false;
            }
        }
        return returnedIngredient;
    }
}
