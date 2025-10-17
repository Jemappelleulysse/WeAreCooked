package Recipes;

import Ingredient.Ingredient;

import java.util.ArrayList;

public abstract class Recipe {

    protected ArrayList<Ingredient> ingredients;

    protected Recipe() {
        ingredients = new ArrayList<>();
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != ArrayList.class) return false;
        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) o;
        if(ingredients.size() != this.ingredients.size()) return false;
        for (Ingredient ingredient : ingredients) {
            boolean ingredientFound = false;
            for (Ingredient ingredient2 : ingredients) {
                if (ingredient.equals(ingredient2)) {
                    ingredientFound = true;
                    break;
                }
            }
            if (!ingredientFound) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return ingredients.toString();
    }
}
