package com.neuron.game.gameLogic.objects.persons.enemy;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.neuron.game.gameLogic.objects.guns.BanditAutoGun;
import com.neuron.game.gameLogic.objects.guns.PlayerGun;

public class Bandit extends Enemy {
    public Bandit(World world, TextureAtlas atlas, Vector2 position, TextureAtlas texture) {
        super(world, atlas, position, 1f, 25, texture);
    }

    @Override
    public void createGun(Stage stage, TextureRegion bulletTexture) {
        gun = new BanditAutoGun(world, this, stage, bulletTexture);
    }
}
