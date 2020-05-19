package com.neuron.game.gameLogic.objects.persons.enemy;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.neuron.game.gameLogic.objects.HpBoost;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;
import com.neuron.game.gameLogic.objects.persons.Person;
import com.neuron.game.gameLogic.contacts.userData.UserData;

public abstract class Enemy extends Person {
    boolean seePlayer = false;
    TextureAtlas HpBoostAnimation;

    public Enemy(World world, TextureAtlas atlas, Vector2 position, float sizeInMeters, int maxHp, TextureAtlas HpBoostAnimation) {
        super(world, maxHp, 2, atlas, position, sizeInMeters, ObjectType.ENEMY, 0, 0);
        this.HpBoostAnimation = HpBoostAnimation;
    }

    private void attack() {
        gun.fire();
    }

    @Override
    public void act(float delta) {
        switch (((UserData) body.getUserData()).getSeeEnemy()) {
            case SEE_ENEMY_FAR_LEFT:
                isRunningRight = false;
                seePlayer = false;
                move(-1);
                break;
            case SEE_ENEMY_FAR_RIGHT:
                isRunningRight = true;
                seePlayer = false;
                move(1);
                break;
            case SEE_ENEMY_RIGHT:
            case SEE_ENEMY_LEFT:
                seePlayer = true;
                resetVelocity();
                attack();
                break;
//            case SEE_ENEMY_UP_RIGHT:
//                isRunningRight = true;
//                jump();
//                attack();
//                break;
//            case SEE_ENEMY_UP_LEFT:
//                isRunningRight = false;
//                jump();
//                attack();
//                break;
            default:
                seePlayer = false;
        }
        super.act(delta);
    }

    @Override
    public boolean remove() {
        new HpBoost(HpBoostAnimation, world, new Vector2(body.getPosition().x, body.getPosition().y - SIZE_IN_METERS / 3), getStage());
        return super.remove();
    }
}
