package com.gdx.shaw.tiled.utils;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.gdx.shaw.utils.DeBug;

public class LeMapProperties {

	/**
 * @param mapObject 地图对象
	 * @param key	属性key
	 * @param defaultValue 编辑时未填写值则使用默认值
	 * @return
	 */
	public static float getFloat(MapObject mapObject,String key,float defaultValue){
		float value = defaultValue;
		MapProperties properties = mapObject.getProperties();
		Object objSpeed = properties.get(key);
		if(objSpeed != null){
			if(objSpeed instanceof String){
				value = objSpeed.equals("") ? defaultValue : Float.valueOf((String) objSpeed);	
			}else if(objSpeed instanceof Float){
				value = (float) objSpeed;
			}
			
		}else {
			DeBug.error(LeMapProperties.class, "对象[ "+mapObject.getName()+" ]没有[ "+key+" ] 这个属性");
		}
		return value;
	}
	/**
	 * @param mapObject 地图对象
	 * @param key	属性key
	 * @param defaultValue 编辑时未填写值则使用默认值
	 * @return
	 */
	public static String getString(MapObject mapObject,String key,String defaultValue){
		String value = defaultValue;
		MapProperties properties = mapObject.getProperties();
		Object objValue = properties.get(key);
		if(objValue != null){
			value = objValue.equals("") ? defaultValue : ((String) objValue);
		}else {
			DeBug.error(LeMapProperties.class, "对象[ "+mapObject.getName()+" ]没有[ "+key+" ] 这个属性");
		}
		return value;
	}
	/**属性中key 属性的值是否与预期相等
	 * @param mapObject 地图对象
	 * @param key	属性key
	 * @param trueValue 判断属性中的字符是否与正确的字符相等
	 * @return
	 */
	public static boolean getBoolean(MapObject mapObject,String key,String trueValue ){
		boolean value = false;
		MapProperties properties = mapObject.getProperties();
		Object objValue = properties.get(key);
		if(objValue != null){
			value = objValue.equals(trueValue);
		}else {
			DeBug.error(LeMapProperties.class, "对象[ "+mapObject.getName()+" ]没有[ "+key+" ] 这个属性");
		}
		return value;
	}
	
	
	/** 属性中是否包含 key 属性
	 * @param mapObject 地图对象
	 * @param key	属性key
	 * @return
	 */
	public static boolean getBoolean(MapObject mapObject,String key  ){
		boolean value = false;
		MapProperties properties = mapObject.getProperties();
		Object objValue = properties.get(key);
		if(objValue != null){
			value = true;
		}else {
			DeBug.error(LeMapProperties.class, "对象[ "+mapObject.getName()+" ]没有[ "+key+" ] 这个属性");
		}
		return value;
	}
	
	
	
	
}
