package flow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import keyio.KeyTable;
import action.*;
import oto.*;
/**
 * ゲーム全体の流れを把握します。
 * @author みそかつおでん
 *
 */
public final class Flow extends Thread{
	private Oto oto;
	private Action action;
	private ButtonListener keytable;
	private int scoreAct;
	private int scoreOto;
	private int frame;
	
	private boolean isContinue;
	
	public static final int dispX=342;
	public static final int dispY=384;
	
	//テスト用
	private BMSData[] bms;
	private BMSData currentbms;
	
	Flow(BMSData currentbms,int stage) throws Exception{
		frame=0;
		isContinue=true;
		oto=OtoFactory.makeOto(currentbms);
		action=ActionFactory.makeAction(stage);

		//キー入力を受け取る
		keytable=new ButtonListener();

	}

	protected void act(){
		Note[] note=oto.act(keytable);
		action.act(note, keytable);	
	}
	
	@Override
	public void run(){
		long time1,time2,dif;
		int sleepCnt=0;
		int playCnt=0;
		
		try {
			while(oto!=null && oto.isContinue()){
				time1 = System.nanoTime();
				frame++;
				playCnt++;
				//実際の動作
				Note[] note=oto.act(keytable);
				action.act(note, keytable);
				
				action.setEvent(oto.getEvent());
				oto.setEvent(action.getEvent());
				
				//得点計算
				scoreOto+=oto.getFrameScore();
				scoreAct+=action.getFrameScore();

				time2 = System.nanoTime();
				dif = GameConst.FRAME_TIME - (time2 - time1);
				if (dif > 0) {
					sleepCnt++;
					Thread.sleep(dif / 1000000, (int) dif % 1000000);
				}
			}
			

		} catch (InterruptedException es){
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.err.println("GameThread.sleepCount:"+sleepCnt+" drawCount:"+playCnt);
		isContinue=false;

	}
	
	protected KeyListener getKeyListener(){
		return keytable;
	}
	
	//描画系
	static final Color skyColor=new Color(148,204,228);
	static final Font normalFont=new Font(Font.MONOSPACED,Font.PLAIN,18);
	protected void drawScoreImage(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2.setColor(skyColor);
		g2.fillRect(0, 0, dispX, dispY);
		g2.setFont(normalFont);
		
		g2.setColor(Color.white);
		g2.drawString(String.format("音スコア    :% 8d", scoreOto), 100, 102);
		g2.drawString(String.format("アクトスコア:% 8d", scoreAct), 100, 152);
		g2.drawString(String.format("移動距離　  :% 8.2fm",getWorldDistance()), 100, 177);
		
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(String.format("音スコア    :% 8d", scoreOto), 100, 100);
		g2.drawString(String.format("アクトスコア:% 8d", scoreAct), 100, 150);
		g2.drawString(String.format("移動距離　  :% 8.2fm",getWorldDistance()), 100, 175);
	}
	
	protected void drawOtoImage(Graphics g){
		if(oto!=null)oto.drawDirect(g);
	}
	
	protected void drawActionImage(Graphics g){
		action.drawDirect(g);
	}
	
	protected boolean isContinue(){
		return isContinue;
	}
	
	protected boolean isNOTExitting(){
		if(keytable.isContinue()){
			return true;
		}else{
			oto=null;
			scoreAct=0;
			scoreOto=0;
			System.gc();
			return false;
		}
	}
	
	protected boolean isOK(){
		return keytable.isPressing(KeyTable.OK);
	}
	
	protected BMSData getCurrentBMSData(){
		return currentbms;
	}

	protected int getScoreAction(){
		return scoreAct;
	}
	
	protected int getScoreOto(){
		return scoreOto;
	}
	
	protected double getWorldDistance(){
		return action.getWorldDistance();
	}

	protected long getFrame(){
		return frame;
	}
}
