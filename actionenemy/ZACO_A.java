package actionenemy;

import java.awt.geom.Rectangle2D;

import action.ImageKeeping;

/**
 * エネミーの実装例です。
 * 
 * @author みそかつおでん
 *
 */
public class ZACO_A extends Enemy{

	public ZACO_A(ImageKeeping ik, double x, double y,double vy){
		super(ik, x, y, ssizex, ssizey, vy,"ZACO_A");
		score=1000;
	}
	@Override
	public void move(){
		super.move();
		if(vx==0.0 && vy==0.0){
			vx=-2.0;
			state=LEFT_MOVE;
		}
	}
	@Override
	public void isHitBarrier(Rectangle2D.Double ba){
		if(isHit(ba)){
			double ceny=(rect.y-rect.height*0.5)-(ba.y-ba.height*0.5);
			double cenx=(rect.x+rect.width*0.5)-(ba.x+ba.width*0.5);
			
			//System.out.println("intersects");
			if(Math.abs(ceny)>=Math.abs(cenx)){
				if(ceny>0){
					if(vy<0.0){vy=0.0;vx=-2.0;state= (state==JUMP ? LEFT_MOVE :state);y=ba.y+rect.height*0.5;}
					if(ay<0.0){ay=0.0;}
				}else if(ceny<0){
					if(vy>0.0){	vy=0.0;y=ba.y-ba.height-rect.height*0.5;}
					if(ay>0.0){	ay=0.0;}
				}
			}else if(Math.abs(ceny)<Math.abs(cenx)-3){
				if(cenx>0){
					if(vx<0.0){	state=RIGHT_MOVE;vx=-vx;x=ba.x+ba.width+rect.width*0.5;}
					if(ax<0.0){	ax=0.0;}
				}else if(cenx<0){
					if(vx>0.0){	state=LEFT_MOVE;vx=-vx;x=ba.x-rect.width*0.5;}
					if(ax>0.0){	ax=0.0;}
				}
			}
		}
	}
}
