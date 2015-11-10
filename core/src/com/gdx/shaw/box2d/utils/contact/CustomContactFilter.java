package com.gdx.shaw.box2d.utils.contact;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class CustomContactFilter implements ContactFilter{
	public CustomContactFilter() {
		CollisionRule.init();
	}
	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		
		FixtureFilter fixtureTypeA = (FixtureFilter) fixtureA.getUserData();
		FixtureFilter fixtureTypeB = (FixtureFilter) fixtureB.getUserData();
		if(fixtureTypeA == null && fixtureTypeB == null){//两个都为空  进行默认的过滤
			return defaultFilter(fixtureA, fixtureB);
		}else {
			if(fixtureTypeA != null && fixtureTypeB != null){//都不为空
//				if(fixtureTypeA.getName() == "monsterSensorGroundFixture" || fixtureTypeB.getName() == "monsterSensorGroundFixture"){
//					DeBug.Log(getClass(),"AAA: "+fixtureTypeA.getName() +"         BBB:"+fixtureTypeB.getName() );
//				}
				if(fixtureTypeA.shouldCollide(fixtureTypeB) || fixtureTypeB.shouldCollide(fixtureTypeA)){
//					if(fixtureTypeA.getName() == "monsterSensorGroundFixture" || fixtureTypeB.getName() == "monsterSensorGroundFixture"){
//						DeBug.Log(getClass(),"发生碰撞" );
//
//					}
					return true;
				}
			}else {
				if(fixtureTypeA != null && fixtureTypeB == null){//其中一个为空
					
					return fixtureTypeA.collideWithNoIdentity();
				}
				if(fixtureTypeB != null && fixtureTypeA == null){//其中一个为空
					return fixtureTypeB.collideWithNoIdentity();
				}
			}
		}
		
	
		return false;
	}
	private boolean defaultFilter(Fixture fixtureA,Fixture fixtureB){
		Filter filterA = fixtureA.getFilterData();
		Filter filterB = fixtureB.getFilterData();
		
		if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0) {
			return filterA.groupIndex > 0;
		}

		boolean collide = (filterA.maskBits & filterB.categoryBits) != 0 && (filterA.categoryBits & filterB.maskBits) != 0;
		return collide;
	}
	
}
