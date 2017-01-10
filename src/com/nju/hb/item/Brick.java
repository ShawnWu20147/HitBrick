package com.nju.hb.item;

import android.graphics.Color;
import android.graphics.Rect;

public class Brick {
	public static final int WIDTH = 240;
	public static final int HEIGHT = 90;
	int left, top, right, bottom;
	
	int cl;
	
	int level;
	
	public Brick(int left, int top){
		this.left = left;
		this.top = top;
		this.right = left + WIDTH;
		this.bottom = top + HEIGHT;
		
	}

	
	public void randomLevel(){
		double what = Math.random()*5;
		int lv = (int) what;
		this.setLevel(lv);
	}
	
	public void setLevel(int level){
		this.level = level;
		switch(level){
		case 0: cl = Color.GREEN;break;
		case 1: cl = Color.BLUE;break;
		case 2: cl = Color.argb(255, 255, 0, 255);break;
		case 3: cl = Color.argb(255, 255, 110, 25);break;
		case 4: cl = Color.argb(255, 255, 0, 0);break;
		}
	}
	
	public Rect generateRect(){
		Rect rc = new Rect(left, top, right, bottom);
		return rc;
	}
}
