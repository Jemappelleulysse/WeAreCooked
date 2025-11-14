package Furnitures;

import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;

public class CuttingBoard extends Furniture {

    private HoldableObject objectOn = null;
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
        return objectOn != null;
    }

    public HoldableObject getObjectOn() {
        return objectOn;
    }

    /// ////// ///
    /// SETTER ///
    /// ////// ///
    public void setObjectOn(HoldableObject objectOn) {
        this.objectOn = objectOn;
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
    @Override
    public HoldableObject interact(HoldableObject objectInHand) {

        HoldableObject returnedObject = null;

        if(hasSomethingOn()) {      // Il y a un ingrédient sur la planche

            HoldableObject objectOnBoard = getObjectOn();
            switch (objectOnBoard) {
                case Ingredient.TOMATO:
                    if (currNb < cutNb-1) {
                        currNb++;
                        returnedObject = objectInHand;
                    } else {
                        setObjectOn(Ingredient.SLICED_TOMATO);
                        returnedObject = objectInHand;
                        currNb = 0;
                    }
                    break;

                case Ingredient.BREAD:
                    if (currNb < cutNb-1) {
                        currNb++;
                        returnedObject = objectInHand;
                    } else {
                        setObjectOn(Ingredient.SLICED_BREAD);
                        returnedObject = objectInHand;
                        currNb = 0;
                    }
                    break;

                default:    // Il y a un ingrédient non coupable sur la planche
                    if(objectInHand == null) {  // Le joueur a les mains vides
                        returnedObject = getObjectOn();
                        setObjectOn(null);
                    } else {    // Le joueur a quelque chose dans les mains
                        returnedObject = objectInHand;
                    }
                    break;
            }
        } else {    // Il n'y a rien sur la planche
            if (objectInHand != null) {     // Le joueur a quelque chose en main
                if (objectInHand instanceof Ingredient) {   // Le joueur tien en ingrédient
                    setObjectOn((Ingredient) objectInHand);
                } else {    // Le joueur tien autre chose
                    returnedObject = objectInHand;
                }
            }
        }
        return returnedObject;
    }
}
