package com.gdx.shaw.game.object;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gdx.shaw.box2d.utils.FixtureInfo;
import com.gdx.shaw.box2d.utils.LeBox2DBody;

public abstract class AbstractBox2DActor extends Actor{
	Body body;
	
	public AbstractBox2DActor(MapObject mapObject) {
		createBody(mapObject);
	}
	
	protected abstract String getFixtureName();
	protected void createBody(MapObject object){
		
		if(object instanceof RectangleMapObject){
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
			
			body = LeBox2DBody.createBox(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new FixtureInfo(getFixtureName()), BodyType.KinematicBody, false);
			
		}
	}
	
	
}
