package com.vsu.game.screens.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.vsu.game.Configuration;
import com.vsu.game.MyGame;
import com.vsu.game.gameLogic.controllers.Controller;
import com.vsu.game.gameLogic.objects.Player;

public class GameScreen implements Screen {

    private final MyGame game;
    private AssetManager assetManager;
    private Controller controller;

    private final Stage stage;
    private Table buttonsTable;

    private final OrthographicCamera camera;
    private final FillViewport viewport;
    private final OrthographicCamera debugCamera;
    private final Box2DDebugRenderer debugRenderer;
    private OrthogonalTiledMapRenderer mapRenderer;

    private final World world;
    private final Player player;



    public GameScreen(MyGame game) {
        this.game = game;
        loadAssets();

        world = new World(new Vector2(0, -10), true);

        player = new Player(world, assetManager.get("images/character.png", Texture.class), new Vector2(1.5f, 2f));

        camera = new OrthographicCamera();
        viewport = new FillViewport(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT, camera);
        stage = new Stage(viewport);
        stage.addActor(player);

        createMap();

        world.setContactListener(new ContactListener() {

            private boolean areContacting(Contact contact) {                                            //Обнаружение контакта с землёй
                return (player.getBody().getFixtureList().contains(contact.getFixtureA(), true) ||
                        player.getBody().getFixtureList().contains(contact.getFixtureB(), true));
            }

            @Override
            public void beginContact(Contact contact) {
//                if(areContacting(contact))
                    player.setGrounded(true);
            }


            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

        //DEBUG
        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Configuration.SCREEN_WIDTH / Configuration.PIXELS_IN_METER,
                Configuration.SCREEN_HEIGHT / Configuration.PIXELS_IN_METER);
        debugRenderer = new Box2DDebugRenderer();

        controller = new Controller(player);
        createButtons();
    }

    private void loadAssets() {
        assetManager = new AssetManager();

        assetManager.load("images/Control_buttons.atlas", TextureAtlas.class);
        assetManager.load("images/character.png", Texture.class);
        assetManager.load("images/groundBlock.png", Texture.class);

        assetManager.finishLoading();
    }

    private void createButtons() {
        Skin skin = new Skin(assetManager.get("images/Control_buttons.atlas", TextureAtlas.class));
        final int PAD = 9;

        buttonsTable = new Table();
        buttonsTable.setFillParent(true);

        //Кнопка передвижения влево
        Sprite leftImg = skin.getSprite("Button_left");
        leftImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);
        ImageButton left = new ImageButton(new SpriteDrawable(leftImg));
        left.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.handleInput(event.getType(), "LEFT");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.handleInput(event.getType(), "LEFT");
            }
        });
        buttonsTable.add(left).space(viewport.getWorldWidth() / 100);

        //Кнопка передвижения вправо
        Sprite rightImg = skin.getSprite("Button_right");
        rightImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);

        ImageButton right = new ImageButton(new SpriteDrawable(rightImg));
        right.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.handleInput(event.getType(), "RIGHT");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.handleInput(event.getType(), "RIGHT");
            }
        });
        buttonsTable.add(right).spaceRight(Configuration.viewportWidth - (buttonsTable.getMinWidth() + PAD) * 2);

        //Кнопка прыжка
        Sprite upImg = skin.getSprite("Button_up");
        upImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);

        ImageButton up = new ImageButton(new SpriteDrawable(upImg));
        up.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.handleInput(event.getType(), "UP");
                return true;
            }
        });
        buttonsTable.add(up).space(viewport.getWorldWidth() / 100);

        //Кнопка стрельбы
        Sprite fireImg = skin.getSprite("Button_fire");
        fireImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);
        ImageButton fire = new ImageButton(new SpriteDrawable(fireImg));
        fire.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        buttonsTable.add(fire);

        buttonsTable.bottom().left();
        buttonsTable.padBottom(Configuration.viewportBottom + PAD);
        buttonsTable.padLeft(Configuration.viewportLeft + PAD);

        stage.addActor(buttonsTable);
    }

    private void createMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        //Создание блоков земли
        for (MapObject object : map.getLayers().get(1).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set((rectangle.getX() + rectangle.getWidth() / 2) / Configuration.PIXELS_IN_METER, (rectangle.getY() + rectangle.getHeight() / 2) / Configuration.PIXELS_IN_METER);

            PolygonShape shape = new PolygonShape();

            shape.setAsBox((rectangle.getWidth() / 2) / Configuration.PIXELS_IN_METER, (rectangle.getHeight() / 2) / Configuration.PIXELS_IN_METER);
            Body body = world.createBody(def);

            body.createFixture(shape, 3);
            body.setUserData("Ground");
        }

        //Создание невидимых стен
        for (MapObject object : map.getLayers().get(1).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set((rectangle.getX() + rectangle.getWidth() / 2) / Configuration.PIXELS_IN_METER, (rectangle.getY() + rectangle.getHeight() / 2) / Configuration.PIXELS_IN_METER);

            PolygonShape shape = new PolygonShape();

            shape.setAsBox((rectangle.getWidth() / 2) / Configuration.PIXELS_IN_METER, (rectangle.getHeight() / 2) / Configuration.PIXELS_IN_METER);
            Body body = world.createBody(def);

            body.createFixture(shape, 3);
        }
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


        if ((player.getX() + (float) (Player.getSizeInPixels() / 2) - Configuration.viewportLeft >= Configuration.viewportWidth / 2) &&
                player.isAlive()) {
            camera.position.set(player.getX() + (float) (Player.getSizeInPixels() / 2), camera.position.y, 0);
            debugCamera.position.set((player.getX() + (float) (Player.getSizeInPixels() / 2)) / Configuration.PIXELS_IN_METER, debugCamera.position.y, 0);
            buttonsTable.setPosition(player.getX() - Configuration.SCREEN_WIDTH / 2 + 60, buttonsTable.getY());
        }

        mapRenderer.setView(camera);
        mapRenderer.render();

        player.update();

        stage.act(delta);
        world.step(delta, 6, 2);
        stage.draw();

        //DEBUG
        debugCamera.update();
        debugRenderer.render(world, debugCamera.combined);
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
        player.detach();
        player.remove();
        world.dispose();
        assetManager.dispose();
        stage.dispose();
        game.dispose();
    }

    public World getWorld() {
        return world;
    }
}