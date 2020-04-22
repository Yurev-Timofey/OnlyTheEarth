package com.neuron.game.gameLogic.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.objects.Hud;
import com.neuron.game.gameLogic.objects.persons.Person;

public abstract class State {
    protected Person person;
    protected states name;
    protected float timer;

    protected State(Person person, states name) {
        this.person = person;
        this.name = name;
        person.setAnimation(name);
    }

    public states getType() {
        return name;
    }

    public float getTimer() {
        return timer;
    }

    public void update(float delta) {
        timer += delta;
    }
    public abstract void handleInput(InputEvent.Type typeOfEvent, Hud.buttons buttonName);

    public enum states{
        JumpingState,
        RunningState,
        StandingState,
    }

}
