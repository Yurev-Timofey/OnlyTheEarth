package com.neuron.game.gameLogic.objects.enemy;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.neuron.game.gameLogic.objects.ObjectTypes;
import com.neuron.game.gameLogic.objects.Person;

public abstract class Enemy extends Person {
    boolean seePlayer;

    public Enemy(World world, TextureAtlas atlas, Vector2 position, float sizeInMeters, int maxHp) {
        super(world, maxHp, atlas, position, sizeInMeters, ObjectTypes.ENEMY);
    }

    private void attack(){}

//    @Override
//    public void act(float delta) {
//        super.act(delta);
//
////        if( player fixture is visible )
////        seePlayer
//    }
}
