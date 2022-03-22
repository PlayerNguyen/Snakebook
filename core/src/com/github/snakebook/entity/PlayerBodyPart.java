package com.github.snakebook.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.github.snakebook.locations.Location;
import com.github.snakebook.locations.Vector;
import com.github.snakebook.world.World;

public class PlayerBodyPart {
    protected boolean isHead;
    protected boolean isTail;
    protected Location location;
    protected Vector direction = new Vector();
    protected Texture texture;
    protected Sprite sprite;
    protected float angle;

    public PlayerBodyPart(Location location, Texture texture) {
        this.location = location;
        this.texture = texture;
        this.sprite = new Sprite(texture);
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public boolean isTail() {
        return isTail;
    }

    public void setTail(boolean tail) {
        isTail = tail;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public void create(World w) {
        this.sprite.setSize(64, 64);
        this.sprite.setOrigin(32, 32);
    }

    public void render(World world) {
        this.location.add(this.direction.x, this.direction.y);
        this.sprite.setX(this.location.x);
        this.sprite.setY(this.location.y);

        this.sprite.setRotation((float) Math.toDegrees(this.angle));
        this.sprite.draw(world.getBatch());
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }
}
