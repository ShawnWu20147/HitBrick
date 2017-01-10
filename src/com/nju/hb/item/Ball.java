package com.nju.hb.item;

import android.graphics.Color;

public class Ball {
	public static final long TIME_POWER = 10000;
	public static final long TIME_THOROUGH = 10000;
	public static final int BALL_SIZE = 30;
	public static final double INITIAL_SPEED = 30;
	
	double dx, dy;
	int x, y;
	int diff;
	
	double ball_speed_level = 0;
	
	double speed;
	
	boolean isStopped;
	
	boolean isPower, isThorough;
	long power_start, thorough_start;
	long speed_start;
	
	int cl;
	
	public Ball(int x, int y){
		isStopped = true;
		
		isPower = false;
		isThorough = false;
		
		dx = 1.0 / Math.sqrt(2);
		dy = -1.0 / Math.sqrt(2);
		
		speed = INITIAL_SPEED;
		
		ball_speed_level = 0;
		
		diff = 0;
		
		this.x = x;
		this.y = y;
		
	}
	
	public Ball(int x, int y, boolean generate){
		this(x, y);
		isStopped = false;	//直接开始动的球
		
	}
	
	public int generateColor(){
		if (isPower) cl = Color.RED;
		else if (isThorough) cl = Color.BLUE;
		else cl = Color.BLACK;
		return cl;
	}

	public void increateSpeed() {
		ball_speed_level += 1.0;
		speed = INITIAL_SPEED * (1.0 + ball_speed_level / 10.0);
		
	}
}
