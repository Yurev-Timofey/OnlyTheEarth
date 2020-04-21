package com.neuron.game.gameLogic.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.neuron.game.gameLogic.objects.ObjectStatus;
import com.neuron.game.gameLogic.objects.ObjectTypes;
import com.neuron.game.gameLogic.objects.Player;

public class MyContactListener implements ContactListener {
    Player player;

    public MyContactListener(Player player) {
        this.player = player;
    }

    private boolean areContactingWith(ObjectTypes nameOfObject, ObjectTypes firstObject, ObjectTypes secondObject) {
        return (firstObject.equals(nameOfObject) ||
                secondObject.equals(nameOfObject));
    }

    @Override
    public void beginContact(Contact contact) {
        ObjectTypes firstObject = (ObjectTypes) contact.getFixtureA().getBody().getUserData();
        ObjectTypes secondObject = (ObjectTypes) contact.getFixtureB().getBody().getUserData();
        float angleOfContact = contact.getWorldManifold().getNormal().angle();

        if (areContactingWith(ObjectTypes.GROUND, firstObject, secondObject) &&
                areContactingWith(ObjectTypes.PLAYER, firstObject, secondObject)) {
            switch ((short) angleOfContact) {
                case 90:
                    player.setGrounded(true);
                    break;
                case 180:
                case 0:
                    player.climbToBlock();
                    break;
            }
        } else if (areContactingWith(ObjectTypes.BULLET, firstObject, secondObject) && !(firstObject.equals(secondObject))) {
            if (firstObject.equals(ObjectTypes.BULLET)) {
                if (secondObject.equals(ObjectTypes.PLAYER) || secondObject.equals(ObjectTypes.ENEMY))
                    contact.getFixtureB().setUserData(ObjectStatus.DAMAGED);

                contact.getFixtureA().setUserData(ObjectStatus.TO_DISPOSE);
            } else {
                if (firstObject.equals(ObjectTypes.PLAYER) || firstObject.equals(ObjectTypes.ENEMY))
                    contact.getFixtureA().setUserData(ObjectStatus.DAMAGED);
                contact.getFixtureB().setUserData(ObjectStatus.TO_DISPOSE);
            }
        }
    }


    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}

