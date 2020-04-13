package com.neuron.game.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Bullet extends Actor {
    Vector2 position;
    int direction;
    TextureRegion texture;

    Body body;
    Fixture fixture;

    public Bullet(World world, Vector2 position, int direction, Stage stage) {
        this.position = position;
        this.direction = direction;

        createBodyDef(world);

        body.applyLinearImpulse(0.5f * direction, 0, body.getPosition().x, body.getPosition().y, true);

        stage.addActor(this);
    }

    private void createBodyDef(World world) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);
        body.setUserData("bullet");
        body.setBullet(true);
        body.setGravityScale(0.01f);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.07f, 0.03f);
        fixture = body.createFixture(shape, 10);
        fixture.setFriction(0);
        shape.dispose();

        body.setUserData("bullet");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (body.getUserData().equals("dispose"))
            remove();
    }

    @Override
    public boolean remove() {
        body.destroyFixture(fixture);
        body.getWorld().destroyBody(body);
        return super.remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.draw(texture, body.getPosition().x * Configuration.PIXELS_IN_METER, body.getPosition().y * Configuration.PIXELS_IN_METER, getWidth(), getHeight());
    }
}
