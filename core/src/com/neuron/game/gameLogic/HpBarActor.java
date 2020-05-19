package com.neuron.game.gameLogic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.neuron.game.gameLogic.objects.persons.Player;

public class HpBarActor extends MyActor {
    private Player player;

    public HpBarActor(TextureRegion textureRegion, Player player, int size) {
        super(textureRegion, size);
        this.player = player;
    }

    @Override
    public void act(float delta) {
        setScaleX(player.getHp() / 100f);
        super.act(delta);
    }
}
