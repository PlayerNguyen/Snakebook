package com.github.snakebook;

import com.github.snakebook.world.World;

public interface Renderable {

    void create(World world);

    void render(World world);

    void dispose(World world);

}
