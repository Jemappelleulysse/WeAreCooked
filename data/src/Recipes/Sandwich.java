package Recipes;

import Ingredient.Ingredient;

public class Sandwich extends Recipe {

    public Sandwich() {
        super();
        ingredients.add(Ingredient.BREAD);
        ingredients.add(Ingredient.SLICED_TOMATO);
        ingredients.add(Ingredient.SALAD);
        ingredients.add(Ingredient.MEAT);
    }
}
