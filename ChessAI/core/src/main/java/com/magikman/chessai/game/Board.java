package com.magikman.chessai.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Board {
	
	float windowX, windowY;
	float posX, posY;
	float boardSize, squareSize;
	
	Texture img;
	Sprite board;
	
	Vector2[][] centerPoints;
	Rectangle[][] squares;
	
	AvailableSquare[][] indicators;
	
	public Board(float windowX, float windowY, float posX, float posY) {
		
		img = new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\board.png");
		board = new Sprite(img);
		
		this.windowX = windowX;
		this.windowY = windowY;
		this.posX = posX;
		this.posY = posY;
		
		this.boardSize = windowY / 8 * 7;
		
		if((windowX - 2) < boardSize) {
			boardSize = windowX - 2;
		}
		
		board.setSize(boardSize,  boardSize);
		board.setCenter(posX + windowX / 2, posY + windowY / 2);
		
		centerPoints = new Vector2[8][8];
		squares = new Rectangle[8][8];
		
		calculateSquares();
		
		indicators = new AvailableSquare[8][8];
		setupIndicators();
	}
	
	public void setupIndicators() {
		for(int x = 0; x < indicators.length; x++) {
			for(int y = 0; y < indicators[x].length; y++) {
				indicators[x][y] = new AvailableSquare(this, x, y);
			}
		}
	}
	
	public void updateSize(float windowX, float windowY, float posX, float posY) {
		this.windowX = windowX;
		this.windowY = windowY;
		this.posX = posX;
		this.posY = posY;
		
		this.boardSize = windowY / 8 * 7;
		
		if((windowX - 2) < boardSize) {
			boardSize = windowX - 2;
		}
		
		board.setSize(boardSize,  boardSize);
		board.setCenter(posX + windowX / 2, posY + windowY / 2);
		
		calculateSquares();
		
		for(int x = 0; x < indicators.length; x++) {
			for(int y = 0; y < indicators[x].length; y++) {
				indicators[x][y].updateSize();
			}
		}
	}
	
	public void calculateSquares() {
		this.squareSize = boardSize / 8;
		
		for(int x = 0; x < squares.length; x++) {
			for(int y = 0; y < squares[x].length; y++) {
				float squareX = board.getX() + (squareSize * x);
				float squareY = board.getY() + (squareSize * y);
				
				Rectangle square = new Rectangle();
				square.set(squareX, squareY, squareSize, squareSize);
				squares[x][y] = square;
				
				float cenX = squareX + (squareSize / 2);
				float cenY = squareY + (squareSize / 2);
				centerPoints[x][y] = new Vector2(cenX, cenY);
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		board.draw(sb);
		
		for(int x = 0; x < indicators.length; x++) {
			for(AvailableSquare ind: indicators[x]) {
				ind.render(sb);
			}
		}
	}
	
	public Rectangle[][] getSquares(){
		return this.squares;
	}
	
	public Vector2[][] getCenters(){
		return this.centerPoints;
	}
	
	public float getSquareSize() {
		return this.squareSize;
	}
	
	public AvailableSquare[][] getIndicators(){
		return this.indicators;
	}
	
	public void dispose() {
		img.dispose();
		board.getTexture().dispose();
		
		for(int x = 0; x < indicators.length; x++) {
			for(AvailableSquare ind: indicators[x]) {
				ind.dispose();
			}
		}
	}
}
