package com.github.snakebook.entity;

import com.badlogic.gdx.math.Vector2;
import com.github.snakebook.locations.Location;

public abstract class SnakebookEntity implements Entity {
    private Location location;

    public SnakebookEntity(Location location) {
        this.location = location;
    }

    public SnakebookEntity() {
        this.location = new Location(0, 0);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
