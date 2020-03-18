package objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class MovingPlatform {
    Body platform;
    Vector2 pos = new Vector2();
    Vector2 dir = new Vector2();
    float dist = 0.0F;
    float maxDist = 0.0F;
    public float width;
    public float height;

    public MovingPlatform(World world, float x, float y, float width, float height, float dx, float dy, float maxDist) {
        this.platform = this.createBox(world, BodyType.KinematicBody, width, height, 1.0F);
        this.pos.x = x;
        this.pos.y = y;
        this.dir.x = dx;
        this.dir.y = dy;
        this.width = width * 2.0F;
        this.height = height * 2.0F;
        this.maxDist = maxDist;
        this.platform.setTransform(this.pos, 0.0F);
        ((Fixture)this.platform.getFixtureList().get(0)).setUserData("p");
        this.platform.setUserData(this);
    }

    public Body getBody() {
        return this.platform;
    }

    public void update(float deltaTime) {
        this.dist += this.dir.len() * deltaTime;
        if (this.dist > this.maxDist) {
//            this.dir.mul(-1.0F); //TODO
            this.dist = 0.0F;
        }

        this.platform.setLinearVelocity(this.dir);
    }

    private Body createBox(World world, BodyType type, float width, float height, float density) {
        BodyDef def = new BodyDef();
        def.type = type;
        Body box = world.createBody(def);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        box.createFixture(poly, density);
        poly.dispose();
        return box;
    }
}