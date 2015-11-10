package com.gdx.shaw.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gdx.shaw.box2d.utils.LeBox2DWorld;
import com.gdx.shaw.box2d.utils.contact.CustomContactFilter;
import com.gdx.shaw.tiled.utils.TiledMapManager;
import com.gdx.shaw.utils.Constants;
import com.gdx.shaw.utils.DeBug;
import com.gdx.shaw.widget.TmxWidget;

public class TiledStage extends Stage implements Constants{
	public static Color skyblue = new Color(73,193,227,255);
	public static boolean debugBox2dRender = true;
	public int mapWidth;
	public int mapHeight;
	public int tileWidth;
	public int tileHeight;
	public float mapPixelWidth;
	public float mapPixelHeight;
	private int background [] = {0};
	private Actor sceneActor;
	private Actor backgroudActor;
	private Actor prospectActor;
	 private OrthographicCamera worldCamera;//相机
	 private Box2DDebugRenderer renderer; //物理世界调试画笔
	 private World world; //物理世界
	 
	public TiledStage() {
		super(new StretchViewport(SCREEN_GAME_WIDTH,SCREEN_GAME_HIGHT));
		getTiledMapSize();
		//物理世界的相机
		worldCamera = new OrthographicCamera();
		worldCamera.setToOrtho(false,STAGE_W,STAGE_H);
		
		renderer = new Box2DDebugRenderer();
		world = LeBox2DWorld.creatWorld(new Vector2(0, -98.1f), true);
		world.setContactFilter(new CustomContactFilter());
	}
	
	private float accumulator = 0;
	
@Override
public void act(float delta) {
	super.act(delta);
	world.step(delta, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);//比较流畅---但是在低fps的时候运动比较慢
	//更新物理世界相机
	worldCamera.update();
	
	LeBox2DWorld.worldEnd(world);
	
	moveBackground();

}
	private void moveBackground(){		//多层移动
		//底层---------底层背景永远跟随镜头移动
		if(backgroudActor != null){
			backgroudActor.setPosition(getCamera().position.x ,getCamera().position.y ,Align.center);
		}
		
		//前景层-------中间层
		if(prospectActor != null){

			float progress  = 1 - (getCamera().position.x - SCREEN_GAME_WIDTH*0.5f) / (mapPixelWidth -SCREEN_GAME_WIDTH);
			float moveX = progress * (mapPixelWidth -  prospectActor.getWidth());
			float actorX = (mapPixelHeight - prospectActor.getWidth() -  moveX);
			prospectActor.setX(Math.max(Math.min(actorX, mapPixelHeight - prospectActor.getHeight()), Math.max(0, actorX)));
		
		}
	}
	
	@Override
	public void draw() {
		 super.draw();
		 
		if(debugBox2dRender){
			//物理世界调试绘制
			 renderer.render(world, worldCamera.combined);
		}
	}
	public void getTiledMapSize(){
		//-------------------获取地图的宽高 块数 和 块的大小  ----
			
				mapWidth = TiledMapManager.mapTileWidth;
				mapHeight = TiledMapManager.mapTileHeight;
				tileWidth = TiledMapManager.tilePixelWidth;
				tileHeight =TiledMapManager.tilePixelHeight;
				mapPixelWidth =  TiledMapManager.mapPixelWidth;
				mapPixelHeight = TiledMapManager.mapPixelHeight;
				 
				DeBug.Log(getClass(),newLine+TiledMapManager.ToString());
		//------------------------------------------------------------------------
	}
	public World getWorld() {
		return world;
	}
	public OrthographicCamera getWorldCamera() {
		return worldCamera;
	}
	public void setProspectActor(Actor prospectActor) {
		addActor(prospectActor);
		prospectActor.setVisible(false);
		this.prospectActor = prospectActor;
	}
	public void setBackgroudActor(Actor backgroudActor) {
		addActor(backgroudActor);
		backgroudActor.setVisible(false);
		this.backgroudActor = backgroudActor;
	}
	public void setSceneActor(Actor sceneActor) {
		addActor(sceneActor);
//		sceneActor.setVisible(false);
		sceneActor.toBack();
		
		((TmxWidget)sceneActor).setDrawLayer(background);
		this.sceneActor = sceneActor;
	}

}
