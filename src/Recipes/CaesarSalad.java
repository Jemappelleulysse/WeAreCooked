package Recipes;

import HoldableObjects.Ingredient;

public class CaesarSalad extends Recipe{

    public CaesarSalad() {
        super(RecipeEnum.CAESAR_SALAD);
        ingredients.add(Ingredient.WASHED_SALAD);
        ingredients.add(Ingredient.COOKED_POTATO);
        ingredients.add(Ingredient.SLICED_TOMATO);
    }

}
