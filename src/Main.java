import Model.Model;
import View.View;
import Controller.Controller;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {

        Model model = new Model(5);
        View view = new View();
        Controller controller = new Controller(model, view);

        //TODO : Modifier ces merde

        System.out.println("Displaying view...");
        JFrame frame = new JFrame("MVC Example");
        frame.setSize(900, 1050);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.add(view);
        view.setVisible(true);
        frame.setResizable(false);
        frame.addKeyListener(controller);

        controller.start();
    }
}