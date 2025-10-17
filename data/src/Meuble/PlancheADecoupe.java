package Meuble;

import Ingredient.Ingredient;

public class PlancheADecoupe extends Meuble {

    private Ingredient ingredientOn = null;
    public int nbDecoupe = 3;
    public int nbactuel = 0;

    /// CONSTRUCTOR ///
    public PlancheADecoupe(int posX, int posY) {
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
            if(getIngredientOn().equals(Ingredient.TOMATE)) {
                if (nbactuel < nbDecoupe-1) {
                    nbactuel++;
                    returnedIngredient = ingredientInHand;
                } else {
                    setIngredientOn(Ingredient.TOMATE_COUPE);
                    returnedIngredient = ingredientInHand;
                    nbactuel = 0;
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
