package com.neuron.game.gameLogic.objects.guns;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.objects.Bullet;
import com.neuron.game.gameLogic.objects.Player;

public abstract class Gun extends Actor {
    Player player;
    TextureRegion texture;
    Sound fireSound;
    World world;
    TextureRegion bulletTexture;

    int ammo;
    int damage;
    float fireRate;
    int direction;
    float timeSinceLastShoot;


    public Vector2 bulletStartPoint;

    public Gun(World world, Player player, TextureRegion texture) {
        this.player = player;
        this.texture = texture;
        this.world = world;
        setSize(texture.getRegionWidth(), texture.getRegionHeight());
    }

    public void fire() {
        if (timeSinceLastShoot >= fireRate) {
            new Bullet(world, bulletStartPoint, direction, getStage());
            timeSinceLastShoot = 0;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        timeSinceLastShoot += delta;
        if (direction == -1)
            bulletStartPoint = new Vector2(getX() / Configuration.PIXELS_IN_METER, (getY() + getHeight() / 2) / Configuration.PIXELS_IN_METER);
        else
            bulletStartPoint = new Vector2((getX() + getWidth()) / Configuration.PIXELS_IN_METER, (getY() + getHeight() / 2) / Configuration.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (!player.isRunningRight() && !texture.isFlipX())
            texture.flip(true, false);
        else if (player.isRunningRight() && texture.isFlipX())
            texture.flip(true, false);

        if (player.isRunningRight()) {
            direction = 1;
            setPosition(player.getX() + player.getWidth() / 4, player.getY() + (int) (player.getHeight() / 5.5));
            batch.draw(texture, player.getX() + player.getWidth() / 4, player.getY() + (int) (player.getHeight() / 5.5), getWidth(), getHeight());
        } else {
            direction = -1;
            setPosition(player.getX() + player.getWidth() / 8, player.getY() + (int) (player.getHeight() / 5.5));
            batch.draw(texture, player.getX() + player.getWidth() / 8, player.getY() + (int) (player.getHeight() / 5.5), getWidth(), getHeight());
        }
    }

}
