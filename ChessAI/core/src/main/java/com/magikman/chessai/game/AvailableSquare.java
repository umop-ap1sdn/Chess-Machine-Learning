package com.magikman.chessai.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AvailableSquare {
	
	boolean on;
	Board gameBoard;
	
	int boardX, boardY;
	float spSize;
	
	Sprite indicator;
	
	public AvailableSquare(Board gameBoard, int boardX, int boardY) {
		this.gameBoard = gameBoard;
		this.boardX = boardX;
		this.boardY = boardY;
		
		on = false;
		
		indicator = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\indicator.png"));
		indicator.setColor(0.5f,  0.5f,  0.5f,  0.5f);
		
		spSize = gameBoard.getSquareSize() / 2;
		indicator.setSize(spSize, spSize);
		indicator.setCenter(gameBoard.getCenters()[boardX][boardY].x, gameBoard.getCenters()[boardX][boardY].y);
	}
	
	public void updateSize() {
		spSize = gameBoard.getSquareSize() / 2;
		indicator.setSize(spSize, spSize);
		indicator.setCenter(gameBoard.getCenters()[boardX][boardY].x, gameBoard.getCenters()[boardX][boardY].y);
	}
	
	public void setActivate(boolean on) {
		this.on = on;
	}
	
	public boolean getActivate() {
		return this.on;
	}
	
	public void render(SpriteBatch sb) {
		if(on) {
			indicator.draw(sb);
		}
	}
	
	public void dispose() {
		indicator.getTexture().dispose();
	}
}
