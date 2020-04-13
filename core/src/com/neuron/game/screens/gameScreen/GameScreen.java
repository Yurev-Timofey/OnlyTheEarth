package com.neuron.game.screens.gameScreen;

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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.neuron.game.Configuration;
import com.neuron.game.MyGame;
import com.neuron.game.gameLogic.controllers.Controller;
import com.neuron.game.gameLogic.objects.Hud;
import com.neuron.game.gameLogic.objects.Player;

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
    private final Player player;


    public GameScreen(MyGame game) {
        this.game = game;
        loadAssets();

        createWorld();
        createContactListener();

        player = new Player(world, assetManager.get("animations/character.atlas", TextureAtlas.class), new Vector2(1.5f, 2f));

        camera = new OrthographicCamera();
        viewport = new FillViewport(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT, camera);
        stage = new Stage(viewport);
        stage.addActor(player);
        stage.addActor(player.createGun(new TextureRegion(assetManager.get("images/AK-47.png", Texture.class))));


        mapRenderer = new OrthogonalTiledMapRenderer(map);

        //DEBUG
        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Configuration.SCREEN_WIDTH / Configuration.PIXELS_IN_METER,
                Configuration.SCREEN_HEIGHT / Configuration.PIXELS_IN_METER);
        debugRenderer = new Box2DDebugRenderer();

        controller = new Controller(player);
        hud = new Hud(assetManager.get("images/Control_buttons.atlas", TextureAtlas.class), viewport, controller);
        stage.addActor(hud.getButtonsTable());
    }

    private void loadAssets() {
        assetManager = new AssetManager();

        assetManager.load("images/Control_buttons.atlas", TextureAtlas.class);
        assetManager.load("images/character.png", Texture.class);
        assetManager.load("images/groundBlock.png", Texture.class);
        assetManager.load("animations/character.atlas", TextureAtlas.class);
        assetManager.load("images/AK-47.png", Texture.class);

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
            def.position.set((rectangle.getX() + rectangle.getWidth() / 2) / Configuration.PIXELS_IN_METER, (rectangle.getY() + rectangle.getHeight() / 2) / Configuration.PIXELS_IN_METER);

            PolygonShape shape = new PolygonShape();

            shape.setAsBox((rectangle.getWidth() / 2) / Configuration.PIXELS_IN_METER, (rectangle.getHeight() / 2) / Configuration.PIXELS_IN_METER);
            Body body = world.createBody(def);

            body.createFixture(shape, 3);
            body.setUserData("ground");
        }

        //Создание невидимых стен
        for (MapObject object : map.getLayers().get(2).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set((rectangle.getX() + rectangle.getWidth() / 2) / Configuration.PIXELS_IN_METER, (rectangle.getY() + rectangle.getHeight() / 2) / Configuration.PIXELS_IN_METER);

            PolygonShape shape = new PolygonShape();

            shape.setAsBox((rectangle.getWidth() / 2) / Configuration.PIXELS_IN_METER, (rectangle.getHeight() / 2) / Configuration.PIXELS_IN_METER);
            Body body = world.createBody(def);

            body.createFixture(shape, 3);
            body.setUserData("invisibleWall");
        }
    }

    private void createContactListener() {
        world.setContactListener(new ContactListener() {
            //Обнаружение контакта с землёй
            private boolean areContactingWith(String nameOfObject, String firstObject, String secondObject) {
                return (firstObject.equals(nameOfObject) ||
                        secondObject.equals(nameOfObject));
            }

            @Override
            public void beginContact(Contact contact) {
                String firstObject = (String) contact.getFixtureA().getBody().getUserData();
                String secondObject = (String) contact.getFixtureB().getBody().getUserData();
//                System.out.println(firstObject);
//                System.out.println(secondObject);
                if (areContactingWith("ground", firstObject, secondObject) &&
                        areContactingWith("player", firstObject, secondObject)) {
                    if (contact.getWorldManifold().getNormal().angle() == 90) {
                        player.setGrounded(true);
                    }
                }

                else if(areContactingWith("bullet", firstObject, secondObject) &&
                        !(firstObject.equals(secondObject))){
                    if (firstObject == "bullet"){
                        contact.getFixtureA().getBody().setUserData("dispose");
                    }
                    else{
                        contact.getFixtureB().getBody().setUserData("dispose");
                    }
                }
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


        if ((player.getX() + (float) (Player.getSizeInPixels() / 2) - Configuration.viewportLeft >= Configuration.viewportWidth / 2) &&
                player.isAlive()) {
            camera.position.set(player.getX() + (float) (Player.getSizeInPixels() / 2), camera.position.y, 0);
            debugCamera.position.set((player.getX() + (float) (Player.getSizeInPixels() / 2)) / Configuration.PIXELS_IN_METER, debugCamera.position.y, 0);
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
        mapRenderer.dispose();
        debugRenderer.dispose();
        player.detach();
        player.remove();
        world.dispose();
        assetManager.dispose();
        stage.dispose();
        game.dispose();
    }
}