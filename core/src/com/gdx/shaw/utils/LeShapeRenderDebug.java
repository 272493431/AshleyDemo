package com.gdx.shaw.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gdx.shaw.tiled.utils.TiledMapManager;

public class LeShapeRenderDebug {
	
	public static boolean debug = true;
	public static ShapeRenderer shapeRenderer;
	public static void init(){
		shapeRenderer = new ShapeRenderer();
	}
	public static void adapter(Stage stage){
		if(!debug)return;
		shapeRenderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
		shapeRenderer.setTransformMatrix(stage.getBatch().getTransformMatrix());
	}
	public static void drawTiledMapTile(){
		if(!debug)return;
		begin(ShapeType.Line);
		float tile = TiledMapManager.tilePixelHeight;
		shapeRenderer.setColor(Color.PURPLE);
		for (int i = 0; i < TiledMapManager.mapPixelWidth / tile; i++) {
			for (int j = 0; j <  TiledMapManager.mapPixelHeight/tile; j++) {
				shapeRenderer.rect(i*tile, j*tile, tile, tile);
			}
		}
		end();
	}
	public static  void setColor(Color color) {
		if(!debug)return;
		shapeRenderer.setColor(color);
	}
	public static  void setColor(float r,float g,float b,float a) {
		if(!debug)return;
		shapeRenderer.setColor(r, g, b, a);
	}
	public static void begin(ShapeType type){
		if(!debug)return;
		if(!shapeRenderer.isDrawing()){
			shapeRenderer.begin(type);
		}else {
			shapeRenderer.end();
			shapeRenderer.begin(type);
		}
	}
	public static void end() {
		if(!debug)return;
		if(shapeRenderer.isDrawing()){
			shapeRenderer.end();
		}
	};
}
