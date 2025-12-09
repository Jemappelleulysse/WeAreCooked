package Recipes;

import HoldableObjects.Ingredient;


public class BolognesePasta extends Recipe {

    public BolognesePasta() {
        super(RecipeEnum.BOLOGNESE_PASTA);
        ingredients.add(Ingredient.COOKED_PASTA);
        ingredients.add(Ingredient.SLICED_TOMATO);
    }
}
