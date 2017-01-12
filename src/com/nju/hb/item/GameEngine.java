package com.nju.hb.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.nju.hb.GameActivity;
import com.nju.hb.GameView;

public class GameEngine {
	
	public static final int BALL_SPEED_RATE = GameActivity.FPS * 5;	
	
	public static final long THOROUGH_TIME = GameActivity.FPS * 6;
	public static final long POWER_TIME = GameActivity.FPS * 3;
	
	public static double CHANCE_ITEM = 7.0;
	
	
	GameView gv;
	BottomPanel bp;
	Canvas canvas;
	
	int true_width = 0, true_height = 0;
	
	HashSet<Ball> set_ball;
	HashSet<Brick> set_brick;
	
	HashSet<Item> set_item;
	
	boolean isOver;
	
	public GameEngine(GameView gv, BottomPanel bp){
		this.gv = gv;
		this.bp = bp;
		
		set_ball = new HashSet<Ball>();
		Ball b = new Ball(bp.getXforBall(), bp.getYforBall());
		set_ball.add(b);
		
		set_item = new HashSet<Item>();
		
		generate_bricks_0();
		
		
		Item i1 = new Item(0);
		set_item.add(i1);
		
		Item i2 = new Item(1);
		i2.y = 60;
		i2.x = 800;
		set_item.add(i2);
		
		
		isOver = false;
		
	}
	
	
	private void generate_bricks_0(){
		set_brick = new HashSet<Brick>();
		Brick br0 = new Brick(40,60);
		br0.randomLevel();
		set_brick.add(br0);
		
		Brick br1 = new Brick(300,60);
		br1.randomLevel();
		set_brick.add(br1);
		
		Brick br2 = new Brick(560,60);
		br2.randomLevel();
		set_brick.add(br2);
		
		Brick br3 = new Brick(40, 240);
		br3.randomLevel();
		set_brick.add(br3);
		
		Brick br4 = new Brick(300, 240);
		br4.randomLevel();
		set_brick.add(br4);
		
		Brick br5 = new Brick(560, 240);
		br5.randomLevel();
		set_brick.add(br5);
		
		Brick br33 = new Brick(40, 400);
		br33.randomLevel();
		set_brick.add(br33);
		
		Brick br44 = new Brick(300, 400);
		br44.randomLevel();
		set_brick.add(br44);
		
		Brick br55 = new Brick(560, 400);
		br55.randomLevel();
		set_brick.add(br55);
		
		Brick br333 = new Brick(40, 560);
		br333.randomLevel();
		set_brick.add(br333);
		
		Brick br444 = new Brick(300, 560);
		br444.randomLevel();
		set_brick.add(br444);
		
		Brick br555 = new Brick(560, 560);
		br555.randomLevel();
		set_brick.add(br555);
		
		Brick br3333 = new Brick(40, 720);
		br3333.randomLevel();
		set_brick.add(br3333);
		
		Brick br4444 = new Brick(300, 720);
		br4444.randomLevel();
		set_brick.add(br4444);
		
		Brick br5555 = new Brick(560, 720);
		br5555.randomLevel();
		set_brick.add(br5555);
	}
	
	
	public void oneWork(Canvas canvas) {
		this.canvas = canvas;
		
		if (isOver) return;
		
		if (true_width == 0 && true_height == 0){
			// 显然 这是第一次正确绘制 游戏肯定还没开始
			true_width = canvas.getWidth();
			true_height = canvas.getHeight();
			bp.adjustPos(true_width, true_height);
			
			for (Ball b : set_ball){
				b.y = bp.y - Ball.BALL_SIZE;
			}
		}
		
		// 需要写碰撞检测逻辑
		// 需要改变球
		
		check_die();	// 如果挂了 其实会广播直接结束
		
		change_direct_for_ball();
		
		
		ball_state_modify();
		
		collision_wall();
		
		collision_brick();	//这里判断物品
		
		
		ballMove();
		
		
		
		drawBrick();
		
		moveItem();
		
		drawItem();
		
		drawBall();
		
		drawPanel();
		
		
	}
	
	private void moveItem() {
		List<Item> li = new ArrayList<Item>();
		li.addAll(set_item);
		
		for (Item i : li){
			i.y += Item.DOWN_SPEED;
			
			if (i.y + Item.HEIGHT >= BottomPanel.BOTTOM - BottomPanel.FIXED_HEIGHT){
				if (i.x <= bp.x + bp.length / 2 && i.x + Item.WIDTH >= bp.x - bp.length / 2){
					//碰到了
					
					switch(i.type){
					case 0:
						// 透明球
						for (Ball b : set_ball){
							b.isPower = false;
							b.isThorough = true;
							b.thorough_left = THOROUGH_TIME;
						}
						break;
					case 1:
						// 威力球
						for (Ball b : set_ball){
							b.isThorough = false;
							b.isPower = true;
							b.power_left = POWER_TIME;
						}
						break;
					}
					
					
					set_item.remove(i);
				}
			}
			
			if (i.y > bp.BOTTOM && set_item.contains(i)) set_item.remove(i);
		}

	}


	private void drawItem() {
		for (Item i : set_item){
			switch(i.type){
			case 0:
				canvas.drawBitmap(Item.bm[0], i.x, i.y, null);
				break;
			case 1:
				canvas.drawBitmap(Item.bm[1], i.x, i.y, null);
				break;
			}
		}
		
	}


	private void collision_brick() {
		for (Ball b : set_ball){
			List<Brick> brl = new ArrayList<Brick>();
			brl.addAll(set_brick);
			
			if (b.isThorough && b.thoroughingID != -1){
				Brick what = null;
				for (Brick bb : brl){
					if (bb.id == b.thoroughingID){
						what = bb;
						break;
					}
				}
				if (what == null){
					b.thoroughingID = -1;
				}
				else{
					// 判断是否还在穿越
					int ball_top = b.y - Ball.BALL_SIZE;
					int ball_bottom = b.y + Ball.BALL_SIZE;
					int ball_left = b.x - Ball.BALL_SIZE;
					int ball_right = b.x + Ball.BALL_SIZE;
					
					int i1 = what.left;
					int i2 = what.top;
					int i3 = what.right;
					int i4 = what.bottom;
					
					if (ball_top <= i4 && ball_bottom >= i2 && ball_left <= i3 && ball_right >= i1){
						continue;
					}
					else b.thoroughingID = -1;
						
					
					
					
					
				}
			}
			
			for (Brick br : brl){

				// 判断 b 和 br 是否相撞
				int i1 = br.left;
				int i2 = br.top;
				int i3 = br.right;
				int i4 = br.bottom;
				
				int ball_top = b.y - Ball.BALL_SIZE;
				int ball_bottom = b.y + Ball.BALL_SIZE;
				int ball_left = b.x - Ball.BALL_SIZE;
				int ball_right = b.x + Ball.BALL_SIZE;
				
				if (ball_top > i4 || ball_bottom < i2 || ball_left > i3 || ball_right < i1){
					// 没有碰撞
					continue;
				}

				if (!b.isThorough){
				
				
					// 已经确定碰撞
					// 先改方向
					if (b.dx > 0 && b.dy < 0){
						// 左边 或者 下边
						double thx = i4 - b.y;
						if (thx > Ball.BALL_SIZE / 2){
							//左边
							b.dx *= -1;
						}
						else{
							//下边
							b.dy *= -1;
						}
						
						
						
					}
					else if (b.dx > 0 && b.dy > 0){
						// 左边 或者 上边
						double thx = b.y - i2;
						if (thx > Ball.BALL_SIZE / 2){
							//左边
							b.dx *= -1;
						}
						else{
							//上边
							b.dy *= -1;
						}
						
					}
					else if (b.dx < 0 && b.dy < 0){
						// 右边 或者 下边
						double thx = i4 - b.y;
						if (thx > Ball.BALL_SIZE / 2){
							//右边
							b.dx *= -1;
						}
						else{
							//下边
							b.dy *= -1;
						}
					}
					else if (b.dx < 0 && b.dy > 0){
						// 右边 或者 上边
						double thx = b.y - i2;
						if (thx > Ball.BALL_SIZE / 2){
							//右边
							b.dx *= -1;
						}
						else{
							//上边
							b.dy *= -1;
						}
					}
				}
				else{
					if (b.thoroughingID != -1) continue;	// 直接考虑下个砖块
					b.thoroughingID = br.id;
				}
				
				tryGenerateAnItem(br);
				
				
				if (!b.isPower) br.setLevel(br.level - 1);
				else br.setLevel(-1);
				
				if (br.level == -1){
					set_brick.remove(br);
					if (set_brick.size() == 0){
						Message msg = new Message();
						msg.what = 1;
						GameActivity.myhandler.sendMessage(msg);
						isOver = true;
					}
				}
				
				
			}
		}
		
	}


	private void tryGenerateAnItem(Brick br) {
		double i1 = Math.random() * 100;
		if (i1 > CHANCE_ITEM) return;
		
		
		Item i = Item.generateAnItem();
		i.x = (br.left + br.right) / 2 - Item.WIDTH / 2;
		i.y = br.bottom;
		
		set_item.add(i);
		
	}


	private void change_direct_for_ball() {
		for (Ball b : set_ball){
			if (b.isStopped) continue;
			if (b.y + Ball.BALL_SIZE> bp.BOTTOM - BottomPanel.FIXED_HEIGHT){

				if (b.x + Ball.BALL_SIZE < bp.x - bp.length / 2 || b.x - Ball.BALL_SIZE > bp.x + bp.length / 2) continue;
				
				
				// 显然 代表一次碰撞 因为死亡的情况已经检测过了
				double ndx = Math.random();
				
				while (ndx > 0.8) ndx = Math.random();
				
				double ndy = Math.sqrt(1.0 - ndx * ndx);

				// Log.i("hehe","之前:" +b.dx +" "+b.dy);
				
				if (b.dx > 0){
					b.dx = ndx;
					b.dy = -ndy;
				}
				else{
					b.dx = -ndx;
					b.dy = -ndy;
				}
				if (b.y > bp.BOTTOM - BottomPanel.FIXED_HEIGHT - Ball.BALL_SIZE) b.y = bp.BOTTOM - BottomPanel.FIXED_HEIGHT - Ball.BALL_SIZE;
				// Log.i("hehe","之后:" +b.dx +" "+b.dy);
				
			}
		}
		
	}


	private void check_die(){
		List<Ball> bl = new ArrayList<Ball>();
		bl.addAll(set_ball);
		
		for (Ball b : bl){
			if (b.isStopped) continue;
			if (b.y - Ball.BALL_SIZE >= bp.BOTTOM){
				// 检测是否撞了挡板
				// Log.i("hehe", b.x + " "+b.y +" "+ " "+bp.x+" "+bp.length);
				if (b.x + Ball.BALL_SIZE >= bp.x - bp.length / 2 && b.x - Ball.BALL_SIZE <= bp.x + bp.length / 2) continue;
				// Log.i("hehe","这个挂了");
				set_ball.remove(b);
			}
		}
		
		if (set_ball.size() == 0){
			Message msg = new Message();
			msg.what = 0;
			GameActivity.myhandler.sendMessage(msg);
			isOver = true;
		}
		
	}
	
	private void ball_state_modify() {
		
		for (Ball b: set_ball){
			
			if (!b.isStopped && b.isThorough && --b.thorough_left <= 0){
				b.isThorough = false;
				b.thoroughingID = -1;
			}
			
			if (!b.isStopped && b.isPower && --b.power_left <= 0){
				b.isPower = false;
			}
			
			b.speed_start--;
			if (b.speed_start == 0){
				b.increateSpeed();
				b.speed_start = BALL_SPEED_RATE;
			}
			
		}
		
	}


	private void collision_wall() {
		for (Ball b: set_ball){
			int x = b.x;
			int y = b.y;
			
			if (x - Ball.BALL_SIZE < 0 || x + Ball.BALL_SIZE > bp.RIGHT){
				b.dx *= -1;
				if (x - Ball.BALL_SIZE < 0) b.x = Ball.BALL_SIZE;
				if (x + Ball.BALL_SIZE > bp.RIGHT) b.x = bp.RIGHT - Ball.BALL_SIZE;
			}
			if (y - Ball.BALL_SIZE < 0){
				b.dy *= -1;
				b.y = Ball.BALL_SIZE;
			}
			
			
		}
		
	}


	private void ballMove() {
		for (Ball b : set_ball){
			if (b.isStopped) continue;
			b.x = (int) (b.x + b.dx * b.speed);
			b.y = (int) (b.y + b.dy * b.speed);
		}
		
	}
	private void drawBrick(){
		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);
		for (Brick b: set_brick){
			p.setColor(b.cl);
			canvas.drawRect(b.generateRect(), p);
		}
	}
	
	private void drawBall(){
		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);
		for (Ball b: set_ball){
			p.setColor(b.generateColor());
			canvas.drawCircle(b.x, b.y, Ball.BALL_SIZE, p);
		}
	}
	
	private void drawPanel() {
		Rect rc = bp.generateRect();

		// RectF rf = bp.generateRectF();
		
		
		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);
		p.setColor(Color.GRAY);

		//canvas.drawArc(rf, -180, 180, false, p);
		
		canvas.drawRect(rc, p);
	}
	public void adjustBallIfStopped(float change) {
		for (Ball b : set_ball){
			if (b.isStopped){
				b.x = bp.x + b.diff;
			}
		}
		
	}
	public void releaseBallIfPossible() {
		for (Ball b: set_ball){
			if (b.isStopped){
				b.isStopped = false;
				b.speed_start = BALL_SPEED_RATE;	//为1500情况下 30秒后会加速	即过去1500次OneWork
				Log.i("hehe","一个球被释放了");
			}
		}
		
	}
	
}
