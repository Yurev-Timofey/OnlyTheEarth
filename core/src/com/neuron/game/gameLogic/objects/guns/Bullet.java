package com.neuron.game.gameLogic.objects.guns;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.MyActor;
import com.neuron.game.gameLogic.contacts.userData.ObjectStatus;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;
import com.neuron.game.gameLogic.contacts.userData.UserData;

public class Bullet extends MyActor {
    private Vector2 position;
    private int direction;

    private Body body;
    private Fixture fixture;

    Bullet(World world, Vector2 position, int direction, Stage stage, TextureRegion texture) {
        super(texture, 2.7f);
        this.position = position;
        this.direction = direction;

        createBody(world);

        body.applyLinearImpulse(0.6f * direction, (float) (Math.random() * 0.07 - 0.035), body.getPosition().x, body.getPosition().y, true);

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
        fixture = body.createFixture(shape, 8);
        fixture.setFriction(0);
        fixture.setSensor(true);
        shape.dispose();

        body.setUserData(new UserData(ObjectType.BULLET, ObjectStatus.DEFAULT, null, false));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (((UserData) body.getUserData()).getStatus().equals(ObjectStatus.TO_DISPOSE) ||  // Пули удаляются при: столкновении с объектами
                (body.getLinearVelocity().x < 3 && body.getLinearVelocity().x > -3) ||                      //при скорости < 3 м/c
                (body.getPosition().x > position.x + 5 || body.getPosition().x < position.x - 5))           //при пройденном расстоянии < 5 м
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
        setPosition((body.getPosition().x - 0.035f) * Configuration.PIXELS_IN_METER - getWidth() / 2,
                (body.getPosition().y - 0.015f) * Configuration.PIXELS_IN_METER - getHeight() / 2);
        super.draw(batch, parentAlpha);
    }
}
