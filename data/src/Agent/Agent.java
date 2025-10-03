package Agent;

import Ingredient.Ingredient;
import Player.Player;
import Recipe.Recipe;

import java.util.ArrayList;

public class Agent {

    private final Player player;
    private Ingredient heldIngredient = null;
    private final ArrayList<Ingredient> currentIngredients = new ArrayList<Ingredient>();
    private ArrayList<Ingredient> missingIngredients;

    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///
    public Agent(Player player, Recipe recipe) {
        this.player = player;
        this.missingIngredients = new ArrayList<Ingredient>(recipe.getIngredients());
    }

    /// /////// ///
    /// GETTERS ///
    /// /////// ///
    private boolean isPlayerHandEmpty() {
        return (heldIngredient == null);
    }

    private boolean isRecipeFinished() {
        return (missingIngredients.isEmpty());
    }

    private boolean isHoldingNeededIngredient() {
        return (missingIngredients.contains(heldIngredient));
    }

    private Ingredient getNextIngredient() {
        return missingIngredients.getFirst();
    }


    /// /////// ///
    /// SETTERS ///
    /// /////// ///
    private void refreshHand() {
        heldIngredient = player.getIngredientHolded();
        //this.currentIngredients = currentIngredients;
    }

    private void reset(Recipe recipe) {
        heldIngredient = null;
        currentIngredients.clear();
        missingIngredients = new ArrayList<Ingredient>(recipe.getIngredients());
    }

    /// /////// ///
    /// METHODS ///
    /// /////// ///
    public void doNextAction() {
        if (isPlayerHandEmpty()) {
            if (isRecipeFinished()) {
                goValidateRecipe();
            }
            goGrab(getNextIngredient());
        }
        else {
            if(isHoldingNeededIngredient()) {
                goPlaceHeldIngredientOnPlate();
            }
            else {
                goPrepareHeldIngredient();
            }
        }
    }

    /// ///////////////////// ///
    /// PLAYER ACTION METHODS ///
    /// ///////////////////// ///
    private void goGrab(Ingredient ingredient) {

        // Aller chercher le prochain ingrédient

        refreshHand();
    }

    private void goValidateRecipe() {

        // Aller valider la recette

        //Recipe nextRecipe = world.getNextRecipe();
        //reset(nextRecipe);
    }

    private void goPrepareHeldIngredient() {
        // Va préparer l'ingrédient tenu
        refreshHand();
    }

    private void goPlaceHeldIngredientOnPlate() {
        // Place l'ingrédient tenu sur l'assiette du comptoir
        currentIngredients.add(heldIngredient);
        missingIngredients.remove(heldIngredient);
        refreshHand();
    }
}
