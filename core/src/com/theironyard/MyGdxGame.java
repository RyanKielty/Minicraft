package com.theironyard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {

    SpriteBatch batch;

    TextureRegion stand, down, up, right, left, downFlip, upFlip, standFlip;

    boolean faceRight = false;
    boolean faceUp = false;
    boolean faceLeft = false;
    boolean faceDown = false;
    boolean walkFaster = false;

    Animation walkRight, walkLeft, walkDown, walkUp;

    float time;

    float x, y, xv, yv;
    static final float MAX_VELOCITY = 200;

    static final int WIDTH = 18;
    static final int HEIGHT = 26;

    static final int DRAW_WIDTH = WIDTH;
    static final int DRAW_HEIGHT = HEIGHT;


    @Override
    public void create() {
        batch = new SpriteBatch();
        Texture tiles = new Texture("tiles.png");
        TextureRegion[][] grid = TextureRegion.split(tiles, 16, 16);

        stand = grid[6][2];
        standFlip = new TextureRegion(stand);
        standFlip.flip(true,false);

        down = grid[6][0];
        downFlip = new TextureRegion(down);
        downFlip.flip(true, false);
        walkDown = new Animation(0.3f, grid[6][0], downFlip);

        up = grid[6][1];
        upFlip = new TextureRegion(up);
        upFlip.flip(true, false);
        walkUp = new Animation(0.3f, grid[6][1], upFlip);

        right = grid[6][3];
        walkRight = new Animation(0.3f, grid[6][2], grid[6][3]);

        left = new TextureRegion(right);
        left.flip(true, false);
        walkLeft = new Animation(0.3f, standFlip, left);
    }

    @Override
    public void render() {
        time += Gdx.graphics.getDeltaTime();

        move();

//        TextureRegion zombie;

        TextureRegion player;
        if (faceUp) {
            player = walkUp.getKeyFrame(time, true);
        } else if (xv < 0) {
            player = walkLeft.getKeyFrame(time, true);
        } else if (xv > 0) {
            player = walkRight.getKeyFrame(time, true);
        } else if (faceDown) {
            player = walkDown.getKeyFrame(time, true);
        } else {
            player = stand;
        }

        Gdx.gl.glClearColor(102/255f, 153/255f, 0/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(player, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        batch.end();
    }

    float decelerate(float velocity) {
        float deceleration = 0.1f;
        velocity *= deceleration;
        if (Math.abs(velocity) < 1) {
            velocity = 0;
        }
        return velocity;
    }


    public void move() {
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
		    walkFaster = true;
		} else {
		    walkFaster = false;
        }
//Make the player move via the arrow keys.
//Make the game draw the correct sprite (down, up, left, right)
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yv = MAX_VELOCITY;
            faceUp = true;
            if (walkFaster) {
                yv = MAX_VELOCITY * 2;
            }
        } else {
            faceUp = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yv = MAX_VELOCITY * -1;
            faceDown = true;
            if (walkFaster) {
                yv = MAX_VELOCITY * -2;
            }
        } else {
            faceDown = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xv = MAX_VELOCITY;
            faceRight = true;
            if (walkFaster) {
                xv = MAX_VELOCITY * 2;
            }
        } else {
            faceRight = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xv = MAX_VELOCITY * -1;
            faceLeft = true;
            if (walkFaster) {
                xv = MAX_VELOCITY * -2;
            }
        } else {
            faceLeft = false;
        }

        y += yv * Gdx.graphics.getDeltaTime();
        x += xv * Gdx.graphics.getDeltaTime();

        if (y < 0) {
            y = Gdx.graphics.getHeight();
        }
        if (x < 0) {
            x = Gdx.graphics.getWidth();
        }
        if (x > Gdx.graphics.getWidth()){
            x = 0;
        }
        if (y > Gdx.graphics.getHeight()){
            y = 0;
        }

        yv = decelerate(yv);
        xv = decelerate(xv);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
