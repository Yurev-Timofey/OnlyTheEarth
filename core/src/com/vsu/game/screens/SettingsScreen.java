package com.vsu.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.vsu.game.MyGame;

public class SettingsScreen implements Screen {

    public SettingsScreen(MyGame game){

    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true); // Это нужно для того, чтобы пользователь возвращался назад, в случае нажатия на кнопку Назад на своем устройстве
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}