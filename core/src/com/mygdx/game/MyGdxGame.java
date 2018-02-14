package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.Player;

public class MyGdxGame extends ApplicationAdapter {
    private static final float SCALE = 2.0f;
    public static final float PIXEL_PER_METER = 32f;
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float VELOCITY_Y = -9.85f;
    private static final float VELOCITY_X = 0f;
    private OrthographicCamera orthographicCamera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private World world;
    private Player player;
    private SpriteBatch batch;
    private Texture texture;

    @Override
    public void create() {
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
        world = new World(new Vector2(VELOCITY_X, VELOCITY_Y), false);
        batch = new SpriteBatch();
        texture = new Texture(Player.PLAYER_IMG_PATH);
        box2DDebugRenderer = new Box2DDebugRenderer();
        player = new Player(world);
    }

    @Override
    public void render() {
        update();
        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, player.getBody().getPosition().x * PIXEL_PER_METER - (texture.getWidth() / 2),
                player.getBody().getPosition().y * PIXEL_PER_METER - (texture.getHeight() / 2));
        batch.end();
        box2DDebugRenderer.render(world, orthographicCamera.combined.scl(PIXEL_PER_METER));
    }

    private void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        cameraUpdate();
        batch.setProjectionMatrix(orthographicCamera.combined);
    }

    private void cameraUpdate() {
        Vector3 position = orthographicCamera.position;
        position.x = player.getBody().getPosition().x * PIXEL_PER_METER;
        position.y = player.getBody().getPosition().y * PIXEL_PER_METER;
        orthographicCamera.position.set(position);
        orthographicCamera.update();
    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.setToOrtho(false, width / SCALE, height / SCALE);
    }

    @Override
    public void dispose() {
        texture.dispose();
        batch.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
    }
}
