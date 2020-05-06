package com.neuron.game.gameLogic.objects.persons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.neuron.game.gameLogic.objects.userData.ObjectStatus;
import com.neuron.game.gameLogic.objects.userData.ObjectType;
import com.neuron.game.gameLogic.objects.guns.AK_47;
import com.neuron.game.gameLogic.objects.guns.Gun;
import com.neuron.game.gameLogic.objects.userData.SeeEnemy;
import com.neuron.game.gameLogic.objects.userData.SensorUserData;
import com.neuron.game.gameLogic.objects.userData.UserData;
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

    protected final float maxVelocity;
    protected float velocity;

    protected World world;
    protected Body body;
    protected Fixture fixture;
    protected Fixture NearSensorFixture;
    protected Fixture FarSensorFixture;

    State state;

    protected Gun gun;

    static float SIZE_IN_METERS;
    static int SIZE_IN_PIXELS;

    protected Person(World world, int maxHp, float maxVelocity, TextureAtlas atlas, Vector2 position, float sizeInMeters, ObjectType type) {
        this.maxHp = maxHp;
        this.maxVelocity = maxVelocity;
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

    private void definePersonBody(Vector2 position, ObjectType type) {
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

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(1.6f);
        NearSensorFixture = body.createFixture(circleShape, 0);
        circleShape.setRadius(2.6f);
        FarSensorFixture = body.createFixture(circleShape, 0);
        circleShape.dispose();

        NearSensorFixture.setSensor(true);
        FarSensorFixture.setSensor(true);
        NearSensorFixture.setUserData(new SensorUserData(SensorUserData.SensorType.NearSensor, type));
        FarSensorFixture.setUserData(new SensorUserData(SensorUserData.SensorType.FarSensor, type));

        body.setUserData(new UserData(type, ObjectStatus.DEFAULT, SeeEnemy.DONT_SEE_ENEMY, true));
    }


    public Gun createGun(TextureRegion texture, TextureRegion bulletTexture) {
        gun = new AK_47(world, this, texture, bulletTexture);
        return gun;
    }

    public void move(int direction) {
        velocity = maxVelocity * direction;
        fixture.setFriction(0);
    }

    public void climbToBlock() {
        setGrounded(false);
        body.applyLinearImpulse(0, 10, body.getPosition().x, body.getPosition().y, true); //TODO
    }

    public void resetVelocity() {
        velocity = 0;
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        fixture.setFriction(2);
    }

    public void jump() {
        if (isGrounded) {
            ((UserData) body.getUserData()).setGrounded(false);
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
        if ((body.getLinearVelocity().x < velocity && velocity > 0) || (body.getLinearVelocity().x > velocity && velocity < 0))
            body.applyLinearImpulse(velocity, 0, body.getPosition().x, body.getPosition().y, true);

        state.update(delta);
        userDataChecker((UserData) body.getUserData());

        if (velocity < 0)
            isRunningRight = false;
        else if (velocity > 0)
            isRunningRight = true;

        textureRegion = getFrame();

        super.act(delta);
    }

    private void userDataChecker(UserData data) {
        isGrounded = (data.isGrounded() && body.getLinearVelocity().y < 1 && body.getLinearVelocity().y > -1);
        switch (data.getStatus()) {
            case DAMAGED:
                hp -= 5;
                data.setStatus(ObjectStatus.DEFAULT);
                if (hp <= 0)
                    remove();
                break;
            case CLIMB_TO_BLOCK:
                if (isGrounded) {
                    climbToBlock();
                    data.setStatus(ObjectStatus.DEFAULT);
                }
                break;
            case HEALED:
                if (hp + 20 <= maxHp)
                    hp += 20;
                else
                    hp = maxHp;
                data.setStatus(ObjectStatus.DEFAULT);
                break;
        }
        if (state.getType().equals(State.states.JumpingState))
            data.setStatus(ObjectStatus.DEFAULT);
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
        alive = false;
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

    public int getHp() {
        return hp;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
        ((UserData) body.getUserData()).setGrounded(false);
    }
}
