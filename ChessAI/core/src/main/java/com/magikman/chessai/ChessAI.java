package com.magikman.chessai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.magikman.chessai.state.Manager;
import com.magikman.chessai.state.MenuState;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ChessAI extends ApplicationAdapter {
	
	SpriteBatch sb;
	Manager gsm;
	
	@Override
	public void create() {
		
		sb = new SpriteBatch();
		gsm = new Manager();
		gsm.set(new MenuState(gsm));
	}
	
	@Override
	public void render() {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(1, 0, 0, 1);
		
		gsm.run(Gdx.graphics.getDeltaTime(), sb);
	}
}