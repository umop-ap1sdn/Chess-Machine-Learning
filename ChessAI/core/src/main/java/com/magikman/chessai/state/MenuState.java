package com.magikman.chessai.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.magikman.chessai.Resources;
import com.magikman.chessai.menuobjects.MenuButton;

public class MenuState extends State{
	
	OrthographicCamera cam;
	MenuButton startButton;
	//MenuButton options;
	
	public MenuState(Manager gsm) {
		super(gsm);
		cam = new OrthographicCamera(Resources.width, Resources.height);
		
		startButton = new MenuButton("Start", 0, 0);
		//options = new MenuButton("Options", 0, -150);
		
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		cam.viewportWidth = Gdx.graphics.getWidth();
		cam.viewportHeight = Gdx.graphics.getHeight();
		
		
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if(startButton.handleInput(cam)) {
			super.setState(new PlayState(gsm));
		}
		
	}

	@Override
	public void render(SpriteBatch sb) {
		// TODO Auto-generated method stub
		cam.update();
		sb.setProjectionMatrix(cam.combined);
		
		sb.begin();
		startButton.render(sb);
		sb.end();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		startButton.dispose();
	}
	
	
}
