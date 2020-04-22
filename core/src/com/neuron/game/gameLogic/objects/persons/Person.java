package com.neuron.game.gameLogic.objects.persons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.neuron.game.gameLogic.objects.ObjectStatus;
import com.neuron.game.gameLogic.objects.ObjectTypes;
import com.neuron.game.gameLogic.objects.guns.AK_47;
import com.neuron.game.gameLogic.objects.guns.Gun;
import com.neuron.game.gameLogic.states.PlayerStates.StandingState;
import com.neuron.game.gameLogic.states.State;


import static com.neuron.game.Configuration.PIXELS_IN_METER;

public abstract class Person extends Actor {
    protected TextureRegion textureRegion;
    protected Animation standingAnimation;
    protected Animation runningAnimation;
    protected Animation jumpingAnimation;
    protected Animation currentAnimation;

    protected int hp;
    protected int maxHp;
    protected boolean alive = true;
    protected boolean isRunningRight = true;
    protected boolean isGrounded;
    protected float velocity;

    protected World world;
    protected Body body;
    protected Fixture fixture;
    protected Fixture sensorFixture;

    State state;

    protected Gun gun;

    static float SIZE_IN_METERS;
    static int SIZE_IN_PIXELS;

    protected Person(World world, int maxHp, TextureAtlas atlas, Vector2 position, float sizeInMeters, ObjectTypes type) {
        this.maxHp = maxHp;
        hp = maxHp;
        this.world = world;
        SIZE_IN_METERS = sizeInMeters;
        SIZE_IN_PIXELS = (int) (PIXELS_IN_METER * SIZE_IN_METERS);

        createAnimations(atlas);
        definePersonBody(position, type);

        setSize(SIZE_IN_PIXELS, SIZE_IN_PIXELS);

        state = new StandingState(this);
    }

    private void createAnimations(TextureAtlas atlas) {
        Array<TextureRegion> frames = new Array<>();
//        for (int i = 0; i < 2; i++)
//            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        frames.add(new TextureRegion(atlas.findRegion("frame_" + 0)));
        standingAnimation = new Animation(0.3f, frames);
        frames.clear();
//
        for (int i = 2; i < 4; i++)
            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        runningAnimation = new Animation(0.2f, frames);
        frames.clear();

        for (int i = 1; i < 2; i++)
            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        jumpingAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    private void definePersonBody(Vector2 position, ObjectTypes type) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        body = world.createBody(def);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(SIZE_IN_METERS / 6, SIZE_IN_METERS / 3);
        fixture = body.createFixture(polygonShape, 13);
        fixture.setFriction(2);
        polygonShape.dispose();

        body.setUserData(type);
        fixture.setUserData(ObjectStatus.DEFAULT);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(1.5f);
        sensorFixture = body.createFixture(circleShape, 0);
        circleShape.dispose();
        sensorFixture.setSensor(true);
        sensorFixture.setUserData(ObjectStatus.DEFAULT);
    }

    public Gun createGun(TextureRegion texture) {
        gun = new AK_47(world, this, texture);
        return gun;
    }

    public void move(int direction) {
        velocity = 3.5f * direction;
        fixture.setFriction(0);
    }

    public void climbToBlock() {
        body.applyLinearImpulse(0, 9.5f, body.getPosition().x, body.getPosition().y, true); //TODO
    }

    public void resetVelocity() {
        velocity = 0;
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        fixture.setFriction(2);
    }

    public void jump() {
        if (isGrounded) {
            isGrounded = false;
            body.applyLinearImpulse(0, 20, body.getPosition().x, body.getPosition().y, true);
        }
    }

    private TextureRegion getFrame() {
        return (TextureRegion) currentAnimation.getKeyFrame(state.getTimer(), true);
    }

    public void setAnimation(State.states stateName) {
        switch (stateName) {
            case RunningState:
                currentAnimation = runningAnimation;
                break;
            case JumpingState:
                currentAnimation = jumpingAnimation;
                break;
            default:
                currentAnimation = standingAnimation;
                break;
        }
    }

    @Override
    public void act(float delta) {
        if (fixture.getUserData().equals(ObjectStatus.DAMAGED)) {
            hp -= 10;
            fixture.setUserData(ObjectStatus.DEFAULT);
            if (hp <= 0)
                remove();
        }

        if ((body.getLinearVelocity().x < velocity && velocity > 0) || (body.getLinearVelocity().x > velocity && velocity < 0))
            body.applyLinearImpulse(velocity, 0, body.getPosition().x, body.getPosition().y, true);

        state.update(delta);

        if (velocity < 0)
            isRunningRight = false;
        else if (velocity > 0)
            isRunningRight = true;

        textureRegion = getFrame();

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - SIZE_IN_METERS / 2) * PIXELS_IN_METER,
                (body.getPosition().y - SIZE_IN_METERS / 3) * PIXELS_IN_METER);

        if (!isRunningRight() && !textureRegion.isFlipX())
            textureRegion.flip(true, false);
        else if (isRunningRight() && textureRegion.isFlipX())
            textureRegion.flip(true, false);

        batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean remove() {
        world.destroyBody(body);
        gun.remove();
        return super.remove();
    }

    public Body getBody() {
        return body;
    }

    public Gun getGun() {
        return gun;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public static int getSizeInPixels() {
        return SIZE_IN_PIXELS;
    }

    public boolean isRunningRight() {
        return isRunningRight;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }
}
