package Controller;

import Agent.Agent;
import HoldableObjects.Ingredient;
import Recipes.BolognesePasta;
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

    private ArrayList<Agent> agents;
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

        agents.add(new Agent(model, 0));
        model.addPlayer(0);

        while(isRunning) {
            update();
        }
    }

    private void update() {
        float dt = (System.nanoTime() - lastTime) / 1000000000.0f;

        lastTime = System.nanoTime();

        score += model.update(dt);

        view.update(dt, model.players, model.furnitures, model.getValidIngredients());
        //System.out.println(System.currentTimeMillis() - dt + " ms needed for the view Update");
        for (Agent agent : agents) {
            agent.update(dt);
        }

        //System.out.println(System.currentTimeMillis() - dt + " ms needed for the agent Update");
    }

    //TODO : A modifier pour que l'event s marche avec la nouvelle structure
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_S) {
            start();
        }
    }
}
