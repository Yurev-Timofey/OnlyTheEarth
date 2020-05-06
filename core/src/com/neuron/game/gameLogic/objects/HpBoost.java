package com.neuron.game.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.neuron.game.Configuration;
import com.neuron.game.MyActor;
import com.neuron.game.gameLogic.objects.userData.ObjectStatus;
import com.neuron.game.gameLogic.objects.userData.ObjectType;
import com.neuron.game.gameLogic.objects.userData.SeeEnemy;
import com.neuron.game.gameLogic.objects.userData.UserData;

import static com.neuron.game.Configuration.PIXELS_IN_METER;

public class HpBoost extends MyActor {
    private Body body;
    private Fixture fixture;

    public HpBoost(TextureRegion texture, World world, Vector2 position, Stage stage) {
        super(texture, 2);
        System.out.println("CREATED");

        BodyDef def = new BodyDef();
        def.position.set(position.x, position.y);
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        body = world.createBody(def);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.1f);
        fixture = body.createFixture(circleShape, 50);

        body.setUserData(new UserData(ObjectType.HPBOOST, ObjectStatus.DEFAULT, SeeEnemy.DONT_SEE_ENEMY, true));

        stage.addActor(this);
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
        setPosition((body.getPosition().x - fixture.getShape().getRadius()) * PIXELS_IN_METER,
                (body.getPosition().y - fixture.getShape().getRadius()) * PIXELS_IN_METER);
        super.draw(batch, parentAlpha);
    }
}
