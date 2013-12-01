package oto;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import keyio.KeyTable;
import action.ActionEvent;
/**
 * �f�o�b�O�p�ł��B�����ł́A���˂�{�[���̃V�~�����[�V�������s���Ă��܂��B
 * �������A���C�A��C��R�͊܂܂�Ă��܂���B
 * �Q�[���̎Q�l�ɂ��Ă���������ƍK���ł��B
 * @version 1.0
 * @author �݂������ł�
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
	String gVector="��";;
	/**
	 * �{�[���̉^�����v�Z���Ă��܂��B
	 */
	public Note[] act(KeyTable keytable) {
		frameScore=0;
		if(keytable.isPressing(KeyTable.OTO_1)){ax=-g;ay=0.0;gVector="��";}
		if(keytable.isPressing(KeyTable.OTO_4)){ax=g;ay=0.0;gVector="��";}
		if(keytable.isPressing(KeyTable.OTO_3)){ax=0.0;ay=-g;gVector="��";}
		if(keytable.isPressing(KeyTable.OTO_2)){ax=0.0;ay=g;gVector="��";}
		
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return null;
	}

	public int getFrameScore() {
		return frameScore;
	}

	public boolean isContinue() {
		return true;
	}

	public void setEvent(ActionEvent[] event) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}
	
	int xt,yt;
	/**
	 * �{�[���̕`������Ă��܂��B
	 */
	public void drawDirect(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0,dispX, dispY);
		
		//���W�ϊ�
		xt=(int) x;
		yt=dispY-(int)y;
		
		g.setColor(Color.WHITE);
		g.drawString("�d�͕���"+gVector, dispX>>2, dispY>>2);
		g.fillOval(xt-5, yt-5, 10, 10);
		
	}

}
