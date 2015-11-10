package com.gdx.shaw.tiled.utils;


import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.gdx.shaw.utils.DeBug;

public class TiledMapManager   {
    public static int mapTileWidth;
    public static int mapTileHeight;
    public static int mapPixelWidth;
    public static int mapPixelHeight;
    public static int tilePixelWidth;
    public static int tilePixelHeight;
    public static TiledMap tiledMap;
    private static String cacheFilePath; 
	public static Parameters parameters = new Parameters();
    public static void loadLevel(String filePath) {
		long start = System.currentTimeMillis();
		
    	parameters.textureMinFilter = TextureFilter.Linear;
    	parameters.textureMagFilter = TextureFilter.Linear;
    	
    	
    	cacheFilePath = filePath;
    
        tiledMap = new TmxMapLoader().load(filePath,parameters);
    	DeBug.Log(TiledMapManager.class,"----加载地图开始-----------："+filePath);
        // get level width/height in both tiles and pixels and hang on to the values
        MapProperties properties = tiledMap.getProperties();
        mapTileWidth = properties.get("width", Integer.class);
        mapTileHeight = properties.get("height", Integer.class);
        tilePixelWidth = properties.get("tilewidth", Integer.class);
        tilePixelHeight = properties.get("tileheight", Integer.class);
        mapPixelWidth = mapTileWidth * tilePixelWidth;
        mapPixelHeight = mapTileHeight * tilePixelHeight;
        
    	long end = System.currentTimeMillis();
    	DeBug.Log(TiledMapManager.class,"----加载地图结束----------");
		DeBug.Log(TiledMapManager.class,"加载地图时间："+(end  - start)+"  加载的地图："+cacheFilePath);
		
    }

    public static void choose(String filePath){
    	cacheFilePath = filePath;
    }
    public static void reLoad(){
    	loadLevel(cacheFilePath);
    }
    
	public static void dispose() {
		tiledMap.dispose();
		tiledMap = null;
	}
	
    public static String ToString() {
    	return  "[地图宽: "+mapTileWidth+",地图高:"+mapTileHeight+"]\n"
    				+"[图块宽:"+tilePixelWidth+",图块高:"+tilePixelHeight+"]\n"
    				+"[地图像素宽："+mapPixelWidth+"高："+mapPixelHeight+"]";
    }
}
