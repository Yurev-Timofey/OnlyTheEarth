package com.vsu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import screens.*;

public class MyGame extends Game {
    public BitmapFont gameFont;
    public BitmapFont menuFont;

    public static final float SCREEN_WIDTH = 720f;
    public static final float SCREEN_HEIGHT = 360f;
    public static float VIEWPORT_LEFT;
    public static float VIEWPORT_RIGHT;
    public float aspectRatio;
    public static float viewportWidth;

    @Override
    public void create() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //Вызываем resize для вычисления aspectRatio

        //Создание шрифта игры
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixelSurNormal.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = (int) (SCREEN_HEIGHT / 16.5); // Размер шрифта
        param.mono = true; //Отключение сглаживания шрифта
        param.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>"; // Наши символы
        gameFont = generator.generateFont(param); // Генерируем шрифт
        gameFont.setColor(Color.WHITE); // Цвет белый
        param.size = Gdx.graphics.getWidth() / 28;
        menuFont = generator.generateFont(param);
        generator.dispose(); // Уничтожаем наш генератор за ненадобностью.

        setScreen(new MainMenuScreen(this));   //при запуске игры открываем экран с меню
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        aspectRatio = (float) width / height;           //Вычисление соотношения экрана для правильного маштаба
        viewportWidth = SCREEN_HEIGHT * aspectRatio;

        VIEWPORT_LEFT = (SCREEN_WIDTH - viewportWidth) / 2;   //Левая и правая граница вьюпорта для привязки к краям экрана
        VIEWPORT_RIGHT = VIEWPORT_LEFT + viewportWidth;
    }
}