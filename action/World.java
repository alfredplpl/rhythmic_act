package action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;

import flow.Flow;
import flow.GameConst;

import actionbarrier.*;
import actionenemy.*;

/**
 * 世界を定義するクラスです。
 * 表示部分とは違い、オブジェクトの位置をすべてここで管理することになります。
 * 適度に読み込み、オブジェクトを再配置する事になります。
 * @author みそかつおでん
 *
 */
public class World {
	volatile ArrayList<Enemy> enemy;
	volatile ArrayList<Barrier> barrier;
	volatile ArrayList<Item> item;
	volatile ImageKeeping ik;
	double worldldx,worldrdx;//世界を左に動かせた距離と右に動かせた距離
	double worlddis;
	int[][] stageData;
	int stagePosLeft,stagePosRight;
	double nextCheckPoint;
	
	/**
	 * オフスクリーン描画の左側の限界点を表します。
	 */
	public static final int OFFSCREEN_MIN_X=-300;
	
	/**
	 * オフスクリーン描画の右側の限界点を表します。
	 */
	public static final int OFFSCREEN_MAX_X=Action.dispX+300;
	
	/**
	 * 障害物を配置する最小の大きさを定義します。なお、障害物は正方形です。
	 */
	static final int BARRIER_SIZE=25;
	
	static final int NUM_OBJ_X=(OFFSCREEN_MAX_X-OFFSCREEN_MIN_X)/BARRIER_SIZE+1;
	static final int NUM_OBJ_Y=Action.dispY/BARRIER_SIZE+1;
	
	boolean isCheckPassed;
	
	World(ArrayList<Enemy> enemy,ArrayList<Barrier> barrier,ArrayList<Item> item,ImageKeeping ik,int stage) throws Exception{
		this.enemy=enemy;
		this.barrier=barrier;
		this.item=item;
		this.ik=ik;
		worlddis=worldldx=worldrdx=0.0;
		File stageFile;
		try {
			if(stage<=Action.EXTERNAL){
				stageFile=new File("stage/ステージ"+stage+".stage");
			}else{
				Properties set=new Properties();
				set.load((new FileInputStream(GameConst.SETTING_PATH)));
				File dir=new File(set.getProperty("selectaction.externalfolder"));
				
				File[] external;
				external=dir.listFiles(new FilenameFilter(){
					public boolean accept(File dir,String name){
						return name.matches("[\\d\\D]+\\.stage$");
					}
				});
				
				stageFile=external[stage-(Action.EXTERNAL+1)];
			}
			BufferedReader br =new BufferedReader(new FileReader(stageFile));
			
			//値と値が空白の場合１行は49バイト
			stageData=new int[(int) stageFile.length()/50+1][NUM_OBJ_Y];
			System.out.println("stage_length:"+(int) (stageFile.length()/50+1));
			int idx=0;
			String tmp;
			while((tmp=br.readLine())!=null){
				for(int i=0;i<16;i++)
					stageData[idx][i]=Integer.parseInt(tmp.substring(i*3, i*3+2), NUM_OBJ_Y);
				idx++;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("ステージファイルの形式が間違ってます。");
		}
		
		//ステージサイズを200に分割して、開始位置を決定する
		int div=stageData.length/200;
		if(div==0){
			stagePosLeft=stagePosRight=0;
		}else{
			stagePosLeft=stagePosRight=200*((int)(Math.random()*1000)%div);
		}
		
		for(int x=OFFSCREEN_MIN_X;x<OFFSCREEN_MAX_X && stagePosRight<stageData.length;x+=BARRIER_SIZE){
			for(int i=0;i<16;i++)
				makeObject(stageData[stagePosRight][i],x,i*BARRIER_SIZE);
			stagePosRight++;
		}
		
		nextCheckPoint=BARRIER_SIZE*200;
		isCheckPassed=false;
	}
	
	protected void moveX(double vx){
		worlddis+=vx;
		if(vx>0.0){
			worldrdx+=vx;
			if(worldldx<0.0)worldldx+=vx;
			else{
				worldldx-=vx;
				worldldx%=100.0;
			}
		}else{
			worldldx+=vx;
			if(worldrdx>0.0)worldrdx+=vx;
			else{
				worldrdx-=vx;
				worldrdx%=100.0;
			}
		}
		
		synchronized(barrier){
			for(Barrier b:barrier){
//				相対的なため
				b.x-=vx;
				b.resetArea();
			}
		}
		
		if(worldldx<=-100.0){
			for(int i=0;i<4;i++){
				stagePosLeft--;
				stagePosRight--;
				if(stagePosLeft<0)stagePosLeft=stageData.length-1;
				if(stagePosRight<0)stagePosRight=stageData.length-1;
				
				for(int j=0;j<16;j++){
					makeObject(stageData[stagePosLeft][j],OFFSCREEN_MIN_X-worldldx-i*BARRIER_SIZE,j*BARRIER_SIZE);
					//(OFFSCREEN_MAX_X-worlddx)-i*BARRIER_SIZE,j*BARRIER_SIZE);
				}
			}
			
			worldldx+=100.0;
		}else if(worldrdx>=100.0){
			for(int i=0;i<4;i++){
				stagePosLeft++;
				stagePosRight++;
				if(stagePosLeft>=stageData.length)stagePosLeft=0;
				if(stagePosRight>=stageData.length)stagePosRight=0;
				

				for(int j=0;j<16;j++){
					makeObject(stageData[stagePosRight][j],(double)OFFSCREEN_MAX_X-worldrdx+(i-1)*BARRIER_SIZE,j*BARRIER_SIZE);
				}
			}
			
			worldrdx-=100.0;
		}


		synchronized(enemy){
			for(Enemy e:enemy){
				e.moveX(-vx);
			}
		}
		
		synchronized(item){
			for(Item i:item){
				i.x-=vx;
				i.resetArea();
			}
		}
	}

	/**
	 * 背景を描画します
	 * @param g　描画するべきGraphics
	 */
	protected void drawBackGround(Graphics g){
		int w=ik.getBackGround().getWidth(null);
		g.clipRect(0, 0, Action.dispX, Action.dispY);
		if(worlddis>=0.0){
			g.drawImage(	ik.getBackGround(),
							-(int)((worlddis*0.25)%w),
							0,null);
			
			int margin=Action.dispX-(w-(int)((worlddis*0.25)%w));
			if(margin>0){
				g.drawImage(ik.getBackGround(), w-(int)((worlddis*0.25)%w), 0, null);
			}
		}else{
			g.drawImage(	ik.getBackGround(),
							(int)Math.abs((worlddis*0.25)%w),
							0,null);
			
			int margin=(int)Math.abs((worlddis*0.25)%w);
			if(margin>0){
				g.drawImage(ik.getBackGround(), -(w-(int)Math.abs((worlddis*0.25)%w)), 0, null);
			}
		}
	}
	
	protected double getWorldDistance(){
		return worlddis;
	}
	
	static final Font font=new Font(Font.SANS_SERIF ,Font.ITALIC,28);
	int effectFrame;
	protected void drawWorldEffect(Graphics g){
		//TODO:チェックポイントの設定と描画
		double checkAfter=worlddis%(BARRIER_SIZE*200);
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		if(checkAfter>BARRIER_SIZE*20 && checkAfter<BARRIER_SIZE*70){
			g2.setFont(font);
			String display=String.format("次のチェックポイントまで%4dm",(int)(BARRIER_SIZE*200-checkAfter));
			//ずらして表示
			g2.setColor(Color.white);
			g2.drawString(display,
					Flow.dispX-display.length()*font.getSize()/2+2,
					Flow.dispY/2+2);
			g2.setColor(Color.RED);
			g2.drawString(display,
					Flow.dispX-display.length()*font.getSize()/2,
					Flow.dispY/2);
			///
		}else if(checkAfter>BARRIER_SIZE*150){
			g2.setFont(font);
			String display=String.format("チェックポイントまで%4dm",(int)(BARRIER_SIZE*200-checkAfter));
			g2.setColor(Color.white);
			g2.drawString(display,Flow.dispX-display.length()*font.getSize()/2+2,Flow.dispY/2+2);
			g2.setColor(Color.RED);
			g2.drawString(display,Flow.dispX-display.length()*font.getSize()/2,Flow.dispY/2);
	
		}
		
		//TODO:nextCheckPointの導入
		int toCheckPoint = (int) (nextCheckPoint-worlddis);
		if(toCheckPoint<OFFSCREEN_MAX_X){
			g2.setFont(font);
			String display="チェックポイント";
			g2.setColor(Color.RED);
			g2.drawString(display,toCheckPoint+Flow.dispX-display.length()*font.getSize()/2,font.getSize());
			g2.drawLine(toCheckPoint+Flow.dispX, 0, toCheckPoint+Flow.dispX, Action.dispY);
		}
		
		if(effectFrame>0){
			g2.setFont(font);
			String display="チェックポイント・ボーナス";
			g2.setColor(Color.white);
			g2.drawString(display,Flow.dispX*3/4-display.length()*font.getSize()/2+2,Flow.dispY/4+2);
			g2.setColor(Color.RED);
			g2.drawString(display,Flow.dispX*3/4-display.length()*font.getSize()/2,Flow.dispY/4);
			
			display="+10000";
			g2.setColor(Color.white);
			g2.drawString(display,Flow.dispX*3/4-display.length()*font.getSize()/2+2,Flow.dispY/4+2+font.getSize());
			g2.setColor(Color.RED);
			g2.drawString(display,Flow.dispX*3/4-display.length()*font.getSize()/2,Flow.dispY/4+font.getSize());
			effectFrame--;
			isCheckPassed=false;
		}
		
		if(nextCheckPoint<worlddis){
			nextCheckPoint+=200*BARRIER_SIZE;
			effectFrame=240;
			isCheckPassed=true;
			g2.setFont(font);
		}
		

	}
	
	protected int getCheckBonus(){
		if(isCheckPassed)		return 10000;
		else					return 0;
	}
	
	
	public boolean isCheckPassed(){
		return isCheckPassed;
		
	}
	
	protected void makeObject(int type,double x,double y){
		switch(type){
		//空白
		case 0:break;
		
		//敵類
		case 1:
			synchronized(enemy){enemy.add(new ZACO_A(ik,x+Enemy.ssizex/2-Barrier.ssizex,y+(Enemy.ssizey-Barrier.ssizey)/2,0.0));}
		break;
		case 2:
			synchronized(enemy){enemy.add(new ZACO_B(ik,x+Enemy.ssizex/2-Barrier.ssizex,y+(Enemy.ssizey-Barrier.ssizey)/2,0.0));}
		break;
		case 3:
			synchronized(enemy){enemy.add(new ZACO_C(ik,x+Enemy.ssizex/2-Barrier.ssizex,y+(Enemy.ssizey-Barrier.ssizey),0.0));}
		break;
		
		//アイテム類
		case 0x40:
			synchronized(item){item.add(new Frank(ik,x,y));}
		break;
		case 0x41:
			synchronized(item){item.add(new Crepe(ik,x,y));}
		break;
		case 0x42:
			synchronized(item){item.add(new StarItem(ik,x,y));}
		break;
		
		//障害物類
		case 0xef:
			synchronized(barrier){barrier.add(new CloudLeft(ik,x,y));}
		break;
		case 0xee:
			synchronized(barrier){barrier.add(new CloudMid(ik,x,y));}
		break;
		case 0xed:
			synchronized(barrier){barrier.add(new CloudRight(ik,x,y));}
		break;
		
		case 0xfe:
			synchronized(barrier){barrier.add(new Ground(ik,x,y));}
		break;
		case 0xff:
			synchronized(barrier){barrier.add(new Block(ik,x,y));}
		break;
		
			//模擬店
		case 0xdf:
			synchronized(barrier){barrier.add(new StoreUpperLeft(ik,x,y));}
		break;
		case 0xde:
			synchronized(barrier){barrier.add(new StoreTop(ik,x,y));}
		break;
		case 0xdd:
			synchronized(barrier){barrier.add(new StoreUpperRight(ik,x,y));}
		break;
		
		case 0xdc:
			synchronized(barrier){barrier.add(new StoreLeft(ik,x,y));}
		break;
		case 0xdb:
			synchronized(barrier){barrier.add(new StoreCenter(ik,x,y));}
		break;
		case 0xda:
			synchronized(barrier){barrier.add(new StoreRight(ik,x,y));}
		break;
		
		case 0xd9:
			synchronized(barrier){barrier.add(new StoreLowerLeft(ik,x,y));}
		break;
		case 0xd8:
			synchronized(barrier){barrier.add(new StoreBottom(ik,x,y));}
		break;
		case 0xd7:
			synchronized(barrier){barrier.add(new StoreLowerRight(ik,x,y));}
		break;
		
		//未定義
		default:System.err.printf("未定義のオブジェクトが指定されました。番号：%02x\n",type);
		}
	}
}
