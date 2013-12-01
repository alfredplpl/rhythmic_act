package flow;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *タイトル画面を表示するクラスのためのインターフェースです。
 *ロゴなども表示します。
 *
 */
public interface Title {
 
	public abstract boolean isContinue();
	public abstract void act(KeyTable key);
	public abstract void draw(Graphics g);
	public abstract void reset();
	
}
 
