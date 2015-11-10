package com.gdx.shaw.box2d.utils.contact;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.gdx.shaw.box2d.utils.LeBox2DWorldListener;
import com.gdx.shaw.game.able.Collisionable;
import com.gdx.shaw.game.player.PlayerActor;

public  class ContactClassification {
	public static enum ContactState{
		beginContact,endContact,preSolve,postSolve	
	}
	public static void begin(Contact contact){
		logic(contact, ContactState.beginContact);
	}
	public static void end(Contact contact){
		logic(contact, ContactState.endContact);
	}
	public static void logic(Contact contact,ContactState state){
		Body	tempBodies [] = {contact.getFixtureA().getBody(),contact.getFixtureB().getBody()};
		
		for (Body body : tempBodies) {
			Object userData = body.getUserData();
			
			if(userData instanceof Collisionable){
				Class<?> clazz = userData.getClass();
				Body [] bodies = LeBox2DWorldListener.sortByOneBody(contact, clazz);
				
				if(bodies == null)return;
				Body bodyA = bodies[0];
				Body bodyB =  bodies[1];
				Collisionable propsBasis = (Collisionable) bodyA.getUserData();
				Object object = bodyB.getUserData();
				propsBasis.collision(contact,state);
				if(object != null){
					if(object instanceof PlayerActor){//与玩家碰撞
						PlayerActor playerActor  = (PlayerActor) object;
						propsBasis.collision(contact,state,playerActor);
					}else {//与其他对象碰撞
						propsBasis.collision(contact,state,object);
					}
				}
			}
		}
	}

}
