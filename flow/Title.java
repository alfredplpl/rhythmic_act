package flow;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *�^�C�g����ʂ�\������N���X�̂��߂̃C���^�[�t�F�[�X�ł��B
 *���S�Ȃǂ��\�����܂��B
 *
 */
public interface Title {
 
	public abstract boolean isContinue();
	public abstract void act(KeyTable key);
	public abstract void draw(Graphics g);
	public abstract void reset();
	
}
 
