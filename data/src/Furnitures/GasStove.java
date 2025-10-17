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

        if ( !hasAPot() ) {     // Il n'y a pas de casserole sur la gazinière
            if (objectInHand  == KitchenUstensils.EMPTY_POT || objectInHand  == KitchenUstensils.FULL_POT) {
                pot = (KitchenUstensils) objectInHand;
            } else {
                returnedObject = objectInHand;
            }
        } else if (hasIngredientInPot()) {
            if (getIngredientInPot().equals(Ingredient.PASTA)) {
                returnedObject = objectInHand;
            } else if (getIngredientInPot().equals(Ingredient.COOKED_PASTA)) {
                returnedObject = getIngredientInPot();
                setIngredientInPot(null);
            } else {
                if (objectInHand == null) {
                    returnedObject = getIngredientInPot();
                    setIngredientInPot(null);
                } else {
                    returnedObject = objectInHand;
                }
            }
        } else {    // Il y a une casserole sans ingrédient
            if (objectInHand == Ingredient.PASTA && pot == KitchenUstensils.FULL_POT) { // Le joueur tien des pâtes et la casserole contient de l'eau
                setIngredientInPot((Ingredient) objectInHand);
            } else if (objectInHand == Ingredient.RAW_MEAT && pot == KitchenUstensils.EMPTY_POT) { // Le joueur tien de la viande crue et la casserole est vide
                setIngredientInPot((Ingredient) objectInHand);
            } else if (objectInHand  == null) { // Le joueur a les mains vides
                returnedObject = pot;
                pot = null;
            } else {    // Autre situation
                returnedObject  = objectInHand;
            }
        }

        return returnedObject ;
    }

    @Override
    public void update(float temps) {

        if (pot == KitchenUstensils.FULL_POT && ingredientInPot == Ingredient.PASTA) {
            currTime += temps;
            if (currTime >= cookingTime) {
                pot = KitchenUstensils.EMPTY_POT;
                setIngredientInPot(Ingredient.COOKED_PASTA);
                currTime = 0;
            }
        } else if (pot == KitchenUstensils.EMPTY_POT && ingredientInPot == Ingredient.RAW_MEAT) {
            currTime += temps;
            if (currTime >= cookingTime) {
                setIngredientInPot(Ingredient.COOKED_PASTA);
                currTime = 0;
            }
        }
    }

}
