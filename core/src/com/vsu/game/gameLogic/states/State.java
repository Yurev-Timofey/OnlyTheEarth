package com.vsu.game.gameLogic.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.vsu.game.gameLogic.objects.Player;

public abstract class State {
    Player player;
    String name;

    State(Player player, String name){
        this.player = player;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void handleInput(InputEvent.Type typeOfEvent, String buttonName);
    public abstract void update();
}
