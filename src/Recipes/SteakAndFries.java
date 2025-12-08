package Recipes;

import HoldableObjects.HoldableObject;
import HoldableObjects.Ingredient;

public class SteakAndFries extends Recipe {


    public SteakAndFries() {
        super();
        ingredients.add(Ingredient.COOKED_MEAT);
        ingredients.add(Ingredient.FRIED_POTATO);
        ingredients.add(Ingredient.WASHED_SALAD);
    }

}
