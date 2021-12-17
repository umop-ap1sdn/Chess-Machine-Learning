package com.magikman.chessai.pgn;

import java.io.File;
import java.util.Formatter;

import com.magikman.chessai.controller.MLController;
import com.magikman.chessai.pieces.*;

public class PgnWriter {
	
	Formatter fileWrite;
	File workSpace;
	int turn;
	MLController whitePlayer = null;
	MLController blackPlayer = null;
	
	public PgnWriter() {
		workSpace = new File("games");
		workSpace.mkdirs();
		this.turn = 1;
		
		int index = 0;
		workSpace = new File("games\\Game" + index + ".pgn");
		while(workSpace.exists()) {
			index++;
			workSpace = new File("games\\Game" + index + ".pgn");
		}
		
		//workSpace.mkdirs();
		
		try { 
			fileWrite = new Formatter(workSpace);
		} catch (Exception e) {
			
		}
	}
	
	public void addMlController(MLController player, boolean white) {
		if(white) whitePlayer = player;
		else blackPlayer = player;
	}
	
	public void makeMove(Piece moving, int startX, int startY, int destX, int destY, int idenModify, boolean takes, boolean white) {
		
		char modifier = ' ';
		
		char[] numLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
		
		if(moving instanceof Pawn && takes) modifier = numLetter[startX];
		if(moving instanceof Rook) modifier = 'R';
		if(moving instanceof Bishop) modifier = 'B';
		if(moving instanceof Queen) modifier = 'Q';
		if(moving instanceof Knight) modifier = 'N';
		if(moving instanceof King) modifier = 'K';
		
		if(white) {
			fileWrite.format("%d.", turn);
			turn++;
			
			if(whitePlayer != null) whitePlayer.increaseFitness(50);
		} else if(blackPlayer != null) blackPlayer.increaseFitness(50);
		
		if(modifier != ' ') fileWrite.format("%s", modifier);
		
		if(!(moving instanceof Pawn)) {
			if(idenModify == 1) fileWrite.format("%s", numLetter[startX]);
			if(idenModify == 2) fileWrite.format("%s", startY + 1);
			if(idenModify == 3) fileWrite.format("%s%s", numLetter[startX], startY + 1);
		}
		
		if(takes) {
			fileWrite.format("%s", 'x');
			
			if(whitePlayer != null) {
				whitePlayer.increaseFitness(100);
				if(blackPlayer != null) blackPlayer.increaseFitness(-40);
			}
			if(blackPlayer != null) {
				blackPlayer.increaseFitness(100);
				if(whitePlayer != null) whitePlayer.increaseFitness(-40);
			}
		}
		
		fileWrite.format("%s%d", numLetter[destX], destY + 1);
		
	}
	
	public void castleK(boolean white) {
		if(white) {
			fileWrite.format("%d.", turn);
			turn++;
			
			if(whitePlayer != null) whitePlayer.increaseFitness(80);
		} else if(blackPlayer != null) blackPlayer.increaseFitness(80);
		
		fileWrite.format("%s", "O-O");
	}
	
	public void castleQ(boolean white) {
		if(white) {
			fileWrite.format("%d.", turn);
			turn++;
			
			if(whitePlayer != null) whitePlayer.increaseFitness(65);
		} else if(blackPlayer != null) blackPlayer.increaseFitness(65);
		
		fileWrite.format("%s", "O-O-O");
	}
	
	public void addCheck(boolean white) {
		fileWrite.format("%s", '+');
		
		if(white) {
			if(whitePlayer != null) {
				whitePlayer.increaseFitness(80);
				if(blackPlayer != null) blackPlayer.increaseFitness(-20);
			}
		} else {
			if(blackPlayer != null) {
				blackPlayer.increaseFitness(80);
				if(whitePlayer != null) whitePlayer.increaseFitness(-20);
			}
		}
	}
	
	public void addCheckmate(boolean white) {
		fileWrite.format("%s ", '#');
		
		if(white) {
			fileWrite.format("%s", "1-0");
			if(whitePlayer != null) whitePlayer.increaseFitness(1000);
		}
		else {
			fileWrite.format("%s", "0-1");
			
			if(blackPlayer != null) blackPlayer.increaseFitness(1000);
		}
		
		fileWrite.close();
	}
	
	public void addDraw() {
		this.completeMove();
		fileWrite.format("%s", "1/2-1/2");
		
		if(whitePlayer != null) whitePlayer.increaseFitness(350);
		if(blackPlayer != null) blackPlayer.increaseFitness(400);
		
		
		fileWrite.close();
	}
	
	public void addPromotion(int type) {
		switch(type) {
		case 0:
			fileWrite.format("%s", "=R");
			break;
		case 1:
			fileWrite.format("%s", "=N");
			break;
		case 2:
			fileWrite.format("%s", "=B");
			break;
		case 3:
			fileWrite.format("%s", "=Q");
			break;
			
		}
	}
	
	public void completeMove() {
		fileWrite.format("%s", ' ');
	}
	
	public void close() {
		fileWrite.close();
	}
}
 