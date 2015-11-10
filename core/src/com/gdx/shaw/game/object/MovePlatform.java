package com.gdx.shaw.game.object;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.gdx.shaw.game.data.FixtureName;

public class MovePlatform extends AbstractBox2DActor{

	Vector2 dir = new Vector2();
	float dist = 0;
	float maxDist = 0;		
	
	public MovePlatform(RectangleMapObject mapObject) {
		super(mapObject);
		dir.x = 5;
		dir.y = 0;
		this.maxDist = 5;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		dist += dir.len() * delta;
		if(dist > maxDist) {
			dir.scl(-1);
			dist = 0;
		}
		body.setLinearVelocity(dir);
	}
	@Override
	protected String getFixtureName() {
		return FixtureName.movePlatform;
	}
}
