package flow;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {
	public static void main(String arg[]) throws Exception{
		String[] filename={	"StoreUpperLeft","StoreTop","StoreUpperRight",
							"StoreLeft","StoreCenter","StoreRight",
							"StoreLowerLeft","StoreBottom","StoreLowerRight"};
		
		BufferedImage img=ImageIO.read(new File("./testimg/Store.png"));
		
		int w=img.getWidth()/25;
		int h=img.getHeight()/25;
		
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				ImageIO.write(img.getSubimage(i*25, j*25, 25, 25), "png", new File("./testimg/"+filename[j*3+i]+".png"));
			}
		}
	}
}
