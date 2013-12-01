package action;

import java.applet.Applet;
import java.applet.AudioClip;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Properties;

import javax.imageio.*;

import flow.GameConst;

import keyio.KeyTable;
/**
 * 外部ファイルはAction.Externalより大きい。
 * @author みそかつおでん
 *
 */
public class SelectAction implements ActionSelect{
	int stage;
	boolean isContinue;
	AudioClip decide,select;
	int inputWait;
	VolatileImage bg;
	BufferedImage selectFrame,selectDif;
	int nowSelected;
	File[] external;
	
	GraphicsConfiguration gc;
	
	static final int NO_SELECT=Integer.MAX_VALUE;
	
	boolean isDecided;
	public SelectAction(){
		decide=Applet.newAudioClip(getClass().getResource("../testse/decide7.wav"));
		select=Applet.newAudioClip(getClass().getResource("../testse/cursor6.wav"));
		gc=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		try {
			BufferedImage tmp;
			tmp=ImageIO.read(new File("./testimg/ActSelectBG.png"));
			bg=gc.createCompatibleVolatileImage(tmp.getWidth(),tmp.getHeight());
			bg.createGraphics().drawImage(tmp,0,0,null);
			
			selectFrame=ImageIO.read(new File("./testimg/ActSelectFrame.png"));

			selectDif=ImageIO.read(new File("./testimg/ActSelectDif.png"));

			Properties set=new Properties();
			set.load((new FileInputStream(GameConst.SETTING_PATH)));
			File dir=new File(set.getProperty("selectaction.externalfolder"));
			if(dir.exists()){
				external=dir.listFiles(new FilenameFilter(){
					public boolean accept(File dir,String name){
						return name.matches("[\\d\\D]+\\.stage$");
					}
				});
			}else{
				external=null;
			}
		} catch (IOException e) {
			bg=null;
			e.printStackTrace();
		}
		
		reset();
	}
	

	public void act(KeyTable key) {
		if(key.isPressing(KeyTable.ACT_CROSS)){
			if(!isContinue)isContinue=true;
			if(stage>Action.EXTERNAL){
				stage=Action.EXTERNAL;
			}
		}
		if(!isContinue)return;
		if(inputWait>0){
			inputWait--;
			return;
		}
		
		if(!isDecided && key.isPressing(KeyTable.ACT_CIRLE)){
			isDecided=true;
			if(stage!=Action.EXTERNAL && stage!=NO_SELECT){
				isContinue=false;
				decide.play();
				inputWait=10;
				return;
			}else{
				if(external==null || external.length==0)
					stage=NO_SELECT;
				else
					stage=Action.EXTERNAL+1;
				
				inputWait=10;
				return;
			}
		}
		isDecided=key.isPressing(KeyTable.ACT_CIRLE);
			
		if(stage<=Action.EXTERNAL){

			switch(stage){
			case Action.EASY:
				if(key.isPressing(KeyTable.ACT_RIGHT)){
					select.play();
					stage=Action.NORMAL;
				}else if(key.isPressing(KeyTable.ACT_LEFT)){
					stage=Action.EXTERNAL;
					select.play();
				}
				inputWait=10;
			break;
			
			case Action.NORMAL:
				if(key.isPressing(KeyTable.ACT_RIGHT)){
					stage=Action.HARD;
					select.play();
				}else if(key.isPressing(KeyTable.ACT_LEFT)){
					stage=Action.EASY;
					select.play();
				}
				inputWait=10;
			break;
			
			case Action.HARD:
				if(key.isPressing(KeyTable.ACT_LEFT)){
					stage=Action.NORMAL;
					select.play();
				}else if(key.isPressing(KeyTable.ACT_RIGHT)){
					stage=Action.EXTERNAL;
					select.play();
				}
				inputWait=10;
			break;
			
			//ユーザ定義ファイルの場合
			case Action.EXTERNAL:
				if(key.isPressing(KeyTable.ACT_LEFT)){
					stage=Action.HARD;
					select.play();
				}else if(key.isPressing(KeyTable.ACT_RIGHT)){
					stage=Action.EASY;
					select.play();
				}
				inputWait=10;
			break;
			}
		}else{
			if(key.isPressing(KeyTable.ACT_LEFT) && stage-(Action.EXTERNAL+1)>0){
				stage--;
				select.play();
			}
			
			if(key.isPressing(KeyTable.ACT_RIGHT) && stage-Action.EXTERNAL<external.length){
				stage++;
				select.play();
			}
			inputWait=10;
		}
		
	}

	static final Font font=new Font(Font.SANS_SERIF ,Font.PLAIN,40);
	static final Font smallFont=new Font(Font.SANS_SERIF ,Font.PLAIN,30);
	public void draw(Graphics gt) {
		Graphics2D g=(Graphics2D)gt;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		g.setFont(font);
		g.setColor(Color.black);
		drawVolatileImage(bg, 0, 0, g);
		
		if(stage<=Action.EXTERNAL){
			int x=(selectDif.getWidth()+40);
			int y=(dispY-selectDif.getHeight())/2-50;
			int left=dispX-x*4;
			
			String[] dif={"EASY","NORMAL","HARD","USER"};
			for(int i=0;i<4;i++){
				g.drawImage(selectDif, left+x*i,y,null);
				if(!isContinue){
					if(stage-1==i){
						g.setColor(Color.red);
					}else{
						g.setColor(Color.black);
					}
				}
				g.drawString(dif[i], left+x*i+(selectDif.getWidth()-dif[i].length()*28)/2,y+40);
			}
			
			g.setFont(smallFont);
			g.setColor(Color.black);
			switch(stage){
			case Action.EASY:
				g.drawImage(selectFrame, left-19,y-16, null);
				g.drawString("ゲームを普段やらない人におすすめです。", 250, 300);
			break;
			
			case Action.NORMAL:
				g.drawImage(selectFrame, left+x*1-19,y-16, null);
				g.drawString("よくゲームする人におすすめです。", 250, 300);
			break;
			
			case Action.HARD:
				g.drawImage(selectFrame, left+x*2-19,y-16, null);
				g.drawString("転倒には気をつけてください。", 250, 300);
			break;
			
//			ユーザ定義ファイルの場合
			case Action.EXTERNAL:
				g.drawImage(selectFrame, left+x*3-19,y-16, null);
				g.drawString("ユーザが作ったステージを選択します。", 250, 300);
			break;
			}
		}else{
			if(stage!=NO_SELECT){
				int x=(selectDif.getWidth()+40);
				int y=(dispY-selectDif.getHeight())/2-50;
				int left=(dispX-x)/2;
				int fileIndex=stage-(Action.EXTERNAL+1);
				int i=fileIndex>3 ? -3:-fileIndex;

				g.setFont(smallFont);
				
				for(;i<3 && fileIndex+i<external.length;i++){
					g.drawImage(selectDif, left+x*i,y,null);
					if(!isContinue){
						if(i==0){
							g.setColor(Color.red);
						}else{
							g.setColor(Color.black);
						}
					}
					
					String name=external[fileIndex+i].getName();
					name=name.substring(0, name.lastIndexOf('.'));
					if(name.length()>=5)name=name.substring(0, 5);
					g.drawString(name, 
							left+x*i+(selectDif.getWidth()-name.length()*28)/2,
							y+40);
					
					g.drawImage(selectFrame, left-19,y-16, null);
					
				}
				
				String name=external[fileIndex].getName();
				name=name.substring(0, name.lastIndexOf('.'));
				g.setColor(Color.black);				
				g.drawString(name, 250, 300);
			}else{
				g.drawString("オリジナルステージがありません。", 250, 300);
			}
			
		}

	}

	public int getDifficulty() {
		if(stage==NO_SELECT)return Action.NORMAL;
		return stage;
	}

	public boolean isContinue() {
		return isContinue;
	}

	public void reset() {
		isContinue=true;
		isDecided=true;
		inputWait=0;
		stage=Action.EASY;
		nowSelected=Action.NORMAL;
	}

	private void drawVolatileImage(VolatileImage vi,int x,int y,Graphics g){
		if(vi.contentsLost())
			vi.validate(gc);
		
		g.drawImage(vi, x, y, null);
	}
}
