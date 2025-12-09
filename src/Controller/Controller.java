package Controller;

import Agent.AgentDuo;
import Agent.AgentState;
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


    /// /////////// ///
    /// CONSTRUCTOR ///
    /// /////////// ///

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.agents = new ArrayList<>();
        this.isRunning = false;
        this.lastTime = System.nanoTime();
    }


    /// //// ///
    /// INIT ///
    /// //// ///

    public void start() {

        agents.add(new AgentDuo(model, 0));
        agents.add(new AgentDuo(model, 1));
        agents.get(0).setMate(agents.get(1));
        agents.get(1).setMate(agents.get(0));
        model.addPlayer(0);
        model.addPlayer(1);
        score = 0;

        while(true) {
            update();
        }
    }


    /// ////// ///
    /// UPDATE ///
    /// ////// ///

    private void update() {

        float dt = (System.nanoTime() - lastTime) / 1000000000.0f;

        lastTime = System.nanoTime();

        score += model.update(dt);

        view.update(dt, model.players, model.furnitures, model.getValidIngredients(),
                model.getFirstRecipe(), model.getNextRecipe());

        boolean winningCondition;
        if(isRunning) {
            winningCondition = true;
            for (AgentDuo agent : agents) {
                agent.update(dt);
                if(agent.getState() != AgentState.END) {
                    winningCondition = false;
                }
            }

            if(winningCondition) {
                isRunning = false;
                System.out.println("Score = "  + score);
            }
        }
    }


    /// //////////// ///
    /// KEY HANDLING ///
    /// //////////// ///

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_S) {
            isRunning = !isRunning;
        }
    }
}
