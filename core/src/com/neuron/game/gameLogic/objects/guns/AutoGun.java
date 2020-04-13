package com.neuron.game.gameLogic.objects.guns;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.neuron.game.gameLogic.objects.Player;

public class AutoGun extends Gun {
    public AutoGun(World world, Player player, TextureRegion texture) {
        super(world, player, texture);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
