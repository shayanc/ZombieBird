package com.kilobolt.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kilobolt.Helpers.AssetLoader;

/**
 * Created by Shayan on 10/06/2015.
 * BIRD GAME OBJECT
 */
public class Bird {

    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;

    private float rotation; // For handling bird rotation
    private int width;
    private int height;
    private Circle boundingCircle;

    private boolean isAlive;

    public Bird(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x,y);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 460);
        boundingCircle = new Circle();
        isAlive = true;
    }

    public void update(float delta) {
        velocity.add(acceleration.cpy().scl(delta));
        //update y position based on velocity
        position.add(velocity.cpy().scl(delta));
        //update rotation
        // Rotate counterclockwise
        if (velocity.y < 0) {
            //scaled by delta to keep rotation constant on fast/slow devices.
            rotation -= 600 * delta;

            if (rotation < -20) {
                rotation = -20;
            }
        }
        // Rotate clockwise
        if (isFalling() || !isAlive) {
            //scaled by delta to keep rotation constant on fast/slow devices.
            rotation += 480 * delta;
            if (rotation > 90) {
                rotation = 90;
            }
        }

        //perform boundary checks
        if(position.y > 180) {position.y = 180;}
        if(position.y <0) {position.y = 0;}
        // Set the circle's center to be (9, 6) with respect to the bird.
        // Set the circle's radius to be 6.5f;
        boundingCircle.set(position.x + 9, position.y + 6, 6.5f);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isFalling() {
        return velocity.y > 110;
    }

    public boolean shouldntFlap() {
        return velocity.y > 70  || !isAlive;
    }

    public void onClick() {
        if (isAlive) {
            AssetLoader.flap.play();
            velocity.y = -140;
        }
    }

    public void die() {
        isAlive = false;
        velocity.y = 0;
    }

    public void decelerate() {
        // We want the bird to stop accelerating downwards once it is dead.
        acceleration.y = 0;
    }

    public void onRestart(int y) {
        rotation = 0;
        position.y = y;
        velocity.x = 0;
        velocity.y = 0;
        acceleration.x = 0;
        acceleration.y = 460;
        isAlive = true;
    }


    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRotation() {
        return rotation;
    }

    public Circle getBoundingCircle() {
        return boundingCircle;
    }

}
