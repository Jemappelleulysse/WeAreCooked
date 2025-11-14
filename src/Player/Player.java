package Player;

import HoldableObjects.HoldableObject;
import Utils.Pair;

public class Player {

    private int ID;
    private int posX;
    private int posY;
    private boolean isHoldingSomething =  false;
    private HoldableObject objectHeld = null;

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public Player(int posX, int posY, int ID) {
        this.ID = ID;
        this.posX = posX;
        this.posY = posY;
    }

    /// /////// ///
    /// GETTERS ///
    /// /////// ///
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Pair getPos() {
        return new Pair(posX, posY);
    }

    public boolean isHoldingSomething() {
        return isHoldingSomething;
    }

    public HoldableObject getObjectHeld() {
        return objectHeld;
    }

    /// /////// ///
    /// SETTERS ///
    /// /////// ///
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setObjectHeld(HoldableObject objectHeld) {
        this.objectHeld = objectHeld;
        isHoldingSomething = objectHeld != null;
    }


    /// /////// ///
    /// GETTERS ///
    /// /////// ///

    public int getID() {
        return ID;
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
}