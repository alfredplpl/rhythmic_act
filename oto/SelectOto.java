package oto;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.*;

import javax.imageio.ImageIO;

import keyio.KeyTable;

public class SelectOto implements OtoSelect {
	static final String bgm="./testbgm/air.mp3";
	BMSInfomation bmsdata[];
	int selectData,clock = 10,passedClock = 0,frames;
	Boolean up = false,isContinue,preview = false,changing;
	Image selectFrame,dispMList,star,voidstar,hidariShita;
	
	VolatileImage backGround;
	GraphicsConfiguration gc;
	
	WaveEngine wE;
	SoundPlayer[] sP; 
	public SelectOto(){
		changing = false;
		frames = 0;
		sP = new SoundPlayer[2];
		sP[0] = new SoundPlayer(); sP[1] = new SoundPlayer();
		sP[0].load(bgm);
		sP[0].play();
		wE = new WaveEngine(4);
		wE.load("0", "..\\testse\\cursor2.wav");
		wE.load("1", "..\\testse\\cursor6.wav");
		wE.load("2", "..\\testse\\decide2.wav");
		wE.load("3", "..\\testse\\decide7.wav");
		bmsdata = OtoFactory.getBMSList();
		
		/*BMSInfomation temps = bmsdata[2];
		bmsdata[2] = bmsdata[1];
		bmsdata[1] = temps;*/
		
		gc=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		try{
			hidariShita = ImageIO.read(new FileInputStream("./testimg/OtoSelectHidarishita.PNG"));
			BufferedImage tmp;
			tmp=ImageIO.read(new File("./testimg/OtoSelectBG.PNG"));
			backGround=gc.createCompatibleVolatileImage(tmp.getWidth(),tmp.getHeight());
			backGround.createGraphics().drawImage(tmp,0,0,null);
			selectFrame = ImageIO.read(new FileInputStream("./testimg/OtoSelectFrame.PNG"));
			star = ImageIO.read(new FileInputStream("./testimg/Star.PNG"));
			voidstar = ImageIO.read(new FileInputStream("./testimg/VoidStar.PNG"));
			dispMList = ImageIO.read(new FileInputStream("./testimg/dispMList.PNG"));
		} catch(Exception e){
			System.out.println("ó·äO" + e + "Ç™î≠ê∂");
		}
		this.reset();
		/*
		for(int n=0;n<bmsdata.length-1;n++){
			for(int m=n;m<bmsdata.length-1;m++){
				if(bmsdata[m].getPlayLevel() > bmsdata[m+1].getPlayLevel()){
					BMSInfomation temp;
					temp = bmsdata[m];
					bmsdata[m] = bmsdata[m+1];
					bmsdata[m+1] = temp;
				}
			}
		}*/
		for(int n=1;n<bmsdata.length;n++){
			int m;
			BMSInfomation temp;
			for(m=n;m>0&&bmsdata[m].getPlayLevel()<bmsdata[m-1].getPlayLevel();m--){
				temp = bmsdata[m];
				bmsdata[m] = bmsdata[m-1];
				bmsdata[m-1] = temp;
			}
			
		}
		
	}

	public void act(KeyTable key) {
		passedClock++;
		if(passedClock == 60 && bmsdata[selectData].getPath2() != null){
			sP[1] = new SoundPlayer();
			sP[1].load(bmsdata[selectData].getPath2());
			sP[1].play(bmsdata[selectData].getStartTime());
			sP[0].setVolume(0.0f);
			preview = true;
		}
		
		
		if(passedClock>100) {
			passedClock = 100;
			changing = false;
		}
		if(clock<10) clock++;
		if(key.isPressing(KeyTable.OK)){
			wE.play("2");
			isContinue = false;
		}
		if(changing && !preview){
			sP[0].setVolume(0.01f*passedClock);
		}
		if(isContinue && key.isPressing(KeyTable.OTO_2) && clock > 9){
			if(preview){
				preview = false;
				changing = true;
				sP[1].close(1000000000L);
				//sP[0].setVolume(1.0f);
			}
			wE.play("0");
			passedClock = 0;
			if(selectData > 0) selectData--;
			else selectData = bmsdata.length-1;
			clock = 0;
			up = true;
		}else if(isContinue && key.isPressing(KeyTable.OTO_4) && clock > 9){
			if(preview){
				preview = false;
				changing = true;
				sP[1].close(1000000000L);
				//sP[0].setVolume(1.0f);
			}
			wE.play("0");
			passedClock = 0;
			if(selectData < bmsdata.length-1) selectData++;
			else selectData = 0;
			clock = 0;
			up = false;
		}
	}
	
	private void drawVolatileImage(VolatileImage vi,int x,int y,Graphics g){
		if(vi.contentsLost())
			vi.validate(gc);
		
		g.drawImage(vi, x, y, null);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		drawVolatileImage(backGround,0,0,g);
		g.drawImage(selectFrame,50,94,null);
		g.setFont(new Font(null,Font.ITALIC,30));
		g.drawString(bmsdata[selectData].getTitle(), 74, 180-g.getFontMetrics().getDescent());
		g.drawString("Artist:"+bmsdata[selectData].getArtist(), 74, 190+g.getFontMetrics().getAscent());
		int i;
		frames++;
		if(frames == 60) frames = 0;
		
		i=0;
		if(frames <30 || !(bmsdata[selectData].getPlayLevel() > 3))for(;i<bmsdata[selectData].getPlayLevel() && i<3;i++){
			g.drawImage(star,492 + 40 * i,140/*412 + 40 * i,140*/,null);
		}
		for(;i<3;i++){
			g.drawImage(voidstar,492 + 40 * i,140,null);
		}
		int j;
		g.setFont(new Font(null,Font.ITALIC,20));
		g.drawString("ëÄ çÏ ï˚ ñ@", 176, 245);
		g.drawImage(hidariShita,136,250,null);
		for(j=0,i=selectData;i<selectData+4 && i<bmsdata.length;j++,i++){
			g.drawImage(dispMList,618,260+40*j+ (clock<10 ? up ? -40*(10-clock)/10:40*(10-clock)/10:0),null);
			g.drawString(bmsdata[i].getTitle(), 628, 280-g.getFontMetrics().getDescent()+40*j+ (clock<10 ? up ? -40*(10-clock)/10:40*(10-clock)/10:0));
		}
		if(i==bmsdata.length){
			for(int sa=0;(sa+j)<3;sa++){
				g.drawImage(dispMList,618,260+40*(j+sa)+ (clock<10 ? up ? -40*(10-clock)/10:40*(10-clock)/10:0),null);
				g.drawString(bmsdata[sa].getTitle(), 628, 280-g.getFontMetrics().getDescent()+40*(j+sa)+ (clock<10 ? up ? -40*(10-clock)/10:40*(10-clock)/10:0));
			}
		}
		for(j=-1,i = selectData-1;i>=0 && i>selectData-8;j--,i--){
			g.drawImage(dispMList,618,260+40*j+ (clock<10 ? up ? -40*(10-clock)/10:40*(10-clock)/10:0),null);
			g.drawString(bmsdata[i].getTitle(), 628, 280-g.getFontMetrics().getDescent()+40*j+ (clock<10 ? up ? -40*(10-clock)/10:40*(10-clock)/10:0));
		}
		if(i < 0){
			for(int sa=0;(j+sa)>-7;sa--){
				//System.out.println(""+sa);
				g.drawImage(dispMList,618,260+40*(j+sa)+ (clock<10 ? up ? -40*(10-clock)/10:40*(10-clock)/10:0),null);
				g.drawString(bmsdata[(bmsdata.length-1+sa)].getTitle(), 628, 280-g.getFontMetrics().getDescent()+40*(j+sa)+ (clock<10 ? up ? -40*(10-clock)/10:40*(10-clock)/10:0));
			}
		}
		
	}

	public BMSData getSelectedBMS() {
		return bmsdata[selectData];
	}

	public boolean isContinue() {
		return isContinue;
	}

	public void reset() {
		isContinue = true;
		bmsdata = OtoFactory.getBMSList();
		selectData = 0;
	}
	public void finalize(){
		if(sP[1].isPlaying()){
			if(sP[0].isPlaying()) sP[0].finish();
			if(!sP[1].close(1000000000L)) sP[1].finish();
		}else if(sP[0].isPlaying()){
			if(!sP[0].close(1000000000L)) sP[1].finish();
		}
	}

}
