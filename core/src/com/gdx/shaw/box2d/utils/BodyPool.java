package com.gdx.shaw.box2d.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.gdx.shaw.box2d.utils.LeBox2DBody;
import com.gdx.shaw.box2d.utils.LeBox2DWorld;
import com.gdx.shaw.utils.DeBug;

public class BodyPool {
	private static Array<Body> freeObjects = new Array<Body>();
	private static int max = 50;
	public static void init(int num){
		for (int i = 0; i < num; i++) {
			free( LeBox2DBody.getEmptyBody(-999, -999, BodyType.KinematicBody));
		}
		DeBug.Log(BodyPool.class,"初始化完成："+freeObjects.size);
	}
	public static Body obtain(){
		return obtain(-999, -999, BodyType.KinematicBody);
	}
	public static Body obtain(float pixX,float pixY,BodyType bodyType){
		DeBug.Log(BodyPool.class,"刚体池:获取实例："+freeObjects.size);
		Body body =  freeObjects.size > 0 ? freeObjects.removeIndex(0) : LeBox2DBody.getEmptyBody(-999, -999,  BodyType.KinematicBody);
		Vector2 vector2 = LeBox2DBody.pixPos2MeterPos(pixX, pixY);
		body.setTransform(vector2.x, vector2.y, 0);
		body.setType(bodyType);
		return body;
	}
	public static void free(Body body){
		if(body.getJointList().size > 0 || freeObjects.size >= max){
			DeBug.Log(BodyPool.class,"刚体池:销毁刚体："+freeObjects.size);
			LeBox2DWorld.world.destroyBody(body);
			return;
		}
		Array<Fixture> fixtures = body.getFixtureList();
		for (Fixture fixture : fixtures) {
			body.destroyFixture(fixture);
		}
		
		body.setTransform(-998,-998, 0);
		body.setSleepingAllowed(true);
		
		body.setBullet(false);
		body.setUserData(null);
		body.setLinearVelocity(0, 0);
		body.setGravityScale(1);
		body.setAngularVelocity(0);
		body.setType(BodyType.StaticBody);
		if(freeObjects.contains(body, true))return;
		freeObjects.add(body);
		DeBug.Log(BodyPool.class,"刚体池:释放刚体："+freeObjects.size);
	}
	public static void clear(){
		freeObjects.clear();
	}
	public int getFree () {
		return freeObjects.size;
	}
}
