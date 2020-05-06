package com.neuron.game.gameLogic.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Background extends Actor { // TODO: 28.04.2020 Удалить нафиг 
    private Sprite backgroundSprite;

    public Background(Texture backgroundTexture, float screenWidth, float screenHeight) {
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(screenWidth, screenHeight);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        backgroundSprite.draw(batch);
    }
}