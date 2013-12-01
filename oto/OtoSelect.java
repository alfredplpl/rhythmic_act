package oto;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *���y�Q�[���̋Ȃ�I�������邽�߂̃C���^�[�t�F�[�X�ł��B
 *���̉�ʂ́A�ȃ��X�g�ƋȂ̏ڍׁA������@�ɂ���č\������Ă��܂��B
 *���[�U�́A
 *KeyTable.OTO_2�ŋȃ��X�g��������ɂ��炵�A
 *KeyTable.OTO_4�ŋȃ��X�g���������ɂ��炷���Ƃ��ł��܂��B
 *�܂�KeyTable.OTO_3�ɉ������ƂŋȂ����肷�邱�Ƃ��ł��܂��B
 *��̓I�Ȕz�u��S�̂̃C���[�W�͕ʓr�}���Q�Ƃ��Ă��������B
 *@version 1.1
 */
public interface OtoSelect {
	/**
	 * �Ȃ̑I����ʂƂ��ĕ\���ł���ő啝�ł��B
	 */
	public static final int dispX=1024;

	/**
	 * �Ȃ̑I����ʂƂ��ĕ\���ł���c�̍ő�̒����ł��B
	 */
	public static final int dispY=384;
	
	/**
	 * �Ȃ̑I���𑱂��邩�ǂ�����Ԃ��܂��B�Ȃ����肵���ꍇ�Afalse��Ԃ��悤�ɂ��Ă��������B
	 * @return �����邩�ǂ���
	 */
	public abstract boolean isContinue();
	
	/**
	 * �Ȃ̌����ȃ��X�g�̑�����s���܂��B
	 * @param key �e�L�[�̏��
	 */
	public abstract void act(KeyTable key);
	
	/**
	 * ���ڗ̈��`�悵�܂��B�w�肳�ꂽ�̈�ȏ��`�悵�Ă��悤�Ƃ��Ă��`�悳��Ȃ��̂ŁA
	 * �͂ݏo��Ƃ��l�����ɕ`�悵�Ă��������i�K�v�ȃI�t�X�N���[���`����������肵�Ă��������j�B
	 * @param g ���ڗ̈��`�悷��O���t�B�b�N�X
	 */
	public abstract void draw(Graphics g);
	
	/**
	 * ��Ԃ̏��������s���B
	 *
	 */
	public abstract void reset();
	
	/**
	 * �I����ʂőI�������Ȃ�Ԃ��܂��B
	 * @return �I�����ꂽBMS�̏��B�I������Ă��Ȃ��ꍇ��null
	 */
	public abstract BMSData getSelectedBMS();
	public abstract void finalize();
}
 
