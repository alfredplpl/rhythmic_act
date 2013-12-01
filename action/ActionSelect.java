package action;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *�A�N�V�����Q�[���̓�Փx��I�������邽�߂̃C���^�[�t�F�[�X�ł��B
 *���̉�ʂ́A��Փx�̈ꗗ�Ɠ�Փx�̏ڍׁA������@�ɂ���č\������Ă��܂��B
 *���[�U�́A
 *KeyTable.ACT_LEFT�œ�Փx�I���̃t���[�����E�����Ɉړ������A
 *KeyTable.ACT_RIGHT�Ńt���[�����������Ɉړ������邱�Ƃ��ł��܂��B
 *�܂�KeyTable.ACT_UP�ɉ������Ƃœ�Փx�����肷�邱�Ƃ��ł��܂��B
 *��̓I�Ȕz�u��S�̂̃C���[�W�͕ʓr�}���Q�Ƃ��Ă��������B
 *@version 1.1
 */
public interface ActionSelect {
	/**
	 * �A�N�V�����Q�[���̓�Փx�̑I����ʂƂ��ĕ\���ł���ő啝�ł��B
	 */
	public static final int dispX=1024;

	/**
	 * �A�N�V�����Q�[���̓�Փx�̑I����ʂƂ��ĕ\���ł���c�̍ő�̒����ł��B
	 */
	public static final int dispY=384;
	
	
	/**
	 * ��Փx�̑I���𑱂��邩�ǂ�����Ԃ��܂��B��Փx�����肵���ꍇ�Afalse��Ԃ��悤�ɂ��Ă��������B
	 * @return �����邩�ǂ���
	 */
	public abstract boolean isContinue();
	
	/**
	 * ��Փx�̌����t���[���̈ړ����s���܂��B
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
	 * �I��������Փx��Ԃ��܂��B
	 * @return�@Action�C���^�[�t�F�[�X�ɂĒ�`���ꂽ��Փx�ɑΉ����鐮���^�̒l�B�I������Ă��Ȃ��ꍇ��0
	 */
	public int getDifficulty();
}
 
