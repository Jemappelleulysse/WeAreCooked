package Furnitures;

import HoldableObjects.*;

public class IngredientChest extends Furniture {

    private final Ingredient ingredient;

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public IngredientChest(int posX, int posY, Ingredient ingredient) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.ingredient = ingredient;
    }

    /// ////// ///
    /// GETTER ///
    /// ////// ///
    public Ingredient getIngredient() {
        return ingredient;
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
    @Override
    public HoldableObject interact(HoldableObject object) {
        if (object == null) {
            return this.ingredient;
        } else if (object != this.ingredient) {
            return object;
        } else {
            return null;
        }
    }
}