package com.neuron.game.gameLogic.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.objects.Player;

public abstract class State {
    Player player;
    String name;
    float timer;

    State(Player player, String name) {
        this.player = player;
        this.name = name;
        player.setAnimation(name);
    }

    public String getName() {
        return name;
    }

    public float getTimer() {
        return timer;
    }

    public void update(float delta) {
        timer += delta;
    }

    public abstract void handleInput(InputEvent.Type typeOfEvent, String buttonName);

}
