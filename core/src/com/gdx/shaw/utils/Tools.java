package com.gdx.shaw.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;
public class Tools  {

	/**
	 * @param string 完整路径
	 * */
	public static Texture loadTexture(String string){
		Texture texture = null;
		texture = new Texture(Gdx.files.internal(string));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return texture;
	
	}
	public static TextureRegionDrawable loadDrawable(String string){
		return new TextureRegionDrawable(new TextureRegion(Tools.loadTexture(string))); 
	}
	/**
	 * @param string 完整路径
	 * */
	@Deprecated
	public static Texture initTexture(String string){
		return loadTexture(string);
	}
	public static void setOriginCenter(Actor... actor){
		for (Actor actor2 : actor) {
			actor2.setOrigin(actor2.getWidth()*0.5f, actor2.getHeight()*0.5f);
		}
	}
	
	/**	以某个Actor为基准 设置另一个Actor  在他的中心
	 * @param bgActor	基准
	 * @param prospectsActor  被设置的
	 */
	public static void setCenter(Actor bgActor, Actor prospectsActor){
		prospectsActor.setPosition(bgActor.getX() + bgActor.getWidth() *0.5f - prospectsActor.getWidth()*0.5f,bgActor.getY() + bgActor.getHeight()*0.5f - prospectsActor.getHeight()*0.5f);
	}
	
	public static void addClickListener(Actor actor,final Runnable run){
		actor.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				run.run();
			}
		});
	}

	public static void copyPropertysWithoutNull(Object des, Object src){
        Class<?> clazz = des.getClass();
        Field[] srcfields=src.getClass().getDeclaredFields();
        for(Field field:srcfields){
            if(field.getName().equals("serialVersionUID"))
                continue;
            Field f;
            try {
                f = clazz.getDeclaredField(field.getName());
                f.setAccessible(true);
                field.setAccessible(true);
                Object obj = field.get(src);
                if(obj!=null)
                    f.set(des,obj);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
	/**	计算组的size
	 * @param group
	 * @return
	 */
	public static Rectangle groupMaxSize(Group group){
		return groupMaxSize(group, true);
	}
	/** 计算组的size
	 * @param group
	 * @param withVisible 是否计算被隐藏的actor
	 * @return
	 */
	public static Rectangle groupMaxSize(Group group , boolean withVisible){
		return groupMaxSize(group, false, withVisible);
	}
	/** 计算组的size
	 * @param group
	 * @param refresh 重新计算
	 * @param withVisible 是否计算被隐藏的actor
	 * @return
	 */
	public static Rectangle groupMaxSize(Group group ,boolean refresh, boolean withVisible){
		float top = 0;
		float left = 0;
		float bottom = 0;
		float right = 0;
		if(refresh){
			group.setSize(0, 0);
		}
		for (Actor actor : group.getChildren()) {
			if(!withVisible){
				if(!actor.isVisible())continue;
			}
			float x = actor.getX();
			float y = actor.getY();
			float width = actor.getWidth();
			float height = actor.getHeight();
			if(actor instanceof Group){
				Rectangle rectangle = groupMaxSize((Group) actor,refresh,withVisible);
				x = rectangle.x;
				y= rectangle.y;
				width = rectangle.width;
				height = rectangle.height;
				
				
			}
			left = Math.min(left,x);
			right = Math.max(right, x+width);
			bottom = Math.min(bottom, y);
			top = Math.max(top, y+height);
		}
		
		left = Math.min(left,group.getX());
		right = Math.max(right, group.getX() + group.getWidth());
		bottom = Math.min(bottom, group.getY());
		top = Math.max(top,group.getY() + group.getHeight());
		
		
		return new Rectangle(left,bottom,right-left,top-bottom);
	}
	
    /** 图片计数
     * @param gBatch	画笔
     * @param texture  数字图片
     * @param num	数字
     * @param x		位置x
     * @param y		位置y
     * @param w		每一个数字的宽度
     * @param h		每一个数字的高度
     */
    public static void drawNum(Batch gBatch,Texture texture, int num, int x, int y,
    		int w, int h) {

    	int a = num / 10;
    	int s = 1;

    	while (a != 0) {
    		s++;
    		a = a / 10;
    	}
    	for (int i = 0; i < s; i++) {
    		int xx = x + (s - 1) * w - w * i - s * w;
    		int ix = (num % 10) * w;

    		gBatch.draw(texture,xx,y,ix,0,w,h);
    		num = num / 10;
    	}
    }
    
    /**
     * @param a	选择排序	降序
     * @return		
     */
    public static int[] selectionSort(int[] a) {
    for(int i=0; i<a.length; i++) {
       for(int j = i+1; j < a.length; j++) {
        if(a[i] < a[j]) {
         int temp = a[i];
         a[i] = a[j];
         a[j] = temp;
        }
       }
    }
    return a;
    }
    /**获得最大值*/
    public static int getMaxValue(int...is){
    	for (int i = 0; i < is.length; i++) {
			for (int j = i+1; j < is.length; j++) {
		        if(is[i] < is[j]) {
			         int temp = is[i];
			         is[i] = is[j];
			         is[j] = temp;
		        }
			}
		}
    	return is[0];
    }
    
    public static ArrayList<String> getFileNameList(String strFolderName) {
    	
		ArrayList<String> arr = new ArrayList<String>();
		File file = new File(strFolderName);
		String[] filesAndDir = file.list();
		for (int i = 0; filesAndDir != null && i < filesAndDir.length; i++) {
			File filesAndDir_file = new File(strFolderName + "/"+ filesAndDir[i]);
			if (filesAndDir_file.isDirectory()) {
				arr.addAll(getFileNameList(filesAndDir_file.getPath()));
			} else if (filesAndDir_file.isFile()) {
				arr.add(filesAndDir[i]);
			}
		}
		return arr;
	}
	
/**
 * 判断两个actor是否相交
 */
	public static boolean isIntersection(Actor r1, Actor r2) {
		float nMaxLeft = 0;
		float nMaxTop = 0;
		float nMinRight = 0;
		float nMinBottom = 0;
		// 计算两矩形可能的相交矩形的边界
		nMaxLeft = r1.getX() >= r2.getX() ? r1.getX() : r2.getX();
		nMaxTop = r1.getY() >= r2.getY() ? r1.getY() : r2.getY();
		nMinRight = (r1.getX() + r1.getWidth()) <= (r2.getX() + r2.getWidth()) ? (r1
				.getX() + r1.getWidth()) : (r2.getX() + r2.getWidth());
		nMinBottom = (r1.getY() + r1.getHeight()) <= (r2.getY() + r2
				.getHeight()) ? (r1.getY() + r1.getHeight()) : (r2.getY() + r2
				.getHeight());
		// 判断是否相交
		if (nMaxLeft > nMinRight || nMaxTop > nMinBottom) {
			return false;
		} else {
			return true;
		}
	}

//对ArrayList  <  Integer > 排序
		public static ArrayList<Integer>  sortingArrayList(ArrayList<Integer> arrayList){
			//对数组中的元素排序
			for (int i = 0; i < arrayList.size()-1; i++) {
				for (int j = 1; j < arrayList.size() ; j++) {
					   Integer a;
					    if((arrayList.get(j-1)).compareTo(arrayList.get(j))<0) {   //比较两个整数的大小  现在为降序 改成 >  为升序
					     
					     a=arrayList.get(j-1);
					     arrayList.set((j-1),arrayList.get(j));
					     arrayList.set(j,a);
					    }								
				}
			}
			return arrayList;
		}
		
		/**转化为舞台坐标*/
		public static Vector2 getActorStageVector2(Actor actor){
			Group group = actor.getParent();
			if(group != null){
				 return getActorStageVector2(group).add(new Vector2(actor.getX(),actor.getY()));
			}
			return new Vector2(actor.getX(),actor.getY());
		}
		
		//被撞击球角度
		public static float Crashed_Ball_Angle(float x,float y,float x1,float y1)
		 {
				float angle = 0;
				if (x > x1 && y > y1) {
					angle = (float) (Math.atan((y - y1) / (x - x1)));
				}
				if (x > x1 && y < y1) {
					angle = (float) (Math.PI * 2 + Math.atan((y - y1) / (x - x1)));
				} else if (x <= x1) {
					angle = (float) (Math.PI + Math.atan((y - y1) / (x - x1)));
				}
				return (float) (angle * 180 / Math.PI);
			}

		/**	切割只有一排的帧动画
		 * @param texture	
		 * @param tiledNum	图块个数--帧数
		 * @return
		 */
		public static TextureRegion [] split(Texture texture,int tiledNum){
			return split(new TextureRegion(texture), tiledNum);
		}
		/**	切割只有一排的帧动画
		 * @param textureRegion	
		 * @param tiledNum	图块个数--帧数
		 * @return
		 */
		public static TextureRegion [] split(TextureRegion textureRegion,int tiledNum){
			int x = 0;
			int y = 0;
			int width = textureRegion.getRegionWidth();
			int height = textureRegion.getRegionHeight();
			int tileWidth = width > height ? width / tiledNum : width;
			int tileHeight = height > width ? height/tiledNum : height;
			
			TextureRegion [] textureRegions = new TextureRegion[tiledNum];
			for (int i = 0; i < textureRegions.length; i++) {
				textureRegions[i] = new TextureRegion(textureRegion, x, y, tileWidth, tileHeight);
				if(width > height){
					x += tileWidth;
				}else {
					y += tileHeight;
				}
			}
			return textureRegions;
		}
		
		/**	删除扩展名
		 * @param fileString
		 * @return
		 */
		public static String deleteExtension(String fileString){
			if(fileString.contains(".")){
				int beginIndex =  fileString.indexOf(".");
				return fileString.replace(fileString.substring(beginIndex), "");
			}
			return fileString;
		}
		public static boolean targetClass(Class<?>currentClass,Class<?> targetClass){
			if(currentClass.getName().equals(targetClass.getName()))return true;
			Class<?> parentClass = currentClass.getSuperclass();
			if(parentClass == null)return false;
			return targetClass(parentClass, targetClass);
		}
		/**
		 * @param clazz	类名
		 * @param args	构造时候所用的参数
		 * @return
		 */
		public static  Object newInstance(Class<?> clazz,Object... args){
			if(clazz == null)return null;
			Class<?> parameterTypes [] = null;
			if(args != null){
				parameterTypes = new Class[args.length];
				for (int i = 0; i < parameterTypes.length; i++) {
					parameterTypes[i] = args[i].getClass();
				}
			}
			Constructor constructor = getConstructor(clazz, parameterTypes);
			try {
				return constructor.newInstance(args);
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
			return null;
		}

		public static  Constructor getConstructor(Class<?> type, Class<?>... parameterTypes) {
			for (int i = 0; i < parameterTypes.length; i++) {
				System.out.println(parameterTypes[i]);
			}
			try {
				return ClassReflection.getConstructor(type, parameterTypes);
			} catch (Exception ex1) {
				System.out.println(ex1);
				try {
					Constructor constructor = ClassReflection.getDeclaredConstructor(type, parameterTypes);
					constructor.setAccessible(true);
					return constructor;
				} catch (ReflectionException ex2) {
					System.out.println(ex2);
					return null;
				}
			}
		}
}
