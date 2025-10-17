package Furnitures;

import Ingredient.Ingredient;

public class GasStove extends Furniture {

    private Ingredient pot = Ingredient.EMPTY_POT;
    private Ingredient ingredientOn = null;

    public float tempsCuisson = 4;
    public float tempsActuel = 0;


    /// CONSTRUCTOR ///
    public GasStove(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }

    @Override
    public void update(float temps) {
        if (pot == Ingredient.FULL_POT && ingredientOn == Ingredient.PASTA) {
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
            if (ingredientInHand == Ingredient.EMPTY_POT || ingredientInHand == Ingredient.FULL_POT) {
                pot = ingredientInHand;
            }
        } else if (hasSomethingOn()) {
            if (getIngredientOn().equals(Ingredient.PASTA)) {
                System.out.println(tempsActuel);
                if (tempsActuel >= tempsCuisson) {
                    setIngredientOn(Ingredient.COOKED_PASTA);
                    returnedIngredient = ingredientInHand;
                    pot = Ingredient.EMPTY_POT;
                    returnedIngredient = getIngredientOn();
                    setIngredientOn(null);

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
            if (ingredientInHand == Ingredient.PASTA && pot == Ingredient.FULL_POT) {
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
