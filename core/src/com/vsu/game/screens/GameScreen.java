package com.vsu.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.vsu.game.Constants;
import com.vsu.game.MyGame;
import com.vsu.game.objects.PlayerEntity;
import com.vsu.game.objects.BackgroundActor;

public class GameScreen implements Screen{

    private final MyGame game;
    private Stage stage;

    private final OrthographicCamera camera;
    private final FillViewport viewport;
    private AssetManager assetManager;
    private World world;
    private PlayerEntity player;

    public GameScreen(MyGame game) {
        this.game = game;
        loadAssets();

        camera = new OrthographicCamera();
        viewport = new FillViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);
        stage = new Stage(viewport);
        BackgroundActor background = new BackgroundActor(assetManager.get("images/gameBackground.png", Texture.class));
        stage.addActor(background);

        createButtons();

        world = new World(new Vector2(0, -10), true);
        player = new PlayerEntity(world, assetManager.get("images/character.png", Texture.class), new Vector2(0,0));
        stage.addActor(player);



    }

    private void loadAssets() {
        assetManager = new AssetManager();

        assetManager.load("images/Control_buttons.atlas", TextureAtlas.class);
        assetManager.load("images/gameBackground.png", Texture.class);
        assetManager.load("images/character.png", Texture.class);
        assetManager.finishLoading();
    }

    private void createButtons() {
        Group uiButtons = new Group();
        uiButtons.setSize(stage.getWidth(), stage.getHeight());

        Skin skin = new Skin(assetManager.get("images/Control_buttons.atlas", TextureAtlas.class));
        final int PAD = 9;


        Table leftControlTable = new Table();
        leftControlTable.setFillParent(true);

        Sprite leftImg = skin.getSprite("Button_left");
        leftImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);

        ImageButton left = new ImageButton(new SpriteDrawable(leftImg));
        left.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        leftControlTable.add(left).space(viewport.getWorldWidth() / 100);

        Sprite rightImg = skin.getSprite("Button_right");
        rightImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);

        ImageButton right = new ImageButton(new SpriteDrawable(rightImg));
        right.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        leftControlTable.add(right);
        leftControlTable.left().bottom();
        leftControlTable.padBottom(game.VIEWPORT_BOTTOM + PAD);
        leftControlTable.padLeft(game.VIEWPORT_LEFT + PAD);

        uiButtons.addActor(leftControlTable);



        Table rightControlTable = new Table();
        rightControlTable.setFillParent(true);

        Sprite upImg = skin.getSprite("Button_up");
        upImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);

        ImageButton up = new ImageButton(new SpriteDrawable(upImg));
        up.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        rightControlTable.add(up).space(viewport.getWorldWidth() / 100);//

        Sprite fireImg = skin.getSprite("Button_right");
        fireImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);
        ImageButton fire = new ImageButton(new SpriteDrawable(fireImg));
        fire.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        rightControlTable.add(fire);
        rightControlTable.right().bottom();
        rightControlTable.padBottom(game.VIEWPORT_BOTTOM + PAD);
        rightControlTable.padRight(game.VIEWPORT_LEFT + PAD);

        uiButtons.addActor(rightControlTable);
//        stage.addActor(rightControlTable);

        stage.addActor(uiButtons);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        camera.update();


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        world.step(delta,6, 2);
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
        player.detach();
        player.remove();
        world.dispose();
        assetManager.dispose();
        stage.dispose();
        game.dispose();

    }
}