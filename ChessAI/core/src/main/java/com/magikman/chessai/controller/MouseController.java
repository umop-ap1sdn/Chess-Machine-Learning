package com.magikman.chessai.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.magikman.chessai.game.Board;
import com.magikman.chessai.pieces.Army;

/**
 * 
 * @author Caleb Devon
 * Basic mouse controller
 * This allows the game to be controlled by click piece -> click square
 * or by dragging the desired piece to the desired square
 * 
 *
 */
public class MouseController extends Controller implements InputProcessor {
	
	boolean select;
	boolean drag;
	
	int boardX, boardY;
	
	OrthographicCamera cam;
	
	//Primary constructor includes the superclass objects and an orthographic camera
	//The camera is used to translate between real pixels and the camera view port
	public MouseController(Army player, Board gameBoard, OrthographicCamera cam) {
		super(player, gameBoard);
		
		select = false;
		drag = false;
		
		boardX = -1;
		boardY = -1;
		
		this.cam = cam;
	}
	
	//Secondary constructor for when player and gameboard are not yet initialized
	//Must call setVars() before using any functions of this class
	public MouseController(OrthographicCamera cam) {
		select = false;
		drag = false;
		
		boardX = -1;
		boardY = -1;
		
		this.cam = cam;
	}
	
	@Override
	public void setVars(Army player, Board gameBoard) {
		super.setVars(player, gameBoard);
	}
	
	@Override
	public void set() {
		Gdx.input.setInputProcessor(this);
	}
	
	//Selects a square based on which square is clicked on by the mouse or touchscreen
	@Override
	public boolean handleInput() {
		
		boolean ret = false;
		if(select) {
			if(boardX != -1 && boardY != -1) {
				
				if(activePiece != null) activePiece.updateSize();
				
				//Calls the chooseSquare method and saves the return to the result of chooseSquare
				ret = super.chooseSquare(boardX, boardY);
				select = false;
				
			}
		}
		
		return ret;
	}
	
	//Finds which square coordinate contains contains the pixel coordinate (touchX, touchY)
	//sets (-1, -1) if no such square exists
	public void getBoardCoords(int touchX, int touchY) {
		boardX = -1;
		boardY = -1;
		
		for(int x = 0; x < gameBoard.getSquares().length; x++) {
			for(int y = 0; y < gameBoard.getSquares()[x].length; y++) {
				if(gameBoard.getSquares()[x][y].contains(touchX, touchY)) {
					boardX = x;
					boardY = y;
					return;
				}
			}
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		//Translates the touch coordinates into screen coordinates based on the camera
		Vector3 realPos = cam.unproject(new Vector3(screenX, screenY, 0));
		
		//Translates the true coordinates into a square on the board
		//Sets boardX and boardY to -1 if the touch position is not a valid square
		getBoardCoords((int)realPos.x, (int)realPos.y);
		
		select = true;
		
		//sets the piece to allow dragging
		drag = true;
		teleport = false;
		//System.out.println(boardX + " " + boardY);
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		//Primarily used to drop a piece that is currently being dragged
		if(activePiece != null) {
			select = true;
			
			Vector3 realPos = cam.unproject(new Vector3(screenX, screenY, 0));
			
			int oldBoardX = boardX;
			int oldBoardY = boardY;
			
			getBoardCoords((int)realPos.x, (int)realPos.y);
			
			//Keeps the piece activated if it's not moved to an active square
			if(!gameBoard.getIndicators()[boardX][boardY].getActivate()) {
				boardX = oldBoardX;
				boardY = oldBoardY;
			}
			
			//sets the piece to be teleported to the target square, instead of going through an entire animation after being dragged
			teleport = true;
		}
		//disables dragging once click is released
		drag = false;
		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		Vector3 realPos = cam.unproject(new Vector3(screenX, screenY, 0));
		dynamicControl(realPos.x, realPos.y);
		
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

}
