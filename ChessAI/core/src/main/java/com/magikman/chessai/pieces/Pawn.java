package com.magikman.chessai.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.magikman.chessai.game.AvailableSquare;
import com.magikman.chessai.game.Board;

public class Pawn extends Piece {
	
	boolean canMove2;
	boolean lEnPassant, rEnPassant;
	
	int direction;
	
	public Pawn(Board gameBoard, int boardX, int boardY, char color) {
		super(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\" + color + "Pawn.png"), gameBoard, boardX, boardY, color == 'W');
		
		canMove2 = true;
		
		lEnPassant = false;
		rEnPassant = false;
		
		if(color == 'W') direction = 1;
		else direction = -1;
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
		
		if(allyPos[boardX][boardY + direction] == null && oppPos[boardX][boardY + direction] == null) {
			ret[boardX][boardY + direction] = true;
			
			if(canMove2 && allyPos[boardX][boardY + direction * 2] == null && oppPos[boardX][boardY + direction * 2] == null) {
				ret[boardX][boardY + direction * 2] = true;
			}
		}
		
		if((validLocation(boardX + 1, boardY + direction) && oppPos[boardX + 1][boardY + direction] != null) || rEnPassant) ret[boardX + 1][boardY + direction] = true;
		
		if((validLocation(boardX - 1, boardY + direction) && oppPos[boardX - 1][boardY + direction] != null) || lEnPassant) ret[boardX - 1][boardY + direction] = true;
		
		
		return ret;
	}
	
	public int getDirection() {
		return this.direction;
	}
	
	public void setEnPassants(boolean left, boolean right) {
		this.lEnPassant = left;
		this.rEnPassant = right;
	}
	
	public void disableMove2() {
		canMove2 = false;
	}
}
