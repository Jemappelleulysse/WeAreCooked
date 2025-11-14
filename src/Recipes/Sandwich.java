package Recipes;

import HoldableObjects.Ingredient;

public class Sandwich extends Recipe {

    public Sandwich() {
        super();
        ingredients.add(Ingredient.SLICED_BREAD);
        ingredients.add(Ingredient.SLICED_TOMATO);
        ingredients.add(Ingredient.WASHED_SALAD);
        ingredients.add(Ingredient.COOKED_MEAT);
    }
}
