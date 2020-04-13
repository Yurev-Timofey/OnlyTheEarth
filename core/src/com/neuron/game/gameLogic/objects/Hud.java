package com.neuron.game.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.neuron.game.Configuration;
import com.neuron.game.gameLogic.controllers.Controller;

public class Hud {
    private final FillViewport viewport;
    private final Controller controller;
    private Table buttonsTable;
    Skin skin;


    public Hud(TextureAtlas atlas, FillViewport viewport, final Controller controller) {
        this.viewport = viewport;
        this.controller = controller;


        skin = new Skin(atlas);
        final int PAD = 9;

        buttonsTable = new Table();
        buttonsTable.setFillParent(true);

        buttonsTable.add(createButton("Button_left")).space(viewport.getWorldWidth() / 100);
        buttonsTable.add(createButton("Button_right")).spaceRight(Configuration.viewportWidth - (buttonsTable.getMinWidth() + PAD) * 2);
        buttonsTable.add(createButton("Button_up")).space(viewport.getWorldWidth() / 100);
        buttonsTable.add(createButton("Button_fire"));

        buttonsTable.bottom().left();
        buttonsTable.padBottom(Configuration.viewportBottom + PAD);
        buttonsTable.padLeft(Configuration.viewportLeft + PAD);
    }

    private Actor createButton(final String buttonName) {
        Sprite sprite = skin.getSprite(buttonName);
        sprite.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);
        ImageButton button = new ImageButton(new SpriteDrawable(sprite));
        button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.handleInput(event.getType(), buttonName);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.handleInput(event.getType(), buttonName);
            }
        });
        return button;
    }

    public Table getButtonsTable() {
        return buttonsTable;
    }
}