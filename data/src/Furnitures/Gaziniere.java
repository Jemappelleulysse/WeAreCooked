package Meuble;

import Ingredient.Ingredient;

public class Gaziniere extends Meuble {

    private Ingredient pot = Ingredient.POT_VIDE;
    private Ingredient ingredientOn = null;

    public float tempsCuisson = 4;
    public float tempsActuel = 0;


    /// CONSTRUCTOR ///
    public Gaziniere(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }

    @Override
    public void update(float temps) {
        if (pot == Ingredient.POT_REMPLI && ingredientOn == Ingredient.PATES) {
            tempsActuel += temps;
            if (tempsActuel >= tempsCuisson) {
                tempsActuel = tempsCuisson+0.001f;
            }
        }
    }

    /// GETTER ///
    public boolean hasSomethingOn() {
        return ingredientOn != null;
    }

    public boolean hasAPot() {
        return pot != null;
    }

    public Ingredient getPot() {
        return pot;
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

        if (!hasAPot()) {
            if (ingredientInHand == Ingredient.POT_VIDE || ingredientInHand == Ingredient.POT_REMPLI) {
                pot = ingredientInHand;
            }
        } else if (hasSomethingOn()) {
            if (getIngredientOn().equals(Ingredient.PATES)) {
                System.out.println(tempsActuel);
                if (tempsActuel >= tempsCuisson) {
                    setIngredientOn(Ingredient.PATES_CUITES);
                    returnedIngredient = ingredientInHand;
                    pot = Ingredient.POT_VIDE;

                } else {
                    returnedIngredient = ingredientInHand;
                }
            } else {
                if (ingredientInHand == null) {
                    returnedIngredient = getIngredientOn();
                    setIngredientOn(null);
                } else {
                    returnedIngredient = ingredientInHand;
                }
            }
        } else {
            if (ingredientInHand == Ingredient.PATES && pot == Ingredient.POT_REMPLI) {
                setIngredientOn(ingredientInHand);

            } else if (ingredientInHand == null) {
                returnedIngredient = pot;
                pot = null;
            } else {
                returnedIngredient = ingredientInHand;
            }
        }
        return returnedIngredient;
    }
}
