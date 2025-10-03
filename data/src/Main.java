import Ingredient.Ingredient;
import Furnitures.IngredientChest;
import Furnitures.WorkSurface;
import Furnitures.CuttingBoard;
import Furnitures.Counter;
import View.ViewController;
import Recipe.PatesBolognaise;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        ViewController.instance = new ViewController();

        ViewController.instance.add(new WorkSurface(3,3));
        ViewController.instance.add(new WorkSurface(3,3));
        ViewController.instance.add(new WorkSurface(3,4));
        ViewController.instance.add(new WorkSurface(4,3));
        ViewController.instance.add(new WorkSurface(4,4));
        ViewController.instance.add(new IngredientChest(0,1, Ingredient.PASTA));
        ViewController.instance.add(new IngredientChest(0,2, Ingredient.TOMATO));
        ViewController.instance.add(new CuttingBoard(6,0));
        ViewController.instance.add(new CuttingBoard(5,0));
        ViewController.instance.add(new Counter(6, 7, new PatesBolognaise()));
        for (int x = 0; x < 8; x++) {

            if (!(x==6 || x==5)) ViewController.instance.add(new WorkSurface(x, 0));  // top row
            if (!(x==6))  ViewController.instance.add(new WorkSurface(x, 7));   // bottom row
        }
        for (int y = 1; y < 7; y++) { // avoid duplicating corners

            if(!(y==1 || y == 2)) ViewController.instance.add(new WorkSurface(0, y));   // left column
            ViewController.instance.add(new WorkSurface(7, y));   // right column
        }
        ViewController.instance.display();
    }
}