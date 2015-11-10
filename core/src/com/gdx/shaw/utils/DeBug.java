package com.gdx.shaw.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class DeBug {

	public static boolean DeBug = true;
	
	public static void Log(Object object){
		if(DeBug){
			System.out.println(object);
		}
	}
	
	public static void Log(String className, String str){
		if (DeBug) {
			Gdx.app.log(className, str);
		}
	}
	public static void Log(Class<?> mClass, String str){
		if (DeBug) {
			Gdx.app.log(mClass.getName(), str);
		}
	}
	
	public static void Log(Class <?> mClass, int str){
		if (DeBug) {
			Gdx.app.log(mClass.getName(), str + "");
		}
	}
	
	public static void Log(Class<?>  mClass, float str){
		if (DeBug) {
			Gdx.app.log(mClass.getName(), str + "");
		}
	}
	
	public static void Log(Class <?> mClass, Vector2 str){
		if (DeBug) {
			Gdx.app.log(mClass.getName(), str.toString() + "");
		}
	}
	
	public static void Log(Class <?> mClass, Vector3 str){
		if (DeBug) {
			Gdx.app.log(mClass.getName(), str.toString() + "");
		}
	}
	public static void error(Class<?> mClass, String str){
		if (DeBug) {
			Gdx.app.error(mClass.getName(), str);
		}
	}
	public static void error(String className, String str){
		if (DeBug) {
			Gdx.app.error(className, str);
		}
	}
	public static void error(Class <?> mClass, int str){
		if (DeBug) {
			Gdx.app.error(mClass.getName(), str + "");
		}
	}
	
	public static void error(Class<?>  mClass, float str){
		if (DeBug) {
			Gdx.app.error(mClass.getName(), str + "");
		}
	}
	
	public static void error(Class <?> mClass, Vector2 str){
		if (DeBug) {
			Gdx.app.error(mClass.getName(), str.toString() + "");
		}
	}
	
	public static void error(Class <?> mClass, Vector3 str){
		if (DeBug) {
			Gdx.app.error(mClass.getName(), str.toString() + "");
		}
	}
	
	
	public static String FPS(){
		String text = 
				"FPS:"+ Gdx.graphics.getFramesPerSecond()
				+"\nJHeap:"+Gdx.app.getJavaHeap()/1024/1204+"M"
				+"\nNHeap:"+Gdx.app.getNativeHeap()/1024/1024+"M";
		return text;
	}
	
	
	// 可以写作配置：true写文件; false输出控制台
	private static boolean fileLog = true;
	private static String logFileName = System.getProperty("user.dir")+"/log/Log.log";
	public static void LogFile(String info)  {
		if(!DeBug)return;
		OutputStream out;
		try {
			out = getOutputStream();
			info+="\n";
			out.write(info.getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static OutputStream getOutputStream() throws IOException {
		if (fileLog) {
			Log("位置--："+logFileName);
			File file = new File(logFileName);
			
			File parent = file.getParentFile(); 
			if(parent!=null&&!parent.exists()){ 
				parent.mkdirs(); 
			} 
				file.createNewFile();
			return new FileOutputStream(file,true);	//文件的续写
		} else {
			return System.out;
		}
	}
 
	
	
}
