package com.neuron.game.gameLogic.objects.guns;

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
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.objects.userData.ObjectStatus;
import com.neuron.game.gameLogic.objects.userData.ObjectType;
import com.neuron.game.gameLogic.objects.userData.SeeEnemy;
import com.neuron.game.gameLogic.objects.userData.UserData;

public class Bullet extends Actor {
    Vector2 position;
    int direction;
    TextureRegion texture;

    Body body;
    Fixture fixture;

    public Bullet(World world, Vector2 position, int direction, Stage stage, TextureRegion texture) { //TODO Добавить наследование от MyActor
        this.position = position;
        this.direction = direction;
        this.texture = texture;

        createBody(world);

        body.applyLinearImpulse(0.5f * direction, (float) (Math.random() * 0.06 - 0.03), body.getPosition().x, body.getPosition().y, true);

        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());

        stage.addActor(this);
    }

    private void createBody(World world) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        body.setBullet(true);
        body.setGravityScale(0.03f);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.07f, 0.03f);
        fixture = body.createFixture(shape, 9);
        fixture.setFriction(0);
        shape.dispose();

        body.setUserData(new UserData(ObjectType.BULLET, ObjectStatus.DEFAULT, SeeEnemy.DONT_SEE_ENEMY, false));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (((UserData) body.getUserData()).getStatus().equals(ObjectStatus.TO_DISPOSE) || (body.getLinearVelocity().x < 3 && body.getLinearVelocity().x > -3))
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
        batch.draw(texture, (body.getPosition().x - 0.035f) * Configuration.PIXELS_IN_METER, (body.getPosition().y - 0.015f) * Configuration.PIXELS_IN_METER, getWidth(), getHeight());
    }
}
