import Ingredient.Ingredient;
import Meuble.Coffre;
import Meuble.PlanDeTravail;
import Meuble.PlancheADecoupe;
import Meuble.Comptoir;
import Recette.Recette;
import Recette.RecetteGenerator;
import Recette.PatesBolo;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        RecetteGenerator test = new RecetteGenerator();


        ViewController controller = new ViewController();
        controller.add(new PlanDeTravail(3,3));
        controller.add(new PlanDeTravail(3,3));
        controller.add(new PlanDeTravail(3,4));
        controller.add(new PlanDeTravail(4,3));
        controller.add(new PlanDeTravail(4,4));
        controller.add(new Coffre(0,1, Ingredient.PATES));
        controller.add(new Coffre(0,2, Ingredient.TOMATE));
        controller.add(new PlancheADecoupe(6,0));
        controller.add(new PlancheADecoupe(5,0));
        controller.add(new Comptoir(6, 7, new PatesBolo()));
        for (int x = 0; x < 8; x++) {

            if (!(x==6 || x==5)) controller.add(new PlanDeTravail(x, 0));  // top row
            if (!(x==6))  controller.add(new PlanDeTravail(x, 7));   // bottom row
        }
        for (int y = 1; y < 7; y++) { // avoid duplicating corners

            if(!(y==1 || y == 2)) controller.add(new PlanDeTravail(0, y));   // left column
            controller.add(new PlanDeTravail(7, y));   // right column
        }


        controller.display();
    }
}