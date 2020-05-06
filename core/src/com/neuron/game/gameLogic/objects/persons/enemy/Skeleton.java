package com.neuron.game.gameLogic.objects.persons.enemy;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Skeleton extends Enemy {
    public Skeleton(World world, TextureAtlas atlas, Vector2 position, TextureRegion texture) {
        super(world, atlas, position, 1.2f, 15, texture);
    }
}
