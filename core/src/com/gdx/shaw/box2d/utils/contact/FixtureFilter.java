package com.gdx.shaw.box2d.utils.contact;

import java.util.ArrayList;
import java.util.Arrays;

public class FixtureFilter {
	
	
	private  String name;
	private ArrayList<String> friendlyList = new ArrayList<String>();
	private ArrayList<String> hateList = new ArrayList<String>();
	private boolean neverCollide;
	
	
	public FixtureFilter(String name) {
		this.name = name;
	}
	public FixtureFilter(String name,String... friendly) {
		this.name = name;
		friendlyList.addAll(Arrays.asList(friendly));
	}
	public FixtureFilter(String name,String[] friendly,String [] hate) {
		this.name = name;
			friendlyList.addAll(Arrays.asList(friendly));
			hateList.addAll(Arrays.asList(hate));
	}

	public FixtureFilter setFriendlyList(String... friendly) {
		friendlyList.addAll(Arrays.asList(friendly));
		return this;
	}
	public FixtureFilter setHateList(String... hate) {
		hateList.addAll(Arrays.asList(hate));
		return this;
	}
	public FixtureFilter setNeverCollide(boolean neverCollide) {
		this.neverCollide = neverCollide;
		return this;
	}
	/**	和没有标识的fixture碰撞
	 */
	public boolean collideWithNoIdentity(){
		if(neverCollide)return false;
		return friendlyList.isEmpty();	
	}
	public boolean shouldCollide(FixtureFilter fixtureType) {
		if(neverCollide || fixtureType.neverCollide)return false;//永不碰撞
		
		if(hateCollide(fixtureType))return false;
		
		if(friendlyCollide(fixtureType))return true;
		
		return false;
	}
	public String getName() {
		return name;
	}
	
	private boolean hateCollide(FixtureFilter fixtureType){//不能发生碰撞的列表里面包含另一方-则不碰撞
		if( hateList.contains(fixtureType.name) ||fixtureType.hateList.contains(name))return true;
		return false;
	}
	
	private  boolean friendlyCollide(FixtureFilter fixtureType){//其中一方的碰撞列表里面含有另一方的类则 一定碰撞
		if(friendlyList.isEmpty()){//A  为空  
			if(fixtureType.friendlyList.isEmpty()) return true;//B 同为空 	碰撞
			if (fixtureType.friendlyList.contains(name)) {		//B不为空包含A  	碰撞
				return true;
			} else {//	 B不包含A 不碰撞
				return false;
			}
		}
		//A不为空
		if(friendlyList.contains(fixtureType.name)){//包含B  	碰撞
			return true;
		}else {//不包含B  	不碰撞
			return false;
		}
	}
}
