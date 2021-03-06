package com.kilobolt.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kilobolt.Helpers.AssetLoader;
import com.kilobolt.gameobjects.Bird;
import com.kilobolt.gameobjects.Grass;
import com.kilobolt.gameobjects.Pipe;
import com.kilobolt.gameobjects.ScrollHandler;

import sun.security.provider.SHA;

/**
 * Created by Shayan on 10/06/2015.
 * TASKED TO RENDER THE UPDATED GAME OBJECTS
 */
public class GameRenderer {

    private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;

    // Game Objects
    private Bird bird;
    private ScrollHandler scroller;
    private Grass frontGrass, backGrass;
    private Pipe pipe1, pipe2, pipe3;


    // Game Assets
    private TextureRegion bg, grass;
    private Animation birdAnimation;
    private TextureRegion birdMid, birdDown, birdUp;
    private TextureRegion skullUp, skullDown, bar;

    private int midPointY;
    private int gameHeight;

    public GameRenderer(GameWorld world,int gameHeight, int midPointY) {
        myWorld = world;
        this.midPointY = midPointY;
        this.gameHeight = gameHeight;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, gameHeight);

        // Attach batcher to camera, this will draw the assets
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        // Call helper methods to initialize instance variables, To stop re-creation for performance.
        initGameObjects();
        initAssets();
    }

    private void initGameObjects() {
        bird = myWorld.getBird();
        scroller = myWorld.getScroller();
        frontGrass = scroller.getFrontGrass();
        backGrass = scroller.getBackGrass();
        pipe1 = scroller.getPipe1();
        pipe2 = scroller.getPipe2();
        pipe3 = scroller.getPipe3();
    }

    private void initAssets() {
        bg = AssetLoader.bg;
        grass = AssetLoader.grass;
        birdAnimation = AssetLoader.birdAnimation;
        birdMid = AssetLoader.bird;
        birdDown = AssetLoader.birdDown;
        birdUp = AssetLoader.birdUp;
        skullUp = AssetLoader.skullUp;
        skullDown = AssetLoader.skullDown;
        bar = AssetLoader.bar;
    }

    private void drawGrass() {
        // Draw the grass
        batcher.draw(grass, frontGrass.getX(), frontGrass.getY(),
                frontGrass.getWidth(), frontGrass.getHeight());
        batcher.draw(grass, backGrass.getX(), backGrass.getY(),
                backGrass.getWidth(), backGrass.getHeight());
    }

    private void drawSkulls() {
        batcher.draw(skullUp, pipe1.getX() - 1,
                pipe1.getY() + pipe1.getHeight() - 14, 24, 14);
        batcher.draw(skullDown, pipe1.getX() - 1,
                pipe1.getY() + pipe1.getHeight() + Pipe.VERTICAL_GAP, 24, 14);

        batcher.draw(skullUp, pipe2.getX() - 1,
                pipe2.getY() + pipe2.getHeight() - 14, 24, 14);
        batcher.draw(skullDown, pipe2.getX() - 1,
                pipe2.getY() + pipe2.getHeight() + Pipe.VERTICAL_GAP, 24, 14);

        batcher.draw(skullUp, pipe3.getX() - 1,
                pipe3.getY() + pipe3.getHeight() - 14, 24, 14);
        batcher.draw(skullDown, pipe3.getX() - 1,
                pipe3.getY() + pipe3.getHeight() + Pipe.VERTICAL_GAP, 24, 14);
    }

    private void drawPipes() {
        batcher.draw(bar, pipe1.getX(), pipe1.getY(), pipe1.getWidth(),
                pipe1.getHeight());
        batcher.draw(bar, pipe1.getX(), pipe1.getY() + pipe1.getHeight() + Pipe.VERTICAL_GAP,
                pipe1.getWidth(), midPointY + 66 - (pipe1.getHeight() + Pipe.VERTICAL_GAP));

        batcher.draw(bar, pipe2.getX(), pipe2.getY(), pipe2.getWidth(),
                pipe2.getHeight());
        batcher.draw(bar, pipe2.getX(), pipe2.getY() + pipe2.getHeight() + Pipe.VERTICAL_GAP,
                pipe2.getWidth(), midPointY + 66 - (pipe2.getHeight() + Pipe.VERTICAL_GAP));

        batcher.draw(bar, pipe3.getX(), pipe3.getY(), pipe3.getWidth(),
                pipe3.getHeight());
        batcher.draw(bar, pipe3.getX(), pipe3.getY() + pipe3.getHeight() + Pipe.VERTICAL_GAP,
                pipe3.getWidth(), midPointY + 66 - (pipe3.getHeight() + Pipe.VERTICAL_GAP));
    }

    public void render(float runTime) {
        // Fill the entire screen with black, to prevent potential flickering.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin ShapeRenderer ==============================
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Draw Background color
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, 136, midPointY + 66);
        // Draw Grass
        shapeRenderer.setColor(111 / 255.0f, 186 / 255.0f, 45 / 255.0f, 1);
        shapeRenderer.rect(0, midPointY + 66, 136, 11);
        // Draw Dirt

        shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1);
        shapeRenderer.rect(0, midPointY + 77, 136, 52);
        shapeRenderer.end();
        // End ShapeRenderer =================================
        // Begin SpriteBatch =================================
        batcher.begin();
        // Disable transparency
        // This is good for performance when drawing images that do not require
        // transparency.
        batcher.disableBlending();
        batcher.draw(this.bg, 0, midPointY + 23, 136, 43);
        drawGrass();
        drawPipes();
        // The bird needs transparency, so we enable that again.
        batcher.enableBlending();
        drawSkulls();
        // Draw bird at its coordinates. Retrieve the Animation object from
        // AssetLoader
        // Pass in the runTime variable to get the current frame.
        if (bird.shouldntFlap()) {
            batcher.draw(birdMid, bird.getX(), bird.getY(),
                    bird.getWidth() / 2.0f, bird.getHeight() / 2.0f,
                    bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());

        } else {
            batcher.draw(birdAnimation.getKeyFrame(runTime), bird.getX(),
                    bird.getY(), bird.getWidth() / 2.0f,
                    bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(),
                    1, 1, bird.getRotation());
        }


        //draw text
        if (myWorld.isReady()) {
            // Draw shadow first
            AssetLoader.shadow.draw(batcher, "Touch me", (136 / 2)- (42), 76);
            // Draw text
            AssetLoader.font.draw(batcher, "Touch me", (136 / 2) - (42 - 1), 75);
        } else {

            if (myWorld.isGameOver() || myWorld.isHighScore()) {

                if (myWorld.isGameOver()) {
                    AssetLoader.shadow.draw(batcher, "Game Over", 25, 56);
                    AssetLoader.font.draw(batcher, "Game Over", 24, 55);

                    AssetLoader.shadow.draw(batcher, "High Score:", 23, 106);
                    AssetLoader.font.draw(batcher, "High Score:", 22, 105);

                    String highScore = AssetLoader.getHighScore() + "";

                    // Draw shadow first
                    AssetLoader.shadow.draw(batcher, highScore, (136 / 2)
                            - (3 * highScore.length()), 128);
                    // Draw text
                    AssetLoader.font.draw(batcher, highScore, (136 / 2)
                            - (3 * highScore.length() - 1), 127);
                } else {
                    AssetLoader.shadow.draw(batcher, "High Score!", 19, 56);
                    AssetLoader.font.draw(batcher, "High Score!", 18, 55);
                }

                AssetLoader.shadow.draw(batcher, "Try again?", 23, 76);
                AssetLoader.font.draw(batcher, "Try again?", 24, 75);

                // Convert integer into String
                String score = myWorld.getScore() + "";

                // Draw shadow first
                AssetLoader.shadow.draw(batcher, score,
                        (136 / 2) - (3 * score.length()), 12);
                // Draw text
                AssetLoader.font.draw(batcher, score,
                        (136 / 2) - (3 * score.length() - 1), 11);

            }
            // Convert integer into String
            String score = myWorld.getScore() + "";
            // Draw shadow first
            AssetLoader.shadow.draw(batcher, "" + myWorld.getScore(), (136 / 2)
                    - (3 * score.length()), 12);
            // Draw text
            AssetLoader.font.draw(batcher, "" + myWorld.getScore(), (136 / 2)
                    - (3 * score.length() - 1), 11);
        }


        batcher.end();
        // End SpriteBatch ====================================


        //check collision boxes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //draw bird collision circle
//        shapeRenderer.setColor(Color.BLUE);
//        shapeRenderer.circle(bird.getBoundingCircle().x, bird.getBoundingCircle().y, bird.getBoundingCircle().radius);
//        //draw pipe rectangels
//        shapeRenderer.setColor(Color.RED);
//        // Bar up for pipes 1 2 and 3
//        shapeRenderer.rect(pipe1.getBarUp().x, pipe1.getBarUp().y,
//                pipe1.getBarUp().width, pipe1.getBarUp().height);
//        shapeRenderer.rect(pipe2.getBarUp().x, pipe2.getBarUp().y,
//                pipe2.getBarUp().width, pipe2.getBarUp().height);
//        shapeRenderer.rect(pipe3.getBarUp().x, pipe3.getBarUp().y,
//                pipe3.getBarUp().width, pipe3.getBarUp().height);
//
//        // Bar down for pipes 1 2 and 3
//        shapeRenderer.rect(pipe1.getBarDown().x, pipe1.getBarDown().y,
//                pipe1.getBarDown().width, pipe1.getBarDown().height);
//        shapeRenderer.rect(pipe2.getBarDown().x, pipe2.getBarDown().y,
//                pipe2.getBarDown().width, pipe2.getBarDown().height);
//        shapeRenderer.rect(pipe3.getBarDown().x, pipe3.getBarDown().y,
//                pipe3.getBarDown().width, pipe3.getBarDown().height);
//
//        // Skull up for Pipes 1 2 and 3
//        shapeRenderer.rect(pipe1.getSkullUp().x, pipe1.getSkullUp().y,
//                pipe1.getSkullUp().width, pipe1.getSkullUp().height);
//        shapeRenderer.rect(pipe2.getSkullUp().x, pipe2.getSkullUp().y,
//                pipe2.getSkullUp().width, pipe2.getSkullUp().height);
//        shapeRenderer.rect(pipe3.getSkullUp().x, pipe3.getSkullUp().y,
//                pipe3.getSkullUp().width, pipe3.getSkullUp().height);
//
//        // Skull down for Pipes 1 2 and 3
//        shapeRenderer.rect(pipe1.getSkullDown().x, pipe1.getSkullDown().y,
//                pipe1.getSkullDown().width, pipe1.getSkullDown().height);
//        shapeRenderer.rect(pipe2.getSkullDown().x, pipe2.getSkullDown().y,
//                pipe2.getSkullDown().width, pipe2.getSkullDown().height);
//        shapeRenderer.rect(pipe3.getSkullDown().x, pipe3.getSkullDown().y,
//                pipe3.getSkullDown().width, pipe3.getSkullDown().height);
//


        shapeRenderer.end();


    }
}
