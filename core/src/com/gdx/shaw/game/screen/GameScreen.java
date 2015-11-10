package com.gdx.shaw.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.shaw.box2d.utils.LeBox2DWorld;
import com.gdx.shaw.box2d.utils.contact.CollisionRule;
import com.gdx.shaw.game.CollisionListener;
import com.gdx.shaw.game.TiledStage;
import com.gdx.shaw.game.UpdateCamera;
import com.gdx.shaw.game.data.Bool;
import com.gdx.shaw.game.data.FixtureName;
import com.gdx.shaw.game.data.TiledLayers;
import com.gdx.shaw.game.manager.MapObjectGenerator;
import com.gdx.shaw.game.object.OneWay;
import com.gdx.shaw.game.player.PlayerActor;
import com.gdx.shaw.game.player.PlayerController;
import com.gdx.shaw.res.Le;
import com.gdx.shaw.tiled.utils.TerrainCollisionGenerator;
import com.gdx.shaw.tiled.utils.TiledMapManager;
import com.gdx.shaw.utils.LeShapeRenderDebug;

public class GameScreen  extends ScreenAdapter{

	/**地图舞台*/public TiledStage tiledStage;
	/**玩家控制器舞台*/public PlayerController playerController;
	/**更新相机*/public UpdateCamera updateCamera;
	public World world;
	 
	 public PlayerActor playerActor;
	
	 
	 @Override
	public void show() {
	
		TiledMapManager.loadLevel(Le.tmx.mapForest0);
		LeShapeRenderDebug.init();
		tiledStage  = new TiledStage();
		
		
		MapObjectGenerator mapObjectGenerator = new MapObjectGenerator(this);
		
		TerrainCollisionGenerator terrainCollisionGenerator = new TerrainCollisionGenerator();
		terrainCollisionGenerator.addWall(TiledMapManager.tiledMap,TiledLayers.TILEDMAP_TERRAIN);
		terrainCollisionGenerator.createPhysics(TiledMapManager.tiledMap,TiledLayers.TILEDMAP_TERRAIN,
				CollisionRule.findFixtureFilter(FixtureName.groundFixture));
		terrainCollisionGenerator.createPhysics(TiledMapManager.tiledMap,TiledLayers.TILEDMAP_ONEWAYTERRAIN, 
				CollisionRule.findFixtureFilter(FixtureName.oneWayGroundFixture),new OneWay());
		
		playerActor = mapObjectGenerator.createPlayer();
		mapObjectGenerator.createObject();
		playerController = new PlayerController(this);
		updateCamera = new UpdateCamera(this);
		
		CollisionListener collisionListener = new CollisionListener(playerActor);
		LeBox2DWorld.world.setContactListener(collisionListener);
		
		Gdx.input.setInputProcessor(new InputMultiplexer(playerController));
	 }
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		updateCamera.update(delta,false);

		tiledStage.act();
		tiledStage.draw();

		playerController.act();
		playerController.draw();
		
		LeShapeRenderDebug.adapter(tiledStage);

		if(Bool.renderTiledMapTile){
			LeShapeRenderDebug.drawTiledMapTile();
		}
		 
		LeShapeRenderDebug.setColor(Color.TEAL);
		
		
		update(delta);
	}
	public void update(float delta) {
		playerActor.update(delta);
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}
	@Override
	public void pause() {
		
		
	}
	@Override
	public void resume() {
		
		
	}
	@Override
	public void resize(int width, int height) {
		tiledStage.getViewport().update(width, height, true);
		playerController.getViewport().update(width, height, true);
	}
	@Override
	public void hide() {

	}


}