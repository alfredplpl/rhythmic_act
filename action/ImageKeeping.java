package action;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import actionbarrier.*;
import actionenemy.*;

/**
 * アクションゲーム内で使われる画像をすべて保管します。 プレーヤーや障害物などの各コンストラクタの引数として用いられ、
 * そのクラスの名前によって、イメージ配列を返します。 なお、取得するに入っているデータは、２次元配列で、
 * 一番目の次元は状態、二番目の次元はフレームを意味します。 たとえば、状態MOVE_LEFTで image[MOVE_LEFT][frame]のように、
 * 一フレームごとで取得するとアニメーションできることになります。
 * 
 * @author みそかつおでん
 * @version 1.1
 * 
 */
public class ImageKeeping {
	/**
	 * ゲーム画像のアドレスです。変わることがあるので必ず定数指定してください。
	 */
	static final String img = "testimg/";
	//BufferedImage Img1 = null;
	//Image Image[][] = new BufferedImage[100][100];	

	HashMap<String, Image[][]> image;
	VolatileImage bg;
	static final String bgPath="./testimg/Act-BackGround.png";
	GraphicsConfiguration gc;
	
	/**
	 * コンストラクタが呼び出されたとき、一斉に読み込んでください。
	 * 
	 * @throws Exception
	 *             読み込みに失敗したとき
	 */
	ImageKeeping() throws Exception {
		image = new HashMap<String, Image[][]>();
		
		//bg=Toolkit.getDefaultToolkit().createImage(bgPath);
		BufferedImage tmp = ImageIO.read(new File(bgPath));
		gc=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		bg=gc.createCompatibleVolatileImage(tmp.getWidth(),tmp.getHeight());
		bg.getGraphics().drawImage(tmp,0,0,null);
		
		loadMASAO();
		loadEnemy();
		loadBarrier();
		loadItem();
	}


	/**
	 * 指定された画像ファイルを１コマあたり(sx,sy)に分割します。 横方向に行くと、表示するコマが進み。 縦方向に行くと、表示する状態が変わります。
	 * 詳しくは<a href="frame.png">図</a>を参照ください。 ImageIOクラスを利用すると楽になります。
	 * 
	 * @param str
	 *            クラス名
	 * @return 分割したイメージ
	 */
	private Image[][] splitImage(String str,int sx,int sy, int[] splitframe)
			throws Exception {
		BufferedImage img1=ImageIO.read(new File(img+str+".png"));
		Image[][] imgt=new Image[splitframe.length][];
		
		for (int i = 0; i < splitframe.length; i++) {
			imgt[i]=new Image[splitframe[i]];
			for (int j = 0; j < splitframe[i]; j++) {
				imgt[i][j]= img1.getSubimage(sx * (j),sy * (i) , sx, sy);
			}
		}
		
		return imgt;
	}

	/**
	 * クラス名から画像を読み出します。
	 * 
	 * @param str
	 *            　クラス名
	 * @return　クラスが持つべき画像
	 */
	public Image[][] getImage(String str){
		return image.get(str);
	}
	
	/**
	 * 背景のイメージを取得するときに使います。
	 * @return 背景イメージ
	 */
	public Image getBackGround(){
		if(bg.contentsLost())
			bg.validate(gc);

		return bg;
	}

	/**
	 * 主人公の画像を読み出します。名前は"MASAO.png"です。
	 * 
	 * @throws Exception
	 *             　読み込みに失敗したとき
	 */
	private void loadMASAO() throws Exception {
		image.put("MASAO",splitImage("MASAO",64,64,MASAO.frame_limit));
		image.put("MASAO_BIG",splitImage("MASAO_BIG",96,96,MASAO.frame_limit));
		image.put("ActEffect",splitImage("ActEffect",125,75,MASAO.EFFECT_LIMIT));
		
	}

	/**
	 * パッケージactionenemy内にあるすべてのエネミーに対応する 画像を読み込みます。画像の名前は、クラス名+".png"です。
	 * ただし、EnemyクラスとEnemyTypeクラスは除きます。 なお、格納するときのキーは、クラス名になります。
	 * 
	 * @throws Exception
	 *             読み込みに失敗したとき
	 */
	private void loadEnemy() throws Exception {
		image.put("ZACO_A",splitImage("ZACO_A",64,64,ZACO_A.frame_limit));
		image.put("ZACO_B",splitImage("ZACO_B",64,64,ZACO_B.frame_limit));
		image.put("ZACO_C",splitImage("ZACO_C",64,64,ZACO_C.frame_limit));
	}

	/**
	 * パッケージactionbarrier内にあるすべての障害物に対応する 画像を読み込みます。画像の名前は、クラス名+".png"です。
	 * ただし、Barrierクラスは除きます。 なお、格納するときのキーは、クラス名になります。
	 * 
	 * @throws Exception
	 *             読み込みに失敗したとき
	 */
	private void loadBarrier() throws Exception {
		image.put("Block",splitImage("Block",25,25,Block.frame_limit));
		image.put("CloudMid",splitImage("CloudMid",25,25,CloudMid.frame_limit));
		image.put("CloudLeft",splitImage("CloudLeft",25,25,CloudLeft.frame_limit));
		image.put("CloudRight",splitImage("CloudRight",25,25,CloudRight.frame_limit));
		image.put("Ground",splitImage("Ground",25,25,Ground.frame_limit));
		
		image.put("StoreUpperLeft",splitImage("StoreUpperLeft",25,25,StoreUpperLeft.frame_limit));
		image.put("StoreTop",splitImage("StoreTop",25,25,StoreTop.frame_limit));
		image.put("StoreUpperRight",splitImage("StoreUpperRight",25,25,StoreUpperRight.frame_limit));
		image.put("StoreLeft",splitImage("StoreLeft",25,25,StoreLeft.frame_limit));
		image.put("StoreCenter",splitImage("StoreCenter",25,25,StoreCenter.frame_limit));
		
		image.put("StoreRight",splitImage("StoreRight",25,25,StoreRight.frame_limit));
		image.put("StoreLowerLeft",splitImage("StoreLowerLeft",25,25,StoreLowerLeft.frame_limit));
		image.put("StoreBottom",splitImage("StoreBottom",25,25,StoreBottom.frame_limit));
		image.put("StoreLowerRight",splitImage("StoreLowerRight",25,25,StoreLowerRight.frame_limit));

	}
	/**
	 * パッケージaction内にあるすべてのアイテムに対応する 画像を読み込みます。画像の名前は、クラス名+".png"です。
	 * ただし、Itemクラスは除きます。 なお、格納するときのキーは、クラス名になります。
	 * @throws Exception 読み込みに失敗したとき
	 */
	private void loadItem() throws Exception {
		image.put("Crepe",splitImage("Crepe",25,25,Crepe.frame_limit));
		image.put("Frank",splitImage("Frank",25,25,Frank.frame_limit));
		image.put("StarItem",splitImage("StarItem",25,25,StarItem.frame_limit));
	}

}
