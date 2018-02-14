package com.mygdx.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;

public class Player {
    private static final int BOX_SIZE = 32;
    private static final float PLAYER_DENSITY = 1.0f;
    public static final float JUMP_FORCE = 250f;
    public static final float RUN_FORCE = 5f;
    public static final String PLAYER_IMG_PATH = "cat.png";
    private static final float PLAYER_START_X = 8f;
    private static final float PLAYER_START_Y = 18f;
    private Body body;
    private boolean isJumping = false;

    public Player(World world) {
        createBoxBody(world, PLAYER_START_X, PLAYER_START_Y);
    }

    private void createBoxBody(World world, float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(BOX_SIZE / MyGdxGame.PIXEL_PER_METER / 2, BOX_SIZE / MyGdxGame.PIXEL_PER_METER / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = PLAYER_DENSITY;
        body = world.createBody(bdef);
        body.createFixture(fixtureDef).setUserData(this);
    }
    public Body getBody() {
        return body;
    }

    public void hit() {
        Gdx.app.log("log", "HIT");
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }
    
    public boolean isJumping() {
        return isJumping;
    }


}
