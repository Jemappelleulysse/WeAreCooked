public class Player {

    private float posX;
    private float posY;
    private boolean isHoldingSomething =  false;
    private Ingredient ingredientHolded = null;

    public Player(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    // GETTERS
    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public boolean isHoldingSomething() {
        return isHoldingSomething;
    }

    public Ingredient getIngredientHolded() {
        return ingredientHolded;
    }

    // SETTERS
    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setHoldingSomething(boolean holdingSomething) {
        isHoldingSomething = holdingSomething;
    }

    public void setIngredientHolded(Ingredient ingredientHolded) {
        this.ingredientHolded = ingredientHolded;
    }
}