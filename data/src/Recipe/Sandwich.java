package Recipe;

import Ingredient.Ingredient;

public class Sandwich extends Recipe {

    public Sandwich() {
        super();
        ingredients.add(Ingredient.PAIN);
        ingredients.add(Ingredient.TOMATE_COUPE);
        ingredients.add(Ingredient.SALADE);
        ingredients.add(Ingredient.VIANDE);
    }
}
