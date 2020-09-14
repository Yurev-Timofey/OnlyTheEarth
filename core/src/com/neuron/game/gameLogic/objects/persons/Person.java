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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.neuron.game.gameLogic.contacts.userData.ObjectStatus;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;
import com.neuron.game.gameLogic.objects.guns.Gun;
import com.neuron.game.gameLogic.contacts.userData.SeeEnemy;
import com.neuron.game.gameLogic.contacts.userData.SensorUserData;
import com.neuron.game.gameLogic.contacts.userData.UserData;
import com.neuron.game.gameLogic.states.PersonStates.StandingState;
import com.neuron.game.gameLogic.states.State;


import static com.neuron.game.Configuration.PIXELS_IN_METER;

public abstract class Person extends Actor {
    protected TextureRegion textureRegion;
    protected Animation standingAnimation;
    protected Animation runningAnimation;
    protected Animation jumpingAnimation;
    protected Animation currentAnimation;

    protected World world;
    protected Body body;
    protected Fixture fixture;
    protected Fixture NearSensorFixture;
    protected Fixture FarSensorFixture;


    protected final int maxHp;
    protected int hp;
    protected final float maxVelocity;
    protected float velocity;
    protected boolean alive = true;
    protected boolean isRunningRight = true;
    protected boolean isGrounded;

    protected State state;

    protected Gun gun;

    protected float SIZE_IN_METERS;
    protected int SIZE_IN_PIXELS;
    protected float HALF_X_SIZE;
    protected float HALF_Y_SIZE;
    protected float X_PAD;
    protected float Y_PAD;

    protected Person(World world, int maxHp, float maxVelocity, TextureAtlas atlas, Vector2 position,
                     float sizeInMeters, ObjectType type, float X_PAD, float Y_PAD) {
        this.world = world;
        this.maxHp = maxHp;
        this.maxVelocity = maxVelocity;
        this.X_PAD = X_PAD;
        this.Y_PAD = Y_PAD;

        hp = maxHp;
        SIZE_IN_METERS = sizeInMeters;
        SIZE_IN_PIXELS = (int) (PIXELS_IN_METER * SIZE_IN_METERS);

        createAnimations(atlas);
        definePersonBody(position, type);

        setSize(SIZE_IN_PIXELS, SIZE_IN_PIXELS);

        state = new StandingState(this);
    }

    protected void createAnimations(TextureAtlas atlas) {
        Array<TextureRegion> frames = new Array<>();
        frames.add(new TextureRegion(atlas.findRegion("frame_" + 0)));
        standingAnimation = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        runningAnimation = new Animation(0.15f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("frame_" + 4)));
        jumpingAnimation = new Animation(0.3f, frames);
        frames.clear();
    }

    private void definePersonBody(Vector2 position, ObjectType type) {
        HALF_X_SIZE = SIZE_IN_METERS / 2 - X_PAD / SIZE_IN_PIXELS;
        HALF_Y_SIZE = SIZE_IN_METERS / 2 - Y_PAD / SIZE_IN_PIXELS;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        body = world.createBody(def);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(HALF_X_SIZE, HALF_Y_SIZE);
        fixture = body.createFixture(polygonShape, 9);
        polygonShape.dispose();
        fixture.setFriction(0);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(1.6f);
        NearSensorFixture = body.createFixture(circleShape, 0);
        circleShape.setRadius(2.6f);
        FarSensorFixture = body.createFixture(circleShape, 0);
        circleShape.dispose();

        fixture.setUserData(new SensorUserData(SensorUserData.SensorType.Default, type));
        NearSensorFixture.setSensor(true);
        FarSensorFixture.setSensor(true);
        NearSensorFixture.setUserData(new SensorUserData(SensorUserData.SensorType.NearSensor, type));
        FarSensorFixture.setUserData(new SensorUserData(SensorUserData.SensorType.FarSensor, type));

        body.setUserData(new UserData(type, ObjectStatus.DEFAULT, SeeEnemy.DONT_SEE_ENEMY, true));
    }


    public abstract void createGun(Stage stage, TextureRegion bulletTexture);

    public void move(int direction) {
        velocity = maxVelocity * direction;
    }


    public void resetVelocity() {
        velocity = 0;
        body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    public void jump() {
        if (isGrounded) {
            setGrounded(false);
            body.applyLinearImpulse(0, 20, body.getPosition().x, body.getPosition().y, true);
        }
    }

    private void climbToBlock() {
        if (isGrounded) {
            setGrounded(false);
            float impulseX = 0.03f;
            if (!isRunningRight)
                impulseX *= -1;
            body.applyLinearImpulse(impulseX, 10.5f, body.getPosition().x, body.getPosition().y, true);
            ((UserData) body.getUserData()).setStatus(ObjectStatus.DEFAULT);
        }
        setGrounded(true);
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

        if (gun != null)
            gun.update(delta);

        super.act(delta);
    }

    private void userDataChecker(UserData data) {
        if (data == null)
            return;
        isGrounded = (data.isGrounded() && body.getLinearVelocity().y < 1 && body.getLinearVelocity().y > -1);
        switch (data.getStatus()) {
            case DAMAGED:
                hp -= 5;
                data.setStatus(ObjectStatus.DEFAULT);
                if (hp <= 0)
                    remove();
                break;
            case CLIMB_TO_BLOCK:
                climbToBlock();
                break;
            case HEALED:
                if (hp + 25 <= maxHp)
                    hp += 25;
                else
                    hp = maxHp;
                data.setStatus(ObjectStatus.DEFAULT);
                break;
        }
        if (state.getType().equals(State.states.JumpingState))
            data.setStatus(ObjectStatus.DEFAULT);
    }

    public int getRealX() {
        return (int) (body.getPosition().x * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        textureRegion = (TextureRegion) currentAnimation.getKeyFrame(state.getTimer(), true);

        if (!isRunningRight && !textureRegion.isFlipX() || (isRunningRight && textureRegion.isFlipX()))
            textureRegion.flip(true, false);

        float PAD = HALF_X_SIZE;
        if (!isRunningRight) {
            PAD *= -1;
            PAD += getWidth() / PIXELS_IN_METER;
        }

        setPosition((body.getPosition().x - PAD) * PIXELS_IN_METER,
                (body.getPosition().y - HALF_Y_SIZE) * PIXELS_IN_METER - 2);

        batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean remove() {
        alive = false;
        world.destroyBody(body);
        gun.dispose();
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

    public int getSizeInPixels() {
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
        ((UserData) body.getUserData()).setGrounded(grounded);
    }
}
