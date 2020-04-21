package com.neuron.game.gameLogic.states.PlayerStates;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.objects.Hud;
import com.neuron.game.gameLogic.objects.Person;
import com.neuron.game.gameLogic.states.State;

public class JumpingState extends State {

    JumpingState(Person person, InputEvent.Type typeOfEvent, Hud.buttons buttonName) {
        super(person, State.states.JumpingState);
        handleInput(typeOfEvent, buttonName);
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
                    person.jump();
                    break;
                case FIRE:
                    person.getGun().fire();
                    break;
            }
        }

        if (typeOfEvent.equals(InputEvent.Type.touchUp)) {
            switch (buttonName) {
                case FIRE:
//                    person.stopFiring();
                    break;
                case UP:
                    break;
                default:
                    person.resetVelocity();
                    break;
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (person.isGrounded()) {
            if (person.getBody().getLinearVelocity().x != 0)
                person.setState(new RunningState(person));
            else
                person.setState(new StandingState(person));
        }
    }
}