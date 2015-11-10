package com.gdx.shaw.box2d.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.gdx.shaw.utils.Constants;

public class LeBox2DWorld implements Constants{

	public static World world;

    /**	 创建物理世界
     * @param gravity	 重力	
     * @param doSleep	为 true 时当动态物体静止时使它休眠，减少性能开销
     * @return
     */
    public static World creatWorld(Vector2 gravity, boolean doSleep){
    	World world = new World(gravity, doSleep);    
    	LeBox2DWorld.world = world;
    	return world;
    }
	
	
	public static void drawDeBug(Batch batch,ShapeRenderer shapeRenderer,Actor actor){
		batch.end();
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		shapeRenderer.rect(actor.getX(), actor.getY(),actor.getWidth(),actor.getHeight());
		shapeRenderer.end();
		
		batch.begin();
	}
	public static void drawContact(World world,ShapeRenderer shapeRenderer){

		Array<Contact> contactArray = world.getContactList();
		Iterator<Contact>cIterator = contactArray.iterator();
		while (cIterator.hasNext()) {
			Contact contact = (Contact) cIterator.next();
			if(contact.isTouching()){
		
				WorldManifold worldManifold = contact.getWorldManifold();
				for (int i = 0; i < worldManifold.getNumberOfContactPoints(); i++) {
					Vector2 vector2 = worldManifold.getPoints()[i];
					shapeRenderer.setColor(Color.RED);
					shapeRenderer.begin(ShapeType.Filled);
					shapeRenderer.circle(vector2.x * METERS_TO_PIXELS, vector2.y*METERS_TO_PIXELS, 3);
					shapeRenderer.end();
					shapeRenderer.setColor(Color.WHITE);
				}
				}
			}
		}
	
	
	private static ArrayList<Body>  freeBodies = new ArrayList<Body>();
	public static void freeBody( ArrayList<Body>  freeBodies) {
		LeBox2DWorld.freeBodies.addAll(freeBodies);
		freeBodies.clear();
	}
	public static void freeBodies(Body body) {
		if(freeBodies.contains(body))return;
		freeBodies.add(body);
	}
	public static void freeBody(World world){
		for (Body body : freeBodies) {
			if(body.getUserData() != null){
				Object object = body.getUserData();
				if(object instanceof Actor){
					((Actor)object).remove();
				}
			}
//			world.destroyBody(body);
			BodyPool.free(body);
		}
		freeBodies.clear();
	}
	
	
	private static HashMap<Body, Boolean> destoryBodiesMap = new HashMap<Body, Boolean>();
	public static void destoryBodies(Body body,boolean removeActor){
		if(destoryBodiesMap.containsKey(body))return;
		destoryBodiesMap.put(body, removeActor);
	}
	public static void destoryBodiesI(World world){
		Iterator<Body> bIterator = destoryBodiesMap.keySet().iterator();
		while (bIterator.hasNext()) {
			Body body = (Body) bIterator.next();
			if(body == null)continue;
			boolean removeActor = destoryBodiesMap.get(body);
			if(removeActor){
				if(body.getUserData() != null){
					Object object = body.getUserData();
					if(object instanceof Actor){
						((Actor)object).remove();
					}
				}
				body.setUserData(null);
			}
			world.destroyBody(body);
		}
		destoryBodiesMap.clear();
	}
	
	
//	private static ArrayList<Body>  dieBodies = new ArrayList<Body>();
//	/**	只让刚体消失但是不会让演员移除
//	 * @param body
//	 */
//	public static void dieBodies(Body body){
//		if(dieBodies.contains(body))return;
//		dieBodies.add(body);
//	}
//
//	public static void dieBodies(World world){
//		for (Body body : dieBodies) {
//			if(body == null)continue;
//			world.destroyBody(body);
//		}
//			dieBodies.clear();
//	}
//	
	
	
	private static ArrayList<Body>  destoryBodies = new ArrayList<Body>();
	public static void destoryBodies( ArrayList<Body>  destoryBodies) {
		LeBox2DWorld.destoryBodies.addAll(destoryBodies);
		destoryBodies.clear();
	}
	public static void destoryBodies(Body body) {
		if(destoryBodies.contains(body))return;
		destoryBodies.add(body);
	}
	public static void destoryBodies(World world){
		for (Body body : destoryBodies) {
			if(body == null)continue;
			if(body.getUserData() != null){
				Object object = body.getUserData();
				if(object instanceof Actor){
					((Actor)object).remove();
				}
			}
			body.setUserData(null);
			world.destroyBody(body);
		}
		destoryBodies.clear();
	}

	private static HashMap< Fixture,Body> destoryBodiesFixture = new HashMap<Fixture,Body>();
	public static void destoryBodiesFixture(Body body,Fixture fixture) {
		if(destoryBodiesFixture.containsKey(body))return;
		destoryBodiesFixture.put(fixture,body);
	}
	public static void destoryFixture(){
		Set<Fixture> keyBodies =  LeBox2DWorld.destoryBodiesFixture.keySet();
		Iterator<Fixture>iterator = keyBodies.iterator();
		while (iterator.hasNext()) {
			Fixture  fixture = iterator.next();
			Body body = LeBox2DWorld.destoryBodiesFixture.get(fixture);
			body.destroyFixture(fixture);
		}
		LeBox2DWorld.destoryBodiesFixture.clear();
	}
	
	private static ArrayList<Joint> destoryJoints = new ArrayList<Joint>();
	
	public static void destoryJoint(ArrayList<Joint> destoryJoint ) {
		LeBox2DWorld.destoryJoints.addAll(destoryJoint);
		destoryJoint.clear();
	}
	public static void destoryJoint(Joint joint) {
		LeBox2DWorld.destoryJoints.add(joint);
	}
	public static void destoryJoint(World world){
		for (Joint joint : LeBox2DWorld.destoryJoints) {
			world.destroyJoint(joint);
		}
		LeBox2DWorld.destoryJoints.clear();
	}
	private static ArrayList<Runnable> runList = new ArrayList<Runnable>();
	public static void run(Runnable runnable) {
		LeBox2DWorld.runList.add(runnable);
	}
	public static void run(){
		for (Runnable runnable : LeBox2DWorld.runList) {
			runnable.run();
		}
		LeBox2DWorld.runList.clear();
	}
	public static void worldEnd(World world){
		
		freeBody(world);
		destoryBodies(world);
		destoryBodiesI(world);
		destoryFixture();
		destoryJoint(world);
		run();
	}
	
}
