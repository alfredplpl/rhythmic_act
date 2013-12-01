package flow;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *�����L���O��\�����邽�߂̃C���^�[�t�F�[�X�ł��B
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
 
