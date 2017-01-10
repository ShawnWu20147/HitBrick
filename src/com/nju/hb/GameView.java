package com.nju.hb;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{	
	
	Bitmap bm;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.getHolder().addCallback(this);

		
		Bitmap bmp = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.gb)).getBitmap();
		
		bm = Bitmap.createScaledBitmap(bmp, 1080, 1920, true);   
        
	}

	public Bitmap getMyBackground(){
		if (bm == null){
			Bitmap bmp = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.gb)).getBitmap();
			bm = Bitmap.createScaledBitmap(bmp, 1080, 1920, true);   
		}
		return bm;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}


	
	public void oneDraw(){
		Canvas canvas = getHolder().lockCanvas();

		canvas.drawBitmap(bm, 0,0, null);
		
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setStyle(Paint.Style.FILL);  
		float f1 = new Random().nextFloat()*1000;
		float f2 = new Random().nextFloat()*1000;
		float f3 = new Random().nextFloat() * 50;
		// Log.i("hehehe", f1+" "+f2);	
		canvas.drawCircle(f1, f2, f3, p);
		
		getHolder().unlockCanvasAndPost(canvas);
	}
	


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = holder.lockCanvas();
		canvas.drawBitmap(bm, 0,0, null);
		holder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	

}
