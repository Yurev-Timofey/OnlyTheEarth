package com.vsu.game.gameLogic.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vsu.game.gameLogic.states.StandingState;
import com.vsu.game.gameLogic.states.State;

import static com.vsu.game.Configuration.PIXELS_IN_METER;

public class Player extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;

    private boolean isAlive = true;
    private boolean isGrounded = true;
    private float velocity;

    private static final float SIZE_IN_METERS = 1f;
    private static final int SIZE_IN_PIXELS = (int) (PIXELS_IN_METER * SIZE_IN_METERS);

    private State state;


    public Player(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(SIZE_IN_METERS / 6, SIZE_IN_METERS / 3);
        fixture = body.createFixture(box, 20);
        fixture.setFriction(0);
        box.dispose();

        setSize(SIZE_IN_PIXELS, SIZE_IN_PIXELS);
        state = new StandingState(this);
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

    public void update() {
        if ((body.getLinearVelocity().x < velocity && velocity > 0) || (body.getLinearVelocity().x > velocity && velocity < 0))
            body.applyLinearImpulse(velocity, 0, body.getPosition().x, body.getPosition().y, true);

        state.update();

        setPosition((body.getPosition().x - SIZE_IN_METERS / 2) * PIXELS_IN_METER,
                (body.getPosition().y - SIZE_IN_METERS / 3) * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
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
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
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
}
