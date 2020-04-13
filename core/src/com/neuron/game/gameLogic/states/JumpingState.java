package com.neuron.game.gameLogic.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.objects.Player;

public class JumpingState extends State {

    JumpingState(Player player, InputEvent.Type typeOfEvent, String buttonName) {
        super(player, "JumpingState");
        handleInput(typeOfEvent, buttonName);
    }

    @Override
    public void handleInput(InputEvent.Type typeOfEvent, String buttonName) {
        if (typeOfEvent.equals(InputEvent.Type.touchDown)) {
            switch (buttonName) {
                case "Button_left":
                    player.move(-1);
                    break;
                case "Button_right":
                    player.move(1);
                    break;
                case "Button_up":
                    player.jump();
                    break;
                case "Button_fire":
                    player.getGun().fire();
                    break;
            }
        }

        if (typeOfEvent.equals(InputEvent.Type.touchUp)) {
            switch (buttonName) {
                case "Button_fire":
//                    player.stopFiring();
                    break;
                case "Button_up":
                    break;
                default:
                    player.resetVelocity();
                    break;
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (player.isGrounded()) {
            if (player.getBody().getLinearVelocity().x != 0)
                player.setState(new RunningState(player));
            else
                player.setState(new StandingState(player));
        }
    }
}