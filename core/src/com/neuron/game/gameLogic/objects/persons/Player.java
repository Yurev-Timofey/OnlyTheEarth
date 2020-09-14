package com.neuron.game.gameLogic.objects.persons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;
import com.neuron.game.gameLogic.objects.guns.PlayerGun;

public class Player extends Person {

    public Player(World world, TextureAtlas atlas, Vector2 position) {
        super(world, 100, 3, atlas, position, 1f, ObjectType.PLAYER, 17, 5);
    }

    @Override
    public void createGun(Stage stage, TextureRegion bulletTexture) {
        gun = new PlayerGun(world, this, stage, bulletTexture);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getStage() != null)
            setZIndex(getStage().getActors().size - 3);
    }
}
