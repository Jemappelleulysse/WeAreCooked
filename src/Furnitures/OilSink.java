package Furnitures;

import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;
import HoldableObjects.KitchenUstensils;

public class OilSink extends Furniture {

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public OilSink(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }


    /// METHODS ///
    @Override
    public HoldableObject interact(HoldableObject objectInHand) {

        HoldableObject returnedObject = objectInHand;;

        switch (objectInHand) {

            case KitchenUstensils.EMPTY_POT:
                returnedObject = KitchenUstensils.OIL_POT;
                break;
            default:
                break;
        }

        return returnedObject;
    }
}
