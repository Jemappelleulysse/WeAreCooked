public class Coffre extends Meuble {

    private Ingredient ingredient;


    /// CONSTRUCTOR ///
    public Coffre(int posX, int posY, Ingredient ingredient) {
        this.setPosX(posX);
        this.setPosX(posX);
        this.ingredient = ingredient;
    }


    /// GETTER ///
    public boolean getIngredient() {
        return new Ingredient(ingredient);
    }
}