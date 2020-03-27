package com.vsu.game.gameLogic.states;

import com.badlogic.gdx.Input;
import com.vsu.game.gameLogic.objects.Player;


public interface State {
    void handleInput(Player player, Input input);
    void update(Player player); //Мб не нужен
}
