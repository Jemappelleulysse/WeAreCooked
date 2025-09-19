public class PlanDeTravail extends Meuble {

    private boolean hasSomethingOn = false;
    private Ingredient ingredientOn = null;

    /// CONSTRUCTOR ///
    public PlanDeTravail(int posX, int posY) {
        this.setPosX(posX);
        this.setPosX(posX);
    }


    /// GETTER ///
    public boolean hasSomethingOn() {
        return hasSomethingOn;
    }

    public Ingredient getIngredientOn() {
        return ingredientOn;
    }


    /// SETTER ///
    public void setHasSomethingOn(boolean hasSomethingOn) {
        this.hasSomethingOn = hasSomethingOn;
    }

    public void setIngredientOn(Ingredient ingredientOn) {
        this.ingredientOn = ingredientOn;
    }
}