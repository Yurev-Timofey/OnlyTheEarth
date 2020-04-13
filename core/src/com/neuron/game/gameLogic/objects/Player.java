package com.neuron.game.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.states.StandingState;
import com.neuron.game.gameLogic.states.State;
import com.neuron.game.gameLogic.objects.guns.AK_47;
import com.neuron.game.gameLogic.objects.guns.Gun;

public class Player extends Actor {
    private TextureRegion textureRegion;
    private World world;
    private Body body;
    private Fixture fixture;

    private boolean alive = true;
    private boolean isRunningRight = true;
    private boolean isGrounded;
    private float velocity;

    private static final float SIZE_IN_METERS = 1f;
    private static final int SIZE_IN_PIXELS = (int) (Configuration.PIXELS_IN_METER * SIZE_IN_METERS);

    private State state;
    private Gun gun;

    private Animation standingAnimation;
    private Animation runningAnimation;
    private Animation jumpingAnimation;
    private Animation currentAnimation;


    public Player(World world, TextureAtlas atlas, Vector2 position) {
        this.world = world;

        definePlayerBody(position);
        createAnimations(atlas);

        state = new StandingState(this);

        setSize(SIZE_IN_PIXELS, SIZE_IN_PIXELS);
    }

    private void definePlayerBody(Vector2 position) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        body = world.createBody(def);
        body.setUserData("player");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(SIZE_IN_METERS / 6, SIZE_IN_METERS / 3);
        fixture = body.createFixture(shape, 20);
        fixture.setFriction(0);
        shape.dispose();
    }

    private void createAnimations(TextureAtlas atlas) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        standingAnimation = new Animation(0.3f, frames);
        frames.clear();

        for (int i = 2; i < 4; i++)
            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        runningAnimation = new Animation(0.2f, frames);
        frames.clear();

        for (int i = 1; i < 2; i++)
            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        jumpingAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    public Gun createGun(TextureRegion texture) {
        gun = new AK_47(world, this, texture);
        return gun;
    }

    public Gun getGun() {
        return gun;
    }

    public Body getBody() {
        return body;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void jump() {
        if (isGrounded) {
            isGrounded = false;
            body.applyLinearImpulse(0, 20, body.getPosition().x, body.getPosition().y, true);
        }
    }

    public void move(int direction) {
        velocity = 3.5f * direction;
    }

    public void resetVelocity() {
        velocity = 0;
        body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    private TextureRegion getFrame() {
        return (TextureRegion) currentAnimation.getKeyFrame(state.getTimer(), true);
    }

    public void setAnimation(String stateName) {
        switch (stateName) {
            case "RunningState":
                currentAnimation = runningAnimation;
                break;
            case "JumpingState":
                currentAnimation = jumpingAnimation;
                break;
            default:
                currentAnimation = standingAnimation;
                break;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if ((body.getLinearVelocity().x < velocity && velocity > 0) || (body.getLinearVelocity().x > velocity && velocity < 0))
            body.applyLinearImpulse(velocity, 0, body.getPosition().x, body.getPosition().y, true);

        state.update(delta);

        if (body.getLinearVelocity().x < 0)
            isRunningRight = false;
        else if (body.getLinearVelocity().x > 0)
            isRunningRight = true;

        textureRegion = getFrame();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - SIZE_IN_METERS / 2) * Configuration.PIXELS_IN_METER,
                (body.getPosition().y - SIZE_IN_METERS / 3) * Configuration.PIXELS_IN_METER);

        if (!isRunningRight && !textureRegion.isFlipX())
            textureRegion.flip(true, false);
        else if (isRunningRight && textureRegion.isFlipX())
            textureRegion.flip(true, false);

        batch.draw(textureRegion, (body.getPosition().x - SIZE_IN_METERS / 2) * Configuration.PIXELS_IN_METER, (body.getPosition().y - SIZE_IN_METERS / 3) * Configuration.PIXELS_IN_METER, getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public float getVelocity() {
        return velocity;
    }

    public static int getSizeInPixels() {
        return SIZE_IN_PIXELS;
    }

    public boolean isRunningRight() {
        return isRunningRight;
    }
}
