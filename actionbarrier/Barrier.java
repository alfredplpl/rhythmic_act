package actionbarrier;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import action.ImageKeeping;
import action.LoadImage;

public abstract class Barrier{
	public double x, y;
	double vx,vy,ax,ay;
	double sizex,sizey;
	Rectangle2D.Double rect;
	public static final double ssizex=25,ssizey=25;
	
	Image[][] animation;
	int state;
	int frame;

	boolean isDestroy;
	
	public static final int STATE_TYPE = 1;
	public static final int STAND = 0;
	
	public static final int MAX_FRAME = 1;
	public static final int STAND_FRAME = 1;

	static final double gframe=9.8/59.88;
	
	public static final int[] frame_limit={
		STAND_FRAME};
	
	/**
	 * あたり判定に関する規定です。このビットを持つ障害物は、上方向に当たり判定を持ちます。
	 */
	public static final int HIT_TOP=0x01;
	public static final int HIT_BOTTOM=0x02;
	public static final int HIT_LEFT=0x04;
	public static final int HIT_RIGHT=0x08;
	
	int hitVector;
	
	Barrier(ImageKeeping ik,double x,double y,double sizex,double sizey,String name){
		state = STAND;
		frame = 0;
		hitVector=HIT_TOP|HIT_BOTTOM|HIT_LEFT|HIT_RIGHT;
		/*
		frame_limit[STAND] = STAND_FRAME;
		
		animation =new Image[STATE_TYPE][MAX_FRAME];
		animation[STAND][0] = new BufferedImage((int)sizex, (int)sizey,BufferedImage.TYPE_INT_ARGB);
		animation[STAND][0].getGraphics().setColor(Color.black);
		animation[STAND][0].getGraphics().fillRect(0, 0, (int)sizex, (int)sizey);
		*/
		
		animation =ik.getImage(name);
		//のように画像を取得してください。

			
		this.x=x;
		this.y=y;
		this.sizex=sizex;
		this.sizey=sizey;
		rect = new Rectangle2D.Double(	x-0.5*sizex, y +0.5*sizey, sizex, sizey);

	}
	
	public void move() {
		vx += ax;
		vy += ay;
		x += vx;
		y += vy;

		rect.setRect(	x-0.5*sizex, y +0.5*sizey, sizex, sizey);

	}

	public Image getImage() {
		frame++;
		if (frame >= frame_limit[state])
			frame = 0;

		return animation[state][frame];
	}
	
	public void destroy(){

	}
	
	public boolean isDestroied(){
		return isDestroy;
	}
	
	public Rectangle2D.Double getArea(){
		return rect;
	}
	
	/**
	 * あたり判定を設定し直します。世界が動いたときに使います。
	 *
	 */
	public void resetArea(){
		rect.setRect(x-0.5*sizex, y +0.5*sizey, sizex, sizey);
	}
	
	/**
	 * 左上のx座標値を取得します。描画時に使用してください。
	 * @return　左上のx座標値
	 */
	public int getUpperLeftX(){
		return (int) rect.getMinX();
	}
	
	/**
	 * 左上のy座標値を取得します。描画時に使用してください。
	 * @return　左上のy座標値
	 */
	public int getUpperLeftY(){
		return (int) rect.getMinY();
	}
	
	/**
	 * 指定の面にあたり判定があるかどうか調べます。
	 * ２つ以上指定する際は、HIT_TOP|HIT_BOTTOMのようにしてください。
	 * @param hit オブジェクトにヒットする方向
	 * @return　その当たる方向に判定を持つかどうか
	 */
	public boolean isHitVector(int hit){
		return (hitVector&hit)==hit;
	}
}

