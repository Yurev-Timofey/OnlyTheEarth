package com.neuron.game.gameLogic.objects.guns;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.objects.persons.Person;
import com.neuron.game.gameLogic.states.State;

import static com.neuron.game.Configuration.PIXELS_IN_METER;

public abstract class Gun implements Disposable {
    private Person person;
    private Sound fireSound;
    private World world;
    private TextureRegion bulletTexture;
    private boolean firing = false;
    private Stage stage;
    private int direction;
    private float timeSinceLastShoot;
    private Vector2 bulletStartPoint;

    float fireRate;


    Gun(World world, Person person, Stage stage, TextureRegion bulletTexture) {
        this.person = person;
        this.world = world;
        this.bulletTexture = bulletTexture;
        this.stage = stage;
    }

    public void fire() {
        if (timeSinceLastShoot >= fireRate) {
            new Bullet(world, bulletStartPoint, direction, stage, bulletTexture);
            timeSinceLastShoot = 0;
        }
    }

    public void update(float delta) {
        if (person == null)
            return;

        timeSinceLastShoot += delta;
        if (person.isRunningRight())
            direction = 1;
        else
            direction = -1;

        if (direction == 1)
            bulletStartPoint = new Vector2((person.getX() + person.getWidth()) / PIXELS_IN_METER, (person.getY() + person.getHeight() / 2 - 5) / PIXELS_IN_METER);
        else
            bulletStartPoint = new Vector2(person.getX() / PIXELS_IN_METER, (person.getY() + person.getHeight() / 2 - 5) / PIXELS_IN_METER);

        if (firing)
            fire();
    }

    @Override
    public void dispose() {
        person = null;
        fireSound = null;
        world = null;
        bulletTexture = null;
    }

    //    @Override
//    public void draw(Batch batch, float parentAlpha) {
//
//        if (!person.isRunningRight() && !texture.isFlipX())
//            texture.flip(true, false);
//        else if (person.isRunningRight() && texture.isFlipX())
//            texture.flip(true, false);
//
//        if (person.isRunningRight()) {
//            direction = 1;
//            setPosition(person.getX() + person.getWidth() / 4, person.getY() + (int) (person.getHeight() / 5.5));
//            batch.draw(texture, person.getX() + person.getWidth() / 4, person.getY() + (int) (person.getHeight() / 5.5), getWidth(), getHeight());
//        } else {
//            direction = -1;
//            setPosition(person.getX() + person.getWidth() / 8, person.getY() + (int) (person.getHeight() / 5.5));
//            batch.draw(texture, person.getX() + person.getWidth() / 8, person.getY() + (int) (person.getHeight() / 5.5), getWidth(), getHeight());
//        }
//    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }
}
