package com.github.snakebook.entity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.snakebook.locations.Location;
import com.github.snakebook.world.World;

/**
 * Terrain block represents a block that rendering when the orthographic camera is nearby.
 */
public class TerrainBlock implements Block {

    private Location location;
    private final Texture texture;
    private float lastDistanceFromCamera = Float.MAX_EXPONENT;
    private Sprite sprite;

    /**
     * @deprecated this method will create a large of texture objects.
     */
    @Deprecated
    public TerrainBlock(Location location, String assetFileName) {
        this.location = location;
        this.texture = new Texture(Gdx.files.internal(assetFileName));
        this.sprite = new Sprite(this.texture);
    }

    public TerrainBlock(Location location, Texture texture) {
        this.location = location;
        this.texture = texture;

    }

    @Override
    public void create(World world) {
        this.lastDistanceFromCamera = calculateDistanceFromCamera(world.getCamera());
        this.sprite = new Sprite(this.texture);
        this.sprite.setSize(32, 32);
        this.sprite.setOrigin(16, 16);
        this.sprite.setX(this.location.x);
        this.sprite.setY(this.location.y);
//        this.sprite.setColor(0.1f, 0.2f, 0.1f, 1);
    }

    @Override
    public void render(World world) {
        // Check with last distance
        if (this.getLastDistanceFromCamera() <= World.Constant.CHUNK_RENDERING_SIZE) {
            this.draw(world);
        }

        // Then re-calculate distance
        final float delta = calculateDistanceFromCamera(world.getCamera());
        this.setLastDistanceFromCamera(delta);
    }

    private void draw(World world) {
//        world.getBatch().draw(this.texture, this.location.getX(), this.location.y, 32, 32);
        this.sprite.draw(world.getBatch());

    }

    private float calculateDistanceFromCamera(OrthographicCamera camera) {
        return (float) this.location.distance(new Location(camera.position.x, camera.position.y));
    }

    @Override
    public float getLastDistanceFromCamera() {
        return lastDistanceFromCamera;
    }

    @Override
    public void setLastDistanceFromCamera(float lastDistanceFromCamera) {
        this.lastDistanceFromCamera = lastDistanceFromCamera;
    }

    @Override
    public void dispose(World world) {
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Texture getTexture() {
        return this.texture;
    }
}
