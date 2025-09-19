package Modele;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecetteGenerator {
    private List<Ingredient> allIngredients;

    public RecetteGenerator() {
        allIngredients = new ArrayList<>();
        allIngredients.add(Ingredient.SALADE);
        allIngredients.add(Ingredient.STEAK);
        allIngredients.add(Ingredient.OIGNON);
        allIngredients.add(Ingredient.CHAMPIGNON);
        allIngredients.add(Ingredient.TOMATE);
    }

    public Recette genererAleatoire(int min, int max) {

        Random rand = new Random();
        Random ingredientsSelector = new Random();

        List<Ingredient> temps = new ArrayList<>(List.copyOf(allIngredients));
        List<Ingredient> res = new ArrayList<>();
        int nbrIngredients = rand.nextInt(min, max+1);

        for (int i = 0; i < nbrIngredients; i++) {
            int choix = ingredientsSelector.nextInt(temps.size());
            Ingredient ingredient = temps.get(choix);
            res.add(ingredient);
            temps.remove(ingredient);
        }

        return new Recette(res);
    }
}
