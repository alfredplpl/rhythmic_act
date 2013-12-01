package action;

import java.awt.Image;
import java.awt.geom.Rectangle2D;

public abstract class Item {
	public double x, y;
	double vx, vy, ax, ay;
	double sizex, sizey;
	static public double ssizex=25,ssizey=25;
	int score;
	Rectangle2D.Double rect;
	
	Image[][] animation;
	int state;
	int frame;
	
	boolean isDestroy;

	public static final int STATE_TYPE = 1;
	public static final int STAND = 0;

	public static final int MAX_FRAME = 1;
	public static final int STAND_FRAME = 1;

	
	static final double gframe = 9.8 / 59.88;

	public static final int[] frame_limit={
		STAND_FRAME
		};
	
	Item(ImageKeeping ik,double x,double y,double sizex,double sizey,double vy,String name){
		isDestroy=false;
		state = STAND;
		frame = 0;
		
		animation=ik.getImage(name);
		this.x=x;
		this.y=y;
		this.vy=vy;
		this.sizex = sizex;
		this.sizey = sizey;
		rect = new Rectangle2D.Double(	x-0.5*sizex,y+0.5*sizey,sizex,sizey);
	}
	
	public void move() {
		vx += ax;
		vy += ay;
		x += vx;
		y += vy;

		//画面外に出る
		if(y<-100){
			destroy();
		}else if(y>Action.dispY+100){
			destroy();
		}
		
		if(x<World.OFFSCREEN_MIN_X){
			destroy();
		}else if(x>World.OFFSCREEN_MAX_X){
			destroy();
		}
		/////////////////////
		
		rect.setRect(	x-0.5*sizex,y+0.5*sizey,sizex,sizey);

		ay = -gframe;
		
	}
	
	protected boolean isHit(Rectangle2D.Double ba){
		boolean x=rect.width*0.5+ba.width*0.5>=Math.abs((rect.x+rect.width*0.5)-(ba.x+ba.width*0.5));
		boolean y=rect.height*0.5+ba.height*0.5>=Math.abs((rect.y-rect.height*0.5)-(ba.y-ba.height*0.5));
		
		return x&&y;
	}

	public Image getImage() {
		frame++;
		if (frame >= frame_limit[state])
			frame = 0;

		return animation[state][frame];
	}
	
	/**
	 * あたり判定を設定し直します。世界が動いたときに使います。
	 *
	 */
	public void resetArea(){
		rect.setRect(x-0.5*sizex, y +0.5*sizey, sizex, sizey);
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
}
