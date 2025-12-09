package Recipes;

import HoldableObjects.Ingredient;

import java.util.ArrayList;

public abstract class Recipe {

    protected ArrayList<Ingredient> ingredients;

    protected RecipeEnum recipeType;

    protected Recipe(RecipeEnum recipeType) {
        this.recipeType = recipeType;
        ingredients = new ArrayList<>();
    }

    public RecipeEnum getRecipeType() { return recipeType; }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public boolean isComplete(ArrayList<Ingredient> validIngredients) {

        if (validIngredients.size() != ingredients.size()) return false;

        return validIngredients.containsAll(ingredients);
    }

    @Override
    public String toString() {
        return ingredients.toString();
    }
}
