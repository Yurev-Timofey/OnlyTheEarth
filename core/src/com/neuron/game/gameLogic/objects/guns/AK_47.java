package com.neuron.game.gameLogic.objects.guns;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.neuron.game.gameLogic.objects.Player;

public class AK_47 extends Gun{
    public AK_47(World world, Player player, TextureRegion texture) {
        super(world, player, texture);
        super.ammo = 30;
        super.damage = 100;
        super.fireRate = 0.1f;
    }
}
