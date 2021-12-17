package com.magikman.chessai.pieces;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.magikman.chessai.controller.Controller;
import com.magikman.chessai.game.Board;
import com.magikman.chessai.pgn.PgnWriter;

/**
 * 
 * @author Caleb Devon
 * 
 * Class for managing the entirety of a color's pieces
 * This class is responsible for initiating movement of pieces, adding in "special moves" such as en passant and castling
 * Additionally this class will also modify the legal moves of pieces in order to ensure the king can never be voluntarily moved in check
 *
 */
public class Army {
	
	Army opponent;
	Piece[][] position;
	Piece[][] tempPos;
	
	int kingX, kingY;
	
	//Controller controller;
	
	boolean[][] thisDangerSquares;
	boolean[][] oppDangerSquares;
	
	boolean white;
	
	Board gameBoard;
	
	ArrayList<Piece> pieceList;
	
	boolean promotionMenu;
	PromotionMenu promote;
	Pawn promotingPawn;
	
	PgnWriter gameSave;
	
	double[] mlInputs;
	
	
	//64 - currentPosition
	//2 - activated piece position
	//64 - activated piece legal moves
	//64 - player danger squares
	//64 - opponent danger squares
	//2 - current pointer location
	//1 - in check
	int mlInputSize = 64 + 64 + 64 + 1;
	
	boolean ai = false;
	
	public Army(Board gameBoard, boolean white, PgnWriter gameSave) {
		this.white = white;
		this.gameBoard = gameBoard;
		
		position = new Piece[8][8];
		tempPos = new Piece[8][8];
		pieceList = new ArrayList<>();
		
		promote = new PromotionMenu(gameBoard, this);
		promotionMenu = false;
		promotingPawn = null;
		
		this.gameSave = gameSave;
		
		mlInputs = new double[mlInputSize];
	}
	
	public void setOpponent(Army opponent) {
		this.opponent = opponent;
	}
	
	//Initialize piece in starting squares based on color
	public void setupArmy() {
		char color;
		int modifier;
		int start;
		
		if(white) {
			color = 'W';
			modifier = -1;
			start = 1;
		}
		else {
			color = 'B';
			modifier = 1;
			start = 6;
		}
		
		for(int x = 0; x < position.length; x++) {
			for(int y = 0; y < position[x].length; y++) {
				position[x][y] = null;
			}
		}
		
		for(int x = 0; x < position.length; x++) {
			position[x][start] = new Pawn(gameBoard, x, start, color);
		}
		
		start += modifier;
		position[0][start] = new Rook(gameBoard, 0, start, color);
		position[7][start] = new Rook(gameBoard, 7, start, color);
		
		position[1][start] = new Knight(gameBoard, 1, start, color);
		position[6][start] = new Knight(gameBoard, 6, start, color);
		
		position[2][start] = new Bishop(gameBoard, 2, start, color);
		position[5][start] = new Bishop(gameBoard, 5, start, color);
		
		position[3][start] = new Queen(gameBoard, 3, start, color);
		position[4][start] = new King(gameBoard, 4, start, color);
		kingX = 4;
		kingY = start;
		
		createPieceList(position);
	}
	
	//simple universal update
	public void update() {
		for(Piece n: pieceList) {
			n.update();
		}
	}
	
	//Sets a piece at a particular square to be activated
	//Returns the piece that is activated, returns null if no such piece exists
	public Piece activate(int boardX, int boardY) {
		
		boolean valid = false;
		
		boolean[][] legalMoves;
		if(position[boardX][boardY] != null) {
			
			//Legal moves is initially set to the activate method of a piece
			legalMoves = position[boardX][boardY].activate(this, opponent, true);
			valid = true;
			
			//Stringed if statements to determine if the king can castle
			if(position[boardX][boardY] instanceof King && ((King)position[boardX][boardY]).getCastle()) {
				//Queenside
				if(position[0][boardY] instanceof Rook && ((Rook)position[0][boardY]).getCastle()) {
					if(position[1][boardY] == null && position[2][boardY] == null && position[3][boardY] == null) {
						if(opponent.position[1][boardY] == null && opponent.position[2][boardY] == null && opponent.position[3][boardY] == null) {
							legalMoves[2][boardY] = true;
						}
					}
				}
				//Kingside
				if(position[7][boardY] instanceof Rook && ((Rook)position[7][boardY]).getCastle()) {
					if(position[6][boardY] == null && position[5][boardY] == null) {
						if(opponent.position[6][boardY] == null && opponent.position[5][boardY] == null) {
							legalMoves[6][boardY] = true;
						}
					}
				}
			}
			
			//Modify legal moves via new method
			legalMoves = modifyLegalMoves(boardX, boardY, legalMoves);
			
			
			//Turns on indicators for coordinates of legal moves
			for(int x = 0; x < 8; x++) {
				for(int y = 0; y < 8; y++) {
					gameBoard.getIndicators()[x][y].setActivate(legalMoves[x][y]);
				}
			}
			
		}
		
		if(valid) return position[boardX][boardY];
		
		return null;
	}
	
	//Re-evaluates moves to determine which moves are legal
	//Restricts the ability to player to allow king to remain in check
	public boolean[][] modifyLegalMoves(int boardX, int boardY, boolean[][] legalMoves){
		
		//create a copy of the current position
		this.deepCopyArray();
		opponent.deepCopyArray();
		
		
		for(int x = 0; x < legalMoves.length; x++) {
			for(int y = 0; y < legalMoves[x].length; y++) {
				
				
				if(legalMoves[x][y]) {
					
					//Perform the move in a temporary copy of position
					
					tempPos[x][y] = tempPos[boardX][boardY];
					tempPos[boardX][boardY] = null;
					
					if(tempPos[x][y] instanceof Pawn && boardX != x && opponent.tempPos[x][y] == null) {
						opponent.tempPos[x][y - ((Pawn)tempPos[x][y]).getDirection()] = null;
					}
					
					opponent.getTempPos()[x][y] = null;
					
					//Gets the position of the king (in temporary position)
					
					findKingPos(false);
					
					this.createPieceList(tempPos);
					opponent.createPieceList(opponent.tempPos);
					
					
					//Calculates what squares are "attacked" and removes the legal moves which leave the king on an attacked square
					if(opponent.calculateDangerSquares()[kingX][kingY]) legalMoves[x][y] = false;
					
					this.deepCopyArray();
					opponent.deepCopyArray();
				}
			}
		}
		
		//Separate cases for whether the king is allowed to castle
		// - Cannot castle through check, cannot castle during check, cannot castle into check
		if(position[boardX][boardY] instanceof King && ((King)position[boardX][boardY]).getCastle()) {
			this.createPieceList(tempPos);
			opponent.createPieceList(opponent.tempPos);
			
			boolean[][] danger = opponent.calculateDangerSquares();
			
			if(danger[boardX][boardY] || danger[boardX + 1][boardY]) legalMoves[boardX + 2][boardY] = false;
			if(danger[boardX][boardY] || danger[boardX - 1][boardY]) legalMoves[boardX - 2][boardY] = false;
		}
		
		this.createPieceList(position);
		opponent.createPieceList(opponent.position);
		return legalMoves;
	}
	
	//Move a piece on squares boardX boardY to destX destY
	public void move(int boardX, int boardY, int destX, int destY, boolean teleport) {
		boolean castlingK = false;
		boolean castlingQ = false;
		
		boolean takes = false;
		
		//Only necessary for the pgn writer
		int modify = this.getEqualMove(position[boardX][boardY], boardX, destX, destY);
		
		position[destX][destY] = position[boardX][boardY];
		position[boardX][boardY] = null;
		
		//Remove castling rights if rook is moved
		if(position[destX][destY] instanceof Rook) ((Rook)position[destX][destY]).removeCastle();
		if(position[destX][destY] instanceof Pawn) {
			//If a pawn is moved, it is no longer permitted to move 2 squares per turn
			((Pawn)position[destX][destY]).disableMove2();
			
			//If a pawn moves 2 squares any opponent pawns it meets is allowed to en passant that pawn
			if(Math.abs(destY - boardY) == 2) {
				if(destX < 7 && opponent.position[destX + 1][destY] instanceof Pawn) ((Pawn)opponent.position[destX + 1][destY]).setEnPassants(true, false);
				if(destX > 0 && opponent.position[destX - 1][destY] instanceof Pawn) ((Pawn)opponent.position[destX - 1][destY]).setEnPassants(false, true);
				
			}
			
			if(destX != boardX && opponent.position[destX][destY] == null) {
				opponent.position[destX][destY - ((Pawn)position[destX][destY]).getDirection()] = null;
				opponent.createPieceList(opponent.position);
				takes = true;
			}
			
			if(destY == 0 || destY == 7) {
				promotionMenu = true;
				promotingPawn = (Pawn)position[destX][destY];
			}
		}
		
		if(position[destX][destY] instanceof King) {
			//Remove king's castling rights as well as calculating whether the king is currently castling
			((King)position[destX][destY]).removeCastle();
			
			if(Math.abs(destX - boardX) == 2) {
				if(destX == 6) castlingK = true;
				else castlingQ = true;
			}
		}
		
		if(castlingK) {
			position[5][destY] = position[7][destY];
			position[7][destY] = null;
		}
		
		if(castlingQ) {
			position[3][destY] = position[0][destY];
			position[0][destY] = null;
		}
		
		if(opponent.getPosition()[destX][destY] != null) {
			opponent.getPosition()[destX][destY] = null;
			opponent.createPieceList(opponent.position);
			takes = true;
		}
		
		if(teleport) {
			position[destX][destY].teleport(destX, destY);
			
			//Moves the rook king is castling with
			if(castlingK) {
				position[5][destY].teleport(5, destY);
				gameSave.castleK(this.white);
			}
			
			if(castlingQ) {
				position[3][destY].teleport(3, destY);
				gameSave.castleQ(this.white);
			}
			
		} else {
			position[destX][destY].move(destX, destY);
			
			//Moves the rook king is castling with
			if(castlingK) {
				position[5][destY].move(5, destY);
				gameSave.castleK(this.white);
			}
			
			if(castlingQ) {
				position[3][destY].move(3, destY);
				gameSave.castleQ(this.white);
			}
		}
		
		//Write move into memory
		if(!castlingQ && !castlingK) {
			gameSave.makeMove(position[destX][destY], boardX, boardY, destX, destY, modify, takes, this.white);
		}
		
		//En passant is only available for one turn
		removeEnPassants();
	}
	
	//Calculates squares under attack based on what squares other pieces can move to
	public boolean[][] calculateDangerSquares(){
		
		boolean[][] accumulativeDanger = new boolean[8][8];
		boolean[][] dangerSquares = new boolean[8][8];
		
		accumulativeDanger = pieceList.get(0).activate(this, opponent, false);
		
		for(int index = 1; index < pieceList.size(); index++) {
			dangerSquares = pieceList.get(index).activate(this, opponent, false);
			
			for(int x = 0; x < dangerSquares.length; x++) {
				for(int y = 0; y < dangerSquares[x].length; y++) {
					accumulativeDanger[x][y] = accumulativeDanger[x][y] || dangerSquares[x][y];
				}
			}
		}
		
		return accumulativeDanger;
	}
	
	//Turns the entire position into a simplified array list with no empty values
	public void createPieceList(Piece[][] position){
		
		pieceList.clear();
		
		for(int x = 0; x < position.length; x++) {
			for(int y = 0; y < position[x].length; y++) {
				if(position[x][y] != null) pieceList.add(position[x][y]);
			}
		}
		
	}
	
	//Locates the coordinates of the king
	public void findKingPos(boolean truePos) {
		
		
		Piece[][] position;
		
		if(truePos) position = this.position;
		else position = tempPos;
		
		for(int x = 0; x < position.length; x++) {
			for(int y = 0; y < position[x].length; y++) {
				if(position[x][y] instanceof King) {
					kingX = x;
					kingY = y;
					return;
				}
			}
		}
	}
	
	//Removes the En passant as an activate-able square
	public void removeEnPassants() {
		for(Piece n: pieceList) {
			if(n instanceof Pawn) ((Pawn) n).setEnPassants(false, false);
		}
	}
	
	public void updateSize() {
		for(Piece p: pieceList) {
			p.updateSize();
		}
		
		promote.updateSize();
	}
	
	public void render(SpriteBatch sb) {
		for(Piece p: pieceList) {
			p.render(sb);
		}
		
		if(promotionMenu) {
			promote.render(sb);
		}
	}
	
	public Piece[][] getPosition(){
		return this.position;
	}
	
	//Creates a copy of the array in a way that creates 2 unique lists
	public void deepCopyArray() {
		for(int x = 0; x < position.length; x++) {
			for(int y = 0; y < position[x].length; y++) {
				tempPos[x][y] = position[x][y];
			}
		}
	}
	
	//Checks the player for any legal moves
	//If any exist, returns -1;
	//If none then will return 1 if the king is in check to indicate checkmate, or 0 to indicate stalemate
	public int getGameEnd() {
		
		boolean[][] legalMoves;
		createPieceList(position);
		opponent.createPieceList(opponent.position);
		for(int index = 0; index < pieceList.size(); index++) {
			
			legalMoves = pieceList.get(index).activate(this, opponent, true);
			legalMoves = modifyLegalMoves(pieceList.get(index).boardX, pieceList.get(index).boardY, legalMoves);
			createPieceList(position);
			opponent.createPieceList(opponent.position);
			
			for(int x = 0; x < legalMoves.length; x++) {
				for(boolean y: legalMoves[x]) {
					if(y) {
						findKingPos(true);
						if(opponent.calculateDangerSquares()[kingX][kingY]) gameSave.addCheck(this.white);
						return -1;
					}
				}
			}
		}
		
		findKingPos(true);
		if(opponent.calculateDangerSquares()[kingX][kingY]) {
			
			legalMoves = position[kingX][kingY].activate(this, opponent, true);
			legalMoves = modifyLegalMoves(kingX, kingY, legalMoves);
			
			for(int index = 0; index < 8; index++) {
				for(boolean a: legalMoves[index])
					if(a) return -1;
			}
			
			return 1;
		} else {
			return 0;
		}
		
	}
	
	//Checks to see if another piece can move to the same square (important for pgn writing)
	public int getEqualMove(Piece moving, int startX, int destX, int destY) {
		boolean[][] legalMoves;
		
		int mod1 = 0;
		int mod2 = 0;
		
		for(int index = 0; index < pieceList.size(); index++) {
			if(pieceList.get(index).getClass() == moving.getClass() && (pieceList.get(index).boardX != moving.boardX || pieceList.get(index).boardY != moving.boardY)) {
				
				legalMoves = pieceList.get(index).activate(this, opponent, true);
				legalMoves = this.modifyLegalMoves(pieceList.get(index).boardX, pieceList.get(index).boardY, legalMoves);
				
				if(legalMoves[destX][destY]) {
					
					if(pieceList.get(index).boardX == startX) mod2 = 2;
					else mod1 = 1;
				}
			}
		}
		
		return mod1 + mod2;
	}
	
	//Determines the decision of a pawn promotion
	//0 = Rook, 1 = Knight, 2 = Bishop, 4 = Queen
	public int setPromotionDecision(int boardX, int boardY) {
		
		int decision = promote.getChoice(boardX, boardY);
		char color = 'W';
		if(!white) color = 'B';
		
		int posX = promotingPawn.boardX;
		int posY = promotingPawn.boardY;
		
		switch(decision) {
		case 0:
			position[posX][posY] = new Rook(gameBoard, posX, posY, color);
			break;
		case 1:
			position[posX][posY] = new Knight(gameBoard, posX, posY, color);
			break;
		case 2:
			position[posX][posY] = new Bishop(gameBoard, posX, posY, color);
			break;
		case 3:
			position[posX][posY] = new Queen(gameBoard, posX, posY, color);
			break;
		}
		
		if(decision != -1) {
			this.createPieceList(position);
			promotionMenu = false;
			promotingPawn = null;
			
			gameSave.addPromotion(decision);
		}
		
		return decision;
	}
	
	public void createMlInputs() {
		for(int index = 0; index < 64; index++) {
			int boardX = index % 8;
			int boardY = index / 8;
			
			Piece test = position[boardX][boardY];
			if(test == null) mlInputs[index] = 0;
			else if(test instanceof Pawn) mlInputs[index] = 1;
			else if(test instanceof Rook) mlInputs[index] = 2;
			else if(test instanceof Knight) mlInputs[index] = 3;
			else if(test instanceof Bishop) mlInputs[index] = 4;
			else if(test instanceof Queen) mlInputs[index] = 5;
			else if(test instanceof King) mlInputs[index] = 6;
			
			if(test != null && !test.color) mlInputs[index] *= -1;
			
		}
		
		boolean[][] playerDanger = calculateDangerSquares();
		boolean[][] oppDanger = opponent.calculateDangerSquares();
		
		for(int index = 64; index < 128; index++) {
			int posX = (index - 64) % 8;
			int posY = (index - 64) / 8;
			
			int index2 = index + 64;
			
			if(playerDanger[posX][posY]) mlInputs[index] = 1;
			else mlInputs[index] = 0;
			if(oppDanger[posX][posY]) mlInputs[index2] = 1;
			else mlInputs[index2] = 0;
		}
		this.findKingPos(true);
		
		if(oppDanger[kingX][kingY]) mlInputs[mlInputs.length - 1] = 1;
		else mlInputs[mlInputs.length - 1] = 0;
	}
	
	//Getters
	
	public Vector2 getKingPos() {
		return new Vector2(kingX, kingY);
	}
	
	public Piece[][] getTempPos(){
		return this.tempPos;
	}
	
	public ArrayList<Piece> getPieceList(){
		return this.pieceList;
	}
	
	public boolean getPromotionActive() {
		return this.promotionMenu;
	}
	
	public double[] getMlInputs() {
		return this.mlInputs;
	}
	
	public void dispose() {
		for(Piece n: pieceList) {
			n.dispose();
		}
		
		promote.dispose();
	}
}
