package com.magikman.chessai.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Manager {
	
	State currentState;
	
	public void set(State state) {
		this.currentState = state;
	}
	
	public void run(float dt, SpriteBatch sb) {
		currentState.run(dt, sb);
	}
}
