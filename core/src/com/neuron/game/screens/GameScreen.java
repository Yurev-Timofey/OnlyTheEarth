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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.MyAnimatedActor;
import com.neuron.game.OnlyTheEarth;
import com.neuron.game.gameLogic.contacts.userData.ObjectStatus;
import com.neuron.game.gameLogic.contacts.userData.SeeEnemy;
import com.neuron.game.gameLogic.contacts.userData.UserData;
import com.neuron.game.gameLogic.Hud;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;
import com.neuron.game.gameLogic.objects.persons.Player;
import com.neuron.game.gameLogic.contacts.MyContactFilter;
import com.neuron.game.gameLogic.contacts.MyContactListener;

import static com.neuron.game.Configuration.PIXELS_IN_METER;

public class GameScreen implements Screen {

    private final OnlyTheEarth game;
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
    private final int LEVEL_LENGTH = 4000;
    private int currentLevel = 0;

    private Body nextLevelJump;


    GameScreen(OnlyTheEarth game) {
        this.game = game;
        loadAssets();

        camera = new OrthographicCamera();
        viewport = new FillViewport(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT, camera);
        stage = new Stage(viewport);

        //DEBUG
        debugCamera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer();

        nextLevel();
    }

    private void loadAssets() {
        assetManager = new AssetManager();

        assetManager.load("images/Hud.atlas", TextureAtlas.class);
        assetManager.load("images/groundBlock.png", Texture.class);
        assetManager.load("animations/player.atlas", TextureAtlas.class);
        assetManager.load("animations/skeleton.atlas", TextureAtlas.class);
        assetManager.load("animations/heart.atlas", TextureAtlas.class);
        assetManager.load("animations/portal.atlas", TextureAtlas.class);
        assetManager.load("images/bullet.png", Texture.class);

        assetManager.finishLoading();
    }

    private void nextLevel() {
        currentLevel++;

        try {
            TmxMapLoader mapLoader = new TmxMapLoader();
            map = mapLoader.load("maps/level_" + currentLevel + ".tmx");
            mapRenderer = new OrthogonalTiledMapRenderer(map);
        } catch (Exception ex) {
            if (!hud.isGameWinLabelCreated()) {
                hud.gameWin(stage, game.gameFont);
                stage.addListener(new ClickListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        game.setScreen(new MenuScreen(game));
                    }
                });
            }
            return;
        }
        if (world != null) {
            stage.clear();
            world.dispose();
            hud.remove();
        }

        camera.position.set(Configuration.SCREEN_WIDTH / 2, Configuration.SCREEN_HEIGHT / 2, 0);
        debugCamera.setToOrtho(false, Configuration.SCREEN_WIDTH / PIXELS_IN_METER, Configuration.SCREEN_HEIGHT / PIXELS_IN_METER);

        createLevel();
        hud = new Hud(assetManager.get("images/Hud.atlas", TextureAtlas.class), viewport, player);
        stage.addActor(hud);
    }

    private void createLevel() {
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

        /*Игрок*/
        MapObject mapObject = map.getLayers().get(4).getObjects().get(0);
        Rectangle playerPos = ((RectangleMapObject) mapObject).getRectangle();
        player = new Player(world, assetManager.get("animations/player.atlas", TextureAtlas.class),
                new Vector2(playerPos.getX() / PIXELS_IN_METER, (playerPos.getY() / PIXELS_IN_METER) + 0.1f));

        stage.addActor(player);
        player.createGun(stage, (new TextureRegion(assetManager.get("images/bullet.png", Texture.class))));

        /*Skeletons*/
//        for (MapObject object : map.getLayers().get(3).getObjects()) {
//            Rectangle skeletonPos = ((RectangleMapObject) object).getRectangle();
//            Skeleton skeleton = new Skeleton(world, assetManager.get("animations/skeleton.atlas", TextureAtlas.class),
//                    new Vector2(skeletonPos.getX() / PIXELS_IN_METER, (skeletonPos.getY() / PIXELS_IN_METER) + 0.1f),
//                    assetManager.get("animations/heart.atlas", TextureAtlas.class));
//            stage.addActor(skeleton);
//            skeleton.createGun(stage, (new TextureRegion(assetManager.get("images/bullet.png", Texture.class))));
//        }

        /*Переход на новый уровень*/
        for (MapObject object : map.getLayers().get(5).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PIXELS_IN_METER, (rectangle.getY() + rectangle.getHeight() / 2) / PIXELS_IN_METER);
            nextLevelJump = world.createBody(def);

            MyAnimatedActor portal = new MyAnimatedActor(8,
                    new Vector2(def.position.x * PIXELS_IN_METER, def.position.y * PIXELS_IN_METER),
                    assetManager.get("animations/portal.atlas", TextureAtlas.class), 3);
            stage.addActor(portal);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox((rectangle.getWidth() / 2) / PIXELS_IN_METER, (rectangle.getHeight() / 2) / PIXELS_IN_METER);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            shape.dispose();
            fixtureDef.density = 0.1f;
            fixtureDef.isSensor = true;
            nextLevelJump.createFixture(fixtureDef);
            nextLevelJump.setUserData(new UserData(ObjectType.NEXT_LEVEL, ObjectStatus.DEFAULT, null, false));
        }

        world.setContactListener(new MyContactListener());
        world.setContactFilter(new MyContactFilter());
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
                    game.setScreen(new MenuScreen(game));
                }
            });
            delta = 0;
        } else if (((UserData) nextLevelJump.getUserData()).getStatus().equals(ObjectStatus.JUMP_TO_NEXT_LEVEL)) {
            nextLevel();
        }
//        if (!hud.isGameWinLabelCreated())

        Gdx.gl.glClearColor(0.38f, 0.61f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        world.step(delta, 6, 2);
        if ((player.getRealX() + (float) (player.getSizeInPixels() / 2) - Configuration.viewportLeft >= Configuration.viewportWidth / 2) &&
                (player.getRealX() + (float) (player.getSizeInPixels() / 2) <= LEVEL_LENGTH - Configuration.viewportWidth / 2)) {
            camera.position.set(player.getRealX() + (float) (player.getSizeInPixels() / 2), camera.position.y, 0);
            debugCamera.position.set((player.getRealX() + (float) (player.getSizeInPixels() / 2)) / PIXELS_IN_METER, debugCamera.position.y, 0);
            hud.setPosition(player.getRealX() - (Configuration.SCREEN_WIDTH - player.getSizeInPixels()) / 2, 0);
        }
        mapRenderer.setView(camera);
        mapRenderer.render();


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
        stage.dispose();
        world.dispose();
        assetManager.dispose();
    }
}