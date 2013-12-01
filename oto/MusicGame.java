package oto;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import action.ActionEvent;
import actionenemy.EnemyType;

//import flow.ButtonListener;
import keyio.KeyTable;
import oto.Note;

public class MusicGame implements Oto{//extends JFrame 
	//static final int startX = 342,startY = 364;
	
	//変更
	//public static final int dispX = 682,dispY = 364;
	int KEYMAX;//下にあるGameEventクラスにも同じ値を設定すること
	final int startX = 0/*1024 - dispX*/,startY = 0;
	final int sizeX = dispX/3; final int sizeY = dispY/3;
	final int WaitSyousetsu = 1;
	boolean firstAct,loadable;
	boolean[] tipedKey,pressing;
	int score,syousetsu,bpm,tipeLank,chain,maxchain,passedsyousetsu,bonusTime,level;
	short timed[],speedLevel,weightTime;
	String pass;
	SoundData soundData;
	GameEvent index,finalAddress;
	long time,playStart,timedef;
	Image frameImage,frameImageSmall,objectImage[],pushedImage[],enemyImage[];
	Area frameArea,drawArea,allArea;
	EventOto[] events;
	int pressCount;
	String s="";
	
	/**
	 * 背景画像を表す
	 */
	VolatileImage bg;
	
	/**
	 * 背景画像を管理します。
	 */
	GraphicsConfiguration gc;
	
	public MusicGame(BMSData bms){
		this(bms,5);
	}
	public MusicGame(BMSData bms,int KEYMAX){
		maxchain = 0;
		pressCount = 0;
		events = new EventOto[1];
		events[0] = new EventOto();
		enemyImage = new Image[3];
		this.KEYMAX = KEYMAX;
		weightTime=0;
		speedLevel=2;
		timed = new short[KEYMAX];
		loadable = true;
		try{
			enemyImage[1] = ImageIO.read(new FileInputStream("./testimg/ZACO_B.png"));
			enemyImage[2] = ImageIO.read(new FileInputStream("./testimg/ZACO_C.png"));
		} catch(Exception e){
			loadable = false;
		}
		try{
			enemyImage[0] = ImageIO.read(new FileInputStream("./testimg/ZACO_A.png"));
			pushedImage = new Image[KEYMAX];
			for(int i=0;i<KEYMAX;i++){
				pushedImage[i] = ImageIO.read(new FileInputStream("./testimg/OtoPushed"+i+".PNG"));
				timed[i]=10;
			}
			objectImage = new Image[2];
			frameImage = ImageIO.read(new FileInputStream("./testimg/otoFrame.PNG"));
			frameImageSmall = ImageIO.read(new FileInputStream("./testimg/otoFrameSmall.PNG"));
			objectImage[0] = ImageIO.read(new FileInputStream("./testimg/otoObject0.PNG"));
			objectImage[1] = ImageIO.read(new FileInputStream("./testimg/otoObject1.PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frameArea = new Area(new Rectangle(startX,startY,dispX,dispY));
		drawArea = new Area(new Rectangle(startX+100,startY,dispX-200,dispY));
		allArea = new Area(new Rectangle(0,0,1024,768));
		passedsyousetsu = 0;
		firstAct = true;
		syousetsu = 0;
		this.pass = bms.getPath();
		soundData = new SoundData();
		soundData.setArtist(bms.getArtist());
		soundData.setBPM(bms.getBPM());
		soundData.setGenre(bms.getGenre());
		soundData.setPlayLevel(bms.getPlayLevel());
		soundData.setTitle(bms.getTitle());
		soundData.setPath(bms.getPath());
		bpm = bms.getBPM();
		level = bms.getPlayLevel();
		tipedKey = new boolean[0x1000];
		pressing = new boolean[20];

		gc=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		//背景の読み込み
		try {
			BufferedImage tmp;
			tmp=ImageIO.read(new File("./testimg/OtoBG.png"));
			bg=gc.createCompatibleVolatileImage(tmp.getWidth(),tmp.getHeight());
			bg.createGraphics().drawImage(tmp,0,0,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/////////
		
		loadData();
	}
	public boolean isContinue(){
		if(firstAct) return true;
		if(finalAddress.getSyousetsu() > (passedsyousetsu-WaitSyousetsu)){
			return true;
		}else {
			soundData.closeSound();
			return false;
		}
	}
	public int getFrameScore(){
		return score;
	}
	
	/**
	 * 背景画像を描画するためのメソッド
	 * @param vi
	 * @param x
	 * @param y
	 * @param g
	 */
	private void drawVolatileImage(VolatileImage vi,int x,int y,Graphics g){
		if(vi.contentsLost())
			vi.validate(gc);
		
		g.drawImage(vi, x, y, null);
	}
	
	public void drawDirect(Graphics g){
		/*g.setClip(frameArea);
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, 1024, 768);
		//変更
		//g.drawImage(frameImage, startX+100, startY+100, this);
		 */
		/*
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, 1024, 768);
		*/
		this.drawVolatileImage(bg,0,0,g);
		
		if(level>2){
			g.drawImage(frameImage, startX+178, startY, null);
		} else{
			g.drawImage(frameImageSmall, startX+242, startY, null);
		}
			Graphics2D g2 = (Graphics2D)g;
		g.setColor(Color.YELLOW);
		g.drawLine(startX+(level>2?178:242), startY+305, startX+(level>2?506:442), startY+305);
		AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F);
		g2.setComposite(comp);
		int i=0;
		if(level<3) i=1;
		for(;i<KEYMAX;i++){
			/*Boolean pushed = false;
			switch(i){
			case 0:pushed = tipedKey[KeyTable.OTO_1]; break;
			case 1:pushed = tipedKey[KeyTable.OTO_2]; break;
			case 2:pushed = tipedKey[KeyTable.OTO_3]; break;
			case 3:pushed = tipedKey[KeyTable.OTO_4]; break;
			case 4:pushed = tipedKey[KeyTable.OTO_5]; break;
			case 5:pushed = tipedKey[KeyTable.OTO_6]; break;
			case 6:pushed = tipedKey[KeyTable.OTO_7]; break;
			case 7:pushed = tipedKey[KeyTable.OTO_8]; break;
			default :break;
			}
			if(pushed){
				g2.drawImage(pushedImage[i],startX+183+64*i,startY,null);
			}*/
			if(level<3 && i==4) break;
			if(pressing[i]){
				g2.drawImage(pushedImage[i],startX+183+64*i,startY,null);
			}
		}
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F);
		g2.setComposite(comp);
		g.setFont(new Font(Font.SANS_SERIF,Font.ITALIC,20));
		g.setColor(Color.RED);
		
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F);
		g2.setComposite(comp);
		
		g2.drawString(s, startX+341-(int)(g.getFontMetrics().stringWidth(s)*0.5), startY+(int)((384-g.getFontMetrics().getAscent()-g.getFontMetrics().getDescent())*0.5));
		if(bonusTime > 0){
			g2.drawString("JUDGE BONUS",startX+341-(int)(g.getFontMetrics().stringWidth("JUDGE BONUS")*0.5), startY+(int)((384+(g.getFontMetrics().getAscent()+g.getFontMetrics().getDescent())*1)*0.5));
			g2.drawString(""+bonusTime,startX+341-(int)(g.getFontMetrics().stringWidth(""+bonusTime)*0.5), startY+(int)((384+(g.getFontMetrics().getAscent()+g.getFontMetrics().getDescent())*3)*0.5));
		}
		
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F);
		g2.setComposite(comp);
		
		//g.drawString("MAX", startX+20, startY+300);
		g.drawString("COMBOS", startX+40, startY+300+g.getFontMetrics().getAscent()+g.getFontMetrics().getDescent());
		g.drawString(""+chain, startX+40, startY+300+g.getFontMetrics().getAscent()*2+g.getFontMetrics().getDescent()*2);
		g.drawString("SCORE", startX+580, startY+300);
		g.drawString("LATE", startX+620, startY+300+g.getFontMetrics().getAscent()+g.getFontMetrics().getDescent());
		g.drawString(""+(int)((100+chain*5)*(bonusTime>0? 1.2 : 1))+"%", startX+600, startY+300+g.getFontMetrics().getAscent()*2+g.getFontMetrics().getDescent()*2);
		g.setClip(drawArea);
		time = System.nanoTime();
		for(GameEvent Ge = index;Ge.getNext() != null && index.getSyousetsu()+speedLevel*2 >= Ge.getSyousetsu();Ge = Ge.getNext()){
			if(Ge.getKey()>0){
				int dan = 0;
				switch(Ge.getKey()){
					case 0:dan = 0; break;
					case KeyTable.OTO_1:dan = 0; break;
					case KeyTable.OTO_2:dan = 1; break;
					case KeyTable.OTO_3:dan = 2; break;
					case KeyTable.OTO_4:dan = 3; break;
					case KeyTable.OTO_5:dan = 4; break;
					case KeyTable.OTO_6:dan = 5; break;
					case KeyTable.OTO_7:dan = 6; break;
					case KeyTable.OTO_8:dan = 7; break;
					//case 17:dan = 1024; break;
					default :dan = 2048; break;
				}
				if(Ge.getYukou()){
					//System.out.println("x"+(int)(startX+116+(int)((Ge.getTime()-time)*240L/timedef)));
					//System.out.println("y"+ (int)(startY+100+dan*32));
					
					//変更
					//g.drawImage(objectImage, startX+116+(int)((Ge.getTime()-time)*240L/timedef),startY+100+dan*32,this);
					//g.drawImage(enemyImage[dan%3], startX+130+dan*60-32,startY+256-32-(int)((Ge.getTime()-time)*256L*(speedLevel*10+(double)weightTime)/(timedef*20)),null);
					if(loadable){
						int d = Ge.getSoundPath()+2;
						g.drawImage(enemyImage[d%3], startX+182+dan*64,startY+305-31-(int)((Ge.getTime()-time)*307L*(speedLevel*10+(double)weightTime)/(timedef*20))
							,startX+182+63+dan*64,startY+307+31-(int)((Ge.getTime()-time)*307L*(speedLevel*10+(double)weightTime)/(timedef*20)),
							0,0,63,63,null);
					} else g.drawImage(enemyImage[0], startX+182+dan*64,startY+305-31-(int)((Ge.getTime()-time)*307L*(speedLevel*10+(double)weightTime)/(timedef*20))
							,startX+182+63+dan*64,startY+307+31-(int)((Ge.getTime()-time)*307L*(speedLevel*10+(double)weightTime)/(timedef*20)),
							0,0,63,63,null);
					g.drawImage(objectImage[dan%2], startX+184+dan*64,startY+305-8-(int)((Ge.getTime()-time)*307L*(speedLevel*10+(double)weightTime)/(timedef*20)),null);
				}
			}
		}
		g.setColor(Color.WHITE);
		for(int k=0;k<KEYMAX;k++){
			if(timed[k]<10)g.drawOval(startX+208+64*k-5*timed[k], startY+302-5*timed[k], 10+10*timed[k], 10+10*timed[k]);
		}
		//g.setClip(allArea);
	}
	
	private static boolean[] keys=new boolean[0x1000];
	public Note[] act(KeyTable keytable){
		try{
			Note[] note = new Note[8];
			int noteNum = 0;
			long passedTime;
			
			if(bonusTime>0) bonusTime--;
			for(int i=0;i<KEYMAX;i++) timed[i]+=timed[i]<10?1:0;
			weightTime += weightTime>0?-1:weightTime<0?1:0;
			score = 0;
			tipeLank = 0;
			if(firstAct){
				chain = 0;
				timedef = 60000000000L * 4 / bpm;
				playStart = System.nanoTime();
				time = playStart;
				index = new GameEvent(0);
				finalAddress = index;
				for(;soundData.visibleObject./*getNext().*/isContinue()/* && soundData.visibleObject.getSyousetsu()<=5*/;soundData.nextVisibleObject()){
					int length = soundData.visibleObject.getDatLength();
					int dat[] = soundData.visibleObject.getObjectDat();
					for(int i=0;i<length;i++){
						if(dat[i]!=0){
							long jikan = playStart + timedef * (WaitSyousetsu+soundData.visibleObject.getSyousetsu()) + timedef*i/length;
							finalAddress = finalAddress.createGameEvent(soundData.visibleObject.getSyousetsu(),jikan, i, dat.length, dat[i], soundData.visibleObject.getChannel(),KEYMAX,bpm);
						}
					}
				}
				firstAct = false;
			}
			keys[KeyTable.OTO_1] = keytable.isPressing(KeyTable.OTO_1) && !tipedKey[KeyTable.OTO_1] ? true:false;
			keys[KeyTable.OTO_2] = keytable.isPressing(KeyTable.OTO_2) && !tipedKey[KeyTable.OTO_2] ? true:false;
			keys[KeyTable.OTO_3] = keytable.isPressing(KeyTable.OTO_3) && !tipedKey[KeyTable.OTO_3] ? true:false;
			keys[KeyTable.OTO_4] = keytable.isPressing(KeyTable.OTO_4) && !tipedKey[KeyTable.OTO_4] ? true:false;
			keys[KeyTable.OTO_5] = keytable.isPressing(KeyTable.OTO_5) && !tipedKey[KeyTable.OTO_5] ? true:false;
			keys[KeyTable.OTO_6] = keytable.isPressing(KeyTable.OTO_6) && !tipedKey[KeyTable.OTO_6] ? true:false;
			keys[KeyTable.OTO_7] = keytable.isPressing(KeyTable.OTO_7) && !tipedKey[KeyTable.OTO_7] ? true:false;
			keys[KeyTable.OTO_8] = keytable.isPressing(KeyTable.OTO_8) && !tipedKey[KeyTable.OTO_8] ? true:false;
			
			pressing[0] = tipedKey[KeyTable.OTO_1] = keytable.isPressing(KeyTable.OTO_1) ? true:false;
			pressing[1] = tipedKey[KeyTable.OTO_2] = keytable.isPressing(KeyTable.OTO_2) ? true:false;
			pressing[2] = tipedKey[KeyTable.OTO_3] = keytable.isPressing(KeyTable.OTO_3) ? true:false;
			pressing[3] = tipedKey[KeyTable.OTO_4] = keytable.isPressing(KeyTable.OTO_4) ? true:false;
			pressing[4] = tipedKey[KeyTable.OTO_5] = keytable.isPressing(KeyTable.OTO_5) ? true:false;
			pressing[5] = tipedKey[KeyTable.OTO_6] = keytable.isPressing(KeyTable.OTO_6) ? true:false;
			pressing[6] = tipedKey[KeyTable.OTO_7] = keytable.isPressing(KeyTable.OTO_7) ? true:false;
			pressing[7] = tipedKey[KeyTable.OTO_8] = keytable.isPressing(KeyTable.OTO_8) ? true:false;
			
			if(keytable.isPressing(KeyTable.OK)){
				if(keys[KeyTable.OTO_1]){
					if(speedLevel>1){
						speedLevel -= 1;
						weightTime+=10;
					}
					keys[KeyTable.OTO_1] = false;
				}
				if(keys[KeyTable.OTO_2]){
					if(speedLevel<4){
						speedLevel += 1;
						weightTime-=10;
					}
					keys[KeyTable.OTO_2] = false;
				}
				if(keys[KeyTable.OTO_3]){
					if(speedLevel>1){
						speedLevel -= 1;
						weightTime+=10;
					}
					keys[KeyTable.OTO_3] = false;
				}
				if(keys[KeyTable.OTO_4]){
					if(speedLevel<4){
						speedLevel += 1;
						weightTime-=10;
					}
					keys[KeyTable.OTO_4] = false;
				}
				if(keys[KeyTable.OTO_5]){
					if(speedLevel>1){
						speedLevel -= 1;
						weightTime+=10;
					}
					keys[KeyTable.OTO_5] = false;
				}
			}
			time = System.nanoTime();
			passedTime = time - playStart;
			syousetsu = (int)(passedTime / timedef);
			passedsyousetsu = syousetsu - WaitSyousetsu;
			
			for(GameEvent Ge = index;Ge.getNext() != null && syousetsu>Ge.getSyousetsu()-1;Ge = Ge.getNext()){
				int i = Ge.keyJudge(passedsyousetsu, time, keytable,keys,speedLevel,bonusTime);
				if(i<7 && i>1){
					//System.out.println("press.");
					tipeLank = i-2;
					score += tipeLank * (100 + chain*5);
					if(i>3){
						chain++;
						maxchain += (chain>maxchain)?1:0;
						events[0].count();
					}
					else {
						chain = 0;
						events[0].reset();
					}
					keys[Ge.getKey()] = false;
					switch(tipeLank){
					case 0:s = "poor";break;//System.out.println("poor"); break;
					case 1:s = "bad";break;//System.out.println("bad"); break;
					case 2:s = "good";break;//System.out.println("good"); break;
					case 3:s = "great";break;//System.out.println("great"); break;
					case 4:s = "perfect";break;//System.out.println("perfect"); break;
					}
					switch(Ge.getKey()){
						case KeyTable.OTO_1:timed[0] = 0; break;
						case KeyTable.OTO_2:timed[1] = 0; break;
						case KeyTable.OTO_3:timed[2] = 0; break;
						case KeyTable.OTO_4:timed[3] = 0; break;
						case KeyTable.OTO_5:timed[4] = 0; break;
						case KeyTable.OTO_6:timed[5] = 0; break;
						case KeyTable.OTO_7:timed[6] = 0; break;
						case KeyTable.OTO_8:timed[7] = 0; break;
						default: break;
					}
				}else if(i == -2){
					tipeLank = 0;
					s = "poor";
					chain = 0;
					events[0].reset();
				}
				if((Ge.getKey()>0 && i == 1 && time > (Ge.getTime() - 500000000L) && 
						time <= (Ge.getTime() - 500000000L + timedef / Ge.getDatMax()))
						|| (Ge.getKey()>0 && i>1) || (Ge.getKey() == 0 && i == -1))
				{
					soundData.playSound(Ge.getSoundPath());
				}
				if(/*i==3 || i==2 || */i==-2){
					int dan,num;
					switch(Ge.getKey()){
						case 0:dan = 0; break;
						case KeyTable.OTO_1:dan = 0; break;
						case KeyTable.OTO_2:dan = 1; break;
						case KeyTable.OTO_3:dan = 2; break;
						case KeyTable.OTO_4:dan = 3; break;
						case KeyTable.OTO_5:dan = 4; break;
						case KeyTable.OTO_6:dan = 5; break;
						case KeyTable.OTO_7:dan = 6; break;
						case KeyTable.OTO_8:dan = 7; break;
						//case 17:dan = 1024; break;
						default :dan = 2048; break;
					}
					num = dan%3;
					if(loadable){
						int d = Ge.getSoundPath()%3;
						double pxPerbeat=305.0/(4.0*2.0/speedLevel);
						note[noteNum]=new NoteInfomation(d==1?EnemyType.ZACO_A:d==2?EnemyType.ZACO_B:EnemyType.ZACO_C,startX+182+dan*64,-pxPerbeat/(3600.0/bpm)
								);
					} else{
						double pxPerbeat=305.0/(4.0*2.0/speedLevel);
						note[noteNum]=new NoteInfomation(EnemyType.ZACO_A,startX+182+dan*64,-pxPerbeat/(3600.0/bpm));
					}
					if(noteNum<KEYMAX-1) noteNum++;
				}
				
			}
			if(index != null) while((index.getNext() != null)&&(passedsyousetsu-1) > index.getSyousetsu()){
				index = index.getNext();
			}
			
			
			if(noteNum == 0){
				return null;
			}
			Note[] notes = new Note[noteNum];
			for(int i=0;i<noteNum;i++){
				notes[i] = note[i];
			}
			//適当にノードの受け渡しテスト
			/*
			Note[] notes=null;
			
			if(System.currentTimeMillis()%1000<10){
			notes=new Note[1];
			notes[0]=new NoteInfomation(EnemyType.ZACO_A,Math.random()*dispY,-2.0);
			}
			 */
			if(bonusTime>0) score = (int)(score * 1.2);
			return notes;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	void print(){
		System.out.println("Artist:"+soundData.getArtist());
		System.out.println("BPM:"+soundData.getBPM());
		System.out.println("Genre:"+soundData.getGenre());
		System.out.println("Path:"+soundData.getPath());
		System.out.println("Level:"+soundData.getPlayLevel());
		System.out.println("Title:"+soundData.getTitle());
		soundData.print();
	}
	
	public void loadData(){
		try{
			//int dataNum = 0;
			BufferedReader br =
				new BufferedReader(new FileReader(pass));
			String line,path;
			int dex = pass.lastIndexOf("\\",pass.length());
			if(dex != -1){
				path = pass.substring(0, dex);
			} else path = "";
			while((line = br.readLine()) != null){
				if(line.length()!=0 && line.charAt(0) == '#'){
					String code,code3;
					int i;
					for(i=1;i<line.length() && line.charAt(i) != ' ';i++){
					}
					code = line.substring(1,i);
					code3 = line.substring(1,4);
					if("PLAYER".equals(code)){
						
					}else if("ARTIST".equals(code)){
						
					}else if("WAV".equals(code3)){
						String num = line.substring(4,6);
						String soundPath = line.substring(i).trim();
						soundData.setSound(StrToInt.strToInt36(num), path +"\\"+ soundPath);
						
					}else if(line.indexOf(":") == 6){
						soundData.setVisibleObject(StrToInt.strToInt(line.substring(1, 6)), line.substring(7));
					}
					
					
				}
			}
			br.close();
			
		} catch (Exception e){
			System.out.println("例外" + e + "が発生しました");
		}
		
		
		
	}
	public Image draw() {
		return null;
	}
	public OtoEvent[] getEvent() {
		return events;
	}
	public void setEvent(ActionEvent[] event) {
		if(event != null) for(int i=0;i<event.length;i++){
			switch(event[i].getEventType()){
			case ActionEvent.NO_EFFCT:break;
			case ActionEvent.JUDGE_BONUS:bonusTime += 600;
			}
		}
	}
	public void finalize(){
		soundData.closeSound();
	}
}

class GameEvent{
	int KEYMAX;
	/*final long PoorLine = 1000000000L;
	final long BadLine = 800000000L;
	final long GoodLine = 650000000L;
	final long GreatLine = 300000000L;
	final long PerfectLine = 100000000L;
	*/
	final long PoorLine = 96L;
	final long BadLine = 72L;
	final long GoodLine = 48L;
	final long GreatLine = 18L;
	final long PerfectLine = 2L;
	long time;
	int BPM;
	int syousetsu;
	int soundPath;
	int key;//0はバックコーラス、1～はキー番号
	int datnum,datmax;
	boolean yuukou,oseru,canPress;//,bad;
	GameEvent next;
	
	GameEvent(int i){
		syousetsu = i;
		next = null;
	}
	GameEvent createGameEvent(int syousetsu,long time,int datnum,int datmax,int soundPath,int key,int KEYMAX,int BPM){
		//bad = false;
		this.BPM = BPM;
		this.KEYMAX = KEYMAX;
		this.time = time;
		this.datnum = datnum;
		this.datmax = datmax;
		this.syousetsu = syousetsu;
		this.soundPath = soundPath;
		yuukou = true;
		if(key>10+KEYMAX) this.key = 0;
		else{
			switch(key){
				case 0:
				case 1:
					this.key = 0; break;
				case 11:this.key = KeyTable.OTO_1; break;
				case 12:this.key = KeyTable.OTO_2; break;
				case 13:this.key = KeyTable.OTO_3; break;
				case 14:this.key = KeyTable.OTO_4; break;
				case 15:this.key = KeyTable.OTO_5; break;
				case 16:this.key = KeyTable.OTO_6; break;
				case 17:this.key = KeyTable.OTO_7; break;
				case 18:this.key = KeyTable.OTO_8; break;
				case 19:this.key = 1024; break;
				default :this.key = -1; yuukou = false; break;
			}
		}
		oseru = canPress = true;
		next = new GameEvent(syousetsu);
		return next;
	}
	int keyJudge(int syousetsu,long frameTime,KeyTable keyTable,boolean[] keys,short speedLevel,int bonusTime){
		//int i=0;
		if(yuukou && oseru){
			long hanteiT1 = (frameTime - time)*48*BPM/60000000000L;
			long hanteiT = Math.abs(hanteiT1);
			if(bonusTime>0) hanteiT-=12L;
			if(canPress && key>0 && keys[key] /*&& keyTable.isPressing(key)*/){
				if(hanteiT<PoorLine){
					if(hanteiT<BadLine){
						if(hanteiT<GoodLine){
							if(hanteiT<GreatLine){
								if(hanteiT<PerfectLine){
									yuukou = false; return 6;
								}else{ yuukou = false; return 5;}
							}else{ yuukou = false; return 4;}
						}else{ canPress = false; return 3;}
					}else{ canPress = false; return 2;}
				}else{return 1;}
				
			}
			if(key == 0){
				//System.out.println("aaaaaaaaaaa\n");
				if(frameTime > time){
					yuukou = false;
					return -1;
				}
			}else if(hanteiT1>(speedLevel == 1? 50: speedLevel == 2? 27:speedLevel == 3? 23 : 16)/*(48-36*(speedLevel-2))*2*0.29*/){
				oseru = false; return -2;
			}
		
		}
		return 0;
	}
	boolean getYukou(){
		return yuukou;
	}
	int getSoundPath(){
		return soundPath;
	}
	int getSyousetsu(){
		return syousetsu;
	}
	GameEvent getNext(){
		return next;
	}
	int getDatNum(){
		return datnum;
	}
	int getDatMax(){
		return datmax;
	}
	long getTime(){
		return time;
	}
	int getKey(){
		return key;
	}
	
	
	//void setBad(){bad = true;}
}

