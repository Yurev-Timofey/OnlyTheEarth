package com.neuron.game.gameLogic;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class MyAnimatedActor extends Actor {
    private Animation animation;
    private TextureRegion textureRegion;
    private float timer = 0;

    public MyAnimatedActor(int frameCount, Vector2 position, TextureAtlas atlas, float scale) {
        float sizeX = atlas.getRegions().first().originalWidth * scale;
        float sizeY = atlas.getRegions().first().originalHeight * scale;
        setPosition(position.x - sizeX / 2, position.y - sizeY / 2);
        setSize(sizeX, sizeY);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < frameCount; i++)
            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        animation = new Animation(0.2f, frames);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timer += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        textureRegion = (TextureRegion) animation.getKeyFrame(timer, true);
        batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
    }
}
