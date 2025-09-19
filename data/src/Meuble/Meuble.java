public abstract class Meuble {

    private int posX;
    private int posY;

    //// GETTERS ////
    public int getPosX() {
        return posX;
    }

    public void getPosY(int posY) {
        return posY;
    }


    //// SETTERS ////
    protected void setPosX(int posX) {
        this.posX = posX;
    }

    protected void setPosY(int posY) {
        this.posY = posY;
    }
}