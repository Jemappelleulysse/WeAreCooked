package Controller;

import Agent.AgentDuo;
import Model.Model;
import View.View;

import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;

public class Controller extends KeyAdapter {

    private boolean isRunning;
    private long lastTime;

    private final Model model;
    private final View view;

    private ArrayList<AgentDuo> agents;
    private int score;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.agents = new ArrayList<>();
        this.isRunning = true;
        this.lastTime = System.nanoTime();
    }

    public void start() {

        this.score = 0;

        //TODO : A Modifier (recuperer la/les recette via le model)
        model.setRecipes(3);
        agents.add(new AgentDuo(model, 0));
        agents.add(new AgentDuo(model, 1));
        agents.get(0).setMate(agents.get(1));
        agents.get(1).setMate(agents.get(0));
        model.addPlayer(0);
        model.addPlayer(1);

        update(false);
    }

    private void update(boolean move) {
        float dt = (System.nanoTime() - lastTime) / 1000000000.0f;

        lastTime = System.nanoTime();

        score += model.update(dt);

        view.update(dt, model.players, model.furnitures, model.getValidIngredients(),
                model.getFirstRecipe(), model.getNextRecipe());
        if (move) {
            for (AgentDuo agent : agents) {
                agent.update(dt);
            }
        }
    }

    //TODO : A modifier pour que l'event marche avec la nouvelle structure
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_S) {
            update(true);
        }
    }
}
