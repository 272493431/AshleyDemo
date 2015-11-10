package com.gdx.shaw.game.manager;


import java.util.Iterator;

import javax.tools.Tool;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gdx.shaw.box2d.utils.LeBox2DWorld;
import com.gdx.shaw.game.data.TiledLayers;
import com.gdx.shaw.game.object.AbstractBox2DActor;
import com.gdx.shaw.game.object.MovePlatform;
import com.gdx.shaw.game.player.PlayerActor;
import com.gdx.shaw.game.screen.GameScreen;
import com.gdx.shaw.tiled.utils.TiledMapManager;
import com.gdx.shaw.utils.Tools;

public class MapObjectGenerator{
	
	TiledMap tiledMap;
	World world;
	GameScreen gameScreen;
	
	public MapObjectGenerator(GameScreen gameScreen) {
		
		this.gameScreen = gameScreen;
		tiledMap = TiledMapManager.tiledMap;
		world = LeBox2DWorld.world;
	}
	
	public PlayerActor createPlayer(){
		MapLayer layer = tiledMap.getLayers().get(TiledLayers.TILEDMAP_PLAYER);
		PlayerActor playerActor = new PlayerActor((TextureMapObject) layer.getObjects().get(0));
		return playerActor;
	}
	
	public void createObject(){
		MapLayers mapLayers = tiledMap.getLayers();
		Iterator<MapLayer> lIterator = mapLayers.iterator();
		while (lIterator.hasNext()) {
			MapLayer mapLayer = (MapLayer) lIterator.next();
			String name = mapLayer.getName();
			System.out.println(name);
			switch (name) {
			case TiledLayers.TILEDMAP_MOVEPLATFORM:
				createObject(mapLayer, MovePlatform.class);
				break;

			default:
				break;
			}
		}
	}

	private void createObject(MapLayer mapLayer,Class<?> clazz){
		Iterator<MapObject> objectIterator = mapLayer.getObjects().iterator();
		while (objectIterator.hasNext()) {
			MapObject object = (MapObject) objectIterator.next();
				gameScreen.tiledStage.addActor((Actor) Tools.newInstance(clazz, object));
		}
	}
	private void createObject(MapLayer mapLayer,CreateObject createObject){
		Iterator<MapObject> objectIterator = mapLayer.getObjects().iterator();
		while (objectIterator.hasNext()) {
			MapObject object = (MapObject) objectIterator.next();
			Actor actor = createObject.createObject(object);
			if(actor != null){
				gameScreen.tiledStage.addActor(actor);;
			}
		}
	}
	//#end ---- 

	private interface CreateObject{
		public Actor createObject(MapObject mapObject);
	}
}
