package com.neuron.game.gameLogic.contacts;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;
import com.neuron.game.gameLogic.contacts.userData.UserData;

public class MyContactFilter implements ContactFilter {
    private UserData findAndReturn(UserData firstObject, UserData secondObject, ObjectType[] required) {
        for (ObjectType type : required) {
            if (firstObject.getObjType().equals(type)) {
                return firstObject;
            }

            if (secondObject.getObjType().equals(type)) {
                return secondObject;
            }
        }
        return null;
    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        UserData hpBoost = findAndReturn(((UserData) fixtureA.getBody().getUserData()),
                ((UserData) fixtureB.getBody().getUserData()),
                new ObjectType[]{ObjectType.HP_BOOST});

        UserData person = findAndReturn((UserData) fixtureA.getBody().getUserData(),
                (UserData) fixtureB.getBody().getUserData(),
                new ObjectType[]{ObjectType.PLAYER, ObjectType.ENEMY});

        if (hpBoost != null)
            return person != null && person.getObjType().equals(ObjectType.PLAYER);

        if (!fixtureA.isSensor() && !fixtureB.isSensor())
            return !(((UserData) fixtureA.getBody().getUserData()).getObjType().equals(ObjectType.BULLET) && ((UserData) fixtureB.getBody().getUserData()).getObjType().equals(ObjectType.BULLET));

        return true;
    }
}
