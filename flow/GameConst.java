package flow;

/**
 * �Q�[���Ŏg�p���鑍���I�Ȓ萔���Ǘ����܂��B
 * @author �݂������ł�
 * @version 1.2
 */
public interface GameConst {
	/**
	 * �ݒ�t�@�C���̏ꏊ��\���܂��B
	 */
	public static final String SETTING_PATH="./setting.properties";
	
	/**
	 * �P�t���[���̃~���b�����ɑ������܂��B
	 */
	static final long FRAME_MILI = 16;
	
	/**
	 * �P�t���[���̃i�m�b�����ɑ������܂��B
	 */
	static final int FRAME_NANO = 700000;
	
	/**
	 * �i�m�b�ɕϊ������P�t���[���̎��Ԃɑ������܂��B���ݓs���ɂ��A�l���ς���Ă��܂��B
	 */
	//static final long FRAME_TIME = 16700000;
	static final long FRAME_TIME = 16666000;
}
