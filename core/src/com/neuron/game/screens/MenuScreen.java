package com.neuron.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.MyAnimatedActor;
import com.neuron.game.OnlyTheEarth;

public class MenuScreen implements Screen {

    private final OnlyTheEarth game;
    private Stage stage;
    Table menuButtons;
    Table settingsButtons;

    Music music;

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private AssetManager assetManager;


    public MenuScreen(final OnlyTheEarth game) {
        this.game = game;

        String path;
        if (Configuration.aspectRatio > 1.9)
            path = "18_9";
        else if (Configuration.aspectRatio > 1.7)
            path = "16_9";
        else
            path = "4_3";

        loadAssets(path);

        music = assetManager.get("music/mainTheme.mp3", Music.class); //Музыка
        music.play();
        music.setLooping(true);

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new Stage(viewport);

        //Фон меню
//        Background background = new Background(assetManager.get(("images/mainMenuBack" + path + ".png"), Texture.class), viewport.getWorldWidth(), viewport.getWorldHeight());
//        background.setPosition(0, 0);
//        stage.addActor(background);

        createButtons();
        activateMenuButtons();

        stage.addActor(new MyAnimatedActor(13, new Vector2(((float) Gdx.graphics.getWidth() / 2), Gdx.graphics.getHeight() / 1.5f),
                assetManager.get("animations/Logo.atlas", TextureAtlas.class), Gdx.graphics.getHeight() / 80));
    }

    private void loadAssets(String path) {
        assetManager = new AssetManager();

        assetManager.load("music/mainTheme.mp3", Music.class);
        assetManager.load("images/MenuButtons.atlas", TextureAtlas.class);
        assetManager.load("images/mainMenuBack" + path + ".png", Texture.class);
        assetManager.load("animations/Logo.atlas", TextureAtlas.class);

        assetManager.finishLoading();
    }

    private void createButtons() {
        final float PAD = 9f;
        final float buttonsSize = Configuration.viewportHeight / 92;
        //Определяем стиль кнопок
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.menuFont;
        Skin buttonSkin = new Skin(assetManager.get("images/MenuButtons.atlas", TextureAtlas.class));

        Sprite buttonUpSprite = new Sprite(buttonSkin.getSprite("Big_button"));
        Sprite buttonDownSprite = new Sprite(buttonSkin.getSprite("Big_button_pressed"));

        buttonUpSprite.setSize(Gdx.graphics.getWidth() / buttonsSize * 1.19f,
                Gdx.graphics.getWidth() / (buttonsSize * (float) buttonUpSprite.getRegionWidth() / buttonUpSprite.getRegionHeight()));
        buttonDownSprite.setSize(Gdx.graphics.getWidth() / buttonsSize * 1.19f,
                Gdx.graphics.getWidth() / (buttonsSize * (buttonsSize * (float) buttonDownSprite.getRegionWidth() / buttonDownSprite.getRegionHeight())));

        SpriteDrawable buttonUp = new SpriteDrawable(buttonUpSprite);
        SpriteDrawable buttonDown = new SpriteDrawable(buttonDownSprite);

        textButtonStyle.up = buttonUp;
        textButtonStyle.down = buttonDown;

        textButtonStyle.unpressedOffsetY = 5;
        textButtonStyle.checkedOffsetY = 5;
        textButtonStyle.pressedOffsetY = 2;

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
                activateSettingsButtons();
            }
        });

        //Группировка кнопок в таблицу
        menuButtons = new Table();
        menuButtons.setFillParent(true);
        menuButtons.center().bottom();

        menuButtons.add(play);
        menuButtons.row().space(PAD);
        menuButtons.add(settings);
        menuButtons.padBottom(PAD * 2 + Configuration.viewportBottom);


        /* НАСТРОЙКИ */
        final TextButton volume = new TextButton("Громкость:" + 100 + '%', textButtonStyle);
        volume.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.vibrate(40);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (Configuration.volume < 0.2f)
                    Configuration.volume = 1f;
                else
                    Configuration.volume -= 0.2f;
                volume.setText("Громкость:" + Math.round(Configuration.volume * 100) + '%');
                music.setVolume(Configuration.volume);
            }
        });

        final TextButton back = new TextButton("Назад", textButtonStyle);
        back.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.vibrate(40);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                activateMenuButtons();
            }
        });

        settingsButtons = new Table();
        settingsButtons.setFillParent(true);
        settingsButtons.center().bottom();

        settingsButtons.add(volume);
        settingsButtons.row().space(PAD);
        settingsButtons.add(back);
        settingsButtons.padBottom(PAD * 2 + Configuration.viewportBottom);
    }

    private void activateMenuButtons() {
        settingsButtons.remove();
        stage.addActor(menuButtons);
    }

    private void activateSettingsButtons() {
        menuButtons.remove();
        stage.addActor(settingsButtons);
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
        dispose();
    }

    @Override
    public void dispose() {
        music.dispose();
        assetManager.dispose();
    }
}
