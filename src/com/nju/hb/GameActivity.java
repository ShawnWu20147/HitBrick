package com.nju.hb;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.nju.hb.item.BottomPanel;
import com.nju.hb.item.GameEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

public class GameActivity extends Activity {
	
	public static final int FPS = 100;
	
	GameView gv;
	Bitmap bm;
	
	Thread t;
	
	volatile boolean isStopped, isEnd;
	
	Canvas canvas;
	
	GameEngine ge;
	
	GestureDetector gd;
	
	public static Handler myhandler;
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		Toast.makeText(this, "˫����Ļ�ͷ���", Toast.LENGTH_SHORT).show();
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_game);
		
		gv = (GameView)findViewById(R.id.gameview);
		
		bm = gv.getMyBackground();
		

		t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true){
					try {
						Thread.currentThread().sleep(1000 / FPS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (isEnd) return;
					if (isStopped) continue;
					
					// һ��ִ��100������ķ���
					oneDraw();
				}
				
			}
		});
		
		t.start();
		
		isStopped = false;
		isEnd = false;
		
		
		initialGame();
		
		myhandler = new MessageHandler();
		
		
	}

	
	BottomPanel bp;
	
	
	@SuppressWarnings("deprecation")
	private void initialGame() {
		bp = new BottomPanel();
		
		ge = new GameEngine(gv, bp);
		
		gd = new GestureDetector(new MyGestureListener(ge));
	}
		


	public void oneDraw(){
		canvas = gv.getHolder().lockCanvas();


		if (canvas == null) return;
		
		canvas.drawBitmap(bm, 0, 0, null);

		ge.oneWork(canvas);
		
		
		
		
		gv.getHolder().unlockCanvasAndPost(canvas);
		
		
	}
	
	



	float beforeX;
	float beforePX;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			beforeX = event.getRawX();
			beforePX = bp.getX();
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE){
			float x1 = event.getRawX();
			float change = x1 - beforeX;
			// Log.i("hehe",String.valueOf(change));
			bp.setX((int)(beforePX + change));
			// Log.i("hehe", String.valueOf(bp.x));
			ge.adjustBallIfStopped(change);
			
		}
		return super.onTouchEvent(event);
	}






	@Override
	public void onBackPressed() {
		isStopped = true;
		new AlertDialog.Builder(GameActivity.this)   
		.setTitle("������ʲô")  
		.setMessage("��ѡ��")  
		.setPositiveButton("������Ϸ", new DialogInterface.OnClickListener(){           
			@Override 
            public void onClick(DialogInterface dialog, int which) {
				isStopped = false;
				
				
            }
		})  
		.setNegativeButton("�˳�����", new DialogInterface.OnClickListener(){ 
            @Override 
            public void onClick(DialogInterface dialog, int which) {
            	setClose();
            }
            })  
		.show();  
	}
	
	
	
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		isEnd = true;
		
	}



	public void setClose(){
		super.onBackPressed();
	}
	
	
	
	class MessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if (msg.what == 0){
				//����
				Toast.makeText(GameActivity.this, "������", Toast.LENGTH_SHORT).show();
				GameActivity.this.finish();
			}
			else if (msg.what == 1){
				//Ӯ��
				Toast.makeText(GameActivity.this, "��Ӯ��", Toast.LENGTH_SHORT).show();
				GameActivity.this.finish();
			}
		}

	}
	
	
}
