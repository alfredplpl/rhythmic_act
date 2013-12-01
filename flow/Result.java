package flow;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *ゲームの結果を表示するためのインターフェースです。
 */
public interface Result {	
	public abstract boolean isContinue();
	public abstract void act(KeyTable key);
	public abstract void draw(Graphics g);
	public int getScore();
}
 
