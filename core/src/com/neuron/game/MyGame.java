package com.neuron.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.neuron.game.screens.MenuScreen;


public class MyGame extends Game {
    public BitmapFont gameFont;
    public BitmapFont menuFont;

    @Override
    public void create() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //Вызываем resize для вычисления aspectRatio

        //Создание шрифта игры
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixelSurNormal.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = (int) (Configuration.SCREEN_HEIGHT / 16.5); // Размер шрифта
        param.mono = true; //Отключение сглаживания шрифта
        param.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.:;,{}:\"´`'<>"; // Наши символы
        gameFont = generator.generateFont(param); // Генерируем шрифт
        gameFont.setColor(Color.WHITE); // Цвет белый
        param.size = Gdx.graphics.getWidth() / 28;
        menuFont = generator.generateFont(param);
        generator.dispose(); // Уничтожаем наш генератор за ненадобностью.

        setScreen(new MenuScreen(this));   //при запуске игры открываем экран с меню
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Configuration.aspectRatio = (float) width / height;           //Вычисление соотношения экрана для правильного маштаба
        if (Configuration.aspectRatio > 2) {
            Configuration.viewportHeight = Configuration.SCREEN_WIDTH / Configuration.aspectRatio;
            Configuration.viewportWidth = Configuration.SCREEN_WIDTH;
            Configuration.viewportLeft = 0;
            Configuration.viewportRight = Configuration.viewportWidth;
            Configuration.viewportBottom = (Configuration.SCREEN_HEIGHT - Configuration.viewportHeight) / 2;
            Configuration.viewportTop = Configuration.viewportHeight - Configuration.viewportBottom;
        } else {
            Configuration.viewportHeight = Configuration.SCREEN_HEIGHT;
            Configuration.viewportWidth = Configuration.SCREEN_HEIGHT * Configuration.aspectRatio;
            Configuration.viewportLeft = (Configuration.SCREEN_WIDTH - Configuration.viewportWidth) / 2;
            Configuration.viewportRight = Configuration.viewportLeft + Configuration.viewportWidth;
            Configuration.viewportBottom = 0;
            Configuration.viewportTop = Configuration.viewportHeight;
        }
    }
}