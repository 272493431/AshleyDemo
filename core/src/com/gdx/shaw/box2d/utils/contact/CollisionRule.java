package com.gdx.shaw.box2d.utils.contact;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.gdx.shaw.game.data.FixtureName;
import com.gdx.shaw.utils.Constants;


public class CollisionRule implements Constants ,FixtureName{
	
	private static HashMap<String, FixtureFilter> fixtureFilterMap = new HashMap<String, FixtureFilter>();
	public static void init(){//collision rules
		
	}
	public static void put(FixtureFilter fixtureFilter){
		if(fixtureFilterMap.containsKey(fixtureFilter.getName())){
			throw new VerifyError("存在过滤器："+fixtureFilter.getName());
		}
		fixtureFilterMap.put(fixtureFilter.getName(),fixtureFilter);
	}
	public static FixtureFilter findFixtureFilter(String key){
		FixtureFilter fixtureType = fixtureFilterMap.get(key);
		if(fixtureType == null){
			Gdx.app.error(CollisionRule.class.getName(), "There is no call [ "+key+" ] !!!! Then I will create a [ "+key+" ]...");
			fixtureType = new FixtureFilter(key);
			fixtureFilterMap.put(key, fixtureType);
		}
		return fixtureType;
	}
	
	public static void clear(){
		fixtureFilterMap.clear();
	}
	
}
