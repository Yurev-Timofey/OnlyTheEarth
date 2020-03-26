package com.vsu.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import static com.vsu.game.Constants.PIXELS_IN_METER;

public class GroundBlock extends Actor {
    public static final float GROUND_BLOCK_SIZE_IN_METERS = 0.4f;

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;

    public GroundBlock(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.StaticBody;

        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(GROUND_BLOCK_SIZE_IN_METERS / 2, GROUND_BLOCK_SIZE_IN_METERS / 2);
        fixture = body.createFixture(box, 3);
        box.dispose();

        setSize(PIXELS_IN_METER * GROUND_BLOCK_SIZE_IN_METERS, PIXELS_IN_METER * GROUND_BLOCK_SIZE_IN_METERS);
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - GROUND_BLOCK_SIZE_IN_METERS / 2) * PIXELS_IN_METER, (body.getPosition().y - GROUND_BLOCK_SIZE_IN_METERS / 2) * PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
