package com.vsu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.vsu.game.screens.menuScreen.MenuScreen;

import static com.vsu.game.Configuration.*;


public class MyGame extends Game {
    public BitmapFont gameFont;
    public BitmapFont menuFont;

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

        setScreen(new MenuScreen(this));   //при запуске игры открываем экран с меню
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        aspectRatio = (float) width / height;           //Вычисление соотношения экрана для правильного маштаба

        viewportHeight = SCREEN_WIDTH / aspectRatio;
        System.out.println(viewportHeight);
        if (aspectRatio > 2) {
            viewportWidth = SCREEN_WIDTH;
            viewportLeft = 0;
            viewportRight = viewportWidth;
            viewportBottom = (SCREEN_HEIGHT - viewportHeight) / 2;
            viewportTop = viewportBottom + viewportHeight;
        } else {
            viewportWidth = SCREEN_HEIGHT * aspectRatio;
            viewportLeft = (SCREEN_WIDTH - viewportWidth) / 2;   //Левая и правая граница вьюпорта для привязки к краям экрана
            viewportRight = viewportLeft + viewportWidth;
            viewportBottom = 0;
            viewportTop = viewportHeight;
        }
    }
}