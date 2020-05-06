package com.neuron.game.gameLogic.objects.userData;

public class UserData {
    private ObjectType objType;
    private ObjectStatus status;
    private SeeEnemy seeEnemy;
    private boolean grounded;

    public UserData(ObjectType objType, ObjectStatus status, SeeEnemy seeEnemy, boolean grounded) { //TODO Добавить уровень абстракции
        this.objType = objType;
        this.status = status;
        this.seeEnemy = seeEnemy;
        this.grounded = grounded;
    }

    public ObjectType getObjType() {
        return objType;
    }

    public ObjectStatus getStatus() {
        return status;
    }

    public void setStatus(ObjectStatus status) {
        this.status = status;
    }

    public SeeEnemy getSeeEnemy() {
        return seeEnemy;
    }

    public void setSeeEnemy(SeeEnemy seeEnemy) {
        this.seeEnemy = seeEnemy;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }
}