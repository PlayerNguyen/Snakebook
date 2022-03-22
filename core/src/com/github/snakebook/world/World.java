package com.github.snakebook.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.snakebook.Renderable;
import com.github.snakebook.entity.Poop;
import com.github.snakebook.entity.blocks.Block;
import com.github.snakebook.entity.blocks.TerrainBlock;
import com.github.snakebook.entity.Entity;
import com.github.snakebook.entity.Player;
import com.github.snakebook.locations.Location;
import com.github.snakebook.utils.ArbitraryRandom;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.github.snakebook.world.World.Constant.WORLD_HEIGHT;
import static com.github.snakebook.world.World.Constant.WORLD_WIDTH;


public class World implements Renderable {

    public static class Constant {

        public static final int WORLD_HEIGHT = 32 * 512;
        public static final int WORLD_WIDTH = 32 * 512;
        public static final Location INIT_LOCATION = new Location(0, 0);

        public static final int CHUNK_RENDERING_SIZE = 800;

    }

    private final Set<Entity> entities = new CopyOnWriteArraySet<>();
    private final List<Block> blocks = new ArrayList<>();
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player player;

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void create(World world) {

        // Sprite batch init
        this.batch = new SpriteBatch();

        // Camera init
        this.camera = new OrthographicCamera(
                (float) Gdx.graphics.getWidth() / 2,
                (float) Gdx.graphics.getHeight() / 2
        );
        this.camera.setToOrtho(false);

        // Player init
        this.player = new Player(Constant.INIT_LOCATION);
        this.entities.add(player);

        // Generate terrain block
        this.generateTerrainBlock();

        // After generate all block, call create from block
        for (Block block : blocks) {
            block.create(this);
        }

        this.entities.add(new Poop(new Location(128, 128)));
        this.entities.add(new Poop(new Location(256, 256)));
        this.entities.add(new Poop(new Location(512, 512)));

        for (Entity entity : this.entities) {
            entity.create(this);
        }
    }

    private void generateTerrainBlock() {
        long firstMeasure = System.currentTimeMillis();
        ArbitraryRandom<Texture> dirtTypeNumber = ArbitraryRandom.createInstance(
                ArbitraryRandom.Pair.create(10, new Texture(Gdx.files.internal("dirtbland.png"))),
                ArbitraryRandom.Pair.create(15, new Texture(Gdx.files.internal("dirt1.png"))),
                ArbitraryRandom.Pair.create(12, new Texture(Gdx.files.internal("dirt2.png"))),
                ArbitraryRandom.Pair.create(1, new Texture(Gdx.files.internal("dirt3.png"))),
                ArbitraryRandom.Pair.create(1, new Texture(Gdx.files.internal("dirt4.png")))
        );

        for (int i = -Constant.WORLD_HEIGHT; i < Constant.WORLD_HEIGHT; i += 32) {
            for (int j = -WORLD_WIDTH; j < WORLD_WIDTH; j += 32) {
                this.blocks.add(new TerrainBlock(
                        new Location(i, j),
                        dirtTypeNumber.getRandom()
                ));
            }
        }
        long current = System.currentTimeMillis() - firstMeasure;
        System.out.println("[World] Generated in " + current + " ms");
    }

    @Override
    public void render(World world) {
        batch.setProjectionMatrix(this.camera.combined);
        this.camera.update();


        batch.begin();

        for (Block block : blocks) {
            block.render(this);
        }
        for (Entity entity : entities) {
            entity.render(this);
        }
        player.render(this);
        batch.end();
    }

    @Override
    public void dispose(World world) {
        // Dispose all entity
        for (Entity entity : entities) {
            entity.dispose(this);
        }

        // Remove batch
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public boolean isOutOfBonds(Location loc) {
        return (loc.y < -WORLD_HEIGHT || loc.y >= WORLD_HEIGHT)
                || (loc.x <= -WORLD_WIDTH || loc.x >= WORLD_WIDTH);
    }

    public Player getPlayer() {
        return player;
    }
}
