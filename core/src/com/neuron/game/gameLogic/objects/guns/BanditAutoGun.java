package com.neuron.game.gameLogic.objects.guns;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.neuron.game.gameLogic.objects.persons.Person;

public class BanditAutoGun extends Gun {
    public BanditAutoGun(World world, Person person, Stage stage, TextureRegion bulletTexture) {
        super(world, person, stage, bulletTexture);
        super.fireRate = 0.15f;
    }
}
