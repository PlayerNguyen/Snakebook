package com.github.snakebook.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.snakebook.locations.Location;
import com.github.snakebook.locations.Vector;
import com.github.snakebook.world.World;

public class Stone implements Entity {

    private final Texture STONE_TEXTURE;
    private Location location;
    private Sprite sprite;
    private Vector direction;

    public Stone(Texture stone_texture, Location location){
        STONE_TEXTURE = stone_texture;
        this.location = location;
        this.direction = new Vector(0, 0);
    }

    @Override
    public void create(World world) {
        this.sprite = new Sprite(this.getTexture());
        this.sprite.setOrigin(16, 16);
        this.sprite.setSize(64, 64);
        this.sprite.setColor(0.67777f, 0.67777f, 0.67777f, 1);

        this.direction.set(0, 0);
    }

    @Override
    public void render(World world) {
        this.sprite.draw(world.getBatch());
        this.sprite.setX(this.location.x);
        this.sprite.setY(this.location.y);

        if (world.getPlayer().getHeadLocation().distance(this.location) <= 32f) {
            System.out.println("Dead");
            world.getEntities().remove(this);
        }
    }

    @Override
    public void dispose(World world) {
        STONE_TEXTURE.dispose();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Texture getTexture() {
        return STONE_TEXTURE;
    }
}
