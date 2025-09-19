package Meuble;

import Ingredient.Ingredient;
import Recette.Recette;

import javax.swing.*;
import java.util.ArrayList;

public class Comptoir extends Meuble{

    private Recette currentRecette;
    private boolean completed = false;
    public ArrayList<Ingredient> currentIngredients =  new ArrayList<Ingredient>();

    /// CONSTRUCTOR ///
    public Comptoir(int posX, int posY, Recette recette) {
        this.setPosX(posX);
        this.setPosY(posY);
        if (recette != null) {
            this.currentRecette = recette;
        } else  {
            throw new NullPointerException("Recette is null");
        }
    }

    public Comptoir(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }


    /// GETTERS ///
    public Recette getCurrentRecette() {
        return currentRecette;
    }


    /// SETTERS ///
    public void setCurrentRecette(Recette currentRecette) {
        if (currentRecette != null) {
            this.currentRecette = currentRecette;
        }  else {
            throw new NullPointerException("Recette can't be null");
        }
    }


    /// METHODS ///
    void checkRecetteCompletion() {
        if(currentRecette.equals(currentIngredients)) {
            if (completed) {
                currentIngredients.clear();
                JOptionPane.showMessageDialog(null,"You win");
            }
            completed = !completed;

        }
    }

    @Override
    public Ingredient interact(Ingredient ingredient){
        if(ingredient == null){
            checkRecetteCompletion();
            return null;
        } else {
            if(currentRecette.getAllIngredients().contains(ingredient)){
                if(currentIngredients.contains(ingredient)){
                    checkRecetteCompletion();
                    return ingredient;
                } else {
                    currentIngredients.add(ingredient);
                    checkRecetteCompletion();
                    return null;
                }
            } else {
                checkRecetteCompletion();
                return ingredient;
            }
        }
    }
}
