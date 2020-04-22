package com.neuron.game.gameLogic.states.PlayerStates;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.objects.Hud;
import com.neuron.game.gameLogic.objects.persons.Person;
import com.neuron.game.gameLogic.states.State;

public class RunningState extends State {

    public RunningState(Person person, InputEvent.Type typeOfEvent, Hud.buttons buttonName) {
        super(person, states.RunningState);
        handleInput(typeOfEvent, buttonName);
    }

    public RunningState(Person person) {
        super(person, states.RunningState);
    }

    @Override
    public void handleInput(InputEvent.Type typeOfEvent, Hud.buttons buttonName) {
        if (typeOfEvent.equals(InputEvent.Type.touchDown)) {
            switch (buttonName) {
                case LEFT:
                    person.move(-1);
                    break;
                case RIGHT:
                    person.move(1);
                    break;
                case UP:
                    person.setState(new JumpingState(person, typeOfEvent, buttonName));
                    break;
                case FIRE:
                    person.getGun().setFiring(true);
                    break;
            }
        }
        else if (typeOfEvent.equals(InputEvent.Type.touchUp)) {
            switch (buttonName) {
                case FIRE:
//                    person.getGun().setFiring(false);
                    break;
                case UP:
                    break;
                default:
                    person.setState(new StandingState(person, typeOfEvent, buttonName));
                    break;
            }
        }

    }
}
