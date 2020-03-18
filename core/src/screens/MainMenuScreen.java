package screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.vsu.game.MyGame;

import objects.BackgroundActor;

public class MainMenuScreen implements Screen {

    final MyGame game;
    private Music mainTheme;
    private TextButton play, settings;
    private Stage stage;


    private final OrthographicCamera camera;
    private final FillViewport viewport;
    private AssetManager assetManager;


    public MainMenuScreen(final MyGame myGame) {
        game = myGame;

        float aspectRatio = Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        String path;
        if (aspectRatio == 2)
            path = "18_9";
        else if (aspectRatio > 1.7)
            path = "16_9";
        else
            path = "4_3";

        this.loadAssets(path);

        mainTheme = assetManager.get("music/mainTheme.mp3", Music.class); //Музыка
        mainTheme.play();
        mainTheme.setLooping(true);

        camera = new OrthographicCamera();
        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        stage = new Stage(viewport);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();  //Определяем стиль кнопок
        textButtonStyle.font = game.menuFont;                                               //Шрифт

//        Skin skin = new Skin();                                                        //Картинка кнопки
//        skin.add("button", Gdx.files.internal("images/button.jpg"));
//        textButtonStyle.up = skin.getDrawable("button");
//        textButtonStyle.down = skin.getDrawable("button");
//        textButtonStyle.checked = skin.getDrawable("button");

        Texture buttonImg = assetManager.get("images/button.png");
        buttonImg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Sprite buttonSprite = new Sprite(buttonImg);
//        if (aspectRatio == 2)
//            buttonSprite.setSize(buttonSprite.getWidth()  * (float)(aspectRatio * 6), buttonSprite.getHeight()*(float)(aspectRatio * 6));
//        else if (aspectRatio > 1.7)
//            buttonSprite.setSize(buttonSprite.getWidth()  * (float)(aspectRatio * 6.5), buttonSprite.getHeight()*(float)(aspectRatio * 6.5));
//        else
            buttonSprite.setSize(buttonSprite.getWidth()  * (float)(aspectRatio * 6), buttonSprite.getHeight()*(float)(aspectRatio * 6));
        //fixme после нового спрайта нормально выставить соотнощение
        SpriteDrawable button = new SpriteDrawable(buttonSprite);
        textButtonStyle.up = button;
        textButtonStyle.down = button;
//        textButtonStyle.checked = button;

        play = new TextButton("Играть", textButtonStyle);
        play.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                Gdx.input.vibrate(20);     //TODO
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        settings = new TextButton("Настройки", textButtonStyle); //Кнопка настроек
        settings.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                Gdx.input.vibrate(20);    //TODO
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });

        BackgroundActor background = new BackgroundActor(assetManager.get(("images/mainMenuBack" + path + ".png"), Texture.class), viewport.getWorldWidth(), viewport.getWorldHeight());

//        Sprite backgroundSprite = new Sprite(assetManager.get(("images/mainMenuBack" + path + ".png"), Texture.class));
////        backgroundSprite.setSize((int)viewport.getWorldWidth(),(int) viewport.getWorldHeight());
//        SimpleActor background = new SimpleActor(backgroundSprite, viewport.getWorldWidth(), viewport.getWorldHeight());

        background.setPosition(0, 0);


        settings.setPosition(((viewport.getScreenWidth() - play.getWidth()) / 2), settings.getHeight() / 3);
        play.setPosition(((viewport.getScreenWidth() - play.getWidth()) / 2), settings.getY() + play.getHeight() + 5);

        stage.addActor(background);
        stage.addActor(play);
        stage.addActor(settings);

        Gdx.input.setInputProcessor(stage);  // Устанавливаем нашу сцену основным процессором для ввода (нажатия, касания, клавиатура etc.)
//        Gdx.input.setCatchBackKey(true); // Это нужно для того, чтобы пользователь возвращался назад, в случае нажатия на кнопку Назад на своем устройстве
    }
    public void loadAssets(String path){
        assetManager = new AssetManager();

        assetManager.load("music/mainTheme.mp3", Music.class);
        assetManager.load("images/button.png", Texture.class);
        assetManager.load("images/mainMenuBack" + path + ".png", Texture.class);
        assetManager.finishLoading();
    }

    @Override
    public void show() {
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
