package com.magikman.chessai.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.magikman.chessai.game.Board;

public class Queen extends Piece {
	
	public Queen(Board gameBoard, int boardX, int boardY, char color) {
		super(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\" + color + "Queen.png"), gameBoard, boardX, boardY, color == 'W');
		
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