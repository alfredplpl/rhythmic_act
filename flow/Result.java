package flow;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *�Q�[���̌��ʂ�\�����邽�߂̃C���^�[�t�F�[�X�ł��B
 */
public interface Result {	
	public abstract boolean isContinue();
	public abstract void act(KeyTable key);
	public abstract void draw(Graphics g);
	public int getScore();
}
 
