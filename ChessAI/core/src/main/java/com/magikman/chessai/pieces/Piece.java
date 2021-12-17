package com.magikman.chessai.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.magikman.chessai.game.Board;

public abstract class Piece {
	
	Board gameBoard;
	int boardX, boardY;
	float spSize;
	
	Sprite piece;
	
	boolean color;
	
	int activeAnim;
	int animTime;
	int deltaX, deltaY;
	int tempX, tempY;
	
	public Piece(Texture img, Board gameBoard, int boardX, int boardY, boolean color) {
		this.piece = new Sprite(img);
		this.color = color;
		
		this.gameBoard = gameBoard;
		this.boardX = boardX;
		this.boardY = boardY;
		
		spSize = gameBoard.getSquareSize() / 8 * 7;
		
		piece.setSize(spSize,  spSize);
		piece.setCenter(gameBoard.getCenters()[boardX][boardY].x, gameBoard.getCenters()[boardX][boardY].y);
		
		animTime = 8;
		activeAnim = 9;
	}
	
	public abstract boolean[][] activate(Army ally, Army opponent, boolean truePos);
	
	public void updateSize() {
		spSize = gameBoard.getSquareSize() / 8 * 7;
		
		piece.setSize(spSize, spSize);
		piece.setCenter(gameBoard.getCenters()[boardX][boardY].x, gameBoard.getCenters()[boardX][boardY].y);
	}
	
	public void update() {
		if(activeAnim < animTime) {
			activeAnim++;
			piece.setPosition(tempX, tempY);
			
			tempX += deltaX;
			tempY += deltaY;
		}
		
		if(activeAnim == animTime) {
			updateSize();
			activeAnim++;
		}
	}
	
	public boolean validLocation(int x, int y) {
		if(Math.max(0, Math.min(x, 7)) == x && Math.max(0, Math.min(y, 7)) == y) return true;
		
		return false;
	}
	
	public void setSpPos(float cenX, float cenY) {
		piece.setCenter(cenX, cenY);
	}
	
	public void move(int destX, int destY) {
		deltaX = (int)gameBoard.getCenters()[destX][destY].x - (int)gameBoard.getCenters()[boardX][boardY].x;
		deltaY = (int)gameBoard.getCenters()[destX][destY].y - (int)gameBoard.getCenters()[boardX][boardY].y;
		
		deltaX /= animTime;
		deltaY /= animTime;
		
		boardX = destX;
		boardY = destY;
		
		tempX = (int)piece.getX();
		tempY = (int)piece.getY();
		
		activeAnim = 0;
	}
	
	public void teleport(int destX, int destY) {
		this.boardX = destX;
		this.boardY = destY;
		
		updateSize();
	}
	
	public void render(SpriteBatch sb) {
		piece.draw(sb);
	}
	
	public boolean getColor() {
		return color;
	}
	
	public int[] getLocation() {
		return new int[] {boardX, boardY};
	}
	
	public void dispose() {
		piece.getTexture().dispose();
	}
}
