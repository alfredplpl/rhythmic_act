package action;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class TestActionDraw extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//ここにテストしたいクラスの名前を入れてください。
	final static String テスト用クラス = "MASAO";
	int sizex,sizey;
	Image[][] imgs;
	
	TestActionDraw(Image[][] imgs,int sizex,int sizey){
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.imgs=imgs;
		this.sizex=sizex;
		this.sizey=sizey;
		JPanel j = new MyPanel();
		j.setPreferredSize(new Dimension(imgs[0].length*sizex,imgs.length*sizey));
		//this.setSize();
		this.setSize(imgs[0].length*sizex+200,imgs.length*sizey+100);
		this.add(j);
	}
	
	public static void main(String arg[]) throws Exception{
		ImageKeeping imgkeep=new ImageKeeping();

		Image img[][] =imgkeep.getImage(テスト用クラス);
		JFrame frame=new TestActionDraw(img,img[0][0].getWidth(null),img[0][0].getHeight(null));
		frame.setVisible(true);
	}
	
	class MyPanel extends JPanel{
		@Override
		public void paint(Graphics g){
			super.paint(g);
			for(int i=0;i<imgs.length;i++){
				for(int j=0;j<imgs[i].length;j++){
					g.drawRect(j*sizex, i*sizey, (j+1)*sizex+sizex, (i+1)*sizey);
					if(imgs[i][j]!=null)
						g.drawImage(imgs[i][j], j*sizex, i*sizey, this);
					else
						g.drawString("画像なし",  j*sizex, i*sizey+sizey/2);
				}
			}
		}
	}
}
