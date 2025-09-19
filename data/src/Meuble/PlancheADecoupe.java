package Meuble;

import Ingredient.Ingredient;

public class PlancheADecoupe extends Meuble {

    /// CONSTRUCTOR ///
    public PlancheADecoupe(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }


    /// METHODS ///
    public Ingredient couper(Ingredient ingredient) {
        switch (ingredient) {
            case Ingredient.TOMATE :
                return Ingredient.TOMATE_COUPE;
            case default :
                return ingredient;
        }
    }
}
