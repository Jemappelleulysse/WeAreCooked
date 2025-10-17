package Furnitures;

import HoldableObjects.*;

public class Sink extends Furniture {

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public Sink(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }


    /// METHODS ///
    @Override
    public HoldableObject interact(HoldableObject objectInHand) {

        HoldableObject returnedObject = objectInHand;;

        switch (objectInHand) {

            case KitchenUstensils.EMPTY_POT:
                returnedObject = KitchenUstensils.FULL_POT;
                break;

            case Ingredient.SALAD:
                returnedObject = Ingredient.WASHED_SALAD;
                break;

            default:
                break;
        }

        return returnedObject;
    }
}
