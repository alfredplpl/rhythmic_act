package oto;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import keyio.KeyTable;
import action.ActionEvent;
/**
 * デバッグ用です。ここでは、跳ねるボールのシミュレーションを行っています。
 * ただし、摩擦、空気抵抗は含まれていません。
 * ゲームの参考にしていただけると幸いです。
 * @version 1.0
 * @author みそかつおでん
 *
 */
public class BM08 implements Oto{
	int frameScore;
	double x=0.0,y=dispY;
	double vx=3,vy=0;
	static final double g=9.8/59.88;
	double ay=-g;
	double ax=0.0;
	double e=0.7;
	String gVector="↓";;
	/**
	 * ボールの運動を計算しています。
	 */
	public Note[] act(KeyTable keytable) {
		frameScore=0;
		if(keytable.isPressing(KeyTable.OTO_1)){ax=-g;ay=0.0;gVector="←";}
		if(keytable.isPressing(KeyTable.OTO_4)){ax=g;ay=0.0;gVector="→";}
		if(keytable.isPressing(KeyTable.OTO_3)){ax=0.0;ay=-g;gVector="↓";}
		if(keytable.isPressing(KeyTable.OTO_2)){ax=0.0;ay=g;gVector="↑";}
		
		vy+=ay;
		vx+=ax;
		y+=vy;
		x+=vx;
		
		if(x<0.0){
			frameScore+=Math.abs(vx);
			vx=-e*vx;
			x=0.0;
		}else if(x>dispX){
			frameScore+=Math.abs(vx);
			vx=-e*vx;
			x=dispX;			
		}
		
		if(y<0.0){
			frameScore+=Math.abs(vy);
			vy=-e*vy;
			y=0.0;
		}else if(y>dispY){
			frameScore+=Math.abs(vy);
			vy=-e*vy;
			y=dispY;			
		}
		
		return null;
	}

	public Image draw() {
		return null;
	}

	public OtoEvent[] getEvent() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public int getFrameScore() {
		return frameScore;
	}

	public boolean isContinue() {
		return true;
	}

	public void setEvent(ActionEvent[] event) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	int xt,yt;
	/**
	 * ボールの描画をしています。
	 */
	public void drawDirect(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0,dispX, dispY);
		
		//座標変換
		xt=(int) x;
		yt=dispY-(int)y;
		
		g.setColor(Color.WHITE);
		g.drawString("重力方向"+gVector, dispX>>2, dispY>>2);
		g.fillOval(xt-5, yt-5, 10, 10);
		
	}

}
