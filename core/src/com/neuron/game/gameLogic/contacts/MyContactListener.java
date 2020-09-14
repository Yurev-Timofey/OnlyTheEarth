package com.neuron.game.gameLogic.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.neuron.game.gameLogic.contacts.userData.ObjectStatus;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;
import com.neuron.game.gameLogic.contacts.userData.SeeEnemy;
import com.neuron.game.gameLogic.contacts.userData.SensorUserData;
import com.neuron.game.gameLogic.contacts.userData.UserData;


public class MyContactListener implements ContactListener {

    private boolean areContactingWith(ObjectType nameOfObject, ObjectType firstObject, ObjectType secondObject) {
        return (firstObject.equals(nameOfObject) ||
                secondObject.equals(nameOfObject));
    }

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

    private void leftOrRight(Fixture fixtureA, Fixture fixtureB, boolean far) {
        SeeEnemy left;
        SeeEnemy right;

        if (!far) {
            left = SeeEnemy.SEE_ENEMY_LEFT;
            right = SeeEnemy.SEE_ENEMY_RIGHT;
        } else {
            left = SeeEnemy.SEE_ENEMY_FAR_LEFT;
            right = SeeEnemy.SEE_ENEMY_FAR_RIGHT;
        }

        if (((UserData) fixtureA.getBody().getUserData()).getObjType().equals(ObjectType.PLAYER)) {
            if (fixtureA.getBody().getPosition().x < fixtureB.getBody().getPosition().x) {
                ((UserData) fixtureB.getBody().getUserData()).setSeeEnemy(left);
                ((UserData) fixtureA.getBody().getUserData()).setSeeEnemy(right);
            } else {
                ((UserData) fixtureB.getBody().getUserData()).setSeeEnemy(right);
                ((UserData) fixtureA.getBody().getUserData()).setSeeEnemy(left);
            }
        } else {
            if (fixtureA.getBody().getPosition().x > fixtureB.getBody().getPosition().x) {
                ((UserData) fixtureB.getBody().getUserData()).setSeeEnemy(left);
                ((UserData) fixtureA.getBody().getUserData()).setSeeEnemy(right);
            } else {
                ((UserData) fixtureB.getBody().getUserData()).setSeeEnemy(right);
                ((UserData) fixtureA.getBody().getUserData()).setSeeEnemy(left);
            }
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        UserData person = findAndReturn((UserData) fixtureA.getBody().getUserData(),
                (UserData) fixtureB.getBody().getUserData(),
                new ObjectType[]{ObjectType.PLAYER, ObjectType.ENEMY});

        UserData heart = findAndReturn(((UserData) fixtureA.getBody().getUserData()),
                ((UserData) fixtureB.getBody().getUserData()),
                new ObjectType[]{ObjectType.HP_BOOST});

        UserData bullet = findAndReturn(((UserData) fixtureA.getBody().getUserData()),
                ((UserData) fixtureB.getBody().getUserData()),
                new ObjectType[]{ObjectType.BULLET});

        UserData ground = findAndReturn(((UserData) fixtureA.getBody().getUserData()),
                ((UserData) fixtureB.getBody().getUserData()),
                new ObjectType[]{ObjectType.GROUND});

        UserData NextLevel = findAndReturn(((UserData) fixtureA.getBody().getUserData()),
                ((UserData) fixtureB.getBody().getUserData()),
                new ObjectType[]{ObjectType.NEXT_LEVEL});


        if (!fixtureA.isSensor() && !fixtureB.isSensor()) {
            if ((person != null) && (ground != null)) {
                switch ((short) contact.getWorldManifold().getNormal().angle()) {
                    case 90:
                        person.setGrounded(true);
                        break;
                    case 180:
                    case 0:
                        person.setStatus(ObjectStatus.CLIMB_TO_BLOCK);
                        break;
                }
            }
        } else if (fixtureA.isSensor() && fixtureB.isSensor()) {                                // TODO: 21.05.2020 Упростить
            if (areContactingWith(ObjectType.PLAYER, ((UserData) fixtureA.getBody().getUserData()).getObjType(),
                    ((UserData) fixtureB.getBody().getUserData()).getObjType()) &&
                    areContactingWith(ObjectType.ENEMY, ((UserData) fixtureA.getBody().getUserData()).getObjType(),
                            ((UserData) fixtureB.getBody().getUserData()).getObjType())) {
                if (((SensorUserData) fixtureA.getUserData()).getSensorType().equals(SensorUserData.SensorType.NearSensor) &&
                        ((SensorUserData) fixtureB.getUserData()).getSensorType().equals(SensorUserData.SensorType.NearSensor))
                    leftOrRight(fixtureA, fixtureB, false);
                else if (((SensorUserData) fixtureA.getUserData()).getSensorType().equals(SensorUserData.SensorType.FarSensor) &&
                        ((SensorUserData) fixtureB.getUserData()).getSensorType().equals(SensorUserData.SensorType.FarSensor))
                    leftOrRight(fixtureA, fixtureB, true);
            }
        } else {
            if (person != null && heart != null && person.getObjType().equals(ObjectType.PLAYER) &&
                    (((SensorUserData) fixtureA.getUserData()).getSensorType().equals(SensorUserData.SensorType.Default) &&
                            ((SensorUserData) fixtureB.getUserData()).getSensorType().equals(SensorUserData.SensorType.Default))) {
                person.setStatus(ObjectStatus.HEALED);
                heart.setStatus(ObjectStatus.TO_DISPOSE);
            }
            if (bullet != null) {
                if ((person != null))
                    person.setStatus(ObjectStatus.DAMAGED);
                bullet.setStatus(ObjectStatus.TO_DISPOSE);
            }
            if (person != null && person.getObjType().equals(ObjectType.PLAYER) && NextLevel != null){
                NextLevel.setStatus(ObjectStatus.JUMP_TO_NEXT_LEVEL);
            }
        }
    }


    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

//        UserData bullet = findAndDeclare(((UserData) fixtureA.getBody().getUserData()),
//                ((UserData) fixtureB.getBody().getUserData()),
//                new ObjectType[]{ObjectType.BULLET});


        if (contact.getFixtureA().isSensor() && contact.getFixtureB().isSensor() && areContactingWith(ObjectType.PLAYER,
                ((UserData) fixtureA.getBody().getUserData()).getObjType(),
                ((UserData) fixtureB.getBody().getUserData()).getObjType()) &&
                areContactingWith(ObjectType.ENEMY, ((UserData) fixtureA.getBody().getUserData()).getObjType(),
                        ((UserData) fixtureB.getBody().getUserData()).getObjType())) {
            if (((SensorUserData) fixtureA.getUserData()).getSensorType().equals(SensorUserData.SensorType.FarSensor) &&
                    ((SensorUserData) fixtureB.getUserData()).getSensorType().equals(SensorUserData.SensorType.FarSensor)) {
                ((UserData) contact.getFixtureA().getBody().getUserData()).setSeeEnemy(SeeEnemy.DONT_SEE_ENEMY);
                ((UserData) contact.getFixtureB().getBody().getUserData()).setSeeEnemy(SeeEnemy.DONT_SEE_ENEMY);
            } else {
                leftOrRight(fixtureA, fixtureB, true);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}

