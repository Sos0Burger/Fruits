package com.fruits.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

public class GameScreen implements Screen {
    final Fruits game;
    Texture appleImg;
    Texture bananaImg;
    Texture fruitCatcherImg;
    Texture grapesImg;
    Texture orangeImg;
    Texture pearImg;
    Texture[] fruitDropArray;
    Texture gameBackgroundImg;

    Sound catchSound;
    Music catchMusic;

    OrthographicCamera camera;
    SpriteBatch batch;

    Rectangle fruitCatcher;
    Array<FruitDrop> fruitDrops;

    long lastFruitTime;
    int combo = 0;

    Vector3 touchPos = new Vector3();

    public GameScreen(final Fruits game) {
        this.game = game;
        appleImg = new Texture(Gdx.files.internal("apple.png"));
        bananaImg = new Texture(Gdx.files.internal("banana.png"));
        fruitCatcherImg = new Texture("fruitCatcher.png");
        grapesImg = new Texture(Gdx.files.internal("grapes.png"));
        orangeImg = new Texture(Gdx.files.internal("orange.png"));
        pearImg = new Texture(Gdx.files.internal("pear.png"));
        gameBackgroundImg = new Texture(Gdx.files.internal("gameBackground.jpg"));

        fruitDropArray = new Texture[] {appleImg, bananaImg,grapesImg, orangeImg, pearImg};

        catchSound = Gdx.audio.newSound(Gdx.files.internal("catchSound.ogg"));
        catchMusic = Gdx.audio.newMusic(Gdx.files.internal("Sweden.mp3"));

        batch = new SpriteBatch();

        fruitCatcher = new Rectangle();
        fruitCatcher.x = (float)Gdx.graphics.getWidth() / 2 - (float) fruitCatcherImg.getWidth() / 2;
        fruitCatcher.y = 215;
        fruitCatcher.width = fruitCatcherImg.getWidth();
        fruitCatcher.height = 10;

        camera =new OrthographicCamera();
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        catchMusic.setLooping(true);
        catchMusic.play();

        fruitDrops = new Array<>();
        spawnFruitdrop();
    }
    private void spawnFruitdrop() {
        Circle fruit = new Circle();
        int type;
        fruit.x = MathUtils.random(0, Gdx.graphics.getWidth()-fruitCatcherImg.getWidth() );
        fruit.y = Gdx.graphics.getHeight();
        fruit.radius = 60;
        type = MathUtils.random(0,4);
        fruitDrops.add(new FruitDrop(fruit, type));
        lastFruitTime = TimeUtils.nanoTime();
    }




    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(gameBackgroundImg,0,0);
        batch.draw(fruitCatcherImg, fruitCatcher.x, fruitCatcher.y-215);
        game.font.draw(batch, String.valueOf(combo), (float)Gdx.graphics.getWidth()/2,(float)Gdx.graphics.getHeight()/2 );
        for(FruitDrop fruitdrop: fruitDrops) {
            batch.draw(fruitDropArray[fruitdrop.type], fruitdrop.circle.x-fruitdrop.circle.radius, fruitdrop.circle.y-fruitdrop.circle.radius);
        }
        batch.end();

        if(Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            fruitCatcher.x = touchPos.x - (float)fruitCatcherImg.getWidth()  / 2;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) fruitCatcher.x -= 1000 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.D)) fruitCatcher.x += 1000 * Gdx.graphics.getDeltaTime();

        if(fruitCatcher.x < 0) fruitCatcher.x = 0;
        if(fruitCatcher.x > Gdx.graphics.getWidth() - fruitCatcherImg.getWidth() ) fruitCatcher.x = Gdx.graphics.getWidth() - fruitCatcherImg.getWidth() ;

        if(TimeUtils.nanoTime() - lastFruitTime > 1000000000) spawnFruitdrop();

        Iterator<FruitDrop> iter = fruitDrops.iterator();
        while(iter.hasNext()) {
            FruitDrop fruitdrop = iter.next();
            fruitdrop.circle.y -= 200 * Gdx.graphics.getDeltaTime();
            if(fruitdrop.circle.y + fruitdrop.circle.radius*2 < 0){ iter.remove();combo=0;}
            if(Intersector.overlaps(fruitdrop.circle, fruitCatcher)) {
                catchSound.play();
                combo++;
                iter.remove();
            }
        }
    }
    public void show() {
        catchMusic.play();
    }
    class FruitDrop {
        Circle circle;
        int type;

        public FruitDrop(Circle circle, int type) {
            this.circle = circle;
            this.type = type;
        }
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
        appleImg.dispose();
        bananaImg.dispose();
        fruitCatcherImg.dispose();
        grapesImg.dispose();
        orangeImg.dispose();
        pearImg.dispose();

        catchSound.dispose();
        catchMusic.dispose();

        batch.dispose();
    }
}
