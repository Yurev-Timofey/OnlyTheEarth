package trashBin;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class SimpleAnimatedActor extends Actor {

    private Animation animation;
    private TextureRegion currentFrame;
    private float stateTime;
    private boolean started;

    public SimpleAnimatedActor(float frameDuration, Array<TextureRegion> frames) {
        animation = new Animation(frameDuration, frames);
        currentFrame = (TextureRegion) animation.getKeyFrame(stateTime); //TODO Мб не сработает
        setBorders(frames.get(0));
    }

    private void setBorders(TextureRegion sample) {
        setSize(sample.getRegionWidth(), sample.getRegionHeight());
        setBounds(0, 0, getWidth(), getHeight());
    }

    public void start() {
        stateTime = 0;
        started = true;
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(stateTime);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!started) {
            return;
        }

        stateTime += delta;
        currentFrame = (TextureRegion) animation.getKeyFrame(stateTime); //TODO Тоже вряд-ли будет работать
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
