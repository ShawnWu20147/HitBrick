package com.nju.hb.item;

import com.nju.hb.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class Item {
	public static final double DOWN_SPEED = 15;
	
	
	public static final int WIDTH = 60;
	public static final int HEIGHT = 60;
	int x, y;
	
	// x,y
	//			( x + width, y + height)
	
	int type;
	
	public Item(int type){
		this.type = type;
	}
	
	public static Item generateAnItem(){
		int a = (int) (Math.random() * 100);
		
		// 0 ~ 50
		if (a < 50){
			return new Item(0);
		}
		else return new Item(1);
	}
	
	public static Bitmap []bm;
	
	public static void generateItems(Context ct){
		bm = new Bitmap[2];
		bm[0] = ((BitmapDrawable)ct.getResources().getDrawable(R.drawable.thorough)).getBitmap();
		bm[0] = Bitmap.createScaledBitmap(bm[0], WIDTH, HEIGHT, true); 

		bm[1] = ((BitmapDrawable)ct.getResources().getDrawable(R.drawable.power)).getBitmap();
		bm[1] = Bitmap.createScaledBitmap(bm[1], WIDTH, HEIGHT, true); 
		
	}
	
}
