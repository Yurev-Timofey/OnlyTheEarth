package com.neuron.game.screens;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.neuron.game.Configuration;
import com.neuron.game.MyGame;
import com.neuron.game.gameLogic.objects.Background;

public class MenuScreen implements Screen {

    private final MyGame game;
    private Stage stage;
    Table menuButtons;
    Table settingsButtons;

    Music music;

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private AssetManager assetManager;


    public MenuScreen(final MyGame game) {
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
        Background background = new Background(assetManager.get(("images/mainMenuBack" + path + ".png"), Texture.class), viewport.getWorldWidth(), viewport.getWorldHeight());
        background.setPosition(0, 0);
        stage.addActor(background);

        createButtons();
        activateMenuButtons();

    }

    private void loadAssets(String path) {
        assetManager = new AssetManager();

        assetManager.load("music/mainTheme.mp3", Music.class);
        assetManager.load("images/button.png", Texture.class);
        assetManager.load("images/mainMenuBack" + path + ".png", Texture.class);

        assetManager.finishLoading();
    }

    private void createButtons() {
        //Определяем стиль кнопок
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.menuFont;
        Texture buttonImg = assetManager.get("images/button.png");
        Sprite buttonSprite = new Sprite(buttonImg);
        buttonSprite.setSize(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getWidth() / (4f * (float) buttonImg.getWidth() / buttonImg.getHeight()));
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
                activateSettingsButtons();
            }
        });

        //Группировка кнопок в таблицу
        menuButtons = new Table();
        menuButtons.setFillParent(true);
        menuButtons.center().bottom();

        menuButtons.add(play);
        menuButtons.row().space(stage.getWidth() / 200);
        menuButtons.add(settings);
        menuButtons.padBottom(stage.getWidth() / 50f + Configuration.viewportBottom);

        /* НАСТРОЙКИ */

        final TextButton volume = new TextButton("Громкость: " + 100 + '%', textButtonStyle);
        volume.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.vibrate(40);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (Configuration.volume < 0.2f) {
                    Configuration.volume = 1f;
                    volume.setText("Громкость: " + 100 + '%');
                } else {
                    Configuration.volume -= 0.2f;
                    volume.setText("Громкость: " + Math.round(Configuration.volume * 100) + '%');
                }
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
        settingsButtons.row().space(stage.getWidth() / 200);
        settingsButtons.add(back);
        settingsButtons.padBottom(stage.getWidth() / 50f + Configuration.viewportBottom);
        settingsButtons.debug();
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
