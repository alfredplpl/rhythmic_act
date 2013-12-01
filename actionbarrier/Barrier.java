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
	 * �����蔻��Ɋւ���K��ł��B���̃r�b�g������Q���́A������ɓ����蔻��������܂��B
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
		//�̂悤�ɉ摜���擾���Ă��������B

			
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
	 * �����蔻���ݒ肵�����܂��B���E���������Ƃ��Ɏg���܂��B
	 *
	 */
	public void resetArea(){
		rect.setRect(x-0.5*sizex, y +0.5*sizey, sizex, sizey);
	}
	
	/**
	 * �����x���W�l���擾���܂��B�`�掞�Ɏg�p���Ă��������B
	 * @return�@�����x���W�l
	 */
	public int getUpperLeftX(){
		return (int) rect.getMinX();
	}
	
	/**
	 * �����y���W�l���擾���܂��B�`�掞�Ɏg�p���Ă��������B
	 * @return�@�����y���W�l
	 */
	public int getUpperLeftY(){
		return (int) rect.getMinY();
	}
	
	/**
	 * �w��̖ʂɂ����蔻�肪���邩�ǂ������ׂ܂��B
	 * �Q�ȏ�w�肷��ۂ́AHIT_TOP|HIT_BOTTOM�̂悤�ɂ��Ă��������B
	 * @param hit �I�u�W�F�N�g�Ƀq�b�g�������
	 * @return�@���̓���������ɔ���������ǂ���
	 */
	public boolean isHitVector(int hit){
		return (hitVector&hit)==hit;
	}
}

