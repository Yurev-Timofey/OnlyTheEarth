package com.vsu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.vsu.game.objects.GroundBlock;

public class XmlParser {
    public void createGroundBlocks(World world, AssetManager assetManager, Stage stage) {
        XmlReader.Element root = new XmlReader().parse(Gdx.files.internal("xml/map.xml"));
        Array<XmlReader.Element> xml_groundBlocks = root.getChildrenByName("groundBlock");

        Array<XmlReader.Element> positionOfGroundBlocks = root.getChildrenByName("groundBlock");
        for (XmlReader.Element element : positionOfGroundBlocks) {
            GroundBlock groundBlock = new GroundBlock(
                    world,
                    assetManager.get("images/groundBlock.png", Texture.class),
                    new Vector2(Float.parseFloat(element.getAttribute("position_x")), Float.parseFloat(element.getAttribute("position_y")))
            );
            stage.addActor(groundBlock);
        }
    }
}
