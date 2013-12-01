package flow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import keyio.KeyTable;

public class TimeCounter {
	private boolean isContinue;
	private int time;
	
	protected TimeCounter(){
		reset();
	}
	
	protected boolean isContinue(){
		return isContinue;
	}
	
	protected void act(KeyTable key){
		if(time<=0)isContinue=false;
		time--;
	}
	
	static final Font font=new Font(Font.SANS_SERIF ,Font.ITALIC,80);
	protected void draw(Graphics g){
		g.setFont(font);
		g.setColor(Color.black);
		g.fillRect(0, 0, 100, 100);
		g.setColor(Color.white);
		g.drawString(String.format("%02d", time/60), 0, 80);
	}
	
	protected void reset(){
		time=99*60;
		isContinue=true;
	}
}
