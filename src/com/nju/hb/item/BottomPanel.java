package com.nju.hb.item;

import android.graphics.Rect;
import android.graphics.RectF;

public class BottomPanel {
	
	public static final int INITIAL_LENGTH = 300;	// should not change
	public static final int FIXED_HEIGHT = 60;		// should not change
	
	public static int BOTTOM = 1720;
	public static int RIGHT = 1080;
	
	public int x, y;	// x是中间位置
	
	int length;
	
	public BottomPanel(){
		x = 540;
		y = BOTTOM - FIXED_HEIGHT;
		
		length = INITIAL_LENGTH;
	}
	
	public void setX(int x){
		this.x = x;
		if (this.x - length / 2 < 0) this.x = length / 2;
		if (this.x + length / 2 > RIGHT) this.x = RIGHT - length / 2;
		
	}
	

	

	
	public int getX(){
		return x;
	}
	
	public int getXforBall(){
		return x;
	}
	
	public int getYforBall(){
		return y - Ball.BALL_SIZE;
	}

	public Rect generateRect(){
		Rect rc = new Rect( x - length / 2, y, x + length / 2, BOTTOM);
		

		
		return rc;
	}
	
	
	public RectF generateRectF(){
		RectF rc = new RectF( x - length / 2, y, x + length / 2, BOTTOM + FIXED_HEIGHT);
		

		
		return rc;
	}

	public void adjustPos(int true_width, int true_height) {
		RIGHT = true_width;
		BOTTOM = true_height;
		y = BOTTOM - FIXED_HEIGHT;
	}
}
