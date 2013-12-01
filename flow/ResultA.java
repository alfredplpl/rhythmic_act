package flow;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import action.Action;

import oto.Oto;

import keyio.KeyTable;

public class ResultA implements Result{
	private int scoreAct,scoreOto;
	private double dist;
	private long frame;
	private static final double fps=1.0e9/GameConst.FRAME_TIME;
	
	private int totalScore;
	
	boolean isContinue;
	
	AudioClip wow;
	VolatileImage bg;
	BufferedImage num[];
	HashMap<Character,BufferedImage> charImage;
	GraphicsConfiguration gc;
	
	public static String BG_FILE="./testimg/InfoBG.png";
	public static String BGM_FILE="../testse/kansei.wav";
	
	ResultA(Flow flow){
		scoreAct=flow.getScoreAction();
		scoreOto=flow.getScoreOto();
		dist=flow.getWorldDistance();
		this.frame=flow.getFrame();
		
		wow=Applet.newAudioClip(getClass().getResource(BGM_FILE));
		gc=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		num=new BufferedImage[11];
		charImage=new HashMap();
		try {
			BufferedImage tmp;
			tmp=ImageIO.read(new File(BG_FILE));
			bg=gc.createCompatibleVolatileImage(tmp.getWidth(),tmp.getHeight());
			bg.createGraphics().drawImage(tmp,0,0,null);
			
			for(int i=0;i<=9;i++){
				num[i]=ImageIO.read(new File("./testimg/InfoNum"+i+".png"));
			}
			num[10]=ImageIO.read(new File("./testimg/InfoNumDot.png"));

			charImage.put('S', ImageIO.read(new File("./testimg/lChar/BIG_S.png")));
			charImage.put('A', ImageIO.read(new File("./testimg/lChar/BIG_A.png")));
			charImage.put('B', ImageIO.read(new File("./testimg/lChar/BIG_B.png")));
			charImage.put('C', ImageIO.read(new File("./testimg/lChar/BIG_C.png")));
			charImage.put('F', ImageIO.read(new File("./testimg/lChar/BIG_F.png")));
			charImage.put('X', ImageIO.read(new File("./testimg/char/X.png")));
		} catch (IOException e) {
			bg=null;
			e.printStackTrace();
		}
		
		//スコア計算の方程式(2分基準)
		if(frame!=0.0)totalScore=(int)((scoreAct+dist+scoreOto)*(fps*120)/frame);
		else	totalScore=777;
		if(totalScore<0)totalScore=0;
		
		isContinue=true;

		
		//totalScore=600000;
		
		wow.play();
	}
	
	boolean OKisPressed;
	public void act(KeyTable key) {
		if(!OKisPressed && key.isPressing(KeyTable.OK)){
			wow.stop();
			isContinue=false;
		}
		OKisPressed=key.isPressing(KeyTable.OK);
	}
	
	static final Font sans=new Font("SansSerif",Font.PLAIN,40);
	public void draw(Graphics g) {
		g.clearRect(0, 0, MainWindow.dispX, MainWindow.dispY);
		
		if(bg!=null)
			drawVolatileImage(bg,0,0,g);
/*		
		Font orgFont= g.getFont();
		g.setFont(sans);
		//g.drawString("Stage Clear!!", MainWindow.dispX/2-120, MainWindow.dispY/2-120);
		g.setFont(orgFont);
		g.drawString(String.format("Action's Score:% 9d",scoreAct), MainWindow.dispX/2-90, MainWindow.dispY/2-45);
		g.drawString(String.format("Sound's  Score:% 9d ×2 = % 9d",scoreOto,scoreOto*2), MainWindow.dispX/2-90, MainWindow.dispY/2-30);
		g.drawString(String.format("Moving   Score:% 9d",(int)dist), MainWindow.dispX/2-90, MainWindow.dispY/2-15);
		g.drawString(String.format("Normalize     :120sec(NormalizedTime)/%5.2fsec(PlayTime)=%5.2f",frame/fps,(fps*120)/frame), MainWindow.dispX/2-90, MainWindow.dispY/2);
		g.setFont(sans);
		g.drawString(String.format("Total Score   :% 9d",totalScore), MainWindow.dispX/2-120, MainWindow.dispY/2+40);
		g.setFont(orgFont);
		g.drawString("Enterで終了", MainWindow.dispX/2-120, MainWindow.dispY/2+100);
*/
		
		drawNumber(scoreAct,530,180,g);
		drawNumber(scoreOto,530,250,g);
		if(dist>0)	drawNumber((int)dist,530,340,g);
		else		drawString("XXXXXXX", 530, 340,g);
		drawFloat((fps*120)/frame,530,420,g);
		drawNumber(totalScore,530,510,g);
		
		String[] rank={"F","C","B","A","AA","AAA","S","SS","SSS"};
		int rankidx=totalScore/30000;
		if(rankidx>=rank.length)rankidx=rank.length-1;
		drawString(rank[rankidx],560,560,g);
	}

	public int getScore() {
		return totalScore;
	}

	public boolean isContinue() {
		return isContinue;
	}
	
	private void drawVolatileImage(VolatileImage vi,int x,int y,Graphics g){
		if(vi.contentsLost())
			vi.validate(gc);
		
		g.drawImage(vi, x, y, null);
	}
	
	static final int NUM_WIDTH=19;
	private void drawNumber(int number,int x,int y,Graphics g){
		if(number<=0)g.drawImage(num[0],x+num[number%10].getWidth()*7,y,null);
		for(int i=7;i>=0&& number!=0;i--){
			g.drawImage(num[number%10],x+num[number%10].getWidth()*i,y,null);
			number/=10;
		}
	}
	
	private void drawFloat(double numDouble,int x,int y,Graphics g){
		int number=(int)numDouble;
		for(int i=4;i>=0&& number!=0;i--){
			g.drawImage(num[number%10],x+num[number%10].getWidth()*i,y,null);
			number/=10;
		}
		
		g.drawImage(num[10],x+NUM_WIDTH*5,y,null);
		
		int frac=(int)(numDouble*100.0);
		
		for(int i=7;i>=6&& frac!=0;i--){
			g.drawImage(num[frac%10],x+num[number%10].getWidth()*i,y,null);
			frac/=10;
		}
	}
	
	static final int CHAR_WIDTH=19;
	private void drawString(String str,int x,int y,Graphics g){
		for(int i=0;i<str.length();i++){
			g.drawImage(charImage.get(str.charAt(i)), x+charImage.get(str.charAt(i)).getWidth()*i, y, null);
		}
	}
}
