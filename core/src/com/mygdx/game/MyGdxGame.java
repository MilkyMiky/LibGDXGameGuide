package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.Player;
import com.mygdx.game.utils.MapParser;

public class MyGdxGame extends ApplicationAdapter {
    private static final float SCALE = 2.0f;
    public static final float PIXEL_PER_METER = 32f;
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float VELOCITY_Y = -9.85f;
    private static final float VELOCITY_X = 0f;
    private static final String MAP_PATH = "map/GameMap.tmx";
    private OrthographicCamera orthographicCamera;
    private World world;
    private Player player;
    private SpriteBatch batch;
    private Texture texture;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;

    @Override
    public void create() {
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);

        world = new World(new Vector2(VELOCITY_X, VELOCITY_Y), false);
        world.setContactListener(new WorldContactListener());

        batch = new SpriteBatch();
        texture = new Texture(Player.PLAYER_IMG_PATH);

        tiledMap = new TmxMapLoader().load(MAP_PATH);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        MapParser.parseMapLayers(world, tiledMap);
        player = new Player(world);
    }

    @Override
    public void render() {
        update();

        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.render();

        batch.begin();
        batch.draw(texture, player.getBody().getPosition().x * PIXEL_PER_METER - (texture.getWidth() / 2),
                player.getBody().getPosition().y * PIXEL_PER_METER - (texture.getHeight() / 2));
        batch.end();
    }

    private void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        inputUpdate();
        cameraUpdate();
        tiledMapRenderer.setView(orthographicCamera);
        batch.setProjectionMatrix(orthographicCamera.combined);
    }

    private void inputUpdate() {
        int horizontalForce = 0;
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos = orthographicCamera.unproject(touchPos);
            if (touchPos.x / PIXEL_PER_METER > player.getBody().getPosition().x)
                horizontalForce += 1;
            if (touchPos.x / PIXEL_PER_METER < player.getBody().getPosition().x)
                horizontalForce -= 1;
            if (touchPos.y / PIXEL_PER_METER > player.getBody().getPosition().y && !player.isJumping())
                player.getBody().applyForceToCenter(0, Player.JUMP_FORCE, false);
        }
        player.getBody().setLinearVelocity(horizontalForce * Player.RUN_FORCE, player.getBody().getLinearVelocity().y);
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
        world.dispose();
        tiledMapRenderer.dispose();
        tiledMap.dispose();
    }
}
