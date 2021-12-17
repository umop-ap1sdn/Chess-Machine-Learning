package com.magikman.chessai.controller;

import com.magikman.chessai.ai.GenomeNet;
import com.magikman.chessai.game.Board;
import com.magikman.chessai.pieces.Army;
import com.magikman.chessai.pieces.Piece;

public class MLController extends Controller {
	
	Army player;
	Board gameBoard;
	GenomeNet hand;
	
	int coordX;
	int coordY;
	
	final int timeLimit = 1;
	int currentTime = 0;
	boolean justPressed = false;
	
	double[] currInputs = new double[193];
	
	public MLController(Army player, Board gameBoard, GenomeNet hand) {
		super(player, gameBoard);
		
		this.player = player;
		this.gameBoard = gameBoard;
		
		this.hand = hand;
	}
	
	public MLController(GenomeNet hand) {
		this.hand = hand;
	}
	
	@Override
	public void setVars(Army player, Board gameBoard) {
		super.setVars(player, gameBoard);
		this.player = player;
		this.gameBoard = gameBoard;
	}
	@Override
	public boolean handleInput() {
		// TODO Auto-generated method stub
		
		boolean ret = false;
		currInputs = player.getMlInputs();
		
		 double[] outputs = hand.calculate(currInputs);
		 double[] firstHalf = new double[64];
		 double[] lastHalf = new double[64];
		 
		 int count = 0;
		 for(double n: outputs) {
			 if(count < 64) firstHalf[count] = n;
			 else lastHalf[count - 64] = n;
			 
			 count++;
		 }
		
		int square = getLargestOutput(firstHalf, false);
		int move = getLargestOutput(lastHalf, true);
		
		int[] squareCoords = getCoords(square);
		int[] moveCoords = getCoords(move);
		
		super.chooseSquare(squareCoords[0], squareCoords[1]);
		
		System.out.println(super.activePiece == null);
		
		ret = super.chooseSquare(moveCoords[0], moveCoords[1]);
		
		/**
		switch(action) {
		case 0:
			ret = super.chooseSquare(coordX, coordY);
			
			System.out.println(coordX + ", " + coordY);
			
			if(!ret) {
				player.createMlInputs(activePiece);
				currInputs = player.getMlInputs();
				if(activePiece != null) this.increaseFitness(0.01);
			}
			
			if(!justPressed) this.increaseFitness(0.06);
			else this.increaseFitness(-0.01);
			
			justPressed = true;
			currentTime++;
			
			break;
		case 1:
			
			if(coordY < 7) coordY++;
			justPressed = false;
			currentTime++;
			this.increaseFitness(0.01);
			
			//System.out.println(coordX + ", " + coordY);
			//System.exit(0);
			
			break;
		case 2:
			
			if(coordY > 0) coordY--;
			justPressed = false;
			currentTime++;
			increaseFitness(0.01);
			
			//System.out.println(coordX + ", " + coordY);
			//System.exit(0);
			
			break;
		case 3:
			
			if(coordX < 7) coordX++;
			justPressed = false;
			currentTime++;
			this.increaseFitness(0.01);
			
			//System.out.println(coordX + ", " + coordY);
			//System.exit(0);
			
			break;
		case 4:
			
			if(coordX > 0) coordX--;
			justPressed = false;
			currentTime++;
			this.increaseFitness(0.01);
			
			//System.out.println(coordX + ", " + coordY);
			//System.exit(0);
			
			break;
		}
		
		**/
		
		if(ret) {
			System.out.println("Successful Move");
			System.exit(0);
		} else {
			currentTime++;
			System.out.println("Unsuccessful Move");
		}
		
		return ret;
	}
	
	public int getLargestOutput(double[] outputs, boolean activeSquare) {
		double max = 0;
		int index = 0;
		
		for(int x = 0; x < outputs.length; x++) {
			int squareX = x % 8;
			int squareY = x / 8;
			
			if(outputs[x] > max) {
				
				if(gameBoard.getIndicators()[squareX][squareY].getActivate() && activeSquare) {
					index = x;
					max = outputs[x];
				} else if(!activeSquare && player.getPosition()[squareX][squareY] != null){
					
					index = x;
					max = outputs[x];
					
					
				}
			}
		}
		
		return index;
	}
	
	public int[] getCoords(int index) {
		return new int[] {index % 8, index / 8};
	}

	@Override
	public void set() {
		this.coordX = (int)player.getKingPos().x;
		this.coordY = (int)player.getKingPos().y;
		
		player.createMlInputs();
		this.currInputs = player.getMlInputs();
		
		currentTime = 0;
	}
	
	@Override
	public boolean getTimeout() {
		
		if(currentTime >= timeLimit) this.increaseFitness(-10);
		return currentTime >= timeLimit;
	}
	
	public void increaseFitness(double amount) {
		hand.increaseFitness(amount);
	}
}
