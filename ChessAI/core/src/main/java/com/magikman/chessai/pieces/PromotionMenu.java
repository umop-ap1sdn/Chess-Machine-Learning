package com.magikman.chessai.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.magikman.chessai.game.Board;

public class PromotionMenu {
	
	Sprite pieceR, pieceN, pieceB, pieceQ;
	Board gameBoard;
	Army army;
	int boardX = 2;
	int boardY;
	
	public PromotionMenu(Board gameBoard, Army army) {
		
		
		this.army = army;
		this.gameBoard = gameBoard;
		
		if(army.white) {
			pieceR = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\WRook.png"));
			pieceN = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\WKnight.png"));
			pieceB = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\WBishop.png"));
			pieceQ = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\WQueen.png"));
			
			boardY = 3;
		} else {
			pieceR = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\BRook.png"));
			pieceN = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\BKnight.png"));
			pieceB = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\BBishop.png"));
			pieceQ = new Sprite(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\BQueen.png"));
			
			boardY = 4;
		}
		
		pieceR.setSize(gameBoard.getSquareSize() / 8 * 7, gameBoard.getSquareSize() / 8 * 7);
		pieceN.setSize(gameBoard.getSquareSize() / 8 * 7, gameBoard.getSquareSize() / 8 * 7);
		pieceB.setSize(gameBoard.getSquareSize() / 8 * 7, gameBoard.getSquareSize() / 8 * 7);
		pieceQ.setSize(gameBoard.getSquareSize() / 8 * 7, gameBoard.getSquareSize() / 8 * 7);
		
		pieceR.setCenter(gameBoard.getCenters()[boardX][boardY].x, gameBoard.getCenters()[boardX][boardY].y);
		pieceN.setCenter(gameBoard.getCenters()[boardX + 1][boardY].x, gameBoard.getCenters()[boardX + 1][boardY].y);
		pieceB.setCenter(gameBoard.getCenters()[boardX + 2][boardY].x, gameBoard.getCenters()[boardX + 2][boardY].y);
		pieceQ.setCenter(gameBoard.getCenters()[boardX + 3][boardY].x, gameBoard.getCenters()[boardX + 3][boardY].y);
		
	}
	
	public void updateSize() {
		pieceR.setSize(gameBoard.getSquareSize() / 8 * 7, gameBoard.getSquareSize() / 8 * 7);
		pieceN.setSize(gameBoard.getSquareSize() / 8 * 7, gameBoard.getSquareSize() / 8 * 7);
		pieceB.setSize(gameBoard.getSquareSize() / 8 * 7, gameBoard.getSquareSize() / 8 * 7);
		pieceQ.setSize(gameBoard.getSquareSize() / 8 * 7, gameBoard.getSquareSize() / 8 * 7);
		
		pieceR.setCenter(gameBoard.getCenters()[boardX][boardY].x, gameBoard.getCenters()[boardX][boardY].y);
		pieceN.setCenter(gameBoard.getCenters()[boardX + 1][boardY].x, gameBoard.getCenters()[boardX + 1][boardY].y);
		pieceB.setCenter(gameBoard.getCenters()[boardX + 2][boardY].x, gameBoard.getCenters()[boardX + 2][boardY].y);
		pieceQ.setCenter(gameBoard.getCenters()[boardX + 3][boardY].x, gameBoard.getCenters()[boardX + 3][boardY].y);
		
	}
	
	public int getChoice(int boardX, int boardY) {
		if(boardY == this.boardY) {
			switch(boardX) {
			case 2: return 0;
			case 3: return 1;
			case 4: return 2;
			case 5: return 3;
			default: return -1;
			
			}
		}
		
		return -1;
	}
	
	public void render(SpriteBatch sb) {
			pieceR.draw(sb);
			pieceN.draw(sb);
			pieceB.draw(sb);
			pieceQ.draw(sb);
			
	}
	
	public void dispose() {
		pieceR.getTexture().dispose();
		pieceN.getTexture().dispose();
		pieceB.getTexture().dispose();
		pieceQ.getTexture().dispose();
	}
}
