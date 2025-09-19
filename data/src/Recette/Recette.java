package Modele;

import java.util.ArrayList;
import java.util.List;

public class Recette {

    private List<Ingredient> neededIngredients;

    private List<Ingredient> currentIngredients;

    public Recette(List<Ingredient> ingredients) {
        this.neededIngredients = ingredients;
        this.currentIngredients = new ArrayList<>();
    }

    public void putIngredient(Ingredient ingredient) {
        this.currentIngredients.add(ingredient);
        recetteValide();
    }

    public void recetteValide(){
        boolean valide = true;
        for  (Ingredient ingredient : currentIngredients) {
            if (!neededIngredients.contains(ingredient)) {
                valide = false;
                break;
            }
        }
        if(valide){
            //TODO : Lancer la transformation en plat à rendre
        }
    }

    @Override
    public String toString() {
        return neededIngredients.toString();
    }
}
