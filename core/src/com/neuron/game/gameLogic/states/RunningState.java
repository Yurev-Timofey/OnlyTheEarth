package com.neuron.game.gameLogic.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.neuron.game.gameLogic.objects.Player;

public class RunningState extends State {

    public RunningState(Player player, InputEvent.Type typeOfEvent, String buttonName) {
        super(player, "RunningState");
        handleInput(typeOfEvent, buttonName);
    }

    public RunningState(Player player) {
        super(player, "RunningState");
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
                    player.setState(new StandingState(player, typeOfEvent, buttonName));
                    break;
            }
        }

    }
}
