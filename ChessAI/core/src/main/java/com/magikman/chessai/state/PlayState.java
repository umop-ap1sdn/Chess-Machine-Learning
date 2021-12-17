package com.magikman.chessai.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.magikman.chessai.Resources;
import com.magikman.chessai.ai.Population;
import com.magikman.chessai.controller.Controller;
import com.magikman.chessai.controller.MLController;
import com.magikman.chessai.controller.MouseController;
import com.magikman.chessai.game.Game;

public class PlayState extends State{

	
	//Game test;
	OrthographicCamera cam;
	
	float prevWidth, prevHeight;
	
	Controller[] whitePlayers;
	Controller[] blackPlayers;
	
	Game[] gameList;
	
	int sizeX = 1;
	int sizeY = 1;
	
	int inputsSize = 64 + 64 + 64 + 1;
	
	int populationSize = sizeX * sizeY;
	
	Population whiteTeam;
	Population blackTeam;
	
	boolean ai = false;
	
	public PlayState(Manager gsm) {
		super(gsm);
		cam = new OrthographicCamera();
		cam.viewportHeight = Resources.height;
		cam.viewportWidth = Resources.width;
		cam.update();
		
		
		whiteTeam = new Population(inputsSize, 128, populationSize, true);
		blackTeam = new Population(inputsSize, 128, populationSize, true);
		
		//Vector3 realCoords = cam.unproject(new Vector3(0, Resources.height, 0));
		
		whitePlayers = new Controller[populationSize];
		blackPlayers = new Controller[populationSize];
		
		for(int x = 0; x < populationSize; x++) {
			
			if(ai) {
				whitePlayers[x] = new MLController(whiteTeam.getGenome(x));
				blackPlayers[x] = new MLController(blackTeam.getGenome(x));
			} else {
				whitePlayers[x] = new MouseController(cam);
				blackPlayers[x] = new MouseController(cam);
			}
		}
		
		gameList = new Game[populationSize];
		
		//test = new Game(whitePlayers[0], blackPlayers[0], Resources.width / 2, Resources.height / 2, realCoords.x, realCoords.y, cam);
		
		initGames();
	}
	
	public void initGames() {
		Vector3 realCoords = cam.unproject(new Vector3(0, Resources.height, 0));
		
		//gameList[0] = new Game(whitePlayers[0], blackPlayers[0], Resources.width / 2, Resources.height / 2, realCoords.x, realCoords.y, cam);
		
		for(int x = 0; x < sizeX; x++) {
			for(int y = 0; y < sizeY; y++) {
				int index = sizeY * x + y;
				gameList[index] = new Game(whitePlayers[index], blackPlayers[index], Resources.width / sizeX, Resources.height / sizeY, realCoords.x + (x * Resources.width / sizeX), realCoords.y + (y * Resources.height / sizeY), cam);
				
			}
		}
	}
	
	public void initMLControllers() {
		for(int index = 0; index < populationSize; index++) {
			whitePlayers[index] = new MLController(whiteTeam.getGenome(index));
			blackPlayers[index] = new MLController(blackTeam.getGenome(index));
		}
	}

	@Override
	public void update(float dt) {
		
		if(prevHeight != Gdx.graphics.getHeight() || prevWidth != Gdx.graphics.getWidth()) {
			resize();
		}
		
		prevHeight = Gdx.graphics.getHeight();
		prevWidth = Gdx.graphics.getWidth();
		
		/*
		test.update();
		
		if(test.getGameEnd() != -1) {
			super.setState(new MenuState(gsm));
		}
		*/
		
		for(int index = 0; index < gameList.length; index++) {
			gameList[index].update();
		}
		
		if(checkEnd()) {
			if(!ai) super.setState(new MenuState(gsm));
			else {
				
				System.out.println(whiteTeam.getGeneration());
				
				whiteTeam.breed();
				blackTeam.breed();
				initMLControllers();
				dispose();
				initGames();
				
			}
		}
	}
	
	public boolean checkEnd() {
		for(int index = 0; index < gameList.length; index++) {
			if(gameList[index].getGameEnd() == -1) return false;
		}
		
		return true;
	}
	
	public void resize() {
		cam.viewportHeight = Gdx.graphics.getHeight();
		cam.viewportWidth = Gdx.graphics.getWidth();
		
		cam.update();
		
		Vector3 realCoords = cam.unproject(new Vector3(0, cam.viewportHeight, 0));
		
		//test.updateSize(cam.viewportWidth / 2, cam.viewportHeight / 2, realCoords.x, realCoords.y);
		//gameList[0].updateSize(cam.viewportWidth / 2, cam.viewportHeight / 2, realCoords.x, realCoords.y);
		for(int x = 0; x < sizeX; x++) {
			for(int y = 0; y < sizeY; y++) {
				int index = x * sizeY + y;
				gameList[index].updateSize(cam.viewportWidth / sizeX, cam.viewportHeight / sizeY, realCoords.x + (x * (cam.viewportWidth / sizeX)), realCoords.y + (y * (cam.viewportHeight / sizeY)));
			}
		}
	}

	@Override
	public void handleInput() {
		//test.handleInput();
		
		
		for(int index = 0; index < gameList.length; index++) {
			if(gameList[index].getGameEnd() == -1) gameList[index].handleInput();
		}
		
		Vector3 pointSelect = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		if(!ai) {
			for(int index = 0; index < gameList.length; index++) {
				if(gameList[index].getBounds().contains(pointSelect.x, pointSelect.y)) gameList[index].set();
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
			super.setState(new MenuState(gsm));
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		
		for(int index = 0; index < gameList.length; index++) {
			if(gameList[index].getGameEnd() == -1) gameList[index].render(sb);
			
		}
		
		
		sb.end();
		
		for(int index = 0; index < gameList.length; index++) {
			gameList[index].shapeRender(cam);
		}
	}

	@Override
	public void dispose() {
		//test.dispose();
		for(int index = 0; index < gameList.length; index++) {
			gameList[index].dispose();
		}
	}
}
