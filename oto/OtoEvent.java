package oto;

/**
 * <s>�܂��������Ȃ��Č��\�ł��B</s>
 * ���̃C���^�[�t�F�[�X�́A
 * ���y�Q�[���Ő������C�x���g��\�����邽�߂Ɏg���܂��B
 * �t�B�[���h�ɂ���C�x���g���������Ƃ���
 * �A�N�V�����Q�[���ɓ`���邱�ƂŁA
 * �A�N�V�����Q�[���ɗL���ȃC�x���g�𔭐������邱�Ƃ��ł��܂��B
 * ���Ƃ��΁A�R���{����30�ȏ�ɂȂ����ꍇ�A
 * �L�����N�^�[�̃X�s�[�h�������Ȃ�܂��B
 * 
 * @author �݂������ł�
 *
 */
public interface OtoEvent {
	/**
	 * �����N�����Ă��܂���B�f�t�H���g�l�Ƃ��Ďg�p���Ă��������B
	 */
	public static int NONEEFFECTIVE=0;
	
	/**
	 * ���y�Q�[���̃R���{����30�ȏ�100�����ł��鎖��\���C�x���g�萔�ł��B�����𖞂�������A���̒l�̃C�x���g�������������܂��B
	 */
	public static int COMBO30 = 5;
	
	/**
	 * ���y�Q�[���̃R���{����100�ȏ�ł��鎖��\���C�x���g�萔�ł��B�����𖞂�������A���̒l�̃C�x���g�������������܂��B
	 */
	public static int COMBO100 = 15;
	
	/**
	 * �A�N�V�����Q�[���ɂ���Ĕ�����������C�x���g�̎�ނ�Ԃ��܂��B
	 * @return OtoEvent�Œ�`�����C�x���g�萔
	 */
	public int getEventType();
}