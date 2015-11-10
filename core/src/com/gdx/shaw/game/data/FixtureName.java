package com.gdx.shaw.game.data;


public interface FixtureName {
	
	
	String playerBodyFixture = "playerBodyFixture";
	String playerHeadFixture = "playerHeadFixture";
	String playerDownFixture = "playerDownFixture";
	String playerDownSensorFixture = "playerDownSensorFixture";
	
	String [] player = {
			playerDownFixture,playerBodyFixture,playerHeadFixture,playerDownSensorFixture
	};
	
	
	String oneWayGroundFixture = "oneWayGroundFixture";
	String groundFixture = "groundFixture";
	String movePlatform = "movePlatform";
	String [] terrainFixtures = {
			groundFixture,oneWayGroundFixture,
	};
	
}
