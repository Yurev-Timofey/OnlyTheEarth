package com.neuron.game.gameLogic.objects.persons.enemy;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.neuron.game.gameLogic.objects.ObjectStatus;
import com.neuron.game.gameLogic.objects.ObjectTypes;
import com.neuron.game.gameLogic.objects.persons.Person;

public abstract class Enemy extends Person {
    boolean seePlayer;

    public Enemy(World world, TextureAtlas atlas, Vector2 position, float sizeInMeters, int maxHp) {
        super(world, maxHp, atlas, position, sizeInMeters, ObjectTypes.ENEMY);
    }

    private void attack() {
        gun.fire();
    }

    @Override
    public void act(float delta) {
        if (sensorFixture.getUserData().equals(ObjectStatus.SEE_ENEMY_RIGHT)) {
            isRunningRight = true;
            attack();
            seePlayer = true;
        } else if (sensorFixture.getUserData().equals(ObjectStatus.SEE_ENEMY_LEFT)) {
            isRunningRight = false;
            attack();
            seePlayer = true;
        } else
            seePlayer = false;

        super.act(delta);
    }
}
