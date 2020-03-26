package com.vsu.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.vsu.game.Constants.PIXELS_IN_METER;

public class Player extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;

    private boolean isAlive = true;
    private boolean isGrounded = true;
    private float velocity;

    private static final float SIZE_IN_METERS = 1f;

    public Player(World world, Texture texture, Vector2 position){
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
        box.dispose();

        setSize(PIXELS_IN_METER * SIZE_IN_METERS, PIXELS_IN_METER * SIZE_IN_METERS);
    }

    public Body getBody() {
        return body;
    }

    public void jump(){
        if(isGrounded){
            isGrounded = false;
            body.applyLinearImpulse(0, 20, body.getPosition().x, body.getPosition().y, true);
        }
    }
    public void move(int direction){
       velocity = 5 * direction;
    }

    public void resetVelocity(){
        velocity = 0;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        body.setLinearVelocity(velocity, body.getLinearVelocity().y);

        setPosition((body.getPosition().x - SIZE_IN_METERS / 2)  * PIXELS_IN_METER,
                (body.getPosition().y - SIZE_IN_METERS / 3)  * PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
