package com.github.snakebook.entity;

import com.badlogic.gdx.graphics.Texture;
import com.github.snakebook.Renderable;
import com.github.snakebook.locations.Location;

public interface Entity extends Renderable {

    Location getLocation();

    void setLocation(Location location);

    Texture getTexture();

}
