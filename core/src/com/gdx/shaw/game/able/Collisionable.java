package com.gdx.shaw.game.able;

import com.badlogic.gdx.physics.box2d.Contact;
import com.gdx.shaw.box2d.utils.contact.ContactClassification.ContactState;
import com.gdx.shaw.game.player.PlayerActor;

public interface Collisionable {
	public void collision(Contact contact,ContactState state,PlayerActor playerActor);
	public void collision(Contact contact,ContactState state,Object notPlayer);
	public void collision(Contact contact,ContactState state);
}
