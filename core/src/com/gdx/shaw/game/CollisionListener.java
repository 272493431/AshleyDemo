package com.gdx.shaw.game;


import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
//import com.gdx.shaw.box2d.module.OneWayPlatform;
import com.gdx.shaw.box2d.utils.contact.ContactClassification;
import com.gdx.shaw.box2d.utils.contact.FixtureFilter;
import com.gdx.shaw.box2d.utils.contact.ContactClassification.ContactState;
import com.gdx.shaw.game.data.FixtureName;
import com.gdx.shaw.game.player.PlayerActor;
import com.gdx.shaw.utils.Constants;
import com.gdx.shaw.utils.DeBug;

public class CollisionListener implements ContactListener, Constants{
	
	private PlayerActor playerActor;
	
	public CollisionListener(PlayerActor playerActor) {
		this.playerActor = playerActor;
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		ContactClassification.logic(contact, ContactState.preSolve);
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		ContactClassification.logic(contact, ContactState.postSolve);
	}
	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		onGround(fixtureA, fixtureB,true);
		ContactClassification.begin(contact);
	
		
	}
	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		onGround(fixtureA, fixtureB,false);
		ContactClassification.end(contact);

	}
	private void onGround(Fixture fixtureA,Fixture fixtureB,boolean begin){
		String player = nullString;
		
		DeBug.Log(getClass(),(begin?"开始":"结束  ")+" 碰撞 ："+getFixtureName(fixtureB)+"   "+getFixtureName(fixtureA));
		
		if(isGround(fixtureA)){
			player = getFixtureName(fixtureB);
		}
		if(isGround(fixtureB)){
			player =  getFixtureName(fixtureA);
		}
		switch (player) {
			case FixtureName.playerDownSensorFixture:
				playerActor.numFoot += begin?1:-1;
				break;
			default:
				break;
		}
	}
	public static boolean isGround(Fixture fixture){
		boolean isGround = false;
		String name = getFixtureName(fixture);
		for (String string : FixtureName.terrainFixtures) {//地形
			if(string.equals(name)){
				isGround = true;
			}
		}
	return isGround;
	}
	public static String getFixtureName(Fixture fixture){
		if(fixture.getUserData() == null)return nullString;
		FixtureFilter filter = (FixtureFilter) fixture.getUserData();
		return getFixtureName(filter);
	}
	public static String getFixtureName(FixtureFilter filter){
		return filter == null ? nullString : filter.getName();
	}
}
