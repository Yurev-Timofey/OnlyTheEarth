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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.neuron.game.Configuration;
import com.neuron.game.MyGame;
import com.neuron.game.gameLogic.objects.persons.enemy.Skeleton;
import com.neuron.game.gameLogic.objects.userData.ObjectStatus;
import com.neuron.game.gameLogic.objects.userData.SeeEnemy;
import com.neuron.game.gameLogic.objects.userData.UserData;
import com.neuron.game.TrashBin.Controller;
import com.neuron.game.gameLogic.objects.Hud;
import com.neuron.game.gameLogic.objects.userData.ObjectType;
import com.neuron.game.gameLogic.objects.persons.Player;
import com.neuron.game.gameLogic.tools.MyContactFilter;
import com.neuron.game.gameLogic.tools.MyContactListener;

import static com.neuron.game.Configuration.PIXELS_IN_METER;

public class GameScreen implements Screen {

    private final MyGame game;
    private AssetManager assetManager;

    private final Stage stage;
    private Hud hud;

    private final OrthographicCamera camera;
    private final FillViewport viewport;
    private final OrthographicCamera debugCamera;
    private final Box2DDebugRenderer debugRenderer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;

    private World world;
    private Player player;


    GameScreen(MyGame game) { //TODO Пофиксить friction
        this.game = game;     // TODO: 06.05.2020 Пофиксить дерганье персонажа
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

        hud = new Hud(assetManager.get("images/Hud.atlas", TextureAtlas.class), viewport, player);
        hud.addToStage(stage);
    }

    private void loadAssets() {
        assetManager = new AssetManager();

        assetManager.load("images/Hud.atlas", TextureAtlas.class);
        assetManager.load("images/groundBlock.png", Texture.class);
        assetManager.load("animations/character.atlas", TextureAtlas.class);
        assetManager.load("images/AK-47.png", Texture.class);
        assetManager.load("animations/skeleton.atlas", TextureAtlas.class);
        assetManager.load("images/Heart.png", Texture.class);
        assetManager.load("images/bullet.png", Texture.class);

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
        stage.addActor(player.createGun(new TextureRegion(assetManager.get("images/AK-47.png", Texture.class)),
                (new TextureRegion(assetManager.get("images/bullet.png", Texture.class)))));

        world.setContactListener(new MyContactListener());
        world.setContactFilter(new MyContactFilter());

        /*Testing Enemy*/
        for (MapObject object : map.getLayers().get(3).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            Skeleton skeleton = new Skeleton(world, assetManager.get("animations/skeleton.atlas", TextureAtlas.class),
                    new Vector2(rectangle.getX() / PIXELS_IN_METER, (rectangle.getY() / PIXELS_IN_METER) + 0.1f),
                    new TextureRegion(assetManager.get("images/Heart.png", Texture.class)));
            stage.addActor(skeleton);
            stage.addActor(skeleton.createGun(new TextureRegion(assetManager.get("images/AK-47.png", Texture.class)),
                    (new TextureRegion(assetManager.get("images/bullet.png", Texture.class)))));
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (!player.isAlive() && !hud.isGameOverLabelCreated()) {
            hud.gameOver(stage, game.menuFont);
            stage.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    dispose();
                    game.setScreen(new GameScreen(game));
                }
            });
            delta = 0;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if ((player.getX() + (float) (Player.getSizeInPixels() / 2) - Configuration.viewportLeft >= Configuration.viewportWidth / 2)) {
            camera.position.set(player.getX() + (float) (Player.getSizeInPixels() / 2), camera.position.y, 0);
            debugCamera.position.set((player.getX() + (float) (Player.getSizeInPixels() / 2)) / PIXELS_IN_METER, debugCamera.position.y, 0);
            hud.setPosition(player.getX() - (Configuration.SCREEN_WIDTH - Player.getSizeInPixels()) / 2, 0);
        }

        mapRenderer.setView(camera);
        mapRenderer.render();

        stage.act(delta);
        world.step(delta, 6, 2);
        stage.draw();

        //DEBUG
        debugCamera.update();
//        debugRenderer.render(world, debugCamera.combined);
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
//        world.dispose();
        assetManager.dispose();
    }
}