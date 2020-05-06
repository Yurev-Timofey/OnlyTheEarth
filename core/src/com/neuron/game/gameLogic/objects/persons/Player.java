package com.neuron.game.gameLogic.objects.persons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.neuron.game.gameLogic.objects.userData.ObjectType;

public class Player extends Person {

    public Player(World world, TextureAtlas atlas, Vector2 position) {
        super(world, 100, 3, atlas, position, 1.2f, ObjectType.PLAYER);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
