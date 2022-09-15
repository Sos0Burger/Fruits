package com.fruits.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenu implements Screen {
    final Fruit game;

    OrthographicCamera camera;

    public MainMenu(final Fruit game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }
    public void show() {

    }
    public void render(float delta) {

    }
    public void resize(int width, int height) {
    }
    public void hide() {
    }
    public void pause() {
    }
    public void resume() {
    }
    public void dispose() {
    }
}
