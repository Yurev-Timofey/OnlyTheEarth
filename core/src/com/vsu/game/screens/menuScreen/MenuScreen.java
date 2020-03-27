package com.vsu.game.screens.menuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vsu.game.Configuration;
import com.vsu.game.MyGame;

public class MenuScreen implements Screen {

    private final MyGame game;
    private Stage stage;


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

        Music mainTheme = assetManager.get("music/mainTheme.mp3", Music.class); //Музыка
        mainTheme.play();
        mainTheme.setLooping(true);

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new MainMenuStage(viewport, game, assetManager, path);
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
        dispose();
    }

    @Override
    public void dispose() {
//        stage.dispose();  //TODO
        assetManager.dispose();
//        game.dispose();
    }
}
