package com.vsu.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vsu.game.MyGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        config.width =  1920;
//        config.height = 1080;
//        config.width = (int) MyGame.SCREEN_WIDTH;
//        config.height = (int) MyGame.SCREEN_HEIGHT;
//        config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
//        config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        config.fullscreen = true;

        new LwjglApplication(new MyGame(), config);
    }
}
