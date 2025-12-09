package Recipes;

import HoldableObjects.Ingredient;

public class Sandwich extends Recipe {

    public Sandwich() {
        super(RecipeEnum.SANDWICH);
        ingredients.add(Ingredient.SLICED_BREAD);
        ingredients.add(Ingredient.WASHED_SALAD);
        ingredients.add(Ingredient.SLICED_TOMATO);
        ingredients.add(Ingredient.COOKED_MEAT);
    }
}
