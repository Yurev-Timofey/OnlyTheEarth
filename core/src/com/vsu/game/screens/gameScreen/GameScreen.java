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
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
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
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.vsu.game.Constants;
import com.vsu.game.MyGame;
import com.vsu.game.XmlParser;
import com.vsu.game.objects.GroundBlock;
import com.vsu.game.objects.Player;
import com.vsu.game.objects.BackgroundActor;

import java.util.Random;

public class GameScreen implements Screen {

    private final MyGame game;
    private Stage stage;

    private final OrthographicCamera camera;
    private final FillViewport viewport;
    private AssetManager assetManager;
    private World world;
    private Player player;
    private Box2DDebugRenderer debugRenderer;
    private final OrthographicCamera debugCamera;
    private Table buttonsTable;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(MyGame game) {
        this.game = game;
        loadAssets();

        camera = new OrthographicCamera();
        viewport = new FillViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);
        stage = new Stage(viewport);


        world = new World(new Vector2(0, -10), true);

//        Random rnd = new Random();
//        for (float i = 0; i < 10; i += GroundBlock.GROUND_BLOCK_SIZE_IN_METERS) {
//            GroundBlock groundBlock = new GroundBlock(world, assetManager.get("images/groundBlock.png", Texture.class), new Vector2(i, /*rnd.nextFloat() / 8*/ 0));
//            stage.addActor(groundBlock);
//        }


        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        for (MapObject object: map.getLayers().get(2).getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set((rectangle.getX() + rectangle.getWidth() / 2) / Constants.PIXELS_IN_METER , (rectangle.getY() +  rectangle.getHeight() / 2) / Constants.PIXELS_IN_METER) ;

            Body body = world.createBody(def);

            PolygonShape shape = new PolygonShape();


            shape.setAsBox((rectangle.getWidth() / 2) / Constants.PIXELS_IN_METER, (rectangle.getHeight() / 2) / Constants.PIXELS_IN_METER);


            Fixture fixture = body.createFixture(shape, 3);
        }

        player = new Player(world, assetManager.get("images/character.png", Texture.class), new Vector2(1.5f, 2f));
        stage.addActor(player);

        world.setContactListener(new ContactListener() {

            private boolean areContacting(Contact contact) {                                            //Обнаружение контакта с землёй
                return (player.getBody().getFixtureList().contains(contact.getFixtureA(), true) ||
                        player.getBody().getFixtureList().contains(contact.getFixtureB(), true));
            }

            @Override
            public void beginContact(Contact contact) {
                if (areContacting(contact))
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

        /* DEBUG */
        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Constants.SCREEN_WIDTH / Constants.PIXELS_IN_METER,
                Constants.SCREEN_HEIGHT / Constants.PIXELS_IN_METER);
        debugRenderer = new Box2DDebugRenderer();
        /* DEBUG */
    }

    private void loadAssets() {
        assetManager = new AssetManager();

        assetManager.load("images/Control_buttons.atlas", TextureAtlas.class);
//        assetManager.load("images/gameBackground.png", Texture.class);
        assetManager.load("images/character.png", Texture.class);
        assetManager.load("images/groundBlock.png", Texture.class);

        assetManager.finishLoading();
    }

    private void createButtons() {
        Skin skin = new Skin(assetManager.get("images/Control_buttons.atlas", TextureAtlas.class));
        final int PAD = 9;

        buttonsTable = new Table();
        buttonsTable.setFillParent(true);

        Sprite leftImg = skin.getSprite("Button_left");                                         //Кнопка передвижения влево
        leftImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);
        ImageButton left = new ImageButton(new SpriteDrawable(leftImg));
        left.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.move(-1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                player.resetVelocity();
            }
        });
        buttonsTable.add(left).space(viewport.getWorldWidth() / 100);


        Sprite rightImg = skin.getSprite("Button_right");                                         //Кнопка передвижения вправо
        rightImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);

        ImageButton right = new ImageButton(new SpriteDrawable(rightImg));
        right.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.move(1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                player.resetVelocity();
            }
        });
        buttonsTable.add(right).spaceRight(game.viewportWidth - (buttonsTable.getMinWidth() + PAD) * 2);


        Sprite upImg = skin.getSprite("Button_up");                                                     //Кнопка прыжка
        upImg.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);

        ImageButton up = new ImageButton(new SpriteDrawable(upImg));
        up.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.jump();
                return true;
            }
        });
        buttonsTable.add(up).space(viewport.getWorldWidth() / 100);


        Sprite fireImg = skin.getSprite("Button_fire");                                                //Кнопка стрельбы
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
        buttonsTable.padBottom(game.VIEWPORT_BOTTOM + PAD);
        buttonsTable.padLeft(game.VIEWPORT_LEFT + PAD);

        stage.addActor(buttonsTable);
    }

    @Override
    public void show() {
        createButtons();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        camera.update();
        debugCamera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if (player.getX() + 60 - game.VIEWPORT_LEFT >= game.viewportWidth / 2/*TODO*/ && player.isAlive()) {
            camera.position.set(player.getX() + 60, camera.position.y, 0);
            debugCamera.position.set((player.getX() + 60) / Constants.PIXELS_IN_METER, debugCamera.position.y, 0);
            buttonsTable.setPosition(player.getX() - Constants.SCREEN_WIDTH / 2 + 60, buttonsTable.getY());
        }
        mapRenderer.setView(camera);
        mapRenderer.render();
        stage.act(delta);
        world.step(delta, 6, 2);
        stage.draw();

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