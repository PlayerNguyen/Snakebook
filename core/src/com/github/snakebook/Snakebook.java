package com.github.snakebook;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.github.snakebook.world.World;

public class Snakebook extends ApplicationAdapter {

	private World world;
	
	@Override
	public void create () {
		world = new World();
		world.create(null);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.render(world);
	}
	
	@Override
	public void dispose () {
		world.dispose(null);
	}
}
