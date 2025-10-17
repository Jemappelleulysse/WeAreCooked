package Furnitures;

import HoldableObjects.HoldableObject;

public class WorkSurface extends Furniture {

    private HoldableObject objectOn = null;

    /// CONSTRUCTOR ///
    public WorkSurface(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }


    /// GETTER ///
    public boolean hasSomethingOn() {
        return objectOn != null;
    }

    public HoldableObject getObjectOn() {
        return objectOn;
    }


    /// SETTER ///
    public void setHoldableObjectOn(HoldableObject objectOn) {
        this.objectOn = objectOn;
    }


    /// METHODS ///
    @Override
    public HoldableObject interact(HoldableObject object) {

        HoldableObject returnedHoldableObject = null;

        if(object == null) {
            if(hasSomethingOn()) {
                returnedHoldableObject = this.objectOn;
                setHoldableObjectOn(null);
            }
        } else {
            if(hasSomethingOn()) {
                returnedHoldableObject = object;
            } else  {
                setHoldableObjectOn(object);
            }
        }
        return returnedHoldableObject;
    }
}