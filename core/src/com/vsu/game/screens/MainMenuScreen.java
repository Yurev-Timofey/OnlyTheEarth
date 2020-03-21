package com.vsu.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.vsu.game.MyGame;

import com.vsu.game.objects.BackgroundActor;

public class MainMenuScreen implements Screen {

    private final MyGame game;
    private Stage stage;


    private final OrthographicCamera camera;
    private final FillViewport viewport;
    private AssetManager assetManager;


    public MainMenuScreen(final MyGame game) {
        this.game = game;

        String path;
        if (game.aspectRatio > 1.9)
            path = "18_9";
        else if (game.aspectRatio > 1.7)
            path = "16_9";
        else
            path = "4_3";

        loadAssets(path);

        Music mainTheme = assetManager.get("music/mainTheme.mp3", Music.class); //Музыка
        mainTheme.play();
        mainTheme.setLooping(true);

        camera = new OrthographicCamera();
        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new Stage(viewport);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();  //Определяем стиль кнопок
        textButtonStyle.font = game.menuFont;

        Texture buttonImg = assetManager.get("images/button.png");
        buttonImg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Sprite buttonSprite = new Sprite(buttonImg);

        buttonSprite.setSize(buttonSprite.getWidth() * 6 + Gdx.graphics.getWidth() / 10, buttonSprite.getHeight() * 7 + Gdx.graphics.getHeight() / 15);
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
                dispose();
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
                dispose();
            }
        });

        BackgroundActor background = new BackgroundActor(assetManager.get(("images/mainMenuBack" + path + ".png"), Texture.class), viewport.getWorldWidth(), viewport.getWorldHeight());
        background.setPosition(0, 0);

//        settings.setPosition(((viewport.getScreenWidth() - settings.getWidth()) / 2), settings.getHeight() / 4);
//        play.setPosition(((viewport.getScreenWidth() - play.getWidth()) / 2), settings.getY() + play.getHeight() + 7);
        Table buttons = new Table();
        buttons.setFillParent(true);
        buttons.add(play);
        buttons.row().space(viewport.getScreenWidth() / 200);
        buttons.add(settings);
        buttons.center().bottom();
        buttons.padBottom(viewport.getScreenWidth() / 50 + game.VIEWPORT_BOTTOM);

        stage.addActor(background);
        stage.addActor(buttons);
    }

    private void loadAssets(String path) {
        assetManager = new AssetManager();

        assetManager.load("music/mainTheme.mp3", Music.class);
        assetManager.load("images/button.png", Texture.class);
        assetManager.load("images/mainMenuBack" + path + ".png", Texture.class);

        assetManager.finishLoading();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);  // Устанавливаем нашу сцену основным процессором для ввода (нажатия, касания, клавиатура etc.)
    }

    @Override
    public void render(float delta) {
        camera.update();

        // Очищаем экран и устанавливаем цвет фона черным
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Рисуем сцену
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        stage.dispose();
        assetManager.dispose();
        game.dispose();
    }
}
