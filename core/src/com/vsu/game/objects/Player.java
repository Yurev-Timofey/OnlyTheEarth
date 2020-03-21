package com.vsu.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player {
    static final float MAX_VELOCITY = 3.0F;
    public static final float SPEED = 5.0F;
    public static final float SIZE = 0.8F;
    public Fixture playerPhysicsFixture;
    public Fixture playerSensorFixture;
    Body box;
    Vector2 velocity = new Vector2();
    boolean isJump = false;

    public Player(Body b) {
        this.box = b;
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(0.4F, 0.4F);
        this.playerPhysicsFixture = this.box.createFixture(poly, 0.0F);
        poly.dispose();
        CircleShape circle = new CircleShape();
        circle.setRadius(0.4F);
        circle.setPosition(new Vector2(0.0F, -0.05F));
        this.playerSensorFixture = this.box.createFixture(circle, 0.0F);
        circle.dispose();
        this.box.setBullet(true);
    }

    public float getFriction() {
        return this.playerSensorFixture.getFriction();
    }

    public Body getBody() {
        return this.box;
    }

    public void setFriction(float f) {
        this.playerSensorFixture.setFriction(f);
        this.playerPhysicsFixture.setFriction(f);
    }

    public Vector2 getPosition() {
        return this.box.getPosition();
    }

    public Vector2 getVelocity() {
        return this.velocity;
    }

    public void update(float delta) {
        Vector2 vel = this.box.getLinearVelocity();
        this.velocity.y = vel.y;
        this.box.setLinearVelocity(this.velocity);
        if (this.isJump) {
            this.box.applyLinearImpulse(0.0F, 14.0F, this.box.getPosition().x, this.box.getPosition().y, true);
            this.isJump = false;
        }

    }

    public void jump() {
        this.isJump = true;
    }

    public void resetVelocity() {
        this.getVelocity().x = 0.0F;
        this.getVelocity().y = 0.0F;
    }
}