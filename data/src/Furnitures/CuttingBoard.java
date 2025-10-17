package Furnitures;

import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;

public class CuttingBoard extends Furniture {

    private Ingredient ingredientOn = null;
    public int cutNb = 3;
    public int currNb = 0;

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public CuttingBoard(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }

    /// ////// ///
    /// GETTER ///
    /// ////// ///
    public boolean hasSomethingOn() {
        return ingredientOn != null;
    }

    public Ingredient getIngredientOn() {
        return ingredientOn;
    }

    /// ////// ///
    /// SETTER ///
    /// ////// ///
    public void setIngredientOn(Ingredient ingredientOn) {
        this.ingredientOn = ingredientOn;
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
    @Override
    public HoldableObject interact(HoldableObject objectInHand) {

        HoldableObject returnedObject = null;

        if(hasSomethingOn()) {      // Il y a un ingrédient sur la planche
            Ingredient ingredientOnBoard = getIngredientOn();
            switch (ingredientOnBoard) {

                case TOMATO:
                    if (currNb < cutNb-1) {
                        currNb++;
                        returnedObject = objectInHand;
                    } else {
                        setIngredientOn(Ingredient.SLICED_TOMATO);
                        returnedObject = objectInHand;
                        currNb = 0;
                    }
                    break;

                case BREAD:
                    if (currNb < cutNb-1) {
                        currNb++;
                        returnedObject = objectInHand;
                    } else {
                        setIngredientOn(Ingredient.SLICED_BREAD);
                        returnedObject = objectInHand;
                        currNb = 0;
                    }
                    break;

                default:    // Il y a un ingrédient non coupable sur la planche
                    if(objectInHand == null) {  // Le joueur a les mains vides
                        returnedObject = getIngredientOn();
                        setIngredientOn(null);
                    } else {    // Le joueur a quelque chose dans les mains
                        returnedObject = objectInHand;
                    }
                    break;
            }
        } else {    // Il n'y a rien sur la planche
            if (objectInHand != null) {     // Le joueur a quelque chose en main
                if (objectInHand instanceof Ingredient) {   // Le joueur tien en ingrédient
                    setIngredientOn((Ingredient) objectInHand);
                } else {    // Le joueur tien autre chose
                    returnedObject = objectInHand;
                }
            }
        }
        return returnedObject;
    }
}
