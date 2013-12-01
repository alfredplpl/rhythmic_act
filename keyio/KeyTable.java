package keyio;

import java.awt.event.KeyEvent;

/**
 * �L�[���͂��Ǘ�����N���X�ɕK�v�Ȓ萔�A�܂��̓��\�b�h���`���Ă��܂��B
 * ���̃C���^�[�t�F�[�X����������N���X�́A���X�i�[�ɂȂ邱�Ƃ��K�v�Ƃ���܂��B
 * �ڂ����̓p�b�P�[�W�̐������������������B
 * <b>1.1����萔�l���ς��܂����B</b>
 * @author �݂������ł�
 * @version 1.1
 *
 */
public interface KeyTable{
	/**
	 * ���y�Q�[���ň�ԍ��̃��[���ɑ�������L�[�ԍ��ł��B
	 */
	public static final int OTO_1 = KeyEvent.VK_Z;
	
	/**
	 * ���y�Q�[���œ�Ԗڂ̃��[���ɑ�������L�[�ԍ��ł��B
	 */
	public static final int OTO_2 = KeyEvent.VK_X;
	
	/**
	 * ���y�Q�[���ŎO�Ԗڂ̃��[���ɑ�������L�[�ԍ��ł��B
	 */
	public static final int OTO_3 = KeyEvent.VK_C;
	
	/**
	 * ���y�Q�[���Ŏl�Ԗڂ̃��[���ɑ�������L�[�ԍ��ł��B
	 */
	public static final int OTO_4 = KeyEvent.VK_V;
	
	/**
	 * �\�񂵂Ă��邾���Ŏ����͂��܂���B
	 */
	public static final int OTO_5 = KeyEvent.VK_B;
	
	/**
	 * �\�񂵂Ă��邾���Ŏ����͂��܂���B
	 */
	public static final int OTO_6 = KeyEvent.VK_N;
	
	/**
	 * �\�񂵂Ă��邾���Ŏ����͂��܂���B
	 */
	public static final int OTO_7 = KeyEvent.VK_M;
	
	/**
	 * �\�񂵂Ă��邾���Ŏ����͂��܂���B
	 */
	public static final int OTO_8 = KeyEvent.VK_SHIFT;
	
	/**
	 * �A�N�V�����Q�[���ō��֍s�����Ƃɑ�������L�[�ԍ��ł��B
	 */
	public static final int ACT_LEFT = KeyEvent.VK_LEFT;
	
	/**
	 * �A�N�V�����Q�[���ŉE�֍s�����Ƃɑ�������L�[�ԍ��ł��B
	 */
	public static final int ACT_RIGHT = KeyEvent.VK_RIGHT;
	
	/**
	 * �A�N�V�����Q�[���ŃW�����v���邱�Ƃɑ�������L�[�ԍ��ł��B
	 */
	public static final int ACT_UP = KeyEvent.VK_UP;
	
	/**
	 * �A�N�V�����Q�[���ł��Ⴊ�ނ��Ƃɑ�������L�[�ԍ��ł��B
	 * �܂��A�������Ȃ��Ă����\�ł��B
	 */
	public static final int ACT_CIRLE = KeyEvent.VK_BACK_SLASH;
	
	/**
	 * �A�N�V�����Q�[���ł��Ⴊ�ނ��Ƃɑ�������L�[�ԍ��ł��B
	 * �܂��A�������Ȃ��Ă����\�ł��B
	 */
	public static final int ACT_CROSS = KeyEvent.VK_SLASH;
	
	
	/**
	 * �A�N�V�����Q�[���ł��Ⴊ�ނ��Ƃɑ�������L�[�ԍ��ł��B
	 * �܂��A�������Ȃ��Ă����\�ł��B
	 */
	public static final int ACT_DOWN = KeyEvent.VK_DOWN;
	
	
	/**
	 * ������Ӗ����܂��B
	 */
	public static final int OK = KeyEvent.VK_ENTER;
	
	/**
	 * �L�����Z�����Ӗ����܂��B
	 */
	public static final int CANCEL = KeyEvent.VK_SPACE;
	
	/**
	 * �I�����Ӗ����܂��B
	 */
	public static final int EXIT = KeyEvent.VK_ESCAPE;
	
	/**
	 * �����ɑΉ�����L�[��������Ă��邩�ǂ������肵�܂��B
	 * �������A���������Ƃ◣�������Ƃɂ��Ă͔���ł��܂���B
	 * @param keycode�@KeyTable�ŋK�肳��Ă���ϐ�
	 * @return �w�肵���L�[��������Ă��邩�ǂ���
	 */
	public boolean isPressing(int keycode);

}
