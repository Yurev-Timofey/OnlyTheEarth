package com.neuron.game.gameLogic.states.PersonStates;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.Hud;
import com.neuron.game.gameLogic.objects.persons.Person;
import com.neuron.game.gameLogic.states.State;

public class StandingState extends State {

    public StandingState(Person person, InputEvent.Type typeOfEvent, Hud.buttons buttonName) {
        super(person, states.StandingState);
        handleInput(typeOfEvent, buttonName);
    }

    public StandingState(Person person) {
        super(person, states.StandingState);
    }

    @Override
    public void handleInput(InputEvent.Type typeOfEvent, Hud.buttons buttonName) {
        if (typeOfEvent.equals(InputEvent.Type.touchDown)) {
            switch (buttonName) {
                case LEFT:
                case RIGHT:
                    person.setState(new RunningState(person, typeOfEvent, buttonName));
                    break;
                case UP:
                    person.setState(new JumpingState(person, typeOfEvent, buttonName));
                    break;
                case FIRE:
                    person.getGun().setFiring(true);
                    break;
            }
        }
        if (typeOfEvent.equals(InputEvent.Type.touchUp)) {
            switch (buttonName) {
                case FIRE:
                    person.getGun().setFiring(false);
                    break;
                case UP:
                    break;
                default:
                    person.resetVelocity();
                    break;
            }
        }

    }
}
