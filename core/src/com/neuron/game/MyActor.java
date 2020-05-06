package com.neuron.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MyActor extends Actor {
    protected TextureRegion textureRegion;

    public MyActor(TextureRegion textureRegion, int size) {
        this.textureRegion = textureRegion;
        setWidth(textureRegion.getRegionWidth() * size);
        setHeight(textureRegion.getRegionHeight() * size);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }
}
