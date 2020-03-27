package com.vsu.game.screens.menuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.vsu.game.Configuration;
import com.vsu.game.MyGame;
import com.vsu.game.gameLogic.objects.BackgroundActor;
import com.vsu.game.screens.gameScreen.GameScreen;

public class MainMenuStage extends Stage {


    public MainMenuStage(FitViewport viewport, final MyGame game, AssetManager assetManager, String path) {
        setViewport(viewport);

        //Фон меню
        BackgroundActor background = new BackgroundActor(assetManager.get(("images/mainMenuBack" + path + ".png"), Texture.class), viewport.getWorldWidth(), viewport.getWorldHeight());
        background.setPosition(0, 0);
        addActor(background);

        //Определяем стиль кнопок
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.menuFont;
        Texture buttonImg = assetManager.get("images/button.png");
        Sprite buttonSprite = new Sprite(buttonImg);
        buttonSprite.setSize(Gdx.graphics.getWidth() / 4f , Gdx.graphics.getWidth() / (4f *(float)buttonImg.getWidth()/buttonImg.getHeight()));
        SpriteDrawable button = new SpriteDrawable(buttonSprite);
        textButtonStyle.up = button;
        textButtonStyle.down = button;

        //Кнопка "Играть"
        TextButton play = new TextButton("Играть", textButtonStyle);
        play.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.vibrate(40);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        //Кнопка настроек
        TextButton settings = new TextButton("Настройки", textButtonStyle);
        settings.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.vibrate(40);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                game.setScreen(new SettingsScreen(game));
//                dispose();
            }
        });

        //Группировка кнопок в таблицу
        Table buttons = new Table();
        buttons.setFillParent(true);
        buttons.center().bottom();
        buttons.debug();

        buttons.add(play);
        buttons.row().space(getWidth() / 200);
        buttons.add(settings);
        buttons.padBottom(getWidth() / 50f + Configuration.viewportBottom);

        addActor(buttons);
    }

    @Override
    public void dispose() {
        super.dispose();
//        MenuScreen.dispose();
    }
}
