package com.gdx.shaw.tiled.utils;

/**
 * Created by Phil on 1/10/2015.
 */
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.gdx.shaw.box2d.utils.LeBox2DWorld;
import com.gdx.shaw.utils.Constants;

public class TerrainCollisionGenerator  implements Constants{
    private World world;
    private Array<Body> bodies = new Array<Body>();
    public TerrainCollisionGenerator() {
        this.world = LeBox2DWorld.world;
    }

    public void addWall(TiledMap tiledMap,String layerName){
    	MapLayer layer = tiledMap.getLayers().get(layerName);
    	MapProperties properties = tiledMap.getProperties();
    	float mapTileWidth = properties.get("width", Integer.class);
    	float mapTileHeight = properties.get("height", Integer.class);
    	float tilePixelWidth = properties.get("tilewidth", Integer.class);
    	float tilePixelHeight = properties.get("tileheight", Integer.class);
    	float mapPixelWidth = mapTileWidth * tilePixelWidth;
    	float mapPixelHeight = mapTileHeight * tilePixelHeight;
    	
    	{
    		float [] vertices = {
        			0,0,mapPixelWidth,0
        	};
        	PolylineMapObject polylineMapObject = new PolylineMapObject(vertices);
        	layer.getObjects().add(polylineMapObject);
    	}
    	{
    		float [] vertices = {
        			0,mapPixelHeight,mapPixelWidth,mapPixelHeight
        	};
        	PolylineMapObject polylineMapObject = new PolylineMapObject(vertices);
        	layer.getObjects().add(polylineMapObject);
    	}
    }
    public void createPhysics(TiledMap tiledMap,String layerName,Object fixtureUserObject) {
    	createPhysics(tiledMap, layerName,fixtureUserObject, layerName);
    }
    public void createPhysics(TiledMap tiledMap,String layerName,Object fixtureUserData,Object userData) {
    	MapLayer layer = tiledMap.getLayers().get(layerName);
    	createPhysics(layer,fixtureUserData,userData);
    }
    public void createPhysics(MapLayer layer,Object fixtureUserData,Object bodyUserData) {
      
        MapObjects objects = layer.getObjects();
        Iterator<MapObject> objectIt = objects.iterator();
        while(objectIt.hasNext()) {
            TerrainGeometry geometry = null;
            MapObject object = objectIt.next();
         
            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.awake = false;
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if (object instanceof RectangleMapObject) {
                geometry = getRectangle((RectangleMapObject)object);
                shape = geometry.getShape();
            }
            else if (object instanceof PolygonMapObject) {
                geometry = getPolygon((PolygonMapObject)object);
                shape = geometry.getShape();
            }
            else if (object instanceof PolylineMapObject) {
            	
                geometry = getPolyline((PolylineMapObject)object);
                shape = geometry.getShape();
            }
            else if (object instanceof CircleMapObject) {
            	
                geometry = getCircle((CircleMapObject)object);
                shape = geometry.getShape();
            }
            else {
                Gdx.app.log("Unrecognized shape", "" + object.toString());
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.friction = 1f;
            Body body = world.createBody(bodyDef);
            body.setUserData(bodyUserData);
            
            // All collisions need an entity, and all entities need a type to handle collisions
            body.createFixture(fixtureDef).setUserData(fixtureUserData);;
            
            bodies.add(body);
            
            fixtureDef.shape = null;
            shape.dispose();
        }
    }
    
    public void destroyPhysics() {
        for (Body body : bodies) {
            world.destroyBody(body);
        }

        bodies.clear();
    }

    private TerrainGeometry getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * PIXELS_TO_METERS,
                (rectangle.y + rectangle.height * 0.5f ) * PIXELS_TO_METERS);
        polygon.setAsBox(rectangle.width * 0.5f * PIXELS_TO_METERS,
                rectangle.height * 0.5f * PIXELS_TO_METERS,
                size,
                0.0f);
        return new TerrainGeometry(polygon);
    }

    private TerrainGeometry getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius * PIXELS_TO_METERS);
        circleShape.setPosition(new Vector2(circle.x * PIXELS_TO_METERS, circle.y * PIXELS_TO_METERS));
        return new TerrainGeometry(circleShape);
    }

    private TerrainGeometry getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] * PIXELS_TO_METERS;
        }

        polygon.set(worldVertices);
        return new TerrainGeometry(polygon);
    }
    
    private TerrainGeometry getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] * PIXELS_TO_METERS;
            worldVertices[i].y = vertices[i * 2 + 1] * PIXELS_TO_METERS;
        }

        
        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return new TerrainGeometry(chain);
    }

    
    
    private static class TerrainGeometry /*implements Collidable*/ {
        private Shape shape;

        public TerrainGeometry(Shape shape) {
            this.shape = shape;
        }

        public Shape getShape() {
            return shape;
        }

//        @Override
//        public void handleSensorCollision(short categoryBits, boolean beginCollision) {
//
//        }
//
//        @Override
//        public void handleCollision(Object object) {
//        }
    }


}
