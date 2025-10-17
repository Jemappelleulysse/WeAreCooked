package Controller;

import Ingredient.Ingredient;
import Model.Model;

import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {


        public Controller(Model model) {
            this.model = model;
        }
        Model model;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP && model.player.getPosY() > 0) {
                model.move(3);
            } else if (key == KeyEvent.VK_DOWN && model.player.getPosY() < 7) {
                model.move(2);
            } else if (key == KeyEvent.VK_LEFT && model.player.getPosX() > 0) {
                model.move(1);
            } else if (key == KeyEvent.VK_RIGHT && model.player.getPosY() < 7) {
                model.move(0);
            } else if (key == KeyEvent.VK_Y) {
                model.agent.goPlaceHeldIngredientOnPlate();
            } else if (key == KeyEvent.VK_P) {
                model.agent.goGrab(Ingredient.TOMATO);
            } else if (key == KeyEvent.VK_M) {
                model.agent.goPrepareHeldIngredient();
            } else if (key == KeyEvent.VK_V) {
                model.agent.goValidateRecipe();
            }
        }

}
