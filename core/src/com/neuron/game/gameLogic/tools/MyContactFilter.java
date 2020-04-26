package com.neuron.game.gameLogic.tools;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.neuron.game.gameLogic.objects.userData.ObjectType;
import com.neuron.game.gameLogic.objects.userData.UserData;

public class MyContactFilter implements ContactFilter {
    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if (!fixtureA.isSensor() && !fixtureB.isSensor())
            return !(((UserData) fixtureA.getBody().getUserData()).getObjType().equals(ObjectType.BULLET) && ((UserData) fixtureB.getBody().getUserData()).getObjType().equals(ObjectType.BULLET));
        return true;
    }
}
