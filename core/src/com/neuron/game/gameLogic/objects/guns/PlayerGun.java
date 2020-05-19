package com.neuron.game.gameLogic.objects.guns;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.neuron.game.gameLogic.objects.persons.Person;

public class PlayerGun extends Gun{
    public PlayerGun(World world, Person person, Stage stage, TextureRegion bulletTexture) {
        super(world, person, stage, bulletTexture);

        super.fireRate = 0.12f;
    }
}
