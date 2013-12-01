package action;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import oto.OtoEvent;

import actionbarrier.Barrier;

import flow.Flow;

import keyio.KeyTable;

/**
 * プレーヤーキャラクターです。
 * 基本的にこのキャラが動きます。
 * このキャラがx>0かつx>Flow.dispXの範囲の外に動こうとすると
 * キャラが動く代わりに世界が動きます。
 * 
 * @author みそかつおでん
 *
 */
public final class MASAO{
	double x,y,vx,vy,ax,ay;
	static public double sizex=64,sizey=64;
	static public double sizex_big=100,sizey_big=100;
	//あたり判定(18,0)~(45,64)
	Rectangle2D.Double rect;
	Image[][] animation,anim_big;
	Image[][] effect;
	int state;
	int frame,effectFrame;
	
	
	static final double defaultX=100.0;
	static final double defaultY=400.0;
	
	static final double baseVx=5.0;
	static final double fastVx=7.0;
	
	static final int EFFECT_BIG=0;
	static final int EFFECT_SPEEDUP=1;
	static final int EFFECT_INVIN=2;
	static final int[] EFFECT_LIMIT={1,1,1};
	static final int EFFECT_X=125;
	static final int EFFECT_Y=75;
	
	/**
	 * 無敵は画像の点滅で表すので専用の画像は必要ない。
	 */
	boolean isInvincible;
	boolean canMove;
	boolean isBig;
	
	int invinFrame;
	int cannotMoveFrame;
	int speedUpFrame;
	
	private int motionFrame;
	
	static final int STATE_TYPE=4;
	static final int STAND=0;
	static final int JUMP=1;
	static final int RIGHT_MOVE=2;
	static final int LEFT_MOVE=3;
	static final int STAND_LEFT=4;
	static final int JUMP_LEFT=5;
	
	static final int DAMEGE=6;
	
	static final int MAX_FRAME=4;
	static final int STAND_FRAME=1;
	static final int JUMP_FRAME=1;
	static final int LEFT_MOVE_FRAME=4;
	static final int RIGHT_MOVE_FRAME=4;
	static final int STAND_LEFT_FRAME=1;
	static final int JUMP_LEFT_FRAME=1;
	
	static final int DAMEGE_FRAME=1;
	
	public static final int[] frame_limit={
		STAND_FRAME,
		JUMP_FRAME,
		RIGHT_MOVE_FRAME,
		LEFT_MOVE_FRAME,
		STAND_LEFT_FRAME,
		JUMP_LEFT_FRAME};
	
	static final double gframe=9.8/59.88*2.0;
	
	/**
	 * 政夫の初期化をコンストラクタで行います。
	 * @throws Exception 
	 *
	 */
	protected MASAO(ImageKeeping ik) throws Exception{
		state=JUMP;
		frame=0;

		animation=ik.getImage(this.getClass().getSimpleName());
		anim_big=ik.getImage(this.getClass().getSimpleName()+"_BIG");
		//TODO:
		effect=ik.getImage("ActEffect");
		
		vx=vy=ax=0.0;
		x=defaultX;
		y=defaultY;
		ay=-gframe;
		isInvincible=false;
		canMove=true;
		invinFrame=0;
		cannotMoveFrame=0;
		speedUpFrame=0;
		motionFrame=0;
		rect=new Rectangle2D.Double(x+20,y+sizey/2,27,sizey);
		isBig=false;
		
	}
	
	/**
	 * 政夫が敵に当たったかどうかを判定します。
	 * @param en 敵の当たり判定
	 * @return　当たったかどうか
	 */
	protected boolean isHitEnemy(Rectangle2D.Double en){

		if(isHit(en)){
			double ceny=(rect.y-rect.height*0.5)-(en.y-en.height*0.5)-5;
			if(ceny>0){
				//反発係数0.3
				if(vy>=0)vy+=2;
				else	vy=3;
				return true;
			}else{
				if(!isInvincible){
					damege();
					//return true;
				}
				return false;
			}
		}
		return false;
	}
	
	
	
	protected boolean isHitItem(Item item){
		if(isHit(item.getArea())){
			if(item instanceof Frank)big();
			if(item instanceof Crepe)speedup();
			if(item instanceof StarItem)invincible();
			return true;
		}
		return false;
	}
	/**
	 * 政夫が床などの障害物に当たったときの動きを設定します。
	 * 
	 * @param barrier　障害物
	 */
	Barrier nearestX=null;
	Barrier nearestY=null;
	Barrier nearestYTop=null;
	protected void isHitBarrier(ArrayList<Barrier> barrier){
		if(barrier.size()<=0)return;
		double x=rect.x+vx;
		double y=rect.y+vy;
		
		//nearestX=null;
		nearestX=barrier.get(0);
		double nearestDistanceX=Double.MAX_VALUE;
		nearestY=barrier.get(0);
		double nearestDistanceY=Double.MAX_VALUE;
		nearestYTop=barrier.get(0);
		
		for(Barrier b:barrier){
			Rectangle2D.Double ba=b.getArea();

			double ceny=(y-rect.height*0.5)-(ba.y+ba.height*0.5);
			double cenx=x-ba.x;
			//ceny/=(rect.height+ba.height);
			//cenx/=(rect.width+ba.width);
			double distanceX=cenx*cenx;
			double distanceY=ceny*ceny;
			
			if(	y-rect.height*0.5<ba.y+ba.height*0.5 && 
				y+rect.height*0.5>ba.y-ba.height*0.5){
				if(distanceX<nearestDistanceX){
					nearestDistanceX=distanceX;
					nearestDistanceY=distanceY;
					nearestX=b;
				}else if(distanceX==nearestDistanceX && distanceY<nearestDistanceY){
					nearestDistanceX=distanceX;
					nearestDistanceY=distanceY;
					nearestX=b;
				}
			}		
		}
		
		nearestDistanceX=Double.MAX_VALUE;
		nearestDistanceY=Double.MAX_VALUE;	
		for(Barrier b:barrier){
			Rectangle2D.Double ba=b.getArea();

			double ceny=y-(ba.y-ba.height);
			double cenx=x-ba.x;
			//ceny/=(rect.height+ba.height);
			//cenx/=(rect.width+ba.width);
			double distanceX=cenx*cenx;
			double distanceY=ceny*ceny;
			
			if(	x+rect.width*0.5>ba.x-ba.width*0.5 &&
				x-rect.width*0.5<ba.x+ba.width*0.5){						
					if(distanceY<nearestDistanceY){
						nearestDistanceX=distanceX;
						nearestDistanceY=distanceY;
						nearestYTop=b;
					}else if(distanceY==nearestDistanceY && distanceX<nearestDistanceX){
						nearestDistanceX=distanceX;
						nearestDistanceY=distanceY;
						nearestYTop=b;
					}
				
			}		
		}
		
		
		nearestDistanceX=Double.MAX_VALUE;
		nearestDistanceY=Double.MAX_VALUE;	
		for(Barrier b:barrier){
			Rectangle2D.Double ba=b.getArea();

			double ceny=(y-rect.height*0.5)-ba.y;
			double cenx=x-ba.x;
			//ceny/=(rect.height+ba.height);
			//cenx/=(rect.width+ba.width);
			double distanceX=cenx*cenx;
			double distanceY=ceny*ceny;
			
			if(	x+rect.width*0.5>ba.x-ba.width*0.5 &&
				x-rect.width*0.5<ba.x+ba.width*0.5){						
					if(distanceY<nearestDistanceY){
						nearestDistanceX=distanceX;
						nearestDistanceY=distanceY;
						nearestY=b;
					}else if(distanceY==nearestDistanceY && distanceX<nearestDistanceX){
						nearestDistanceX=distanceX;
						nearestDistanceY=distanceY;
						nearestY=b;
					}
				
			}		
		}
		Rectangle2D.Double ba=null;
		//if(nearestX!=null)
			ba=nearestX.getArea();
		//boolean flg=false;
		
		if(nearestX!=nearestY || nearestX!=nearestYTop){
			Rectangle2D.Double bay=nearestY.getArea();
			Rectangle2D.Double  bayT=nearestYTop.getArea();
			if(isHit(ba) && bay.y!=bayT.y){	
					double cenx=(x+rect.width*0.5)-(ba.x+ba.width*0.5);
					hitX(cenx,nearestX,ba);
			}
			
			
			
			//double dist=Math.hypot(ba.x-bay.x, ba.y-bay.y);
			//dist<1.4*World.BARRIER_SIZE
			if(nearestX!=nearestY)
			if(isHit(bay) && ba.x!=bay.x){	
					double ceny=(y-rect.height*0.5)-(bay.y-bay.height*0.5);
					hitY(ceny,nearestY,bay);
			}
			
			if(nearestX!=nearestYTop)
			if(isHit(bay) && ba.x!=bay.x){	
					double ceny=(y-rect.height*0.5)-(bay.y-bay.height*0.5);
					hitY(ceny,nearestYTop,bay);
			}
		}else{
			double ceny=(rect.y-rect.height*0.5)-(ba.y-ba.height*0.5);
			double cenx=(rect.x+rect.width*0.5)-(ba.x+ba.width*0.5);
			ceny/=(rect.height+ba.height);
			cenx/=(rect.width+ba.width);
			
			if(Math.abs(cenx)>Math.abs(ceny)){	
				//double cenx=(x+rect.width*0.5)-(ba.x-ba.width*0.5);
				hitX(cenx,nearestX,ba);
			
			}else{
				hitY(ceny,nearestY,ba);
			}
		}
		
			/*
		}else{
			if(isHit(ba)){	
				double ceny=(y-rect.height*0.5)-(ba.y-ba.height*0.5);
				double cenx=(x+rect.width*0.5)-(ba.x+ba.width*0.5);
				ceny/=(rect.height+ba.height);
				cenx/=(rect.width+ba.width);
				
				if(Math.abs(cenx)>Math.abs(ceny)){				
					hitX(cenx,nearestX,ba);
				}else {
					hitY(ceny,nearestX,ba);
				}
			
			}
		}
		/*
		//斜め
		nearest=barrier.get(0);
		 nearestDistance=Double.MAX_VALUE;
		
		for(Barrier b:barrier){
			 ba=b.getArea();
				if(		rect.y-rect.height*0.5<ba.y+ba.height*0.5 || 
						rect.y+rect.height*0.5>ba.y-ba.height*0.5 ||
						rect.x+rect.width*0.5<ba.x-ba.height*0.5 ||
						ba.x+ba.height*0.5< rect.x-rect.width*0.5)
						continue;
			double ceny=rect.y-ba.y;
			double cenx=rect.x-ba.x;
			double distance=cenx*cenx+ceny*ceny;
			
			if(distance<nearestDistance){
				nearestDistance=distance;
				nearest=b;
			}
		}
		
		ba=nearest.getArea();
		
		//あたり判定を位置でなく速度にする。
		if(isHit(ba)){	
				double ceny=(rect.y-rect.height*0.5)-(ba.y-ba.height*0.5);
				double cenx=(rect.x+rect.width*0.5)-(ba.x+ba.width*0.5);
				ceny/=(rect.height+ba.height);
				cenx/=(rect.width+ba.width);
				
				if(Math.abs(cenx)>Math.abs(ceny)){				
					hitX(cenx,nearest,ba);
				}else {
					hitY(ceny,nearest,ba);
				}
			
		}
		*/
		/*
		Barrier nearest=barrier.get(0);
		double nearestDistance=Double.MAX_VALUE;
		
		for(Barrier b:barrier){
			Rectangle2D.Double ba=b.getArea();
			double ceny=rect.y-ba.y;
			double cenx=rect.x-ba.x;
			double distance=cenx*cenx+ceny*ceny;
			
			if(distance<nearestDistance){
				nearestDistance=distance;
				nearest=b;
			}
		}
		
		Rectangle2D.Double ba=nearest.getArea();
		
		//あたり判定を位置でなく速度にする。
		if(isHit(ba)){	
				double ceny=(rect.y-rect.height*0.5)-(ba.y-ba.height*0.5);
				double cenx=(rect.x+rect.width*0.5)-(ba.x+ba.width*0.5);
				ceny/=(rect.height+ba.height);
				cenx/=(rect.width+ba.width);
				
				if(Math.abs(cenx)>Math.abs(ceny)){				
					hitX(cenx,nearest,ba);
				}else {
					hitY(ceny,nearest,ba);
				}
			
		}
		*/
	}
	
	/**
	 * キー入力と内部状態から動きを指定します。
	 * @param key　キー入力
	 */
	protected void setMotion(KeyTable key){
		boolean no_action=true;
		if(key.isPressing(KeyTable.ACT_RIGHT)){
			if(speedUpFrame>0){
				ax=(vx>fastVx ? 0.0 : 1.0);
			}else{
				ax=(vx>baseVx ? 0.0 : 0.5);
				
			}
			state=(state==JUMP || state==JUMP_LEFT? JUMP : RIGHT_MOVE );
			no_action=false;
		}else if(key.isPressing(KeyTable.ACT_LEFT)){
			if(speedUpFrame>0){
				ax=(vx<-fastVx ? 0.0 : -1.0);
			}else{
				ax=(vx<-baseVx ? 0.0 : -0.5);
				
			}
			state=(state==JUMP || state==JUMP_LEFT ? JUMP_LEFT : LEFT_MOVE );
			no_action=false;
		}
		
		if(key.isPressing(KeyTable.ACT_UP) && state!=JUMP && state!=JUMP_LEFT){
			vy=10;
			if(state==STAND_LEFT || state==LEFT_MOVE)state=JUMP_LEFT;
			else state=JUMP;
			
			no_action=false;
		}
		
		
		if(key.isPressing(KeyTable.ACT_DOWN))vy=-4;
		
		if(no_action){
			ax=0.0;
			vx=0.8*vx;//何も押されていなかったら適当に減速 vy=0.8*vy;
			if(state==LEFT_MOVE)state=STAND_LEFT;
			else if(state==RIGHT_MOVE)state=STAND;
			
			//state= (state==JUMP || state==JUMP_LEFT ? state : STAND );
		}
		
		//スピードアップの残りフレーム
		if(speedUpFrame>0)speedUpFrame--;
	}
	
	/**
	 * 実際に動かします。あたり判定も動かします。
	 *
	 */
	protected void move(World world){
		//物理の近似計算
		if(canMove){
			vx+=ax;
			vy+=ay;
			x+=vx;
			y+=vy;
		}else{
			cannotMoveFrame--;
			if(cannotMoveFrame<=0)canMove=true;
		}
		//////////////
		
		//画面外に出るのを防ぐところ
		if(y-rect.height*0.5<-100){
			//落ちたときの処理
			if(vy<0.0){damege();}
//			state= DAMEGE;に設定
			if(ay<0.0){ay=0.0;}
		}else if(y>Action.dispY+rect.height*2){
			if(vy>0.0){	vy=0.0;	y=Action.dispY+rect.height*2;}
			if(ay>0.0){	ay=0.0;}
		}
		
		if(x-rect.width*0.5<0){
			if(vx<0.0){	
				world.moveX(vx);
				x=rect.width*0.5;
			}
			if(ax<0.0){	ax=0.0;}
		}else if(x+rect.width*0.5>Flow.dispX){
			if(vx>0.0){
				world.moveX(vx);
				x=Flow.dispX-rect.width*0.5;
			}
			if(ax>0.0){	ax=0.0;}
		}
		/////////////////////
		
		rect.setRect(x-0.5*rect.width,y+0.5*rect.height,rect.width,rect.height);
		
		if(isInvincible){
			invinFrame--;
			if(invinFrame<=0)isInvincible=false;
		}
		ay=-gframe;
	}

	
	/**
	 * 次のフレームの画像を渡します。
	 * @return　次のフレームの画像
	 */
	protected Image getImage(){
		frame++;
		//4フレームに一度アニメーションのコマをずらす
		if(frame>=6){
			motionFrame++;
			frame=0;
		}
		if(motionFrame>=frame_limit[state])motionFrame=0;
	
		if(isInvincible){
			//無敵中は10フレームに数回描画しない＝点滅
			int mod=invinFrame%10;
			if(mod<=4)return null;
		}
		
		if(this.isBig){
			return anim_big[state][motionFrame];
		}else{
			return animation[state][motionFrame];
		}
	}
	
	private Image getEffect(){
		if(effectFrame>0){
			effectFrame--;
			if(this.isBig)return effect[EFFECT_BIG][0];
			else if(speedUpFrame>0)return effect[EFFECT_SPEEDUP][0];
			else if(this.isInvincible)return effect[EFFECT_INVIN][0];
		}
		return null;
	}
	
	protected void drawEffect(Graphics g){
		//デバッグよう

		if(nearestY!=null){
			g.setColor(Color.magenta);
			Rectangle2D.Double rd=nearestY.getArea();
			g.fillRect((int)rd.x, Action.dispY-(int)rd.y, (int)rd.width, (int)rd.height);
		}
		if(nearestYTop!=null){
			g.setColor(Color.DARK_GRAY);
			Rectangle2D.Double rd=nearestYTop.getArea();
			g.fillRect((int)rd.x, Action.dispY-(int)rd.y, (int)rd.width, (int)rd.height);
		}
		if(nearestX!=null){
			g.setColor(Color.blue);
			Rectangle2D.Double rd=nearestX.getArea();
			g.fillRect((int)rd.x, Action.dispY-(int)rd.y, (int)rd.width, (int)rd.height);
		}
		if(isBig)	
			g.drawImage(getEffect(),getUpperLeftX()-(EFFECT_X-(int)sizex_big)/2,Action.dispY-(getUpperLeftY()+EFFECT_Y+10),null);
		else		
			g.drawImage(getEffect(),getUpperLeftX()-(EFFECT_X-(int)sizex)/2,Action.dispY-(getUpperLeftY()+EFFECT_Y+10),null);

		
	}
	
	/**
	 * 左上のx座標値を取得します。描画時に使用してください。
	 * @return　左上のx座標値
	 */
	protected int getUpperLeftX(){
		if(!isBig)	return (int) rect.getMinX()-20;
		else		return (int) (rect.getMinX()-20*sizex_big/sizex);
	}
	
	/**
	 * 左上のy座標値を取得します。描画時に使用してください。
	 * @return　左上のy座標値
	 */
	protected int getUpperLeftY(){
		return (int) rect.getMinY();
	}

	private boolean isHit(Rectangle2D.Double ba){
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
	
	/**
	 * ダメージを受けたときの処理。
	 *
	 */
	private void damege(){
		if(isBig==true){
			isBig=false;
			rect.setRect(x-27/2,y+sizey/2,27,sizey);
			isInvincible=true;
			invinFrame=90;
		}else{
			vy=0.0;
			x=defaultX;
			y=defaultY;
			
			//行動不能・無敵設定
			canMove=false;
			cannotMoveFrame=120;
			isInvincible=true;
			invinFrame=240;
			/////////
		}
	}
	
	private void big(){
		isBig=true;
		rect.setRect(rect.x-(sizex-sizex_big)*0.5,rect.y+(sizey-sizey_big)*0.5,41,sizey_big);
		effectFrame=120;
	}
	
	private void speedup(){
		speedUpFrame=300;
		effectFrame=120;
	}
	
	private void invincible(){
		isInvincible=true;
		invinFrame=5*60;
		effectFrame=120;
	}
	
	protected void setEvent(OtoEvent[] event){
		for(OtoEvent e:event){
			if(e.getEventType()==OtoEvent.NONEEFFECTIVE)continue;
			if(e.getEventType()==OtoEvent.COMBO30)speedup();
			if(e.getEventType()==OtoEvent.COMBO100){
				speedup();
				invincible();
			}
			
		}
	}
	
	private void hitX(double cenx,Barrier barrier,Rectangle2D.Double ba){
		if(cenx>0.0 && barrier.isHitVector(Barrier.HIT_LEFT)){
				if(vx<0.0){	vx=0.0;x=ba.x+ba.width+rect.width*0.5+1;}
				if(ax<0.0){	ax=0.0;}
		}else if(cenx<0.0 && barrier.isHitVector(Barrier.HIT_RIGHT)){
				if(vx>0.0){	vx=0.0;x=ba.x-rect.width*0.5;}
				if(ax>0.0){	ax=0.0;}
		}

	}
	
	private void hitY(double ceny,Barrier barrier,Rectangle2D.Double ba){
		//if(barrier.isHitVector(Barrier.HIT_TOP) && ceny>0.0){
		if(ceny>0.0){
			if(vy<0.0 && barrier.isHitVector(Barrier.HIT_TOP)){
				vy=0.0;
				if(state==JUMP )state=STAND;
				else if(state==JUMP_LEFT )state=STAND_LEFT;
				
				y=ba.y+rect.height*0.5;
			}
			if( ay<0.0){
				ay=0.0;
				if(state==JUMP )state=STAND;
				else if(state==JUMP_LEFT )state=STAND_LEFT;
				
			}
		//}else if(barrier.isHitVector(Barrier.HIT_BOTTOM) && ceny<0.0){
		}else if(ceny<0.0 && barrier.isHitVector(Barrier.HIT_BOTTOM)){
			if(vy>0.0){	vy=0.0;y=ba.y-ba.height-rect.height*0.5;}
			if(ay>0.0){	ay=0.0;}
		}		
	}
}
