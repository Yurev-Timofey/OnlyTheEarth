package com.neuron.game.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.neuron.game.Configuration;
import com.neuron.game.HpBarActor;
import com.neuron.game.MyActor;
import com.neuron.game.gameLogic.objects.persons.Player;

public class Hud {
    private final FillViewport viewport;
    private Table buttonsTable;
    private Player player;
    private Skin skin;
    HpBarActor hpBar;
    MyActor hpFrame;
    private final int PAD = 9;
    private float position = 0;
    private boolean gameOverLabelCreated = false;

    public enum buttons {
        LEFT("Button_left"),
        RIGHT("Button_right"),
        UP("Button_up"),
        FIRE("Button_fire");

        private String name;

        buttons(String name) {
            this.name = name;
        }


    }

    public Hud(TextureAtlas atlas, FillViewport viewport, Player player) {
        this.viewport = viewport;
        this.player = player;

        skin = new Skin(atlas);

        hpFrame = new MyActor(skin.getRegion("hpFrame"), 4);
        hpBar = new HpBarActor(skin.getRegion("hpBar"), player, 4);

        hpFrame.setPosition(Configuration.viewportLeft + PAD * 2, Configuration.viewportHeight - PAD * 2 - hpFrame.getHeight());
        hpBar.setPosition(Configuration.viewportLeft + PAD * 2 + (hpFrame.getWidth() - hpBar.getWidth()) - 8, Configuration.viewportHeight - PAD * 2 - hpBar.getHeight());

        buttonsTable = new Table();
        buttonsTable.setFillParent(true);

        buttonsTable.add(createButton(buttons.LEFT)).space(PAD);
        buttonsTable.add(createButton(buttons.RIGHT)).spaceRight(Configuration.viewportWidth - (buttonsTable.getMinWidth() + PAD) * 2);
        buttonsTable.add(createButton(buttons.UP)).space(PAD);
        buttonsTable.add(createButton(buttons.FIRE));

        buttonsTable.bottom().left();
        buttonsTable.padBottom(Configuration.viewportBottom + PAD);
        buttonsTable.padLeft(Configuration.viewportLeft + PAD);
    }

    private Actor createButton(final buttons buttonName) {
        Sprite spriteUp = skin.getSprite(buttonName.name);
        spriteUp.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);
        spriteUp.setAlpha(0.88f);

        Sprite spriteDown = skin.getSprite(buttonName.name + "_pressed");
        spriteDown.setSize(viewport.getWorldWidth() / 10, viewport.getWorldHeight() / 5);
        spriteDown.setAlpha(0.88f);

        ImageButton button = new ImageButton(new SpriteDrawable(spriteUp), new SpriteDrawable(spriteDown));
        button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.getState().handleInput(event.getType(), buttonName);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                player.getState().handleInput(event.getType(), buttonName);
            }
        });
        return button;
    }

    public void gameOver(Stage stage, BitmapFont font) {
        buttonsTable.remove();
        hpFrame.remove();
        hpBar.remove();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        Label gameOver = new Label("GAME OVER", style);
        gameOver.setPosition(Configuration.viewportLeft + position + (Configuration.viewportWidth - gameOver.getMinWidth()) / 2,
                (Configuration.viewportHeight - gameOver.getMinHeight()) / 2);
        stage.addActor(gameOver);
        gameOverLabelCreated = true;
    }

    public boolean isGameOverLabelCreated() {
        return gameOverLabelCreated;
    }

    public void addToStage(Stage stage) {
        stage.addActor(buttonsTable);
        stage.addActor(hpFrame);
        stage.addActor(hpBar);
    }

    public void setPosition(float x, float y) {
        position = x;
        buttonsTable.setPosition(position, buttonsTable.getY());
        hpBar.setPosition(position + Configuration.viewportLeft + PAD * 2 + (hpFrame.getWidth() - hpBar.getWidth()) - 8, hpBar.getY());
        hpFrame.setPosition(position + Configuration.viewportLeft + PAD * 2, hpFrame.getY());
    }
}
