package com.vsu.game.gameLogic.controllers;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.vsu.game.gameLogic.objects.Player;

public class Controller{
    private Player player;

    public Controller(Player player){
        this.player = player;
    }

    public void handleInput(InputEvent.Type typeOfEvent, String buttonName){
        player.getState().handleInput(typeOfEvent, buttonName);
    }
}
