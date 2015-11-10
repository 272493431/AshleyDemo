package com.gdx.shaw.box2d.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gdx.shaw.box2d.utils.contact.FixtureFilter;
import com.gdx.shaw.box2d.utils.contact.ContactClassification.ContactState;

public class LeBox2DWorldListener {
	
	public static  boolean haveTheBits(Fixture fixtureA,Fixture fixtureB,short categoryBits){
		if(fixtureA.getFilterData().categoryBits == categoryBits || fixtureB.getFilterData().categoryBits == categoryBits){
			return true;
		}
		return false;
	}
	public static  boolean haveTheBits(Fixture fixtureA,Fixture fixtureB,String categoryBits){
		return getFixtureName(fixtureA).equals(categoryBits)||getFixtureName(fixtureB).equals(categoryBits);
	}
	public static  boolean isCollisioned(Contact contact,String fixtureNameA,String fixtureNameB){
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		if((getFixtureName(fixtureA).equals(fixtureNameA)&&
				getFixtureName(fixtureB).equals(fixtureNameB))||
		(getFixtureName(fixtureA).equals(fixtureNameB) &&
			getFixtureName(fixtureB).equals(fixtureNameA)
			))return true;
		
		return false;	
	}
	public static String getFixtureName(Fixture fixture){
		if(fixture.getUserData() == null)return "null";
		FixtureFilter filter = (FixtureFilter) fixture.getUserData();
		return getFixtureName(filter);
	}
	public static String getFixtureName(FixtureFilter filter){
		return filter == null ? "null" : filter.getName();
	}
	public static Fixture [] beginContactWith(Contact contact,ContactState state,String fixtureName){
		if(state == ContactState.beginContact){
			Fixture [] fixtures = sortByOneFixture(contact, fixtureName);
			if(fixtures != null)return fixtures;
		}
		return null;
	}
	public static Fixture [] endContactWith(Contact contact,ContactState state,String fixtureName){
		if(state == ContactState.endContact){
			Fixture [] fixtures = sortByOneFixture(contact, fixtureName);
			if(fixtures != null)return fixtures;
		}
		return null;
	}
	public static  Fixture[] sortByOneFixture(Contact contact,short tragetAcategoryBits){
		Fixture	tempFixtureA = contact.getFixtureA();
		Fixture	tempFixtureB = contact.getFixtureB();
		
		short categoryA =  tempFixtureA.getFilterData().categoryBits;
		short categoryB =  tempFixtureB.getFilterData().categoryBits;
		
		
		Fixture array[] = null;
		if(categoryA == tragetAcategoryBits){
			array = new Fixture[]{
					tempFixtureA,tempFixtureB
			};
		}else if(categoryB  == tragetAcategoryBits){
			array = new Fixture[]{
					tempFixtureB,tempFixtureA
			};
		}
		return array;
	}
	/** 判断对象是否属于这个类
	 * @param clazz	
	 * @param object 	
	 * @return
	 */
	public static boolean isTheClass(Class<?> clazz ,Object object){
		if( clazz == null || object == null)return false;
		String tragetA2String = clazz.getCanonicalName().toString();
		String objectString = object.getClass().getCanonicalName().toString();
		
		
//		DeBug.Log(LeBox2DWorldListener.class,"目标类："+tragetA2String);
//		DeBug.Log(LeBox2DWorldListener.class,"当前类："+objectString);
		
		if(tragetA2String.equals(objectString))return true;//被判断类就是目标类
		
		{//判断父类--父类的父类---父类的父类的父类----噗噗噗	
			
			boolean hasParent = true;
			Class<?> parent = object.getClass().getSuperclass();
			while (hasParent) {
				String parentClassString = parent.getCanonicalName().toString();
//				DeBug.Log(LeBox2DWorldListener.class,"当前父类："+parentClassString);
				if(parentClassString.equals("java.lang.Object")){//父类为Object的时候打断循环
					break;
				}
				if(tragetA2String.equals(parentClassString)){
					return true;
				}
				
				parent = parent.getSuperclass();
				hasParent = true;
			}
		}
		
		return false;
	}
	
	public static <T> Fixture [] sortByOneFixture(Contact contact,String fixtureName){
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		FixtureFilter filterA = (FixtureFilter) fixtureA.getUserData();
		FixtureFilter filterB = (FixtureFilter) fixtureB.getUserData();
		Fixture[] fixtures = null;
		if(filterA != null){
			if(filterA.getName().equals(fixtureName)){
				fixtures = new Fixture[]{
						fixtureA,fixtureB
				};
			}
		}
		if(filterB != null){
			if(filterB.getName().equals(fixtureName)){
				fixtures = new Fixture[]{
						fixtureB,fixtureA
				};
			}
		}
		return fixtures;
	}
	
	public static  <T> Body[] sortByOneBody(Contact contact,Class<T> tragetA){
		Body	tempBodyA = contact.getFixtureA().getBody();
		Body	tempBodyB = contact.getFixtureB().getBody();
		
		Object userStringA =  tempBodyA.getUserData();
		Object userStringB =  tempBodyB.getUserData();
	
		Body array[] = null;
		if(userStringA !=null){
			if(isTheClass(tragetA, userStringA)){
				array = new Body[]{
						tempBodyA,tempBodyB
				};
			}
		}
		if(userStringB !=null){
			if(isTheClass(tragetA, userStringB)){
				array = new Body[]{
						tempBodyB,tempBodyA
				};
			}
		}
		return array;
	}
	public static  Body[] sortByOneBody(Contact contact,Object tragetA){
		Body	tempBodyA = contact.getFixtureA().getBody();
		Body	tempBodyB = contact.getFixtureB().getBody();
		
		Object userStringA =  tempBodyA.getUserData();
		Object userStringB =  tempBodyB.getUserData();
		
		
		Body array[] = null;
		if(userStringA !=null && userStringA.equals(tragetA)){
			array = new Body[]{
					tempBodyA,tempBodyB
			};
		}else if(userStringB !=null && userStringB.equals(tragetA)){
			array = new Body[]{
					tempBodyB,tempBodyA
			};
		}
		return array;
	}
	

	public static  Body[] sortByOneBodyInShort(Contact contact,short tragetA){
		Fixture	fixtureA = contact.getFixtureA();
		Fixture	 fixtureB = contact.getFixtureB();
	
		short categoryBitsA = fixtureA.getFilterData().categoryBits;
		short categoryBitsB = fixtureB.getFilterData().categoryBits;

		Body array[] = null;
		if(categoryBitsA == tragetA){
			array = new Body[]{
					fixtureA.getBody(),fixtureB.getBody()
			};
		}else if(categoryBitsB == tragetA){
			array = new Body[]{
					fixtureB.getBody(),fixtureA.getBody()
			};
		}
		return array;
	}
}
