package com.neuron.game.gameLogic.objects.persons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.neuron.game.gameLogic.contacts.userData.ObjectType;

import static com.neuron.game.Configuration.PIXELS_IN_METER;

public class Player extends Person {

    public Player(World world, TextureAtlas atlas, Vector2 position) {
        super(world, 100, 3, atlas, position, 1f, ObjectType.PLAYER, 17, 5);
    }

    @Override
    protected void createAnimations(TextureAtlas atlas) {
        Array<TextureRegion> frames = new Array<>();
        frames.add(new TextureRegion(atlas.findRegion("frame_" + 0)));
        standingAnimation = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(atlas.findRegion("frame_" + i)));
        runningAnimation = new Animation(0.15f, frames);
        frames.clear();

            frames.add(new TextureRegion(atlas.findRegion("frame_" + 4)));
        jumpingAnimation = new Animation(0.3f, frames);
        frames.clear();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setZIndex(getStage().getActors().size - 2);
    }
}
