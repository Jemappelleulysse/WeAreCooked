package Furnitures;

import HoldableObjects.*;

public class GasStove extends Furniture {

    private KitchenUstensils pot = KitchenUstensils.EMPTY_POT;
    private Ingredient ingredientInPot = null;

    public float cookingTime = 4;
    public float currTime = 0;

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public GasStove(int posX, int posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }

    /// ////// ///
    /// GETTER ///
    /// ////// ///
    public boolean hasAPot() {
        return pot != null;
    }

    public KitchenUstensils getPot() {
        return pot;
    }

    public boolean hasIngredientInPot() {
        return ingredientInPot != null;
    }

    public Ingredient getIngredientInPot() {
        return ingredientInPot;
    }

    /// ////// ///
    /// SETTER ///
    /// ////// ///
    public void setIngredientInPot(Ingredient ingredient) {
        this.ingredientInPot = ingredient;
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
    @Override
    public HoldableObject interact(HoldableObject objectInHand) {

        HoldableObject returnedObject = null;

        // S'il n'y a pas de casserole sur la gazinière
        if ( !hasAPot() ) {
            // Si l'objet en main est une casserole
            if (objectInHand  == KitchenUstensils.EMPTY_POT || objectInHand  == KitchenUstensils.WATER_POT || objectInHand == KitchenUstensils.OIL_POT ) {
                pot = (KitchenUstensils) objectInHand;
            } else {
                returnedObject = objectInHand;
            }
        // S'il y a une casserole sur la gazinière avec un ingrédient dedans
        } else if (hasIngredientInPot()) {
            Ingredient ingredientInPot = getIngredientInPot();
            switch (ingredientInPot) {
                case RAW_MEAT, PASTA, SLICED_POTATO:
                    returnedObject = objectInHand;
                    break;

                case COOKED_MEAT, COOKED_PASTA, COOKED_POTATO, FRIED_POTATO:
                    returnedObject = getIngredientInPot();
                    setIngredientInPot(null);
                    break;

                default:
                    if (objectInHand == null) {
                        returnedObject = getIngredientInPot();
                        setIngredientInPot(null);
                    } else {
                        returnedObject = objectInHand;
                    }
                    break;
            }
        // S'il y a une casserole sans ingrédient
        } else {
            switch (objectInHand) {
                case Ingredient.PASTA:
                    if (pot == KitchenUstensils.WATER_POT) {
                        setIngredientInPot((Ingredient) objectInHand);
                    } else {
                        returnedObject  = objectInHand;
                    }
                    break;

                case Ingredient.SLICED_POTATO:
                    if (pot == KitchenUstensils.WATER_POT || pot == KitchenUstensils.OIL_POT) {
                        setIngredientInPot((Ingredient) objectInHand);
                    } else {
                        returnedObject  = objectInHand;
                    }
                    break;

                case Ingredient.RAW_MEAT:
                    if (pot == KitchenUstensils.EMPTY_POT) {
                        setIngredientInPot((Ingredient) objectInHand);
                    } else {
                        returnedObject  = objectInHand;
                    }
                    break;

                case null:
                    returnedObject = pot;
                    pot = null;
                    break;

                default:
                    returnedObject  = objectInHand;
                    break;
            }
        }

        return returnedObject;
    }

    @Override
    public void update(float temps) {

        if (pot == KitchenUstensils.WATER_POT && (ingredientInPot == Ingredient.PASTA || ingredientInPot == Ingredient.SLICED_POTATO)) {
            currTime += temps;
            if (currTime >= cookingTime) {
                pot = KitchenUstensils.EMPTY_POT;
                setIngredientInPot((ingredientInPot == Ingredient.PASTA) ? Ingredient.COOKED_PASTA : Ingredient.COOKED_POTATO);
                currTime = 0;
            }
        } else if (pot == KitchenUstensils.EMPTY_POT && ingredientInPot == Ingredient.RAW_MEAT) {
            currTime += temps;
            if (currTime >= cookingTime) {
                setIngredientInPot(Ingredient.COOKED_MEAT);
                currTime = 0;
            }
        } else if (pot == KitchenUstensils.OIL_POT && (ingredientInPot == Ingredient.SLICED_POTATO)) {
            currTime += temps;
            if (currTime >= cookingTime) {
                pot = KitchenUstensils.EMPTY_POT;
                setIngredientInPot(Ingredient.FRIED_POTATO);
                currTime = 0;
            }
        }
    }

}
