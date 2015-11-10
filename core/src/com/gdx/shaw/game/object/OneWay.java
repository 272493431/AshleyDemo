package com.gdx.shaw.game.object;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gdx.shaw.box2d.utils.LeBox2DWorldListener;
import com.gdx.shaw.box2d.utils.contact.ContactClassification.ContactState;
import com.gdx.shaw.game.able.Collisionable;
import com.gdx.shaw.game.data.FixtureName;
import com.gdx.shaw.game.player.PlayerActor;

public class OneWay implements Collisionable{
	boolean flag;
	@Override
	public void collision(Contact contact, ContactState state,
			PlayerActor playerActor) {
		Fixture [] fixtures = LeBox2DWorldListener.beginContactWith(contact, state, FixtureName.oneWayGroundFixture);
		if(fixtures != null){
			Fixture fixtureB = fixtures[1];
			if(LeBox2DWorldListener.getFixtureName(fixtureB).equals(FixtureName.playerDownSensorFixture)){
					flag = true;
			}else if (LeBox2DWorldListener.getFixtureName(fixtureB).equals(FixtureName.playerBodyFixture)) {
					flag = false;
			}
		}
		contact.setEnabled(flag);
	}

	@Override
	public void collision(Contact contact, ContactState state, Object notPlayer) {
		
	}

	@Override
	public void collision(Contact contact, ContactState state) {
		
	}

}
