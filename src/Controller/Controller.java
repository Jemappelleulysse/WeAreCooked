package Controller;

import Agent.Agent;
import Recipes.BolognesePasta;
import Model.Model;
import Utils.Pair;
import View.View;

import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;

public class Controller extends KeyAdapter {

    private final Model model;
    private ArrayList<Agent> agents;
    private boolean isRunning;
    private final View view;
    private long lastTime;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.agents = new ArrayList<>();
        this.isRunning = true;
        this.lastTime = System.nanoTime();
    }

    public void start() {
        //
        // Des trucs à initialiser?
        //

        //TODO : A Modifier (recuperer la/les recette via le model)

        agents.add(new Agent(model, new BolognesePasta(), 0));

        while(isRunning) {

            float dt = (System.nanoTime() - lastTime) / 1000000000.0f;

            lastTime = System.nanoTime();

            model.update(dt);

            view.update(dt, model.players, model.furnitures);
            //System.out.println(System.currentTimeMillis() - dt + " ms needed for the view Update");
            for (Agent agent : agents) {
                agent.update(dt);
            }

            //System.out.println(System.currentTimeMillis() - dt + " ms needed for the agent Update");
        }
    }

    //TODO : A modifier pour que l'event s marche avec la nouvelle structure
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_S) {
            model.move(new Pair(0,-1),0);
        } else if (key == KeyEvent.VK_Z) {
            model.move(new Pair(0,1),0);
        } else if (key == KeyEvent.VK_Q) {
            model.move(new Pair(1,0),0);
        } else if (key == KeyEvent.VK_D) {
            model.move(new Pair(-1,0),0);
        }
    }
}
