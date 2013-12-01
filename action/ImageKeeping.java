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
 * �A�N�V�����Q�[�����Ŏg����摜�����ׂĕۊǂ��܂��B �v���[���[���Q���Ȃǂ̊e�R���X�g���N�^�̈����Ƃ��ėp�����A
 * ���̃N���X�̖��O�ɂ���āA�C���[�W�z���Ԃ��܂��B �Ȃ��A�擾����ɓ����Ă���f�[�^�́A�Q�����z��ŁA
 * ��Ԗڂ̎����͏�ԁA��Ԗڂ̎����̓t���[�����Ӗ����܂��B ���Ƃ��΁A���MOVE_LEFT�� image[MOVE_LEFT][frame]�̂悤�ɁA
 * ��t���[�����ƂŎ擾����ƃA�j���[�V�����ł��邱�ƂɂȂ�܂��B
 * 
 * @author �݂������ł�
 * @version 1.1
 * 
 */
public class ImageKeeping {
	/**
	 * �Q�[���摜�̃A�h���X�ł��B�ς�邱�Ƃ�����̂ŕK���萔�w�肵�Ă��������B
	 */
	static final String img = "testimg/";
	//BufferedImage Img1 = null;
	//Image Image[][] = new BufferedImage[100][100];	

	HashMap<String, Image[][]> image;
	VolatileImage bg;
	static final String bgPath="./testimg/Act-BackGround.png";
	GraphicsConfiguration gc;
	
	/**
	 * �R���X�g���N�^���Ăяo���ꂽ�Ƃ��A��Ăɓǂݍ���ł��������B
	 * 
	 * @throws Exception
	 *             �ǂݍ��݂Ɏ��s�����Ƃ�
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
	 * �w�肳�ꂽ�摜�t�@�C�����P�R�}������(sx,sy)�ɕ������܂��B �������ɍs���ƁA�\������R�}���i�݁B �c�����ɍs���ƁA�\�������Ԃ��ς��܂��B
	 * �ڂ�����<a href="frame.png">�}</a>���Q�Ƃ��������B ImageIO�N���X�𗘗p����Ɗy�ɂȂ�܂��B
	 * 
	 * @param str
	 *            �N���X��
	 * @return ���������C���[�W
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
	 * �N���X������摜��ǂݏo���܂��B
	 * 
	 * @param str
	 *            �@�N���X��
	 * @return�@�N���X�����ׂ��摜
	 */
	public Image[][] getImage(String str){
		return image.get(str);
	}
	
	/**
	 * �w�i�̃C���[�W���擾����Ƃ��Ɏg���܂��B
	 * @return �w�i�C���[�W
	 */
	public Image getBackGround(){
		if(bg.contentsLost())
			bg.validate(gc);

		return bg;
	}

	/**
	 * ��l���̉摜��ǂݏo���܂��B���O��"MASAO.png"�ł��B
	 * 
	 * @throws Exception
	 *             �@�ǂݍ��݂Ɏ��s�����Ƃ�
	 */
	private void loadMASAO() throws Exception {
		image.put("MASAO",splitImage("MASAO",64,64,MASAO.frame_limit));
		image.put("MASAO_BIG",splitImage("MASAO_BIG",96,96,MASAO.frame_limit));
		image.put("ActEffect",splitImage("ActEffect",125,75,MASAO.EFFECT_LIMIT));
		
	}

	/**
	 * �p�b�P�[�Wactionenemy���ɂ��邷�ׂẴG�l�~�[�ɑΉ����� �摜��ǂݍ��݂܂��B�摜�̖��O�́A�N���X��+".png"�ł��B
	 * �������AEnemy�N���X��EnemyType�N���X�͏����܂��B �Ȃ��A�i�[����Ƃ��̃L�[�́A�N���X���ɂȂ�܂��B
	 * 
	 * @throws Exception
	 *             �ǂݍ��݂Ɏ��s�����Ƃ�
	 */
	private void loadEnemy() throws Exception {
		image.put("ZACO_A",splitImage("ZACO_A",64,64,ZACO_A.frame_limit));
		image.put("ZACO_B",splitImage("ZACO_B",64,64,ZACO_B.frame_limit));
		image.put("ZACO_C",splitImage("ZACO_C",64,64,ZACO_C.frame_limit));
	}

	/**
	 * �p�b�P�[�Wactionbarrier���ɂ��邷�ׂĂ̏�Q���ɑΉ����� �摜��ǂݍ��݂܂��B�摜�̖��O�́A�N���X��+".png"�ł��B
	 * �������ABarrier�N���X�͏����܂��B �Ȃ��A�i�[����Ƃ��̃L�[�́A�N���X���ɂȂ�܂��B
	 * 
	 * @throws Exception
	 *             �ǂݍ��݂Ɏ��s�����Ƃ�
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
	 * �p�b�P�[�Waction���ɂ��邷�ׂẴA�C�e���ɑΉ����� �摜��ǂݍ��݂܂��B�摜�̖��O�́A�N���X��+".png"�ł��B
	 * �������AItem�N���X�͏����܂��B �Ȃ��A�i�[����Ƃ��̃L�[�́A�N���X���ɂȂ�܂��B
	 * @throws Exception �ǂݍ��݂Ɏ��s�����Ƃ�
	 */
	private void loadItem() throws Exception {
		image.put("Crepe",splitImage("Crepe",25,25,Crepe.frame_limit));
		image.put("Frank",splitImage("Frank",25,25,Frank.frame_limit));
		image.put("StarItem",splitImage("StarItem",25,25,StarItem.frame_limit));
	}

}
