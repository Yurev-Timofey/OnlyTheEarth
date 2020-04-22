package com.neuron.game.gameLogic.tools;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.objects.Hud;
import com.neuron.game.gameLogic.objects.persons.Player;

public class Controller{
    private Player player;

    public Controller(Player player){
        this.player = player;
    }

    public void handleInput(InputEvent.Type typeOfEvent, Hud.buttons buttonName){
        player.getState().handleInput(typeOfEvent, buttonName);
    }
}
