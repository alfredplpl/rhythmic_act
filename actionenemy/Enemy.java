package actionenemy;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import action.Action;
import action.ImageKeeping;
import action.LoadImage;
import action.World;

import keyio.KeyTable;

/**
 * すべてのエネミーのひな形となるクラスです。
 * すべてのエネミーはこのクラスを引き継いでいなければなりません。
 * なお、コンストラクタは適当なので、
 * 適当に変更してください。
 * @author みそかつおでん
 *
 */
public abstract class Enemy{
	double x, y;
	double vx, vy, ax, ay;
	double sizex, sizey;
	int score;
	Rectangle2D.Double rect;
	
	Image[][] animation;
	int state;
	int frame;
	int motionFrame;
	
	boolean isDestroy;
	
	static public double ssizex=64,ssizey=64;

	public static final int STATE_TYPE = 4;
	public static final int STAND = 0;
	public static final int JUMP = 1;
	public static final int LEFT_MOVE = 2;
	public static final int RIGHT_MOVE = 3;
	
	public static final int MAX_FRAME = 4;
	public static final int STAND_FRAME = 1;
	public static final int JUMP_FRAME = 1;
	public static final int LEFT_MOVE_FRAME = 4;
	public static final int RIGHT_MOVE_FRAME = 4;
	
	static final double gframe = 9.8 / 59.88;

	public static final int[] frame_limit={
		STAND_FRAME,
		JUMP_FRAME,
		LEFT_MOVE_FRAME,
		RIGHT_MOVE_FRAME};
	
	//生成されてから左右に動かない時間
	int waitFrame;
	
	Enemy(ImageKeeping ik,double x,double y,double sizex,double sizey,double vy,String name){
		isDestroy=false;
		state = JUMP;
		frame = 0;
		motionFrame=0;
		waitFrame=20;
		/*
		animation = new Image[STATE_TYPE][MAX_FRAME];
		animation[STAND][0] = new BufferedImage((int)sizex, (int)sizey,BufferedImage.TYPE_3BYTE_BGR);
		animation[STAND][0].getGraphics().setColor(Color.cyan);
		animation[STAND][0].getGraphics().fillRect(0, 0, (int)sizex, (int)sizey);
		 */
		
		animation=ik.getImage(name);
		this.x=x;
		this.y=y;
		this.vy=vy;
		this.sizex = sizex;
		this.sizey = sizey;
		rect = new Rectangle2D.Double(	x-0.5*sizex,y+0.5*sizey,sizex,sizey);
	}
	
	public void move() {
		if(waitFrame<0){
			vx += ax;
			x += vx;
			vy += ay;
		}else{
			waitFrame--;
		}
		y += vy;

		//画面外に出る
		if(y<-100){
			destroy();
		}
		/*else if(y>Action.dispY+100){
			destroy();
		}
		*/
		if(x<World.OFFSCREEN_MIN_X){
			destroy();
		}else if(x>World.OFFSCREEN_MAX_X){
			destroy();
		}
		/////////////////////
		
		rect.setRect(	x-0.5*sizex,y+0.5*sizey,sizex,sizey);

		ay = -gframe;
		
	}
	
	public void isHitBarrier(Rectangle2D.Double ba){
		if(isHit(ba)){
			double ceny=(rect.y-rect.height*0.5)-(ba.y-ba.height*0.5);
			double cenx=(rect.x+rect.width*0.5)-(ba.x+ba.width*0.5);
			
			//System.out.println("intersects");
			if(Math.abs(ceny)>=Math.abs(cenx)){
				if(ceny>0){
					if(vy<0.0){vy=0.0;state= (state==JUMP ? LEFT_MOVE :state);y=ba.y+rect.height*0.5;}
					if(ay<0.0){ay=0.0;}
				}else if(ceny<0){
					if(vy>0.0){	vy=0.0;y=ba.y-ba.height-rect.height*0.5;}
					if(ay>0.0){	ay=0.0;}
				}
			}else{
				if(cenx>0){
					if(vx<0.0){	vx=-vx;x=ba.x+ba.width+rect.width*0.5;}
					if(ax<0.0){	ax=0.0;}
				}else if(cenx<0){
					if(vx>0.0){	vx=-vx;x=ba.x-rect.width*0.5;}
					if(ax>0.0){	ax=0.0;}
				}
			}
		}
	}
	
	protected boolean isHit(Rectangle2D.Double ba){
		/*
		boolean top=rect.height*0.5+ba.height*0.5>=rect.getCenterY()-ba.getCenterY();
		boolean bottom=rect.height*0.5+ba.height*0.5>=ba.getCenterY()-rect.getCenterY();
		boolean left=rect.width*0.5+ba.width*0.5>=ba.getCenterX()-rect.getCenterX();
		boolean right=rect.width*0.5+ba.width*0.5>=rect.getCenterX()-ba.getCenterX();
		
		if( top&&left || top&&right || bottom&&left || bottom&&right)return true;
		*/
		boolean x=rect.width*0.5+ba.width*0.5>=Math.abs((rect.x+rect.width*0.5)-(ba.x+ba.width*0.5));
		boolean y=rect.height*0.5+ba.height*0.5>=Math.abs((rect.y-rect.height*0.5)-(ba.y-ba.height*0.5));
		
		return x&&y;
	}


	public Image getImage() {
		frame++;
		if(frame>=4){
			motionFrame++;
			frame=0;
		}
		if(motionFrame>=frame_limit[state])motionFrame=0;
		
		return animation[state][motionFrame];
	}
	
	public void destroy(){
		isDestroy=true;
	}
	
	public boolean isDestroyed(){
		return isDestroy;
	}
	
	public Rectangle2D.Double getArea(){
		return rect;
	}
	
	public int getScore(){
		return score;
	}
	
	public void moveX(double vx){
		if(waitFrame<0)x+=vx;
	}
}
