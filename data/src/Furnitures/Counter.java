package Furnitures;

import Ingredient.Ingredient;
import Recipes.Recipe;

import javax.swing.*;
import java.util.ArrayList;

public class Counter extends Furniture{

    private Recipe currentRecipe;
    private boolean completed = false;
    public ArrayList<Ingredient> currentIngredients =  new ArrayList<Ingredient>();

    /// CONSTRUCTOR ///
    public Counter(int posX, int posY, Recipe recipe) {
        this.setPosX(posX);
        this.setPosY(posY);
        if (recipe != null) {
            this.currentRecipe = recipe;
        } else  {
            throw new NullPointerException("Recipe is null");
        }
    }

    public Counter(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }


    /// GETTERS ///
    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }


    /// SETTERS ///
    public void setCurrentRecipe(Recipe currentRecipe) {
        if (currentRecipe != null) {
            this.currentRecipe = currentRecipe;
        }  else {
            throw new NullPointerException("Recipe can't be null");
        }
    }


    /// METHODS ///
    void checkRecipeCompletion() {
        if(currentRecipe.equals(currentIngredients)) {
            if (completed) {
                currentIngredients.clear();
                //JOptionPane.showMessageDialog(null,"You win");
            }
            completed = !completed;
        }
    }

    @Override
    public Ingredient interact(Ingredient ingredient){
        if(ingredient == null){
            checkRecipeCompletion();
            return null;
        } else {
            if(currentRecipe.getIngredients().contains(ingredient)){
                if(currentIngredients.contains(ingredient)){
                    checkRecipeCompletion();
                    return ingredient;
                } else {
                    currentIngredients.add(ingredient);
                    checkRecipeCompletion();
                    return null;
                }
            } else {
                checkRecipeCompletion();
                return ingredient;
            }
        }
    }
}
