package com.neuron.game.gameLogic.contacts.userData;

public class SensorUserData {
    private SensorType sensorType;
    private ObjectType bodyType;

    public SensorUserData(SensorType sensorType, ObjectType bodyType) {
        this.sensorType = sensorType;
        this.bodyType = bodyType;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public ObjectType getBodyType() {
        return bodyType;
    }

    public enum SensorType {
        Default,
        FarSensor,
        NearSensor
    }
}
