package flow;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *ランキングを表示するためのインターフェースです。
 */
public interface Ranking {
	public static final String rankDirectory="./rank/";
	public static int RANK=10;
	
	public abstract boolean isContinue();
	public void act(KeyTable key);
	public abstract void draw(Graphics g);
	public abstract void reset();
	public void addScoreAndDifficulty(int score,int stage);
	
}
 
