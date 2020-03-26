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
import com.vsu.game.MyGame;
import com.vsu.game.objects.BackgroundActor;
import com.vsu.game.screens.gameScreen.GameScreen;

public class MainMenuStage extends Stage{


    public MainMenuStage(FitViewport viewport, final MyGame game, AssetManager assetManager, String path){
        setViewport(viewport);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();  //Определяем стиль кнопок
        textButtonStyle.font = game.menuFont;

        Texture buttonImg = assetManager.get("images/button.png");
        buttonImg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Sprite buttonSprite = new Sprite(buttonImg);

        buttonSprite.setSize(buttonSprite.getWidth() * 6 + Gdx.graphics.getWidth() / 10f, buttonSprite.getHeight() * 7 + Gdx.graphics.getHeight() / 15f);
        SpriteDrawable button = new SpriteDrawable(buttonSprite);
        textButtonStyle.up = button;
        textButtonStyle.down = button;

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
//                dispose();
            }
        });

        TextButton settings = new TextButton("Настройки", textButtonStyle); //Кнопка настроек
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

        BackgroundActor background = new BackgroundActor(assetManager.get(("images/mainMenuBack" + path + ".png"), Texture.class), viewport.getWorldWidth(), viewport.getWorldHeight());
        background.setPosition(0, 0);

//        settings.setPosition(((viewport.getScreenWidth() - settings.getWidth()) / 2), settings.getHeight() / 4);
//        play.setPosition(((viewport.getScreenWidth() - play.getWidth()) / 2), settings.getY() + play.getHeight() + 7);
        Table buttons = new Table();
        buttons.setFillParent(true);
        buttons.add(play);
        buttons.row().space(getWidth() / 200);
        buttons.add(settings);
        buttons.center().bottom();
        buttons.padBottom(getWidth() / 50f + game.VIEWPORT_BOTTOM);
        buttons.debug();

        addActor(background);
        addActor(buttons);
    }

    @Override
    public void dispose() {
        super.dispose();
//        MenuScreen.dispose();
    }
}
