package Furnitures;

import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;
import HoldableObjects.KitchenUstensils;
import Model.Model;
import Recipes.Recipe;

import javax.swing.*;
import java.util.ArrayList;

public class Counter extends Furniture {

    Model model;

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public Counter(int posX, int posY, Model model) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.model = model;
    }


    /// /////// ///
    /// METHODS ///
    /// /////// ///

    @Override
    public HoldableObject interact(HoldableObject heldObject){

        if (model.isRecipeFinished()) {
            model.updateRecipe();
            return heldObject;
        }

        if (heldObject instanceof Ingredient) {
            Ingredient heldIngredient = (Ingredient) heldObject;

            if(model.getRecipeIngredients(0).contains(heldIngredient)){
                if(!model.getValidIngredients().contains(heldIngredient)){
                    model.addValidIngredient(heldIngredient);
                    return null;
                }
            }
        }

        return heldObject;
    }
}
