package com.vsu.game.gameLogic.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.vsu.game.gameLogic.objects.Player;

public class StandingState extends State {

    public StandingState(Player player){
        super(player, "StandingState");
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
                    player.setState(new JumpingState(player, typeOfEvent, buttonName));
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

    }
}
