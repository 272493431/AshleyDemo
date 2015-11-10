package com.gdx.shaw;

import com.badlogic.gdx.Game;
import com.gdx.shaw.game.screen.GameScreen;

public class MyGame extends Game{
	
	private static MyGame myGame;
	public static MyGame getInstance(){
		return myGame;
	}
	public MyGame() {
		myGame = this;
	}

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
	
	@Override
	public void render() {
		super.render();
		
	}

}
