package com.gdx.shaw.box2d.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gdx.shaw.utils.Constants;

public class LeBox2DShape implements Constants{
	
	
	public static PolygonShape createBox(float pixW,float pixH,Vector2 position,float angle){

		Vector2 size = LeBox2DBody.pixSize2MeterSize(pixW, pixH);
		Vector2 pos = LeBox2DBody.pixPos2MeterPos(position.x, position.y);
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(size.x,size.y,pos,angle);
		return polygonShape;
	}
	
	public static PolygonShape createBox(float pixW,float pixH){
		Vector2 size = LeBox2DBody.pixSize2MeterSize(pixW, pixH);
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(size.x,size.y);
		return polygonShape;
	}
	
	
	public static CircleShape createCircle(float pixR,Vector2 position){
	 	CircleShape circleShape = createCircle(pixR);
	 	Vector2 pos = LeBox2DBody.pixPos2MeterPos(position.x, position.y);
	 	circleShape.setPosition(pos);
	 	return circleShape;
	}
	
	public static CircleShape createCircle(float pixR){
	 	float r =  LeBox2DBody.pixSize2MeterSize(pixR, 0).x; // 不知道为什么还要缩小一半
	 	CircleShape circleShape = new CircleShape();
	 	circleShape.setRadius(r);
	 	return circleShape;
	}
}
