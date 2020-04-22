package com.neuron.game.gameLogic.objects.persons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.neuron.game.gameLogic.objects.ObjectTypes;

public class Player extends Person {

    public Player(World world, TextureAtlas atlas, Vector2 position) {
        super(world, 60, atlas, position, 1.2f, ObjectTypes.PLAYER);
    }

}
