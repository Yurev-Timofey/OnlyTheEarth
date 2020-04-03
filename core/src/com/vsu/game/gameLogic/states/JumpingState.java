package com.vsu.game.gameLogic.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.vsu.game.gameLogic.objects.Player;

public class JumpingState extends State {

    JumpingState(Player player, InputEvent.Type typeOfEvent, String buttonName) {
        super(player, "JumpingState");
        handleInput(typeOfEvent, buttonName);
    }

    @Override
    public void handleInput(InputEvent.Type typeOfEvent, String buttonName) {
        if (typeOfEvent.equals(InputEvent.Type.touchDown)) {
            switch (buttonName) {
                case "LEFT":
                    player.move(-1);
                    break;
                case "RIGHT":
                    player.move(1);
                    break;
                case "UP":
                        player.jump();
                    break;
                case "FIRE":
//                    player.fire();
                    break;
            }
        }
        if (typeOfEvent.equals(InputEvent.Type.touchUp)) {
            switch (buttonName) {
                case "FIRE":
//                    player.stopFiring();
                    break;
                default:
                    player.resetVelocity();
                    break;
            }
        }
    }

    @Override
    public void update() {
        if (player.isGrounded()){
            player.setState(new StandingState(player));
        }
    }
}
