package Controller;

import Agent.Agent;
import Recipes.BolognesePasta;
import Model.Model;
import View.View;

import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;

public class Controller extends KeyAdapter {

    private Model model;
    private ArrayList<Agent> agents;
    private boolean doContinue;
    private View view;
    private long lastTime;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.agents = new ArrayList<>();
        this.doContinue = true;
        this.lastTime = System.nanoTime();
    }

    public void start() {
        //
        // Des trucs à initialiser?
        //

        //TODO : A Modifier (recuper la/les recette via le model)

        agents.add(new Agent(model, new BolognesePasta()));

        while(doContinue) {

            float dt = (System.nanoTime() - lastTime) / 1000000000.0f;

            lastTime = System.nanoTime();

            model.update(dt);
            view.update(dt);
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
        if (key == KeyEvent.VK_UP && model.player.getPosY() > 0) {
            model.move(3);
        } else if (key == KeyEvent.VK_DOWN && model.player.getPosY() < 7) {
            model.move(2);
        } else if (key == KeyEvent.VK_LEFT && model.player.getPosX() > 0) {
            model.move(1);
        } else if (key == KeyEvent.VK_RIGHT && model.player.getPosY() < 7) {
            model.move(0);
        } else if (key == KeyEvent.VK_S) {
            for(Agent agent : agents) {
                agent.start();
            }
        }
    }
}
