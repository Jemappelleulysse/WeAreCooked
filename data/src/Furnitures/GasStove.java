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
            }
        } else if ( hasIngredientInPot() ) {    // Il y a une casserole sur la gazinière et un ingrédient dans la casserole
            if (getIngredientInPot().equals(Ingredient.PASTA)) {    // L'ingrédient dans la casserole sont des pâtes
                if (currTime >= cookingTime) {  // L'ingrédient est cuit
                    setIngredientInPot (Ingredient.COOKED_PASTA);
                    pot = KitchenUstensils.EMPTY_POT;
                    returnedObject  = getIngredientInPot();
                    setIngredientInPot (null);
                } else {    // L'ingrédient n'est pas encore cuit
                    returnedObject  = objectInHand ;
                }
            } else {    // L'ingrédient dans la casserole est autre chose que des pâtes
                if (objectInHand  == null) {    // Le joueur n'a rien en main
                    returnedObject  = getIngredientInPot ();
                    setIngredientInPot(null);
                } else {    // Le joueur a quelque chose en main
                    returnedObject  = objectInHand ;
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
                currTime = cookingTime + 0.001f;
            }
        }

        if (pot == KitchenUstensils.EMPTY_POT && ingredientInPot == Ingredient.RAW_MEAT) {
            currTime += temps;
            if (currTime >= cookingTime) {
                currTime = cookingTime + 0.001f;
            }
        }
    }
}
