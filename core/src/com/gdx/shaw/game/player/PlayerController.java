package com.gdx.shaw.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gdx.shaw.game.data.Bool;
import com.gdx.shaw.game.player.PlayerActor.Dir;
import com.gdx.shaw.game.screen.GameScreen;
import com.gdx.shaw.utils.Constants;

public class PlayerController extends Stage implements InputProcessor,  Constants{
	
	 PlayerActor playerActor;
	public PlayerController(GameScreen gameScreen) {
		super(new StretchViewport(SCREEN_UI_WIDTH,SCREEN_UI_HIGHT));
		playerActor = gameScreen.playerActor;
	}
	@Override
    public boolean keyDown(int keyCode) {
    	switch (keyCode) {

		case Keys.A:
		case Keys.LEFT:
			playerActor.dir = Dir.left;
			break;
		case Keys.D:
		case Keys.RIGHT:
			playerActor.dir = Dir.right;
			break;
			
		case Keys.W:
		case Keys.UP:
			if(playerActor.canJump()){
				playerActor.jump = true;
			}
			break;
		case Keys.ESCAPE:
			Gdx.app.exit();
			break;
			
		case Keys.T:
			Bool.renderTiledMapTile = !Bool.renderTiledMapTile;
			break;
		default:
			break;
		}
    	return super.keyDown(keyCode);
    }
    @Override
    public boolean keyUp(int keyCode) {
    	switch (keyCode) {

		
		case Keys.W:
		case Keys.UP:
			playerActor.jump = false;
			break;
		case Keys.A:
		case Keys.D:
		case Keys.LEFT:
		case Keys.RIGHT:
			playerActor.dir = Dir.idle;
			break;

		default:
			break;

		}
		return super.keyUp(keyCode);
    }
    
    
    
}
