package flow;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import oto.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * メインのウィンドウクラスです。
 * フルスクリーン化については、
 * <a href="http://www.javainthebox.net/laboratory/JDK1.4/Graphics/FullScreen/FullScreen.html">
 * http://www.javainthebox.net/</a>
 * を参考にしました。
 * また、BufferStrategyについては、
 * <a href="http://www.javainthebox.net/laboratory/JDK1.4/Graphics/BufferStrategy/BufferStrategy.html">
 * http://www.javainthebox.net/</a>
 * を参考にしました。
 * @author みそかつおでん
 *
 */
public final class MainWindow extends JFrame implements WindowListener,Runnable{
	GraphicsDevice device;
	DisplayMode orgMode;
	
	Flow flow;
	Title title;
	Ranking rank;
	
	Timer timer;
	BufferStrategy bs;
	Thread drawThread;
	Thread gameThread;
	Image loading;
	ButtonListener keytable;
	
	static final int dispX=1024;
	static final int dispY=768;
	
	static final int BUFFER_SIZE=2;
	
	MainWindow(){
		super();
        setResizable(false);
		setUndecorated(true);
        setIgnoreRepaint(true);
        setAlwaysOnTop(true);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = device.getDefaultConfiguration();
	
		try{
			device.setFullScreenWindow(this);
			
			orgMode=device.getDisplayMode();
			DisplayMode mode = new DisplayMode(dispX, dispY, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
			
			device.setDisplayMode(mode);
			this.addWindowListener(this);
			
			this.createBufferStrategy(BUFFER_SIZE);
			bs=this.getBufferStrategy();

	        //ゲームの中身の生成
			loading= ImageIO.read(new File("./testimg/Manual.PNG"));
			
			keytable=new ButtonListener();
			this.addKeyListener(keytable);
			
			//各画面の生成
			title=new TitleA(this);
			rank=new RankingA();
			
		} catch (Exception e){
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(this,e.getClass()+"\n"+e.getMessage());
        	device.setFullScreenWindow(null);
        	System.exit(0);
    	}
		
		setVisible(true);
        drawThread=new Thread(this);
        //drawThread.setPriority(Thread.MAX_PRIORITY);
        drawThread.start();
        
	}
	
	public static void main(String arg[]){
		MainWindow window=new MainWindow();
		System.err.println("start");
	}
	
	public void windowClosing(WindowEvent arg0) {

	}
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}

	public void run() {
		long time1, time2,dif;
		double fps=60.0;
		double nano = GameConst.FRAME_TIME;
		int frameCount=0;
		int sleepCount=0;
		int drawCount=0;
		long trackTime;
		long startTime;
		
		//ゲームのメインループ
		MAINLOOP:while(true){
			//タイトル画面(唯一終了可能)
			boolean ExitisPressed=true;
			title.reset();

			while(title.isContinue()){
				try {
					time1 = System.nanoTime();
					
					Graphics2D g = (Graphics2D) bs.getDrawGraphics();
					if (!bs.contentsLost()) {//ここでエラー
						title.act(keytable);
						//タイトル画面でExitキーを押したら終了
						if(!ExitisPressed && !keytable.isContinue()){
							break MAINLOOP;
						}
						ExitisPressed=!keytable.isContinue();
						title.draw(g);
						
						bs.show();
						Toolkit.getDefaultToolkit().sync();
						g.dispose();
					} else{
						System.err.println("contents lost!");
					}
					time2 = System.nanoTime();
					dif = GameConst.FRAME_TIME - (time2 - time1);
					if (dif > 0) {
						Thread.sleep(dif / 1000000, (int) dif % 1000000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//ゲーム選択画面
			Selection sel=new Selection();
			while(sel.isContinue()){
				try {
					time1 = System.nanoTime();
					
					Graphics2D g = (Graphics2D) bs.getDrawGraphics();
					if (!bs.contentsLost()) {//ここでエラー
						sel.act(keytable);
						ExitisPressed=!keytable.isContinue();
						sel.draw(g);
						
						bs.show();
						Toolkit.getDefaultToolkit().sync();
						g.dispose();
					} else{
						System.err.println("contents lost!");
					}
					time2 = System.nanoTime();
					dif = GameConst.FRAME_TIME - (time2 - time1);
					if (dif > 0) {
						Thread.sleep(dif / 1000000, (int) dif % 1000000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//ゲームのロード
			BMSData bms=sel.getBMS();
			int stage=sel.getDifficulty();
			
			//選択画面の開放
			sel=null;
			System.gc();
			
			try {
				flow=new Flow(bms,stage);
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(	this, 
												"ゲーム読み込み中にエラーが発生しました。\n"+
												"詳細はコンソールの出力結果をご覧ください。"
				);
				e2.printStackTrace();
				continue;
			}
			this.addKeyListener(flow.getKeyListener());
			
//			ローディング画面（ゲーム本編）			
			while(flow.isNOTExitting() && !flow.isOK()){
				
				try {
					time1 = System.nanoTime();
					
					Graphics2D g = (Graphics2D) bs.getDrawGraphics();
					if (!bs.contentsLost()) {
						g.drawImage(loading,0,0,this);
	
						/*
						g.setColor(Color.white);
						g.drawString("Title:"+bms.getTitle(), dispX/2-20, dispY/2-45);
						g.drawString("Artist:"+bms.getArtist(), dispX/2-20, dispY/2-30);
						g.drawString("BPM:"+bms.getBPM(), dispX/2-20, dispY/2-15);
						g.drawString("Enterでスタート", dispX/2, dispY/2);
						*/
						bs.show();
						g.dispose();
					} else{
						System.err.println("contents lost!");
					}
					time2 = System.nanoTime();
					
					//たら付を防ぐ
					dif = GameConst.FRAME_TIME - (time2 - time1);
					if (dif > 0) {
						Thread.sleep(dif / 1000000, (int) dif % 1000000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			startTime=trackTime = System.nanoTime();
			
			gameThread=flow;

			int readyFrame;
			readyFrame=0;
			int finishFrame;
			finishFrame=240;
	        // ゲーム画面
			while (flow.isNOTExitting() && finishFrame>0) {
				time1 = System.nanoTime();
					frameCount++;
					
					Graphics2D g = (Graphics2D) bs.getDrawGraphics();
					
					Graphics gs=g.create(0, 0, Flow.dispX, Flow.dispY);
					Graphics go=g.create(Flow.dispX, 0, dispX, Oto.dispY);
					Graphics ga=g.create(0, Flow.dispY, dispX, dispY);
					//開始前の時間
					if(readyFrame==180)
						gameThread.start();
					
					//終了後の時間
					if(!flow.isContinue())finishFrame--;
					
					if (!bs.contentsLost()) {				
						flow.drawActionImage(ga);
						//バグらないように防ぐところ
						if(readyFrame>180)		flow.drawOtoImage(go);
						else					{
							final Color skyColor=new Color(148,204,228);
							go.setColor(skyColor);
							go.fillRect(0, 0, Oto.dispX, Oto.dispY);
						}
						flow.drawScoreImage(gs);					
						g.setColor(Color.DARK_GRAY);
						g.drawString(String.format("FPS     :%.2f", fps), 150,200);
						g.drawString(String.format("プレイ時間 :% 5d[ms]", (time1-startTime)/1000000),150,200+g.getFont().getSize());

						//Readyとか表示する部分
						if(	readyFrame<180){
							final Font font=new Font(Font.SANS_SERIF ,Font.ITALIC,72);
							Graphics2D g2=(Graphics2D)g;
							g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
							
							g2.setFont(font);
							g2.setColor(Color.white);
							g2.fillRect(0, MainWindow.dispY/2-40, MainWindow.dispX, 80);
							g2.setColor(Color.BLUE);
							g2.fillRect(0, MainWindow.dispY/2-40, MainWindow.dispX*(180-readyFrame)/180, 10);
							g2.fillRect(MainWindow.dispX*readyFrame/180, MainWindow.dispY/2+30, MainWindow.dispX, 10);
							g2.setColor(Color.red);
							int d=(180-readyFrame-1)/60+1;
							g2.drawString(String.format("%d Ready? %d",d,d), MainWindow.dispX/2-250, MainWindow.dispY/2+20);
							readyFrame++;
						}else if(readyFrame<180+120){
							final Font font=new Font(Font.SANS_SERIF ,Font.ITALIC,80);
							Graphics2D g2=(Graphics2D)g;
							g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						
							g.setFont(font);
							g.setColor(Color.white);
							g.drawString("GO", MainWindow.dispX/2-80+3, MainWindow.dispY/2+20+3);
							g.setColor(Color.red);
							g.drawString("GO", MainWindow.dispX/2-80, MainWindow.dispY/2+20);							
							readyFrame++;
						}else if(!flow.isContinue()){
							final Font font=new Font(Font.SANS_SERIF ,Font.ITALIC,80);
							Graphics2D g2=(Graphics2D)g;
							g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						
							g.setFont(font);
							g.setColor(Color.white);
							g.drawString("FINISH", MainWindow.dispX/2-160+3, MainWindow.dispY/2+20+3);
							g.setColor(Color.red);
							g.drawString("FINISH", MainWindow.dispX/2-160, MainWindow.dispY/2+20);							
							readyFrame++;						
						}
						
						gs.dispose();
						go.dispose();
						ga.dispose();
						g.dispose();
						
						drawCount++;
						bs.show();
						Toolkit.getDefaultToolkit().sync();
						
						if(time1 - trackTime>1000000000.0){
							fps=1000000000.0*frameCount/(double)(time1 - trackTime);
							trackTime+=time1 - trackTime;
							frameCount=0;
						}
						
					} else{
						System.err.println("contents lost!");
					}
				time2 = System.nanoTime();
				try {
					long waitTime=time1+GameConst.FRAME_TIME;
					dif = GameConst.FRAME_TIME- (time2 - time1);
					
					if (dif > 0) {
						long difa=dif*3/5;
						
						// Thread.sleepは、時間が過ぎた後、待機状態から実行可能状態に遷移する。
						// しかし、スケジューラの都合上、いつ実行状態に遷移するか分からない
						// このため、意図しないラスタスクロールが発生する。
						//これを防ぐため、余裕を持って終わらせた後、空回りで時間を調整する
						Thread.sleep(difa / 1000000);
						
						sleepCount++;
						
						//必要時間になるまで空回り
						while(waitTime>System.nanoTime());
						//Thread.sleep(dif / 1000000, (int) dif % 1000000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
			
			gameThread.interrupt();	
			//ゲームを強制終了した場合
			//if(!flow.isNOTExitting())continue;
			
			//リザルト画面
			Result result=new ResultA(flow);
			
			//ゲーム画面の開放
			flow=null;
			System.gc();
			
			while(result.isContinue()){
			//while(result.isContinue()){
				try {
					time1 = System.nanoTime();
					
					Graphics2D g = (Graphics2D) bs.getDrawGraphics();
					if (!bs.contentsLost()) {
						result.act(keytable);
						result.draw(g);

						bs.show();
						g.dispose();
					} else{
						System.err.println("contents lost!");
					}
					time2 = System.nanoTime();
					dif = GameConst.FRAME_TIME - (time2 - time1);
					
					if (dif > 0) {
						Thread.sleep(dif / 1000000, (int) dif % 1000000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}

			//ランキング画面
			rank.reset();
			rank.addScoreAndDifficulty(result.getScore(),stage);

			while(rank.isContinue()){
			//while(rank.isContinue()){
				try {
					time1 = System.nanoTime();
					
					Graphics2D g = (Graphics2D) bs.getDrawGraphics();
					if (!bs.contentsLost()) {
						rank.act(keytable);
						rank.draw(g);
						
						bs.show();						
						g.dispose();
					} else{
						System.err.println("contents lost!");
					}
					
					time2 = System.nanoTime();
					dif = GameConst.FRAME_TIME - (time2 - time1);
					if (dif > 0) {
						Thread.sleep(dif / 1000000, (int) dif % 1000000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
	        
	        //適当なウェイト
			System.gc();
			try {
				Thread.sleep(1024);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		System.err.println("frame:" + frameCount + " sleep:" + sleepCount+" draw:" + drawCount);
		if(drawCount!=0)
			System.err.println("処理落ち率：" + (drawCount - sleepCount)* 100.0 / drawCount + "%");

		//ランクデータ開放のため
		rank=null;
		title=null;
		
		//最後に余分なメモリの開放
		System.gc();

		//描画バッファ吐き出し用
		try {
			Thread.sleep(512);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		//全体の開放
        Window w = device.getFullScreenWindow();
        if (w != null) {
            w.dispose();
        }
        device.setFullScreenWindow(null);

	}

}
