package Recipe;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecetteGenerator {

    private List<Recette> allRecettes;

    public RecetteGenerator() {
        allRecettes = new ArrayList<>();
        allRecettes.add(new PatesBolo());
        allRecettes.add(new Sandwich());
    }

    public Recette genererAleatoire() {

        Random rand = new Random();

        return allRecettes.get(rand.nextInt(allRecettes.size()));
    }
}
