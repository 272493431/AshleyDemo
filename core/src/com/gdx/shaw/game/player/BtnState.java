package com.gdx.shaw.game.player;

/**
 *	按钮状态  
 */
public class BtnState {

	boolean isBtnPressing;
	boolean isGameRuning;
	
	public void btnDown(){
		isBtnPressing = true;
		isGameRuning = true;
	}
	public void btnUp(){
		isBtnPressing = false;
		isGameRuning = false;
	}
	
	/**
	 * 按钮还未抬起的时候恢复游戏更新
	 */
	public void resume(){
		if(isBtnPressing){
			isGameRuning = true;
		}	
	}

}
