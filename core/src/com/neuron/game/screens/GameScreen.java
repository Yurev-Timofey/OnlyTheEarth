package com.neuron.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.neuron.game.Configuration;
import com.neuron.game.MyGame;
import com.neuron.game.gameLogic.objects.persons.enemy.Skeleton;
import com.neuron.game.gameLogic.objects.userData.ObjectStatus;
import com.neuron.game.gameLogic.objects.userData.SeeEnemy;
import com.neuron.game.gameLogic.objects.userData.UserData;
import com.neuron.game.gameLogic.tools.Controller;
import com.neuron.game.gameLogic.objects.Hud;
import com.neuron.game.gameLogic.objects.userData.ObjectType;
import com.neuron.game.gameLogic.objects.persons.Player;
import com.neuron.game.gameLogic.tools.MyContactFilter;
import com.neuron.game.gameLogic.tools.MyContactListener;

import static com.neuron.game.Configuration.PIXELS_IN_METER;

public class GameScreen implements Screen {

    private final MyGame game;
    private AssetManager assetManager;
    private Controller controller;

    private final Stage stage;
    private Hud hud;
//    private Table buttonsTable;

    private final OrthographicCamera camera;
    private final FillViewport viewport;
    private final OrthographicCamera debugCamera;
    private final Box2DDebugRenderer debugRenderer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;

    private World world;
    private Player player;


    final public static short CATEGORY_PLAYER = 0x0001; // 0000000000000001 in binary
    final public static short CATEGORY_ENEMY = 0x0002; // 0000000000000010 in binary
    final public static short CATEGORY_GROUND = 0x0004; // 0000000000000100 in binary


    GameScreen(MyGame game) {
        this.game = game;
        loadAssets();

        camera = new OrthographicCamera();
        viewport = new FillViewport(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT, camera);
        stage = new Stage(viewport);

        createWorld();

        mapRenderer = new OrthogonalTiledMapRenderer(map);

        //DEBUG
        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Configuration.SCREEN_WIDTH / PIXELS_IN_METER,
                Configuration.SCREEN_HEIGHT / PIXELS_IN_METER);
        debugRenderer = new Box2DDebugRenderer();

        controller = new Controller(player);
        hud = new Hud(assetManager.get("images/Control_buttons.atlas", TextureAtlas.class), viewport, controller);
        stage.addActor(hud.getButtonsTable());
    }

    private void loadAssets() {
        assetManager = new AssetManager();

        assetManager.load("images/Control_buttons.atlas", TextureAtlas.class);
        assetManager.load("images/groundBlock.png", Texture.class);
        assetManager.load("animations/character.atlas", TextureAtlas.class);
        assetManager.load("images/AK-47.png", Texture.class);
        assetManager.load("animations/skeleton.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/map.tmx");
    }

    private void createWorld() {
        world = new World(new Vector2(0, -10), true);

        //Создание блоков земли
        for (MapObject object : map.getLayers().get(1).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PIXELS_IN_METER, (rectangle.getY() + rectangle.getHeight() / 2) / PIXELS_IN_METER);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox((rectangle.getWidth() / 2) / PIXELS_IN_METER, (rectangle.getHeight() / 2) / PIXELS_IN_METER);

            Body body = world.createBody(def);
            body.createFixture(shape, 3);

            body.setUserData(new UserData(ObjectType.GROUND, ObjectStatus.DEFAULT, SeeEnemy.DONT_SEE_ENEMY, false));
        }

        //Создание невидимых стен
        for (MapObject object : map.getLayers().get(2).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PIXELS_IN_METER, (rectangle.getY() + rectangle.getHeight() / 2) / PIXELS_IN_METER);

            PolygonShape shape = new PolygonShape();

            shape.setAsBox((rectangle.getWidth() / 2) / PIXELS_IN_METER, (rectangle.getHeight() / 2) / PIXELS_IN_METER);
            Body body = world.createBody(def);

            body.createFixture(shape, 3);
            body.setUserData(new UserData(ObjectType.WALL, ObjectStatus.DEFAULT, SeeEnemy.DONT_SEE_ENEMY, false));
        }

        player = new Player(world, assetManager.get("animations/character.atlas", TextureAtlas.class), new Vector2(1.5f, 2f));
        stage.addActor(player);
        stage.addActor(player.createGun(new TextureRegion(assetManager.get("images/AK-47.png", Texture.class))));

        world.setContactListener(new MyContactListener());
        world.setContactFilter(new MyContactFilter());

        /*Testing Enemy*/
        for (MapObject object : map.getLayers().get(3).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            Skeleton skeleton = new Skeleton(world, assetManager.get("animations/skeleton.atlas", TextureAtlas.class), new Vector2(rectangle.getX() / PIXELS_IN_METER, (rectangle.getY() / PIXELS_IN_METER) + 0.1f));
            stage.addActor(skeleton);
            stage.addActor(skeleton.createGun(new TextureRegion(assetManager.get("images/AK-47.png", Texture.class))));
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
//        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if ((player.getX() + (float) (Player.getSizeInPixels() / 2) - Configuration.viewportLeft >= Configuration.viewportWidth / 2)) {
            camera.position.set(player.getX() + (float) (Player.getSizeInPixels() / 2), camera.position.y, 0);
            debugCamera.position.set((player.getX() + (float) (Player.getSizeInPixels() / 2)) / PIXELS_IN_METER, debugCamera.position.y, 0);
            hud.getButtonsTable().setPosition(player.getX() - Configuration.SCREEN_WIDTH / 2 + 60, hud.getButtonsTable().getY());
        }

        mapRenderer.setView(camera);
        mapRenderer.render();


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
        map.dispose();
        player.remove();
        world.dispose();
        assetManager.dispose();
        stage.dispose();
    }
}