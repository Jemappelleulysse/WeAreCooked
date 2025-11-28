package Recipes;

import HoldableObjects.Ingredient;

public class Salad extends Recipe{


    public Salad() {
        super();
        ingredients.add(Ingredient.WASHED_SALAD);
        ingredients.add(Ingredient.COOKED_POTATO);
        ingredients.add(Ingredient.SLICED_TOMATO);
    }

}
