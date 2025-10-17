import Ingredient.Ingredient;
import Furnitures.IngredientChest;
import Furnitures.WorkSurface;
import Furnitures.CuttingBoard;
import Furnitures.Counter;
import View.ViewController;
import Model.Model;
import View.View;
import Recipe.PatesBolo;
import Controller.Controller;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {

        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model);
        model.setView(view);
        view.setModel(model);

        System.out.println("Displaying view...");
        JFrame frame = new JFrame("MVC Example");
        frame.setSize(900, 930);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.add(view);
        view.setVisible(true);
        frame.setResizable(false);
        frame.addKeyListener(controller);

        model.start();
    }
}