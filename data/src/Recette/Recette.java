package Recette;

import Ingredient.Ingredient;

import java.util.ArrayList;

public abstract class Recette {

    protected ArrayList<Ingredient> allIngredients;

    protected Recette() {
        allIngredients = new ArrayList<>();
    }

    public ArrayList<Ingredient> getAllIngredients() {
        return allIngredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recette recette = (Recette) o;
        for (Ingredient ingredient : allIngredients) {
            boolean ingredientFound = false;
            for (Ingredient ingredient2 : recette.getAllIngredients()) {
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
        return allIngredients.toString();
    }
}
