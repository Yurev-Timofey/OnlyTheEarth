package com.neuron.game.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.neuron.game.gameLogic.MyAnimatedActor;
import com.neuron.game.gameLogic.contacts.userData.ObjectStatus;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;
import com.neuron.game.gameLogic.contacts.userData.SeeEnemy;
import com.neuron.game.gameLogic.contacts.userData.SensorUserData;
import com.neuron.game.gameLogic.contacts.userData.UserData;

import static com.neuron.game.Configuration.PIXELS_IN_METER;

public class HpBoost extends MyAnimatedActor {
    private Body body;
    private Fixture fixture;
    private final float RADIUS_IN_METERS = 0.1f;

    public HpBoost(TextureAtlas atlas, World world, Vector2 position, Stage stage) {
        super(11, position, atlas, 2);

        definePersonBody(position, world);

        stage.addActor(this);
    }

    private void definePersonBody(Vector2 position, World world) {
        BodyDef def = new BodyDef();
        def.position.set(position.x, position.y + RADIUS_IN_METERS);
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        body = world.createBody(def);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(RADIUS_IN_METERS);
        fixture = body.createFixture(circleShape, 1);
        fixture.setSensor(true);

        body.setUserData(new UserData(ObjectType.HP_BOOST, ObjectStatus.DEFAULT, SeeEnemy.DONT_SEE_ENEMY, true));
        fixture.setUserData(new SensorUserData(SensorUserData.SensorType.Default, ObjectType.HP_BOOST));
    }

    @Override
    public void act(float delta) {
        if (((UserData) body.getUserData()).getStatus().equals(ObjectStatus.TO_DISPOSE))
            remove();
        super.act(delta);
    }

    @Override
    public boolean remove() {
        body.getWorld().destroyBody(body);
        return super.remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - RADIUS_IN_METERS) * PIXELS_IN_METER,
                (body.getPosition().y - RADIUS_IN_METERS) * PIXELS_IN_METER);
        super.draw(batch, parentAlpha);
    }
}
