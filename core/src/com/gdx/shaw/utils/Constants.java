package com.gdx.shaw.utils;

public interface Constants {
	
	float METERS_TO_PIXELS = 32;
	float PIXELS_TO_METERS = 1 / METERS_TO_PIXELS;
	
	float TIME_STEP = 1/60f;
	int VELOCITY_ITERATIONS = 6;
	int POSITION_ITERATIONS = 2;
	
	float SCREEN_UI_WIDTH = 1136;
	float SCREEN_UI_HIGHT = 768;
	float SCREEN_GAME_WIDTH = 1136;
	float SCREEN_GAME_HIGHT = 768;
	float STAGE_W = SCREEN_GAME_WIDTH * PIXELS_TO_METERS;
	float STAGE_H = SCREEN_GAME_HIGHT * PIXELS_TO_METERS;
	
	String nullString = "null";
	String newLine = "\n";
}
