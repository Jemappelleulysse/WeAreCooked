package Meuble;

import Ingredient.Ingredient;

public class Coffre extends Meuble {

    private final Ingredient ingredient;

    /// CONSTRUCTOR ///
    public Coffre(int posX, int posY, Ingredient ingredient) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.ingredient = ingredient;
    }


    /// GETTER ///
    public Ingredient getIngredient() {
        return ingredient;
    }


    /// METHODS ///
    @Override
    public Ingredient interact(Ingredient ingredient) {
        if(ingredient == null) {
            return this.ingredient;
        } else {
            return null;
        }
    }
}