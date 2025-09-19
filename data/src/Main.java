import Modele.RecetteGenerator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        RecetteGenerator test = new RecetteGenerator();

        for (int i = 1; i <= 10; i++) {
            System.out.println(test.genererAleatoire(2,5).toString());
        }
    }
}