package com.gdx.shaw.box2d.utils;
/**
 * PhysicsEditor Importer Library
 *
 * Usage:
 * - Create an instance of this class
 * - Use the "open" method to load an XML file from PhysicsEditor
 * - Invoke "createBody" to create bodies from library.
 *
 * by Adrian Nilsson (ade at ade dot se)
 * BIG IRON GAMES (bigirongames.org)
 * Date: 2011-08-30
 * Time: 11:51
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.shaw.box2d.utils.contact.FixtureFilter;
import com.gdx.shaw.utils.Constants;
import com.gdx.shaw.utils.DeBug;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class PhysicsEditorShapeLibrary {
    private HashMap<String, BodyTemplate> shapes = new HashMap<String, BodyTemplate>();
    private float pixelToMeterRatio = Constants.PIXELS_TO_METERS;
    static PhysicsEditorShapeLibrary physicsEditorShapeLibrary;
    public PhysicsEditorShapeLibrary() {
        //Default constructor
    }
    public static PhysicsEditorShapeLibrary getInstance() {
    	if(physicsEditorShapeLibrary == null){
    		physicsEditorShapeLibrary = new PhysicsEditorShapeLibrary();
    	}
    	return physicsEditorShapeLibrary;
    }
    
    public PhysicsEditorShapeLibrary(float pixelToMeterRatio) {
        this.pixelToMeterRatio = pixelToMeterRatio;
    }

    /**
     * Read shapes from an XML file and add to library.
     * Path is relative to your assets folder so if your file is in
     * "assets/shapes/shapes.xml", the path should be "shapes/shapes.xml"
     * @param xmlFile
     */
    public void open(String xmlFile) {
    	FileHandle fileHandle = Gdx.files.internal(xmlFile);
    	open(fileHandle.read());
    }
    public void open(InputStream inputStream) {
    	append(inputStream, this.pixelToMeterRatio);
    }

    /**
     * If you wish, you may access the template data and create custom bodies.
     * Be advised that vertex positions are pre-adjusted for Box2D coordinates (pixel to meter ratio).
     * @param key
     * @return
     */
    public BodyTemplate get(String key) {
        return this.shapes.get(key);
    }

    public Body createBody(String name,Vector2 position, World physicsWorld,FixturFilterConverter fixturFilterConverter){
    	BodyTemplate bodyTemplate = this.shapes.get(name);
        
        final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyTemplate.isDynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;

		boxBodyDef.position.x = position.x * pixelToMeterRatio;
		boxBodyDef.position.y = position.y * pixelToMeterRatio;
		Body boxBody = physicsWorld.createBody(boxBodyDef);
		
    	return createBodyFixture(name, boxBody, fixturFilterConverter);
    }
    /**
     * Create a Box2D Body with a shape from the library.
     * @param name name of the shape (usually filename of the image, without extension)
     * @param pShape the AndEngine shape you will be associating the body with (physics connector is not created here).
     * @param physicsWorld the AndEngine Box2D physics world object.
     * @return
     */
    public Body createBodyFixture(String name, Body boxBody,FixturFilterConverter fixturFilterConverter) {
        BodyTemplate bodyTemplate = this.shapes.get(name);
//        
//        final BodyDef boxBodyDef = new BodyDef();
//		boxBodyDef.type = bodyTemplate.isDynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
//		
//		boxBodyDef.position.x = position.x * pixelToMeterRatio;
//		boxBodyDef.position.y = position.y * pixelToMeterRatio;
//
//		final Body boxBody = physicsWorld.createBody(boxBodyDef);
        DeBug.Log(getClass(),"要变大-----:" + name +"      "+bodyTemplate);
        for(FixtureTemplate fixtureTemplate : bodyTemplate.fixtureTemplates) {
				if(fixtureTemplate.polygons !=null){
			        	for(int i = 0; i < fixtureTemplate.polygons.length; i++) {
			                final PolygonShape polygonShape = new PolygonShape();
			                final FixtureDef fixtureDef = fixtureTemplate.fixtureDef;
			                polygonShape.set(fixtureTemplate.polygons[i].vertices);
			                fixtureDef.shape = polygonShape;
			                DeBug.Log(getClass(),"多边形的groupIndex：： "+fixtureDef.filter.groupIndex);
			                Object userData = fixturFilterConverter !=null ? fixturFilterConverter.find(fixtureDef.filter.groupIndex) : new FixtureFilter("null");
//			                if(userData != null){//当设置了用户数据的时候 把组信息还原成 0 
//			                	fixtureDef.filter.groupIndex = 0;
//			                }
			                boxBody.createFixture(fixtureDef).setUserData(userData);;
			                polygonShape.dispose();
			            }
				}
			if(fixtureTemplate.circleTemplate !=null){//圆形
				final CircleShape circleShape = new CircleShape();
				   final FixtureDef fixtureDef = fixtureTemplate.fixtureDef;
				   CircleTemplate circleTemplate = fixtureTemplate.circleTemplate;
				   circleShape.setRadius(circleTemplate.r);
				   circleShape.setPosition(new Vector2(circleTemplate.x, circleTemplate.y));
	                fixtureDef.shape = circleShape;
	                Object userData = fixturFilterConverter !=null ? fixturFilterConverter.find(fixtureDef.filter.groupIndex) : new FixtureFilter("null");
//				      if(userData != null){//当设置了用户数据的时候 把组信息还原成 0
//	                	fixtureDef.filter.groupIndex = 0;
//				      }
	                boxBody.createFixture(fixtureDef).setUserData(userData);;
	                circleShape.dispose();
			}
        }

        return boxBody;
    }

    private void append(InputStream name, float pixelToMeterRatio) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            ShapeLoader handler = new ShapeLoader(shapes, pixelToMeterRatio);
           	parser.parse(name, handler);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    protected static class ShapeLoader extends DefaultHandler {
        public static final String TAG_BODY = "body";
        public static final String TAG_FIXTURE = "fixture";
        public static final String TAG_POLYGON = "polygon";
        public static final String TAG_VERTEX = "vertex";
        public static final String TAG_CIRCLE = "circle";
        public static final String TAG_R = "r";
        public static final String TAG_NAME = "name";
        public static final String TAG_X = "x";
        public static final String TAG_Y = "y";
        public static final String TAG_DENSITY = "density";
        public static final String TAG_RESTITUTION = "restitution";
        public static final String TAG_FRICTION = "friction";
        public static final String TAG_FILTER_CATEGORY_BITS = "filter_categoryBits";
        public static final String TAG_FILTER_GROUP_INDEX = "filter_groupIndex";
        public static final String TAG_FILTER_MASK_BITS = "filter_maskBits";
        public static final String TAG_ISDYNAMIC = "dynamic";
        public static final String TAG_ISSENSOR = "isSensor";

        private float pixelToMeterRatio;
        private StringBuilder builder;
        private HashMap<String, BodyTemplate> shapes;
        private BodyTemplate currentBody;
        private ArrayList<Vector2> currentPolygonVertices = new ArrayList<Vector2>();
        private ArrayList<FixtureTemplate> currentFixtures = new ArrayList<FixtureTemplate>();
        private ArrayList<PolygonTemplate> currentPolygons = new ArrayList<PolygonTemplate>();
        private CircleTemplate currentCircle;

        protected ShapeLoader(HashMap<String, BodyTemplate> shapes, float pixelToMeterRatio) {
            this.shapes = shapes;
            this.pixelToMeterRatio = pixelToMeterRatio;
        }
        
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            builder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            super.endElement(uri, localName, name);
            if (name.equalsIgnoreCase(TAG_POLYGON)) {
                currentPolygons.add(new PolygonTemplate(currentPolygonVertices));
            } else if (name.equalsIgnoreCase(TAG_FIXTURE)) {
            	if(currentPolygons.size() > 0){
            		currentFixtures.get(currentFixtures.size()-1).setPolygons(currentPolygons);
            	}else {
            		currentFixtures.get(currentFixtures.size()-1).setCircie(currentCircle);
				}
            } else if (name.equalsIgnoreCase(TAG_BODY)) {
                currentBody.setFixtures(currentFixtures);
                shapes.put(currentBody.name, currentBody);
//                DeBug.Log(getClass(),""+shapes.size() + "        几个Body："+currentBody.name);
            }
            builder.setLength(0);
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            builder = new StringBuilder();
        }
        
        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, name, attributes);
            builder.setLength(0);
            if (name.equalsIgnoreCase(TAG_BODY)) {
                this.currentFixtures.clear();
                this.currentBody = new BodyTemplate();
                this.currentBody.name = attributes.getValue(TAG_NAME);
                this.currentBody.isDynamic = attributes.getValue(TAG_ISDYNAMIC).equalsIgnoreCase("true");
            } else if (name.equalsIgnoreCase(TAG_FIXTURE)) {
                FixtureTemplate fixture = new FixtureTemplate();
                currentPolygons.clear();
                currentCircle = null;
                float restitution = Float.parseFloat(attributes.getValue(TAG_RESTITUTION));
                float friction = Float.parseFloat(attributes.getValue(TAG_FRICTION));
                float density = Float.parseFloat(attributes.getValue(TAG_DENSITY));
                short category = parseShort(attributes.getValue(TAG_FILTER_CATEGORY_BITS));
                short groupIndex = parseShort(attributes.getValue(TAG_FILTER_GROUP_INDEX));
                short maskBits = parseShort(attributes.getValue(TAG_FILTER_MASK_BITS));
                boolean isSensor = attributes.getValue(TAG_ISSENSOR).equalsIgnoreCase("true");
                fixture.fixtureDef = createFixtureDef(density, restitution, friction, isSensor, category, maskBits, groupIndex);
                currentFixtures.add(fixture);
            } else if (name.equalsIgnoreCase(TAG_POLYGON)) {
                currentPolygonVertices.clear();
            }  else if (name.equalsIgnoreCase(TAG_VERTEX)) {
                currentPolygonVertices.add(new Vector2(Float.parseFloat(attributes.getValue(TAG_X)) * this.pixelToMeterRatio, -Float.parseFloat(attributes.getValue(TAG_Y)) * this.pixelToMeterRatio));
            } else if (name.equalsIgnoreCase(TAG_CIRCLE)) {
            	currentCircle = new CircleTemplate(Float.parseFloat(attributes.getValue(TAG_R)) * this.pixelToMeterRatio,
            			Float.parseFloat(attributes.getValue(TAG_X)) * this.pixelToMeterRatio,
            			-Float.parseFloat(attributes.getValue(TAG_Y)) * this.pixelToMeterRatio);
			}
        }
    }
    private static FixtureDef createFixtureDef(float density,float restitution,float friction,boolean isSensor,short category,short maskBits,short groupIndex){
    	FixtureDef fixtureDef = new FixtureDef();
    	fixtureDef.density = density;
    	fixtureDef.restitution = restitution;
    	fixtureDef.friction = friction;
    	fixtureDef.isSensor = isSensor;
    	fixtureDef.filter.groupIndex =  groupIndex;
    	fixtureDef.filter.categoryBits =  category;
    	fixtureDef.filter.maskBits =  maskBits;
		return fixtureDef;
    }
    private static short parseShort(String val) {
        int intVal = Integer.parseInt(val);
        short ret = (short)(intVal & 65535);
        return (short)intVal;
    }
    private static class BodyTemplate {
        public String name;
        public boolean isDynamic = true;
        public FixtureTemplate[] fixtureTemplates;
        public void setFixtures(ArrayList<FixtureTemplate> fixtureTemplates) {
            this.fixtureTemplates = fixtureTemplates.toArray(new FixtureTemplate[fixtureTemplates.size()]);
        }
    }
    private static class FixtureTemplate {
        public PolygonTemplate[] polygons;
        public CircleTemplate circleTemplate;
        public FixtureDef fixtureDef;
        public void setPolygons(ArrayList<PolygonTemplate> polygonTemplates) {
            polygons = polygonTemplates.toArray(new PolygonTemplate[polygonTemplates.size()]);
        }
        public void setCircie(CircleTemplate circleTemplate){
        	this.circleTemplate = circleTemplate;
        }
    }
    private static class PolygonTemplate {
        public Vector2[] vertices;
        public PolygonTemplate(ArrayList<Vector2> vectorList) {
            vertices = vectorList.toArray(new Vector2[vectorList.size()]);
        }
    }
    private static class CircleTemplate {
    	float r;
    	float x;
    	float y;
    	public CircleTemplate(float r,float x,float y) {
    		this.r = r;
    		this.x =x;
    		this.y = y;
    	}
    }
    
    /**
     *	 过滤转换器
     */
    public static class FixturFilterConverter implements Disposable{
    	static HashMap<Short, Object> hashMap = new HashMap<Short, Object>();
    	public FixturFilterConverter  put(short group, Object object) {
    		hashMap.put(group, object);
    		return this;
		}
    	public Object  find(short group) {
    		return hashMap.get(group);
    	}
		@Override
		public void dispose() {
			hashMap.clear();
		}
    	
    }
    
    
    
    
}
