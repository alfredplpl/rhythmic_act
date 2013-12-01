package flow;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;

import action.Action;

import keyio.KeyTable;

public class RankingA implements Ranking{
	private class RankData{
		int difficulty;
		int score;
		String nameAct,nameOto;
		
		RankData(){}
		RankData(int difficulty,int score,String nameAct,String nameOto){
			this.difficulty=difficulty;
			this.score=score;
			this.nameAct=nameAct;
			this.nameOto=nameOto;
		}
	}
	
	RankData[] today;
	RankData[] total;
	Vector<RankData> all;
	
	File todayFile;
	File totalFile;
	File allFile;
	
	boolean isContinue;
	/** 現在処理している点数*/
	int scoreNow;
	int rankNow;
	int phase;
	int stageNow;
	
	static final int READY=-2;
	static final int ENDED=-1;
	static final int REGISTER=1;
	static final int INSERT=5;
	
	char[] nameOto,nameAct;
	int[] numOto,numAct;
	
	static final int NAME_LENGTH= 6;
	static final int CHAR_KIND=31;
	
	BufferedImage num[];
	BufferedImage frame;
	HashMap<Character,BufferedImage> charImage;
	
	VolatileImage bg_in,bg_rank;
	GraphicsConfiguration gc;
	
	public static String BG_FILE_IN="./testimg/RankIn.png";
	public static String BG_FILE_RANK="./testimg/Ranking.png";
	
	RankingA(){
		today=new RankData[RANK];
		total=new RankData[RANK];
		all=new Vector<RankData> (RANK);
		Calendar cal=Calendar.getInstance();
		todayFile=new File(	String.format(Ranking.rankDirectory+"本日%04d-%02d-%02d.rank",
							cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)+1),cal.get(Calendar.DAY_OF_MONTH)));

		try {
			BufferedReader reader=new BufferedReader(new FileReader(todayFile));
			StreamTokenizer st=new StreamTokenizer(reader);
			
			for(int i=0;i<today.length;i++){
				today[i]=new RankData();
				RankData rank=today[i];
				
				//終了処理
				if(StreamTokenizer.TT_EOF==st.nextToken())break;
				
				if(StreamTokenizer.TT_NUMBER!=st.ttype)throw new Exception();
				rank.difficulty=(int) st.nval;
				if(StreamTokenizer.TT_NUMBER!=st.nextToken())throw new Exception();
				rank.score=(int) st.nval;
				if(StreamTokenizer.TT_WORD!=st.nextToken())throw new Exception();
				rank.nameAct=st.sval;
				if(StreamTokenizer.TT_WORD!=st.nextToken())throw new Exception();
				rank.nameOto=st.sval;
			}
			reader.close();
			
		} catch (Exception e) {
			for(int i=0;i<today.length;i++)
				today[i]=new RankData(1,1000*(RANK-i),"MASAO","NIDERA");
		}
		
		totalFile=new File(Ranking.rankDirectory+"総合.rank");

		try {
			BufferedReader reader=new BufferedReader(new FileReader(totalFile));
			StreamTokenizer st=new StreamTokenizer(reader);
			
			for(int i=0;i<total.length;i++){
				total[i]=new RankData();
				RankData rank=total[i];
				
				//終了処理
				if(StreamTokenizer.TT_EOF==st.nextToken())break;

				if(StreamTokenizer.TT_NUMBER!=st.ttype)throw new Exception();
				rank.difficulty=(int) st.nval;
				if(StreamTokenizer.TT_NUMBER!=st.nextToken())throw new Exception();
				rank.score=(int) st.nval;
				if(StreamTokenizer.TT_WORD!=st.nextToken())throw new Exception();
				rank.nameAct=st.sval;
				if(StreamTokenizer.TT_WORD!=st.nextToken())throw new Exception();
				rank.nameOto=st.sval;
			}
			
			reader.close();
		} catch (Exception e) {
			for(int i=0;i<total.length;i++)
				total[i]=new RankData(1,1000*(RANK-i),"MASAO","NIDERA");
		}
		
		num=new BufferedImage[11];
		charImage=new HashMap();
		gc=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		try{
			BufferedImage tmp;
			tmp=ImageIO.read(new File(BG_FILE_IN));
			System.out.println(tmp.getWidth());
			bg_in=gc.createCompatibleVolatileImage(tmp.getWidth(),tmp.getHeight());
			bg_in.createGraphics().drawImage(tmp,0,0,null);
			
			tmp=ImageIO.read(new File(BG_FILE_RANK));
			bg_rank=gc.createCompatibleVolatileImage(tmp.getWidth(),tmp.getHeight());
			bg_rank.createGraphics().drawImage(tmp,0,0,null);
			
			for(int i=0;i<=9;i++){
				num[i]=ImageIO.read(new File("./testimg/InfoNum"+i+".png"));
			}
			
			for(int i=1;i<CHAR_KIND-1;i++){
				//System.out.println("read:./testimg/char/"+table(i)+".png");
				charImage.put(table(i), ImageIO.read(new File("./testimg/char/"+table(i)+".png")));
			
			}
			charImage.put('?', ImageIO.read(new File("./testimg/char/question.png")));
			charImage.put(' ', ImageIO.read(new File("./testimg/char/space.png")));
			
			frame=ImageIO.read(new File("./testimg/RankInFrame.png"));
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	boolean ExitKeyisPressed;
	public void act(KeyTable key) {
		switch(phase){
		case READY:
			//ランクインしたら
			if(scoreNow>today[today.length-1].score){
				phase=REGISTER;
				int i;
				for(i=today.length-2;i>=0;i--)
					if(scoreNow<today[i].score)break;
				
				rankNow=i+2;
				
			}else{
				phase=ENDED;
			}		
			System.out.println("rankNow:"+rankNow+"scoreNow:"+scoreNow);
		break;
		
		//登録画面
		case REGISTER:
			resister(key);
			if(!ExitKeyisPressed && key.isPressing(KeyTable.OK))phase=INSERT;
			ExitKeyisPressed=key.isPressing(KeyTable.OK);
		break;
		
		case INSERT:
			int j;
			for(j=today.length-2;j>=0;j--){
				if(scoreNow>today[j].score){
					today[j+1]=today[j];
				}else{
					break;
				}
			}
			today[j+1]=new RankData(stageNow,scoreNow,new String(nameAct),new String(nameOto));
			phase=ENDED;
			
			try {
				BufferedWriter writer=new BufferedWriter(new FileWriter(todayFile));
				
				for(int i=0;i<today.length;i++){
					writer.write(today[i].difficulty+" ");
					writer.write(today[i].score+" ");
					writer.write(today[i].nameAct+" ");
					writer.write(today[i].nameOto+"\n");
				}
				writer.close();
				
				writer=new BufferedWriter(new FileWriter(totalFile));
				
				for(int i=0;i<total.length;i++){
					writer.write(total[i].difficulty+" ");
					writer.write(total[i].score+" ");
					writer.write(today[i].nameAct+" ");
					writer.write(today[i].nameOto+"\n");
				}
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		break;
		
		case ENDED:
			//ランキング処理が終了している場合
			if(ExitKeyisPressed==false && key.isPressing(KeyTable.OK))isContinue=false;
			ExitKeyisPressed=key.isPressing(KeyTable.OK);
		break;
		}
	}

	static final Font font=new Font(Font.SANS_SERIF ,Font.PLAIN,40);
	static final Font normalFont=new Font(Font.DIALOG,Font.PLAIN,20);
	int cursorTOP;
	int cursorBOTTOM;
	public void draw(Graphics g) {
		//g.setFont(font);
		//g.clearRect(0, 0, MainWindow.dispX, MainWindow.dispY);
		
		switch(phase){
		case REGISTER:
			//g.drawString("本日のランキングの第"+rankNow+"位ランクインしました。", 100, 200);
			

			/*
			for(int i=0;i<6;i++)
				g.drawChars(nameOto, i, 1, i*40+200, MainWindow.dispY/2-60);
			*/
			//g.drawLine(100, MainWindow.dispY/2, MainWindow.dispX-100, MainWindow.dispY/2);
			//g.drawRect(cursorTOP*40+195, MainWindow.dispY/2-95, 40, 40);
			//g.drawRect(cursorBOTTOM*40+195, MainWindow.dispY/2+65, 40, 40);
			
			drawVolatileImage(bg_in,0,0,g);
			
			drawNumber(rankNow,115,50,g);
			
			g.drawImage(frame, cursorTOP*40+290, MainWindow.dispY/2-100, null);
			drawString(nameOto,300,MainWindow.dispY/2-60-CHAR_HEIGHT,g,40-CHAR_WIDTH);
			
			g.drawImage(frame, cursorBOTTOM*40+290, MainWindow.dispY/2+60, null);
			drawString(nameAct,300,MainWindow.dispY/2+65,g,40-CHAR_WIDTH);
		break;
		
		case ENDED:
			//g.drawString("本日のランキング", 100, 100);
			//g.setFont(normalFont);
			//g.drawString("順位　難易度　　スコア　　プレイヤー", 250, 250);
			drawVolatileImage(bg_rank,0,0,g);
			for(int i=0;i<today.length;i++){
				String dif;
				if(today[i].difficulty<=Action.EASY)dif="EASY";
				else if(today[i].difficulty<=Action.NORMAL)dif="NORMAL";
				else dif="HARD";
				
				drawNumber(i+1,10,230+(CHAR_HEIGHT+4)*i,g);
				drawString(dif.toCharArray(),210,230+(CHAR_HEIGHT+4)*i,g);
				drawNumber(today[i].score,360,230+(CHAR_HEIGHT+4)*i,g);
				drawString((today[i].nameAct+" "+today[i].nameOto).toCharArray(),560,230+(CHAR_HEIGHT+4)*i,g);
				/*
				g.drawString(String.format("% 2d : %10s % 8d %s %s", 
						i+1,
						dif,today[i].score,today[i].nameAct,today[i].nameOto),
						250,i*25+275);
				*/
			}
			
		break;
		}
	}

	public boolean isContinue() {
		return isContinue;
	}

	public void reset() {
		isContinue=true;
		ExitKeyisPressed=true;
		phase=scoreNow=rankNow=stageNow=READY;
		
		nameAct=Arrays.copyOf("ACT".toCharArray(),NAME_LENGTH+1);
		nameOto=Arrays.copyOf("DDR".toCharArray(),NAME_LENGTH+1);
		
		numAct=new int[nameAct.length];
		numOto=new int[nameOto.length];
		
		for(int i=0;i<3;i++){
			numAct[i]=nameAct[i]-'A';
		}
		for(int i=3;i<NAME_LENGTH;i++){
			numAct[i]=0;
		}
		for(int i=0;i<3;i++){
			numOto[i]=nameOto[i]-'A';
		}
		for(int i=3;i<NAME_LENGTH;i++){
			numOto[i]=0;
		}
		
		cursorTOP=cursorBOTTOM=0;
		
		invalidFrameOto=0;
		isPressed=true;
	}
	
	@Override
	public void finalize(){
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(todayFile));
			
			for(int i=0;i<today.length;i++){
				writer.write(today[i].difficulty+" ");
				writer.write(today[i].score+" ");
				writer.write(today[i].nameAct+" ");
				writer.write(today[i].nameOto+"\n");
			}
			writer.close();
			
			writer=new BufferedWriter(new FileWriter(totalFile));
			
			for(int i=0;i<total.length;i++){
				writer.write(total[i].difficulty+" ");
				writer.write(total[i].score+" ");
				writer.write(today[i].nameAct+" ");
				writer.write(today[i].nameOto+"\n");
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void addScoreAndDifficulty(int score,int stage) {
		scoreNow=score;
		stageNow=stage;
	}
	
	int invalidFrameOto;
	boolean isPressed;
	private void resister(KeyTable key){
		if(invalidFrameOto>0){
			invalidFrameOto--;
		}else{
			if (key.isPressing(KeyTable.OTO_1) && cursorTOP > 0){
				invalidFrameOto=10;
				cursorTOP--;
			}
				
			if (key.isPressing(KeyTable.OTO_5) && cursorTOP < numOto.length - 1){
				cursorTOP++;
				invalidFrameOto=10;
			}

			if (key.isPressing(KeyTable.OTO_2)) {
				invalidFrameOto=10;
				if (numOto[cursorTOP] > 0)
					nameOto[cursorTOP] = table(--numOto[cursorTOP]);
				else
					nameOto[cursorTOP] = table(numOto[cursorTOP] = 30);
			}
			if (key.isPressing(KeyTable.OTO_4)) {
				invalidFrameOto=10;
				if (numOto[cursorTOP] < CHAR_KIND)
					nameOto[cursorTOP] = table(++numOto[cursorTOP]);
				else
					nameOto[cursorTOP] = table(numOto[cursorTOP] = 0);
			}
		}
		
		if(!isPressed){
			if(key.isPressing(KeyTable.ACT_LEFT) && cursorBOTTOM>0)cursorBOTTOM--;
			if(key.isPressing(KeyTable.ACT_RIGHT) && cursorBOTTOM<nameAct.length-1)cursorBOTTOM++;
			
			if(key.isPressing(KeyTable.ACT_UP)){
				if(numAct[cursorBOTTOM]>0)
					nameAct[cursorBOTTOM]=table(--numAct[cursorBOTTOM]);
				else
					nameAct[cursorBOTTOM]=table(numAct[cursorBOTTOM]=30);
			}
			
			if(key.isPressing(KeyTable.ACT_DOWN)){
				if(numAct[cursorBOTTOM]<31)
					nameAct[cursorBOTTOM]=table(++numAct[cursorBOTTOM]);
				else
					nameAct[cursorBOTTOM]=table(numAct[cursorBOTTOM]=0);
			}
		}
		isPressed=	key.isPressing(KeyTable.ACT_DOWN)||key.isPressing(KeyTable.ACT_UP)||
					key.isPressing(KeyTable.ACT_LEFT)||key.isPressing(KeyTable.ACT_RIGHT);

	}

	private char table(int num){
		switch(num){
		case 1:return 'A';
		case 2:return 'B';
		case 3:return 'C';
		case 4:return 'D';
		case 5:return 'E';
		case 6:return 'F';
		case 7:return 'G';
		case 8:return 'H';
		case 9:return 'I';
		case 10:return 'J';
		case 11:return 'K';
		case 12:return 'L';
		case 13:return 'M';
		case 14:return 'N';
		case 15:return 'O';
		case 16:return 'P';
		case 17:return 'Q';
		case 18:return 'R';
		case 19:return 'S';
		case 20:return 'T';
		case 21:return 'U';
		case 22:return 'V';
		case 23:return 'W';
		case 24:return 'X';
		case 25:return 'Y';
		case 26:return 'Z';
		case 27:return '!';
		case 28:return '_';
		case 29:return '^';
		case 30:return '?';
		default:return ' ';
		}
	}
	
	static final int NUM_WIDTH=19;
	private void drawNumber(int number,int x,int y,Graphics g){
		if(number<=0)g.drawImage(num[0],x+NUM_WIDTH*7,y,null);
		for(int i=7;i>=0&& number!=0;i--){
			g.drawImage(num[number%10],x+NUM_WIDTH*i,y,null);
			number/=10;
		}
	}
	
	static final int CHAR_WIDTH=19;
	static final int CHAR_HEIGHT=37;
	private void drawString(char[] str,int x,int y,Graphics g){
		for(int i=0;i<str.length;i++){
			g.drawImage(charImage.get(str[i]), x+CHAR_WIDTH*i, y, null);
		}
	}
	
	private void drawString(char[] str,int x,int y,Graphics g,int interval){
		for(int i=0;i<str.length;i++){
			g.drawImage(charImage.get(str[i]), x+(CHAR_WIDTH+interval)*i, y, null);
		}
	}
	
	private void drawVolatileImage(VolatileImage vi,int x,int y,Graphics g){
		if(vi.contentsLost())
			vi.validate(gc);
		
		g.drawImage(vi, x, y, null);
	}
	
}
