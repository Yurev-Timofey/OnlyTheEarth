//package screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.Application.ApplicationType;
//import com.badlogic.gdx.graphics.Texture;
////import .controller.WorldController;
//
////import .WorldRenderer;
//
//public class GameScreen implements Screen, InputProcessor {
//    private WorldRenderer renderer;
//    public MyWorld world;
//    Texture texture;
//    public int width;
//    public int height;
//    private WorldController controller;
//
//    public GameScreen() {
//    }
//
//    public void show() {
//        MyWorld.CAMERA_WIDTH = MyWorld.CAMERA_HEIGHT * (float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
//        this.loadTextures();
//        this.world = new MyWorld();
//        this.renderer = new WorldRenderer(this.world, MyWorld.CAMERA_WIDTH, MyWorld.CAMERA_HEIGHT, true);
//        this.controller = new WorldController(this.world);
//        Gdx.input.setInputProcessor(this);
//    }
//
//    private void loadTextures() {
//    }
//
//    public boolean touchMoved(int x, int y) {
//        return false;
//    }
//
//    public boolean mouseMoved(int x, int y) {
//        return false;
//    }
//
//    public boolean keyTyped(char character) {
//        return false;
//    }
//
//    public void resize(int width, int height) {
//        this.width = width;
//        this.height = height;
//    }
//
//    public void hide() {
//        Gdx.input.setInputProcessor((InputProcessor)null);
//    }
//
//    public void pause() {
//    }
//
//    public void resume() {
//    }
//
//    public void dispose() {
//        this.renderer.dispose();
//        this.controller.dispose();
//        Gdx.input.setInputProcessor((InputProcessor)null);
//    }
//
//    public boolean keyDown(int keycode) {
//        return true;
//    }
//
//    public void render(float delta) {
//        Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
//        Gdx.gl.glClear(16384);
//        this.controller.update(delta);
//        this.renderer.render(delta);
//    }
//
//    public boolean keyUp(int keycode) {
//        return true;
//    }
//
//    private void ChangeNavigation(int x, int y) {
//        this.controller.resetWay();
//        if ((float)(this.height - y) > this.world.getPlayer().getPosition().y * this.renderer.ppuY) {
//            this.controller.upPressed();
//        }
//
//        if ((float)x < this.world.getPlayer().getPosition().x * this.renderer.ppuX) {
//            this.controller.leftPressed();
//        }
//
//        if ((float)x > this.world.getPlayer().getPosition().x * this.renderer.ppuX) {
//            this.controller.rightPressed();
//        }
//
//    }
//
//    public boolean touchDown(int x, int y, int pointer, int button) {
//        if (!Gdx.app.getType().equals(ApplicationType.Android)) {
//            return false;
//        } else {
//            this.ChangeNavigation(x, y);
//            return true;
//        }
//    }
//
//    public boolean touchUp(int x, int y, int pointer, int button) {
//        if (!Gdx.app.getType().equals(ApplicationType.Android)) {
//            return false;
//        } else {
//            this.controller.resetWay();
//            return true;
//        }
//    }
//
//    public boolean touchDragged(int x, int y, int pointer) {
//        this.ChangeNavigation(x, y);
//        return false;
//    }
//
//    public boolean scrolled(int amount) {
//        return false;
//    }
//}
//
