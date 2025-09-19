package Recette;

import Ingredient.Ingredient;

import java.util.ArrayList;

public class Sandwich extends Recette {

    public Sandwich() {
        super();
        allIngredients.add(Ingredient.PAIN);
        allIngredients.add(Ingredient.TOMATE_COUPE);
        allIngredients.add(Ingredient.SALADE);
        allIngredients.add(Ingredient.VIANDE);
    }
}
