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
	
	public static final int BALL_SPEED_RATE = 3000;
	
	GameView gv;
	BottomPanel bp;
	Canvas canvas;
	
	int true_width = 0, true_height = 0;
	
	HashSet<Ball> set_ball;
	HashSet<Brick> set_brick;
	
	boolean isOver;
	
	public GameEngine(GameView gv, BottomPanel bp){
		this.gv = gv;
		this.bp = bp;
		
		set_ball = new HashSet<Ball>();
		Ball b = new Ball(bp.getXforBall(), bp.getYforBall());
		set_ball.add(b);
		
		generate_bricks_0();
		
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
	}
	
	
	public void oneWork(Canvas canvas) {
		this.canvas = canvas;
		
		if (isOver) return;
		
		if (true_width == 0 && true_height == 0){
			// ��Ȼ ���ǵ�һ����ȷ���� ��Ϸ�϶���û��ʼ
			true_width = canvas.getWidth();
			true_height = canvas.getHeight();
			bp.adjustPos(true_width, true_height);
			
			for (Ball b : set_ball){
				b.y = bp.y - Ball.BALL_SIZE;
			}
		}
		
		// ��Ҫд��ײ����߼�
		// ��Ҫ�ı���
		
		check_die();	// ������� ��ʵ��㲥ֱ�ӽ���
		
		change_direct_for_ball();
		
		
		ball_state_modify();
		
		collision_wall();
		
		collision_brick();
		
		
		ballMove();
		
		drawPanel();
		
		drawBrick();
		
		drawBall();
		
		
	}
	
	private void collision_brick() {
		for (Ball b : set_ball){
			List<Brick> brl = new ArrayList<Brick>();
			brl.addAll(set_brick);
			for (Brick br : brl){
				// �ж� b �� br �Ƿ���ײ
				int i1 = br.left;
				int i2 = br.top;
				int i3 = br.right;
				int i4 = br.bottom;
				
				int ball_top = b.y - Ball.BALL_SIZE;
				int ball_bottom = b.y + Ball.BALL_SIZE;
				int ball_left = b.x - Ball.BALL_SIZE;
				int ball_right = b.x + Ball.BALL_SIZE;
				
				if (ball_top >= i4 || ball_bottom <= i2 || ball_left >= i3 || ball_right <= i1) continue;
				
				// �Ѿ�ȷ����ײ
				// �ȸķ���
				if (b.dx > 0 && b.dy < 0){
					// ��� ���� �±�
					double thx = i4 - b.y;
					if (thx > Ball.BALL_SIZE / 2){
						//���
						b.dx *= -1;
					}
					else{
						//�±�
						b.dy *= -1;
					}
					
					
					
				}
				else if (b.dx > 0 && b.dy > 0){
					// ��� ���� �ϱ�
					double thx = b.y - i2;
					if (thx > Ball.BALL_SIZE / 2){
						//���
						b.dx *= -1;
					}
					else{
						//�ϱ�
						b.dy *= -1;
					}
					
				}
				else if (b.dx < 0 && b.dy < 0){
					// �ұ� ���� �±�
					double thx = i4 - b.y;
					if (thx > Ball.BALL_SIZE / 2){
						//�ұ�
						b.dx *= -1;
					}
					else{
						//�±�
						b.dy *= -1;
					}
				}
				else if (b.dx < 0 && b.dy > 0){
					// �ұ� ���� �ϱ�
					double thx = b.y - i2;
					if (thx > Ball.BALL_SIZE / 2){
						//�ұ�
						b.dx *= -1;
					}
					else{
						//�ϱ�
						b.dy *= -1;
					}
				}
				
				br.setLevel(br.level - 1);
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


	private void change_direct_for_ball() {
		for (Ball b : set_ball){
			if (b.isStopped) continue;
			if (b.y > bp.BOTTOM - BottomPanel.FIXED_HEIGHT - Ball.BALL_SIZE){

				// ��Ȼ ����һ����ײ ��Ϊ����������Ѿ�������
				double ndx = Math.random();
				double ndy = Math.sqrt(1.0 - ndx * ndx);

				// Log.i("hehe","֮ǰ:" +b.dx +" "+b.dy);
				
				if (b.dx > 0){
					b.dx = ndx;
					b.dy = -ndy;
				}
				else{
					b.dx = -ndx;
					b.dy = -ndy;
				}
				// if (b.y >= bp.BOTTOM) b.y = bp.BOTTOM - BottomPanel.FIXED_HEIGHT - 1;
				// Log.i("hehe","֮��:" +b.dx +" "+b.dy);
				
			}
		}
		
	}


	private void check_die(){
		List<Ball> bl = new ArrayList<Ball>();
		bl.addAll(set_ball);
		
		for (Ball b : bl){
			if (b.isStopped) continue;
			if (b.y > bp.BOTTOM){
				// ����Ƿ�ײ�˵���
				// Log.i("hehe", b.x + " "+bp.x+" "+bp.length);
				if (b.x + Ball.BALL_SIZE >= bp.x - bp.length / 2 && b.x - Ball.BALL_SIZE <= bp.x + bp.length / 2) continue;
				// Log.i("hehe","�������");
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
		// TODO ��״̬������������ߴ�͸ ��������
		for (Ball b: set_ball){
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
			
			if (x <= 0 || x >= bp.RIGHT){
				b.dx *= -1;
			}
			if (y <= 0){
				b.dy *= -1;
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
				b.speed_start = BALL_SPEED_RATE;	//Ϊ1500����� 30�������	����ȥ1500��OneWork
				Log.i("hehe","һ�����ͷ���");
			}
		}
		
	}
	
}
