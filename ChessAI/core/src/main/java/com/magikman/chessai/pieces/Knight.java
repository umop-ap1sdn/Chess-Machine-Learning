package com.magikman.chessai.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.magikman.chessai.game.AvailableSquare;
import com.magikman.chessai.game.Board;

public class Knight extends Piece {
	
	public Knight(Board gameBoard, int boardX, int boardY, char color) {
		super(new Texture("C:\\Users\\Owner\\LibGDX Projects\\ChessAI\\assets\\" + color + "Knight.png"), gameBoard, boardX, boardY, color == 'W');
		
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
		
		for(int delta1 = -2; delta1 <= 2; delta1 += 4) {
			for(int delta2 = -1; delta2 <= 1; delta2 += 2) {
				int x1 = boardX + delta1;
				int y1 = boardY + delta2;
				
				int x2 = boardX + delta2;
				int y2 = boardY + delta1;
				
				if(validLocation(x1, y1) && allyPos[x1][y1] == null) ret[x1][y1] = true;
				if(validLocation(x2, y2) && allyPos[x2][y2] == null) ret[x2][y2] = true;
			}
		}
		
		return ret;
	}
}