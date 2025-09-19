package Meuble;

import Ingredient.Ingredient;

public class PlanDeTravail extends Meuble {

    private Ingredient ingredientOn = null;

    /// CONSTRUCTOR ///
    public PlanDeTravail(int posX, int posY) {
        this.setPosX(posX);
        this.setPosX(posX);
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
    public Ingredient interact(Ingredient ingredient) {

        Ingredient returnedIngredient = null;

        if(ingredient == null) {
            if(hasSomethingOn()) {
                returnedIngredient = this.ingredientOn;
                setIngredientOn(null);
            }
        } else {
            if(hasSomethingOn()) {
                returnedIngredient = ingredient;
            } else  {
                setIngredientOn(ingredient);
            }
        }
        return returnedIngredient;
    }
}