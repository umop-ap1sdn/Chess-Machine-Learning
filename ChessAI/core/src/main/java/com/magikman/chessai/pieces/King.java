package com.magikman.chessai.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.magikman.chessai.game.AvailableSquare;
import com.magikman.chessai.game.Board;

public class King extends Piece {
	
	boolean allowCastle;
	
	public King(Board gameBoard, int boardX, int boardY, char color) {
		super(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\" + color + "King.png"), gameBoard, boardX, boardY, color == 'W');
		allowCastle = true;
	}

	@Override
	public boolean[][] activate(Army ally, Army opponent, boolean truePos) {
		
		Piece[][] allyPos;
		Piece[][] oppPos;
		
		if(truePos) {
			allyPos = ally.getPosition();
			oppPos = opponent.getPosition();
		} else {
			allyPos = ally.getTempPos();
			oppPos = opponent.getTempPos();
		}
		
		boolean[][] ret = new boolean[8][8];
		
		for(int x = 0; x < ret.length; x++) {
			for(int y = 0; y < ret[x].length; y++) {
				ret[x][y] = false;
			}
		}
		
		for(int deltaX = -1; deltaX <= 1; deltaX++) {
			for(int deltaY = -1; deltaY <= 1; deltaY++) {
				
				if(deltaX == 0 && deltaY == 0) continue;
				
				int x = boardX + deltaX;
				int y = boardY + deltaY;
				
				if(validLocation(x, y) && allyPos[x][y] == null) ret[x][y] = true;
			}
		}
		
		return ret;
	}
	
	public boolean getCastle() {
		return this.allowCastle;
	}
	
	public void removeCastle() {
		allowCastle = false;
	}
}