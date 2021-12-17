package com.magikman.chessai.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.magikman.chessai.game.AvailableSquare;
import com.magikman.chessai.game.Board;

public class Bishop extends Piece {
	
	public Bishop(Board gameBoard, int boardX, int boardY, char color) {
		super(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\" + color + "Bishop.png"), gameBoard, boardX, boardY, color == 'W');
		
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
		
		boolean upRightBlock = false;
		boolean downRightBlock = false;
		boolean upLeftBlock = false;
		boolean downLeftBlock = false;
		
		for(int delta = 1; delta < 8; delta++) {
			int rightX = boardX + delta;
			int leftX = boardX - delta;
			int upY = boardY + delta;
			int downY = boardY - delta;
			
			if(validLocation(rightX, upY) && !upRightBlock) {
				
				if(allyPos[rightX][upY] != null) {
					upRightBlock = true;
				} else {
					ret[rightX][upY] = true;
					
					if(oppPos[rightX][upY] != null) upRightBlock = true;
				}
			}
			
			if(validLocation(rightX, downY) && !downRightBlock) {
				
				if(allyPos[rightX][downY] != null) {
					downRightBlock = true;
				} else {
					ret[rightX][downY] = true;
					
					if(oppPos[rightX][downY] != null) downRightBlock = true;
				}
			}
			
			if(validLocation(leftX, upY) && !upLeftBlock) {
				
				if(allyPos[leftX][upY] != null) {
					upLeftBlock = true;
				} else {
					ret[leftX][upY] = true;
					
					if(oppPos[leftX][upY] != null) upLeftBlock = true;
				}
			}
			
			if(validLocation(leftX, downY) && !downLeftBlock) {
				
				if(allyPos[leftX][downY] != null) {
					downLeftBlock = true;
				} else {
					ret[leftX][downY] = true;
					
					if(oppPos[leftX][downY] != null) downLeftBlock = true;
				}
			}
		}
		
		return ret;
	}
}