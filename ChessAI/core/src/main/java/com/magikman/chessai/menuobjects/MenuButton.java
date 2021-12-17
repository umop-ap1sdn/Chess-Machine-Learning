package com.magikman.chessai.menuobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.magikman.chessai.Resources;

public class MenuButton {
	
	Texture[] imgs;
	Sprite button;
	
	String text;
	//Texture img;
	
	BitmapFont fnt;
	GlyphLayout gl;
	
	float textX, textY;
	
	public MenuButton(String text, float cenX, float cenY) {
		this.text = text;
		this.imgs = new Texture[2];
		imgs[0] = new Texture("C:\\Users\\Owner\\LibGDX Projects\\Paper Thin\\assets\\menuButton.png");
		imgs[1] = new Texture("C:\\Users\\Owner\\LibGDX Projects\\Paper Thin\\assets\\mouseOverButton.png");
		button = new Sprite(imgs[0]);
		
		float scale = (Resources.height / 8) / button.getHeight();
		button.setSize(button.getWidth() * scale, button.getHeight() * scale);
		button.setCenter(cenX, cenY);
		
		fnt = new BitmapFont();
		fnt.setColor(Color.BLACK);
		fnt.getData().setScale(3f);
		gl = new GlyphLayout();
		gl.setText(fnt, text);
		
		textX = cenX - gl.width / 2;
		textY = cenY + gl.height / 2;
	}
	
	public boolean handleInput(OrthographicCamera cam) {
		
		Vector3 touchPos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		float touchX = touchPos.x;
		float touchY = touchPos.y;
		if(button.getBoundingRectangle().contains(touchX, touchY)) {
			button.setTexture(imgs[1]);
			
			if(Gdx.input.justTouched()) return true;
			
		} else {
			button.setTexture(imgs[0]);
		}
		
		return false;
	}
	
	public void render(SpriteBatch sb) {
		button.draw(sb);
		fnt.draw(sb, gl, textX, textY);
	}
	
	public void dispose() {
		fnt.dispose();
		imgs[0].dispose();
		imgs[1].dispose();
	}
}
