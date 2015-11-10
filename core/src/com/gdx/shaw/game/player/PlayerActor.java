package com.gdx.shaw.game.player;


import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gdx.shaw.box2d.utils.LeBox2DBody;
import com.gdx.shaw.box2d.utils.LeBox2DShape;
import com.gdx.shaw.box2d.utils.contact.ContactClassification.ContactState;
import com.gdx.shaw.game.able.Collisionable;
import com.gdx.shaw.tiled.utils.LeMapProperties;

import static com.gdx.shaw.utils.Constants.*;
import static com.gdx.shaw.game.data.FixtureName.*;
public class PlayerActor  implements Collisionable{

	public static class Dir{
		public static final int idle = 0;	
		public static final int left = -1;	
		public static final int right = 1;
	}
	
	public static enum HealthState{
		 normal ,	//正常
		 hurt ,	//受伤
		 niubility ,	//无敌
		 change ,	//改变大小
		 death 	//死亡
	}
	
	public static enum State{
		onAir,	//空中
		onGround	//地面
	}
	
	public int numFoot;
	public int cacheDir;
	public int dir;
	public boolean jump = false;
	
	public float PIXELS_WIDTH;
	public float PIXELS_HIGHT;
	
	private final float velocityCache = 12;
	private float velocity = velocityCache;
	
	private final float velocityJumpCache = 40;
	private float velocityJump = velocityJumpCache;
	
	private float speed = 300;
	public Body body;
	public State state = State.onGround;
	
	Vector2 movement = new Vector2();
	public PlayerActor(TextureMapObject textureMapObject) {
		 PIXELS_WIDTH =  LeMapProperties.getFloat(textureMapObject, "width", 50);
		 PIXELS_HIGHT =  LeMapProperties.getFloat(textureMapObject, "height", 50);
		 PIXELS_WIDTH =  textureMapObject.getTextureRegion().getRegionWidth();
		 PIXELS_HIGHT = 	 textureMapObject.getTextureRegion().getRegionHeight();
		float pixX = LeMapProperties.getFloat(textureMapObject, "x", 0);
		float pixY = LeMapProperties.getFloat(textureMapObject, "y", 0);
		
		body = 	getBody(pixX, pixY);
		body.setFixedRotation(true);
		body.setUserData(this);
	}
	
	
	public Body getBody(float pixX,float pixY){
		Body body = LeBox2DBody.getEmptyBody(pixX + PIXELS_WIDTH*0.5f, pixY + PIXELS_HIGHT*0.5f,BodyType.DynamicBody);
		
		PolygonShape bodyPolygonShape = LeBox2DShape.createBox(PIXELS_WIDTH, PIXELS_HIGHT);
		PolygonShape footSensorShape = LeBox2DShape.createBox(10,15,new Vector2(0,-PIXELS_WIDTH*0.5f - PIXELS_HIGHT*0.5f),0);
		
		CircleShape footCircleShape = LeBox2DShape.createCircle(PIXELS_WIDTH,new Vector2(0,-PIXELS_HIGHT*0.5f));
		
		LeBox2DBody.createFixture(body, bodyPolygonShape, playerBodyFixture).setFriction(0);;
		LeBox2DBody.createFixture(body,footCircleShape, playerDownFixture);
		LeBox2DBody.createFixture(body,footSensorShape, playerDownSensorFixture).setSensor(true);;
		
		
		return body;
	}
	
	
	public void update(float delta){
		 state = (numFoot > 0)?State.onGround : State.onAir;
		 
		move();
	}	
	
	public void move(){
//		movement.set(jump? velocityJump : body.getLinearVelocity().x , dir * velocity);
		movement.set(dir * velocity,jump? velocityJump : body.getLinearVelocity().y);
		jump = false;
		LeBox2DBody.moveBodyApplyLinearImpulse(body, movement);
		
	}
	public boolean canJump(){
		return state == State.onGround;
	}
	
	@Override
	public void collision(Contact contact, ContactState state,
			PlayerActor playerActor) {
		
	}

	@Override
	public void collision(Contact contact, ContactState state, Object notPlayer) {
		
	}
	
	@Override
	public void collision(Contact contact, ContactState state) {
		
	}
	

}
