package com.vsu.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundActor extends Actor {
    private Sprite backgroundSprite;

    public BackgroundActor(Texture backgroundTexture, float screenWidth, float screenHeight) {
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest); //Тип фильтрации NEAR для пиксель арта
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(screenWidth, screenHeight);
    }
    public BackgroundActor(Texture backgroundTexture) {
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest); //Тип фильтрации NEAR для пиксель арта
        backgroundSprite = new Sprite(backgroundTexture);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        backgroundSprite.draw(batch);
    }
}