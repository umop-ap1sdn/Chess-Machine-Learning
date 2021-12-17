package com.magikman.chessai.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {
	
	Manager gsm;
	
	public State(Manager gsm) {
		this.gsm = gsm;
	}
	
	public void run(float dt, SpriteBatch sb) {
		handleInput();
		update(dt);
		render(sb);
	}
	
	public void setState(State st) {
		dispose();
		gsm.set(st);
	}
	
	public abstract void update(float dt);
	public abstract void handleInput();
	public abstract void render(SpriteBatch sb);
	public abstract void dispose();
	
}
