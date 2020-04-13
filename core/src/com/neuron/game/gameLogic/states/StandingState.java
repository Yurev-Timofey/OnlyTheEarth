package com.neuron.game.gameLogic.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.objects.Player;

public class StandingState extends State {

    public StandingState(Player player, InputEvent.Type typeOfEvent, String buttonName) {
        super(player, "StandingState");
        handleInput(typeOfEvent, buttonName);
    }

    public StandingState(Player player) {
        super(player, "StandingState");
    }

    @Override
    public void handleInput(InputEvent.Type typeOfEvent, String buttonName) {
        if (typeOfEvent.equals(InputEvent.Type.touchDown)) {
            switch (buttonName) {
                case "Button_left":
                case "Button_right":
                    player.setState(new RunningState(player, typeOfEvent, buttonName));
                    break;
                case "Button_up":
                    player.setState(new JumpingState(player, typeOfEvent, buttonName));
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
}
