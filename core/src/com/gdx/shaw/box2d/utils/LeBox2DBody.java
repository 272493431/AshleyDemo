package com.gdx.shaw.box2d.utils;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.gdx.shaw.box2d.utils.contact.CollisionRule;
import com.gdx.shaw.utils.Constants;

public class LeBox2DBody implements Constants{

    /** 移动刚体
     * @param body 被移动的刚体
     * @param dir	 方向速度
     */
    public static void moveBodyApplyLinearImpulse(Body body ,Vector2 dir){
		
    	Vector2 vector2 = body.getLinearVelocity();
		float mass = body.getMass();// the mass of the body
		Vector2 desiredVector2 = new Vector2(dir);  //will move to
		Vector2 changeVector2 = desiredVector2.sub(vector2); //the change
		Vector2 impulseVector2 = changeVector2.scl(mass); //impulse
		body.applyLinearImpulse(impulseVector2, body.getWorldCenter(), true);//applyLinearImpulse
		
    }
    
    public static Body createBox(Actor actor,FixtureInfo filter ,BodyType bodyType,boolean fromPool){
    	actor.setOrigin(Align.center);
    	float w =  actor.getWidth();
		float h =  actor.getHeight();

		float x =  actor.getX()  + w *0.5f; // 物理世界中的锚点是在中心 作为演员要进行偏移
		float y = actor.getY()  + h *0.5f; // 物理世界中的锚点是在中心 作为演员要进行偏移
		float angle =  actor.getRotation() * MathUtils.degreesToRadians;
		return createBox( x,y,w,h ,angle, filter, bodyType,fromPool);
    }
    
    
    /**	包围着舞台的刚体
     * @param world
     * @param stage
     */
    public static void createWrapWall(World world,Stage stage ){
    float w = stage.getWidth();
    float h = stage.getHeight();
    float wallThick = 10f;
    createBox( w/2, 0, w , wallThick,null, BodyType.StaticBody,false);
    createBox( w/2, h, w , wallThick, null,BodyType.StaticBody,false);
    createBox( 0f, h/2, wallThick, h ,null, BodyType.StaticBody,false);
    createBox( w  , h/2, wallThick, h ,null, BodyType.StaticBody,false);
    }
    
	
	public static Stage getBodyStage(Body body){
		if(body.getUserData() != null){
			Object object = body.getUserData();
			if(object instanceof Actor){
				return ((Actor)object).getStage();
			}
		}
		return null;
	}
	

	
	public static Body createBox(float pixX,float pixY,float pixW,float pixH,FixtureInfo fixtureInfo,BodyType bodyType,boolean fromPool){
		return createBox(pixX, pixY, pixW, pixH, 0, fixtureInfo, bodyType,fromPool);
	}
	public static Body createBox(float pixX,float pixY,float pixW,float pixH,float angle,FixtureInfo fixtureInfo,BodyType bodyType,boolean fromPool){
		if(fixtureInfo == null){
			fixtureInfo = new FixtureInfo();
		}
//		Body body = getEmptyBody(pixX, pixY,angle, bodyType);
		Body body = fromPool? BodyPool.obtain(pixX,pixY,bodyType) : getEmptyBody(pixX, pixY,angle, bodyType);
		PolygonShape polygonShape = LeBox2DShape.createBox(pixW, pixH);
		FixtureDef fixtureDef = getFixtureDef(polygonShape,fixtureInfo);
		body.createFixture(fixtureDef).setUserData(fixtureInfo.userData);;
		
		polygonShape = null;
		fixtureDef = null;
		return body;
	}
	
	public static Body createCircle(float pixX,float pixY,float pixR,FixtureInfo fixtureInfo,BodyType bodyType,boolean fromPool){
		if(fixtureInfo == null){
			fixtureInfo = new FixtureInfo();
		}
//		Body body = getEmptyBody(pixX, pixY, bodyType);
		Body body = fromPool ? BodyPool.obtain(pixX,pixY,bodyType) : getEmptyBody(pixX, pixY, bodyType);
		CircleShape  circleShape = LeBox2DShape.createCircle(pixR);
		FixtureDef fixtureDef = getFixtureDef(circleShape,fixtureInfo);
		body.createFixture(fixtureDef).setUserData(fixtureInfo.userData);
		circleShape = null;
		fixtureDef = null;
		return body;
	}

	public static Fixture createFixture(Body body,Shape shape,String fixtureName){
		FixtureDef fixtureDef = LeBox2DBody.getFixtureDef(shape, fixtureName);
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData(CollisionRule.findFixtureFilter(fixtureName));
		fixtureDef = null;
		return fixture;
	}
	public static FixtureDef getFixtureDef(Shape shape,String fixtureName){
		return getFixtureDef(shape, new FixtureInfo(fixtureName));
	}
	
	public static FixtureDef getFixtureDef(Shape shape,FixtureInfo fixtureInfo){
		if(fixtureInfo == null){
			fixtureInfo = new FixtureInfo();
		}
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = fixtureInfo.density;
		fixtureDef.friction = fixtureInfo.friction;
		fixtureDef.restitution = fixtureInfo.restitution;
		fixtureDef.isSensor = fixtureInfo.isSensor;
		
		fixtureDef.filter.categoryBits = fixtureInfo.filter.categoryBits;
		fixtureDef.filter.groupIndex = fixtureInfo.filter.groupIndex;
		fixtureDef.filter.maskBits = fixtureInfo.filter.maskBits;
		return fixtureDef;
	}
	
	public static Body getEmptyBody(float pixX, float pixY , BodyType bodyType) {
		return getEmptyBody(pixX, pixY, 0, bodyType);
	}
	public static Body getEmptyBody(float pixX, float pixY,float angle, BodyType bodyType) {
		Vector2 position = pixPos2MeterPos(pixX, pixY);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(position.x, position.y);
		bodyDef.angle = angle;
		return LeBox2DWorld.world.createBody(bodyDef);
	}
	
	public static Vector2 pixSize2MeterSize(float pixW, float pixH){
		float w =  pixW * PIXELS_TO_METERS * 0.5f; // 不知道为什么还要缩小一半
		float h =  pixH * PIXELS_TO_METERS * 0.5f; // 不知道为什么还要缩小一半
		
		return new Vector2(w,h);
	}
	
	public static Vector2 pixPos2MeterPos(float pixX, float pixY){
		float x = pixX * PIXELS_TO_METERS;
		float y = pixY * PIXELS_TO_METERS;
		return new Vector2(x, y);
	}
	
	
}
