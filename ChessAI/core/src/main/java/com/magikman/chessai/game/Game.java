package com.magikman.chessai.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.magikman.chessai.controller.Controller;
import com.magikman.chessai.controller.MLController;
import com.magikman.chessai.controller.MouseController;
import com.magikman.chessai.pgn.PgnWriter;
import com.magikman.chessai.pieces.*;

public class Game {
	
	float windowX, windowY;
	float posX, posY;
	boolean whiteTurn;
	
	Board gameBoard;
	
	ShapeRenderer sr;
	
	Army whiteArmy, blackArmy;
	
	Controller whitePlayer, blackPlayer;
	
	boolean checkForEnd;
	
	int gameEnd;
	int turns;
	
	PgnWriter gameSave;
	
	public Game(Controller whitePlayer, Controller blackPlayer, float windowX, float windowY, float posX, float posY, OrthographicCamera cam) {
		this.windowX = windowX;
		this.windowY = windowY;
		this.posX = posX;
		this.posY = posY;
		
		gameBoard = new Board(windowX, windowY, posX, posY);
		
		gameSave = new PgnWriter();
		
		if(whitePlayer instanceof MLController) gameSave.addMlController((MLController)whitePlayer, true);
		if(blackPlayer instanceof MLController) gameSave.addMlController((MLController)blackPlayer, true);
		
		whiteArmy = new Army(gameBoard, true, gameSave);
		blackArmy = new Army(gameBoard, false, gameSave);
		
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		
		sr.setColor(0, 0, 0, 1);
		
		setupBoard();
		
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		
		whitePlayer.setVars(whiteArmy, gameBoard);
		blackPlayer.setVars(blackArmy, gameBoard);
		
		whiteTurn = true;
		
		checkForEnd = false;
		
		gameEnd = -1;
		turns = 0;
		
		this.set();
	}
	
	public void setupBoard() {
		
		whiteArmy.setupArmy();
		blackArmy.setupArmy();
		
		whiteArmy.setOpponent(blackArmy);
		blackArmy.setOpponent(whiteArmy);
	}
	
	public void handleInput() {
		
		if(whiteTurn) {
			
			if(whitePlayer.getTimeout()) gameEnd = 0;
			
			if(checkForEnd) {
				int end = whiteArmy.getGameEnd();
				
				if(end == 1) {
					gameEnd = 1;
					gameSave.addCheckmate(false);
				} else if(end == 0) {
					gameEnd = 0;
					gameSave.addDraw();
				} else {
					gameSave.completeMove();
				}
				
				checkForEnd = false;
			}
			
			if(whitePlayer.handleInput() && !whiteArmy.getPromotionActive()) {
				whiteTurn = !whiteTurn;
				this.set();
				checkForEnd = true;
			}
		} else {
			
			if(blackPlayer.getTimeout()) gameEnd = 0;
			
			if(checkForEnd) {
				int end = blackArmy.getGameEnd();
				if(blackPlayer.getTimeout()) end = 0;
				if(end == 1) {
					gameEnd = 2;
					gameSave.addCheckmate(true);
					
				} else if(end == 0) {
					gameEnd = 0;
					gameSave.addDraw();
				} else {
					gameSave.completeMove();
				}
				checkForEnd = false;
			}
			
			if(blackPlayer.handleInput() && !blackArmy.getPromotionActive()) {
				whiteTurn = !whiteTurn;
				this.set();
				checkForEnd = true;
				turns++;
			}
		}
	}
	
	public void set() {
		if(whiteTurn) whitePlayer.set();
		else blackPlayer.set();
	}
	
	public void updateSize(float windowX, float windowY, float posX, float posY) {
		this.windowX = windowX;
		this.windowY = windowY;
		this.posX = posX;
		this.posY = posY;
		
		gameBoard.updateSize(windowX, windowY, posX, posY);
		
		whiteArmy.updateSize();
		blackArmy.updateSize();
		
		for(int x = 0; x < 8; x++) {
			//whiteArmy.getPosition()[x][1].activate(whiteArmy, blackArmy, gameBoard.getIndicators());
			//blackArmy.getPosition()[x][6].activate(blackArmy, whiteArmy, gameBoard.getIndicators());
		}
		
		/*
		
		whiteArmy.getPosition()[1][0].activate(whiteArmy, blackArmy, gameBoard.getIndicators());
		whiteArmy.getPosition()[6][0].activate(whiteArmy, blackArmy, gameBoard.getIndicators());
		blackArmy.getPosition()[1][7].activate(blackArmy, whiteArmy, gameBoard.getIndicators());
		blackArmy.getPosition()[6][7].activate(blackArmy, whiteArmy, gameBoard.getIndicators());
		
		whiteArmy.getPosition()[4][0].activate(whiteArmy, blackArmy, gameBoard.getIndicators());
		blackArmy.getPosition()[4][7].activate(blackArmy, whiteArmy, gameBoard.getIndicators());
		
		whiteArmy.getPosition()[2][0].activate(whiteArmy, blackArmy, gameBoard.getIndicators());
		blackArmy.getPosition()[2][7].activate(blackArmy, whiteArmy, gameBoard.getIndicators());
		
		whiteArmy.getPosition()[5][0].activate(whiteArmy, blackArmy, gameBoard.getIndicators());
		blackArmy.getPosition()[5][7].activate(blackArmy, whiteArmy, gameBoard.getIndicators());
		
		whiteArmy.getPosition()[0][0].activate(whiteArmy, blackArmy, gameBoard.getIndicators());
		blackArmy.getPosition()[0][7].activate(blackArmy, whiteArmy, gameBoard.getIndicators());
		
		whiteArmy.getPosition()[7][0].activate(whiteArmy, blackArmy, gameBoard.getIndicators());
		blackArmy.getPosition()[7][7].activate(blackArmy, whiteArmy, gameBoard.getIndicators());
		
		*/
		
	}
	
	public void update() {
		whiteArmy.update();
		blackArmy.update();
	}
	
	public void render(SpriteBatch sb) {
		gameBoard.render(sb);
		whiteArmy.render(sb);
		blackArmy.render(sb);
	}
	
	public void shapeRender(OrthographicCamera cam) {
		
		sr.setProjectionMatrix(cam.combined);
		sr.begin();
		sr.set(ShapeType.Line);
		
		sr.rect(posX + 1, posY, windowX - 1, windowY - 1);
		sr.end();
	}
	
	public Rectangle getBounds() {
		Rectangle ret = new Rectangle();
		ret.set(posX, posY, windowX, windowY);
		return ret;
	}
	
	public void dispose() {
		//sr.dispose();
		gameBoard.dispose();
		whiteArmy.dispose();
		blackArmy.dispose();
	}
	
	public int getGameEnd() {
		if(turns >= 200) return 0;
		return this.gameEnd;
	}
}
