package com.neuron.game.gameLogic.objects.guns;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.objects.persons.Person;

public abstract class Gun extends Actor {
    Person person;
    TextureRegion texture;
    Sound fireSound;
    World world;
    TextureRegion bulletTexture;
    boolean firing = false ;

    float fireRate;
    int direction;
    float timeSinceLastShoot;


    public Vector2 bulletStartPoint;

    public Gun(World world, Person person, TextureRegion texture, TextureRegion bulletTexture) {
        this.person = person;
        this.texture = texture;
        this.world = world;
        this.bulletTexture = bulletTexture;
        setSize(texture.getRegionWidth(), texture.getRegionHeight());
    }

    public void fire() {
        if (timeSinceLastShoot >= fireRate) {
            new Bullet(world, bulletStartPoint, direction, getStage(), bulletTexture);
            timeSinceLastShoot = 0;
        }
    }

    @Override
    public void act(float delta) {
        timeSinceLastShoot += delta;

        if (direction == -1)
            bulletStartPoint = new Vector2(getX() / Configuration.PIXELS_IN_METER, (getY() + getHeight() / 2) / Configuration.PIXELS_IN_METER);
        else
            bulletStartPoint = new Vector2((getX() + getWidth()) / Configuration.PIXELS_IN_METER, (getY() + getHeight() / 2) / Configuration.PIXELS_IN_METER);

        if (firing)
            fire();

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (!person.isRunningRight() && !texture.isFlipX())
            texture.flip(true, false);
        else if (person.isRunningRight() && texture.isFlipX())
            texture.flip(true, false);

        if (person.isRunningRight()) {
            direction = 1;
            setPosition(person.getX() + person.getWidth() / 4, person.getY() + (int) (person.getHeight() / 5.5));
            batch.draw(texture, person.getX() + person.getWidth() / 4, person.getY() + (int) (person.getHeight() / 5.5), getWidth(), getHeight());
        } else {
            direction = -1;
            setPosition(person.getX() + person.getWidth() / 8, person.getY() + (int) (person.getHeight() / 5.5));
            batch.draw(texture, person.getX() + person.getWidth() / 8, person.getY() + (int) (person.getHeight() / 5.5), getWidth(), getHeight());
        }
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }
}
