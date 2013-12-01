package action;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import flow.Flow;
import flow.GameConst;

import actionbarrier.Barrier;
import actionbarrier.Block;
import actionenemy.*;

import keyio.KeyTable;
import oto.Note;
import oto.OtoEvent;

/**
 * Actionを実装したゲームです。マリオっぽい雰囲気でゲームを進めます。
 * 
 * @author みそかつおでん
 *
 */
public class SuperMASAO implements Action{
	MASAO masao;
	ArrayList<Enemy> enemy;
	ArrayList<Enemy> removeList;
	ArrayList<Item> item;
	ArrayList<Item> removeItem;	
	ArrayList<Barrier> barrier;
	ArrayList<Barrier> removeBarrier;
	ImageKeeping ik;
	World world;
	int framescore;
	
	boolean SEon;
	
	
	SuperMASAO(int stage) throws Exception{
		framescore=0;
		ik=new ImageKeeping();
		masao=new MASAO(ik);
		enemy=new ArrayList<Enemy>();
		removeList=new ArrayList<Enemy>();
		item=new ArrayList<Item>();
		removeItem=new ArrayList<Item>();		
		barrier=new ArrayList<Barrier>();
		removeBarrier=new ArrayList<Barrier>();
		world=new World(enemy, barrier,item,ik,stage);

		//SEの設定
		Properties set=new Properties();
		set.load((new FileInputStream(GameConst.SETTING_PATH)));
		String se=set.getProperty("supermasao.se");
		if(se==null){
			SEon=false;
		}else{
			SEon=Boolean.valueOf(se);
		}
		
		/*
		for(int x=0;x<dispX-100;x+=25){
			barrier.add(new Block(ik,x,100));
		}
		*/
		/*
		item.add(new StarItem(ik,500,200));
		item.add(new Crepe(ik,400,200));
		item.add(new Frank(ik,600,200));
		*/
	}

	public void act(Note[] note, KeyTable keytable) {
		masao.setMotion(keytable);
		
		if(note!=null){
			for(Note n:note){
				synchronized(enemy) {
					makeEnemy(n);
				}
			}
		}
			
		for(Enemy e:enemy){
			if(masao.isHitEnemy(e.getArea())){
				framescore+=e.getScore();
				e.destroy();
			}
			if(e.isDestroyed())removeList.add(e);
		}
		
		synchronized(enemy){
			enemy.removeAll(removeList);
		}
		removeList.clear();
		
		masao.isHitBarrier(barrier);
		
		for(Barrier r:barrier){
			for(Enemy e:enemy){
				e.isHitBarrier(r.getArea());
			}
			
			if(r.getArea().getMinX()<World.OFFSCREEN_MIN_X || r.getArea().getMaxX()>World.OFFSCREEN_MAX_X)
				removeBarrier.add(r);
			
		}
		synchronized(barrier){
			barrier.removeAll(removeBarrier);
		}
		removeBarrier.clear();
		
		
		for(Item i:item){
			if(masao.isHitItem(i)){
				framescore+=i.getScore();
				i.destroy();
			}
			if(i.isDestroyed())removeItem.add(i);
		}
		
		synchronized(item){
			item.removeAll(removeItem);
		}
		removeItem.clear();
		
		
		for(Enemy e:enemy){
			e.move();
		}
		
		masao.move(world);
	}

	private void makeEnemy(Note note) {
		switch(note.getType()){
		case EnemyType.ZACO_A:
			//スピードが速すぎると、下についたことを検出する前に下へ貫通してしまう。
			enemy.add(new ZACO_A(ik,Flow.dispX+note.getX()+Enemy.ssizex/2,dispY+ZACO_A.ssizey/2,note.getVelocity()));
		break;
		case EnemyType.ZACO_B:
			enemy.add(new ZACO_B(ik,Flow.dispX+note.getX()+Enemy.ssizex/2,dispY+ZACO_B.ssizey/2,note.getVelocity()));
		break;
		case EnemyType.ZACO_C:
			enemy.add(new ZACO_C(ik,Flow.dispX+note.getX()+Enemy.ssizex/2,dispY+ZACO_C.ssizey/2,note.getVelocity()));
		break;
		
		default:System.out.println("未定義の敵です。番号："+note.getType());
		}
	}

	public Image draw() {
		return null;
	}

	public void drawDirect(Graphics g) {
		//Graphics2D g2=(Graphics2D)g;
		//g.setColor(Color.gray);
		//g.fillRect(0, 0, dispX, dispY);
		
		//g.setColor(Color.cyan);
		
		world.drawBackGround(g);
		world.drawWorldEffect(g);		
		//g.drawRect(masao.getUpperLeftX(),Action.dispY-masao.getUpperLeftY(),(int)masao.rect.getWidth(),(int)masao.rect.getHeight());
		//g.fillRect((int)(masao.rect.getX()+masao.rect.getWidth()), dispY-(int)(masao.rect.getY()), 1, 1);
		
		//まさおの描画
		g.drawImage(masao.getImage(),masao.getUpperLeftX(),Action.dispY-masao.getUpperLeftY(),null);
		//デバッグ:政夫のあたり判定
		g.drawRect((int)masao.rect.x,Action.dispY-(int)masao.rect.y,(int)masao.rect.width,(int)masao.rect.height);
		
		synchronized(enemy){
			for(int i=0;i<enemy.size();i++){
				Enemy e=enemy.get(i);
				int w=(int)e.getArea().getWidth();
				int h=(int)e.getArea().getHeight();
				int x=(int)e.getArea().getMinX();
				int y=Action.dispY-(int)e.getArea().getMinY();
				
				g.drawImage(e.getImage(),x,y,null);
				//g.drawRect(x,y,w,h);
				//g.fillRect((int)e.getArea().getCenterX(), Action.dispY-(int)(e.getArea().getY()-e.getArea().height/2), 1, 1);
			}
		}
		
		synchronized(barrier){
			for(Barrier e:barrier){
				int x=e.getUpperLeftX();
				int y=Action.dispY-e.getUpperLeftY();
				int w=(int)e.getArea().getWidth();
				int h=(int)e.getArea().getHeight();
				
				g.drawImage(e.getImage(),x,y,null);
				//g.drawRect(x,y,w,h);
				//g.fillRect((int)e.getArea().getCenterX(), Action.dispY-(int)(e.getArea().getY()-e.getArea().height/2), 1, 1);
			}
		}
		
		synchronized(item){
			for(Item e:item){
				int x=(int)e.getArea().getMinX();
				int y=Action.dispY-(int)e.getArea().getMinY();
				int w=(int)e.getArea().getWidth();
				int h=(int)e.getArea().getHeight();
				
				g.drawImage(e.getImage(),x,y,null);
			}
		}
		masao.drawEffect(g);
		//if(masao.getImage()==null)g.drawString("政夫死す", masao.getUpperLeftX(),Action.dispY-masao.getUpperLeftY()-20);
	}

	public ActionEvent[] getEvent() {
		if(world.isCheckPassed()){
			ActionEvent[] ae=new ActionEvent[1];
			ae[0]=new MASAOEvent(ActionEvent.JUDGE_BONUS);
			
			return ae;
		}else{
			return null;
		}
	}

	public int getFrameScore() {
		int framet;
		framet=framescore;
		framescore=0;
		
		return framet+world.getCheckBonus();
	}

	public void setEvent(OtoEvent[] event) {
		masao.setEvent(event);
	}

	public double getWorldDistance() {
		return world.getWorldDistance();
	}
	
}
