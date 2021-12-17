package com.magikman.chessai.controller;

import com.magikman.chessai.game.Board;
import com.magikman.chessai.pieces.*;


/**
 * 
 * @author Caleb Devon
 * 
 * Abstract Class for different control methods
 * This class is responsible for handling inputs it receives from its subclasses
 * and normalizing them all to allows for equivalent values for a multitude of input
 * methods.
 * 
 * Planned controlling types include:
 * Mouse/Touch Screen Controller
 * DPad + Selector Controller
 * Various input methods from machine/AI models
 *
 */
public abstract class Controller {
	
	Army player;
	Piece activePiece;
	
	boolean teleport;
	
	Board gameBoard;
	
	//Primary constructor contains the player and gameboard
	public Controller(Army player, Board gameBoard) {
		this.player = player;
		this.gameBoard = gameBoard;
		activePiece = null;
		
		teleport = false;
	}
	
	//Use this constructor when the important objects have not been initialized yet
	public Controller() {
		activePiece = null;
		teleport = false;
	}
	
	//If using the secondary constructor this method must be called before using any controller functions
	public void setVars(Army player, Board gameBoard) {
		this.player = player;
		this.gameBoard = gameBoard;
	}
	
	//Called by subclasses - This method is responsible for performing the action on a particular square
	//Returns true only when a move is successfully made, activating a piece still returns false
	public boolean chooseSquare(int boardX, int boardY) {
		
		//getPromotionActive() refers to when a player is deciding a which piece to promote to after moving their pawn to the end of the board (Promotion menu is open)
		if(!player.getPromotionActive()) {
			
			//Detects whether the selected square has been activated, indicating a legal move has been selected completing the turn
			if(gameBoard.getIndicators()[boardX][boardY].getActivate()) {
				player.move(activePiece.getLocation()[0], activePiece.getLocation()[1], boardX, boardY, teleport);
				activePiece = null;
				clearIndicators();
				
				return true;
			}
			
			//If the input isn't placed on an activated square, it will instead activate the piece on the input square
			//If the square is empty activePiece will just be set to 'null'
			activePiece = player.activate(boardX, boardY);
			
			if(activePiece == null) {
				clearIndicators();
			}
		} else {
			
			//If the promotion menu is active it will instead evaluate the promotion decision on the input square
			if(player.setPromotionDecision(boardX, boardY) != -1) return true;
		}
		
		return false;
	}
	
	//This allows pieces to be manually dragged across the screen
	//Mainly used as an additional input for touch screen/mouse and merely serves as overall polish
	public void dynamicControl(float cenX, float cenY) {
		if(activePiece != null) {
			activePiece.setSpPos(cenX, cenY);
		}
	}
	
	//Turns off all active squares
	public void clearIndicators() {
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				gameBoard.getIndicators()[x][y].setActivate(false);
			}
		}
	}
	
	public boolean getTimeout() {
		return false;
	}
	
	//Abstract methods that define the differences between controlling methods
	public abstract boolean handleInput();
	public abstract void set();
}
