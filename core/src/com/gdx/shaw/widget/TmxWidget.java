package com.gdx.shaw.widget;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gdx.shaw.tiled.utils.TiledMapManager;

public class TmxWidget extends Group{
	public int mapTileWidth;
	public int mapTileHeight;
	public int mapPixelWidth;
	public int mapPixelHeight;
	public int tilePixelWidth;
	public int tilePixelHeight;
	int [] layers;
	private TiledMap tiledMap;
	private OrthoCachedTiledMapRenderer mMapRenderer;
	private OrthographicCamera camera = new OrthographicCamera();
	private boolean begin = false;
	public TmxWidget(String filePath) {
		this(new TmxMapLoader().load(filePath,TiledMapManager.parameters));
	}
	public TmxWidget(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
		mapChange();
	}
	public void setDrawLayer(int[] layer) {
		this.layers = layer;
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(batch != null){
			batch.end();
		}

		begin();
		if(layers == null){
			mMapRenderer.render();
		}else {
			draw(layers);
		}
		end();
	
		if(batch != null){
			batch.begin();
		}
	}
	public void begin(){
		if(begin) throw new IllegalStateException("TmxWidget.end must be called before begin.");
		begin = true;
		mMapRenderer.setView(camera);
	}
	public void draw(int[] layers) {
		if(!begin) throw new IllegalStateException("TmxWidget.begin must be called before draw.");
			mMapRenderer.render(layers);
	}
	public void end(){
		if(!begin) throw new IllegalStateException("TmxWidget.begin must be called before end.");
		begin = false;
		camera.update();
	}
	public void draw() {
		mMapRenderer.setView(camera);
		mMapRenderer.render();
		camera.update();
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		cameraUpdate();
	}
	private void cameraUpdate(){
		OrthographicCamera stageCamera = (OrthographicCamera) getStage().getCamera();
		float stageCameraX = stageCamera.position.x - stageCamera.viewportWidth*0.5f;
		float stageCameraY = - stageCamera.position.y + stageCamera.viewportHeight*0.5f;
		camera.zoom = stageCamera.zoom;
		camera.setToOrtho(false, stageCamera.viewportWidth, stageCamera.viewportHeight);
		camera.position.x = stageCameraX - getX() + camera.viewportWidth*0.5f;
		camera.position.y = - stageCameraY - getY()  + camera.viewportHeight*0.5f;
		camera.update();
	}
	
	protected void mapChange() {
		MapProperties properties = tiledMap.getProperties();
		mapTileWidth = properties.get("width", Integer.class);
		mapTileHeight = properties.get("height", Integer.class);
		tilePixelWidth = properties.get("tilewidth", Integer.class);
		tilePixelHeight = properties.get("tileheight", Integer.class);
		mapPixelWidth = mapTileWidth * tilePixelWidth;
		mapPixelHeight = mapTileHeight * tilePixelHeight;
		setSize(mapPixelWidth, mapPixelHeight);
		if(mMapRenderer != null){
			mMapRenderer.dispose();
			mMapRenderer = null;
		}
		mMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
		mMapRenderer.setOverCache(0.5f);
		mMapRenderer.setMaxTileSize(512,512);
		mMapRenderer.setBlending(true);
		
	}
	public void setTiledMap(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
		mapChange();
	}
	@Override
	protected void setStage(Stage stage) {
		super.setStage(stage);
		if(stage!=null){
			OrthographicCamera stageCamera = (OrthographicCamera) stage.getCamera();
			this.camera.setToOrtho(true, stageCamera.viewportWidth, stageCamera.viewportHeight);
		}
	}
	@Override
	public void clear() {
		super.clear();
		tiledMap.dispose();
	}
}
