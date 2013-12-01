package flow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.*;
import java.util.Vector;


import javax.imageio.ImageIO;
import javax.swing.JFrame;

import oto.SoundPlayer;

import keyio.KeyTable;

public final class TitleA implements Title{
	volatile VolatileImage logo4I,logoProject,title,staff;
	BufferedImage textForPressing;
	SoundPlayer mp3;
	static final String bgm="./testbgm/shiki_endless_dreamer.mp3";
	static final String img="./testimg/";
	static final String STAFF="./staff";
	
	int frame;
	boolean isContinue;
	static final int FPS=60;
	static final int splitFrame=FPS*6;
	Color fade;
	Vector<String> staffRoll;
	
	boolean ExitisPressed;
	boolean OKisPressed;
	
	TitleA(JFrame jf) throws IOException{
		BufferedImage tmp;
		
		tmp=ImageIO.read(new File(img+"logo4I.png"));
		logo4I=jf.createVolatileImage(tmp.getWidth(), tmp.getHeight());
		logo4I.getGraphics().drawImage(tmp,0,0,jf);
		
		tmp=ImageIO.read(new File(img+"logoProject.png"));
		logoProject=jf.createVolatileImage(tmp.getWidth(), tmp.getHeight());
		logoProject.getGraphics().drawImage(tmp,0,0,jf);
		
		tmp=ImageIO.read(new File(img+"title.png"));
		title=jf.createVolatileImage(tmp.getWidth(), tmp.getHeight());
		title.getGraphics().drawImage(tmp,0,0,jf);
		
		staff=jf.createVolatileImage(tmp.getWidth(), tmp.getHeight());
		Graphics g=tmp.getGraphics();
		g.setColor(new Color(0xFF,0xFF,0xFF,0xBB));
		g.fillRect(0, 0, MainWindow.dispX, MainWindow.dispY);
		staff.getGraphics().drawImage(tmp,0,0,null);	
		
		textForPressing=ImageIO.read(new File(img+"textForPressing.png"));

		staffRoll=new Vector<String>(10);
		BufferedReader br=new BufferedReader(new FileReader(STAFF));
		
		String str;
		while((str=br.readLine())!=null){
			staffRoll.add(str);
		}
		
		tmp=null;
		System.gc();
		reset();
	}
	
	boolean isEnd;
	int endFrame;
	public void act(KeyTable key) {	
		if(isEnd){
			if(endFrame<=0)	isContinue=false;
			else			endFrame--;
			return;
		}
		//125秒でループ
		if(frame>FPS*125){
			mp3.close();
			reset();
			frame=0;
		}
		
		if(!OKisPressed && key.isPressing(KeyTable.OK)){
			if(frame<splitFrame){
				frame=splitFrame;
				OKisPressed=true;
			}else if(frame<splitFrame+splitFrame){
				frame=splitFrame+splitFrame;
				OKisPressed=true;
			}else{
				isEnd=true;
				endFrame=30;
				mp3.close(1000000000L);
			}
		}
		OKisPressed=key.isPressing(KeyTable.OK);
		
		if(frame==splitFrame+splitFrame){
			mp3.play();
		}
		
		frame++;
	}
	
	static final Font font=new Font(Font.SANS_SERIF ,Font.PLAIN,72);
	static final Font normalFont=new Font(Font.DIALOG,Font.PLAIN,36);
	public void draw(Graphics g) {
		//Graphics2D g2=(Graphics2D)g;
		
		if(frame<splitFrame){
			//フェードインとフェードアウト
			if(frame<splitFrame/3){
				g.setColor(new Color(0,0,0,255-(int)(frame*255/(splitFrame/3.0))));
			}else if(frame>splitFrame*2/3){
				g.setColor(new Color(0,0,0,(int)((frame-splitFrame*2.0/3.0)*255/(splitFrame/3.0))));
			}
			
			
			drawVolatileImage(logo4I,0,0,g);
			if(frame<splitFrame/3 || frame>splitFrame*2/3)g.fillRect(0, 0, MainWindow.dispX, MainWindow.dispY);

		}else if(frame<splitFrame+splitFrame){
			if(frame-splitFrame<splitFrame/3){
				g.setColor(new Color(0,0,0,255-(int)((frame-splitFrame)*255/(splitFrame/3.0))));
			}else if(frame-splitFrame>splitFrame*2/3){
				g.setColor(new Color(0,0,0,(int)((frame-splitFrame-splitFrame*2.0/3.0)*255/(splitFrame/3.0))));
			}
			
			drawVolatileImage(logoProject,0,0,g);
			
			if(frame-splitFrame<splitFrame/3 || frame-splitFrame>splitFrame*2/3)
				g.fillRect(0, 0, MainWindow.dispX, MainWindow.dispY);
		}else if(frame<FPS*65){
			
			drawVolatileImage(title,0,0,g);
			if(frame%120<60){
				g.drawImage(textForPressing,	(MainWindow.dispX-textForPressing.getWidth(null))/2,
												(MainWindow.dispY-textForPressing.getHeight(null))/2+200,null);
			}
		}else{
			int rollFrame=frame-FPS*65;
			int FramePerPage=60*FPS*15/staffRoll.size();
			int page=rollFrame/FramePerPage;
			
			drawVolatileImage(staff,0,0,g);
			
			Graphics2D g2=(Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2.setColor(Color.black);
			g2.setFont(font);
			g2.drawString("製作スタッフ", 100, 100);
			g.setFont(normalFont);
			//g.setColor(new Color(0,0,0,0xFF*(300-rollFrame%300)/300));
			g2.setColor(Color.black);
			for(int i=0;i<5 && page*15+i*3+1<staffRoll.size();i++){
				g2.drawString(staffRoll.get(page*15+i*3), 300, 200+100*i);
				g2.drawString(staffRoll.get(page*15+i*3+1), 400, 200+100*i+45);
			}
		}
		
		if(isEnd && endFrame>=0){
			g.setColor(new Color(0,0,0,0xFF*(30-endFrame)/30));
			g.fillRect(0, 0, MainWindow.dispX, MainWindow.dispY);
		}
	}

	public boolean isContinue() {
		return isContinue;
	}
	
	private void drawVolatileImage(VolatileImage vi,int x,int y,Graphics g){
		if(!vi.contentsLost()){
			g.drawImage(vi,x,y,null);
		}
	}
	
	public void reset() {
		frame=0;
		isContinue=true;
		fade=new Color(0xFF,0,0,0);
		
		if(mp3!=null)mp3.finish();
		mp3=new SoundPlayer();
		mp3.load(bgm);
		ExitisPressed=true;
		OKisPressed=true;
		isEnd=false;
	}

	@Override
	public void finalize(){
		mp3.finish();
	}
}
