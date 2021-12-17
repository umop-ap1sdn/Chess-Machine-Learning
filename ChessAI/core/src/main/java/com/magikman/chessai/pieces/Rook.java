package com.magikman.chessai.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.magikman.chessai.game.Board;

public class Rook extends Piece {
	
	boolean allowCastle;
	
	public Rook(Board gameBoard, int boardX, int boardY, char color) {
		super(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\" + color + "Rook.png"), gameBoard, boardX, boardY, color == 'W');
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
		
		boolean upBlock = false;
		boolean downBlock = false;
		boolean leftBlock = false;
		boolean rightBlock = false;
		
		for(int delta = 1; delta < 8; delta++) {
			
			int right = boardX + delta;
			int left = boardX - delta;
			int up = boardY + delta;
			int down = boardY - delta;
			
			if(validLocation(right, boardY) && !rightBlock) {
				
				if(allyPos[right][boardY] != null) rightBlock = true;
				else {
					ret[right][boardY] = true;
					
					if(oppPos[right][boardY] != null) rightBlock = true;
				}
				
			}
			
			if(validLocation(left, boardY) && !leftBlock) {
				
				if(allyPos[left][boardY] != null) leftBlock = true;
				else {
					ret[left][boardY] = true;
					
					if(oppPos[left][boardY] != null) leftBlock = true;
				}
			}
			
			if(validLocation(boardX, up) && !upBlock) {
				
				if(allyPos[boardX][up] != null) upBlock = true;
				else {
					ret[boardX][up] = true;
					
					if(oppPos[boardX][up] != null) upBlock = true;
				}
			}
			
			if(validLocation(boardX, down) && !downBlock) {
				
				if(allyPos[boardX][down] != null) downBlock = true;
				else {
					ret[boardX][down] = true;
					
					if(oppPos[boardX][down] != null) downBlock = true;
				}
			}
		}
		
		return ret;
	}
	
	public void removeCastle() {
		allowCastle = false;
	}
	
	public boolean getCastle() {
		return this.allowCastle;
	}
}
