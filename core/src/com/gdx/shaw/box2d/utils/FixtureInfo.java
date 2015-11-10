package com.gdx.shaw.box2d.utils;

import com.badlogic.gdx.physics.box2d.Filter;
import com.gdx.shaw.box2d.utils.contact.CollisionRule;
import com.gdx.shaw.box2d.utils.contact.FixtureFilter;

public class FixtureInfo  {
	public Filter filter = new Filter();
	public FixtureFilter userData;

	
	/** The friction coefficient, usually in the range [0,1]. **/
	public float friction = 1f;

	/** The restitution (elasticity) usually in the range [0,1]. **/
	public float restitution = 0;

	/** The density, usually in kg/m^2. **/
	public float density = 1;

	/** A sensor shape collects contact information but never generates a collision response. */
	public boolean isSensor = false;

	public FixtureInfo() {
		
	}
	public FixtureInfo(String userDataName) {
		this.userData = CollisionRule.findFixtureFilter(userDataName);
	}
	public FixtureInfo(FixtureFilter userData) {
		this.userData = userData;
	}
	public FixtureInfo(Filter filter) {
		this.filter = filter;
	}
	public FixtureInfo setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}
	public FixtureInfo setCategoryBits(short categoryBits){
		filter.categoryBits = categoryBits;
		return this;
	}
	public FixtureInfo setGroupIndex(short groupIndex){
		filter.groupIndex = groupIndex;
		return this;
	}
	public FixtureInfo setMaskBits(short maskBits){
		filter.maskBits = maskBits;
		return this;
	}
	
	/**	和{@link  com.gdx.shaw.box2d.utils.FixtureInfo# setUserData(com.gdx.shaw.box2d.utils.contact.FixtureFilter) }功能相同
	 * @param userData 
	 * @return
	 */
	public FixtureInfo setFixtureFilter(FixtureFilter userData) {
		this.userData = userData;
		return this;
	}
	/**	和{@link  com.gdx.shaw.box2d.utils.FixtureInfo# setFixtureFilter(com.gdx.shaw.box2d.utils.contact.FixtureFilter) }功能相同
	 * @param userData
	 * @return
	 */
	public FixtureInfo setUserData(FixtureFilter userData) {
		this.userData = userData;
		return this;
	}
	public FixtureInfo setDensity(float density) {
		this.density = density;
		return this;
	}
	public FixtureInfo setFriction(float friction) {
		this.friction = friction;
		return this;
	}
	public FixtureInfo setRestitution(float restitution) {
		this.restitution = restitution;
		return this;
	}
	public FixtureInfo setSensor(boolean isSensor) {
		this.isSensor = isSensor;
		return this;
	}
	
	
	
}
