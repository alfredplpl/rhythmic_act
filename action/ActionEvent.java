package action;

/**
 * ���y�Q�[���Ŕ���������ꂽ�C�x���g���ԐړI�Ɏ󂯓n��
 * �C���^�[�t�F�[�X�ł��B
 * ���͂܂���������K�v�͂���܂���B
 * 
 * @author �݂������ł�
 *
 */
public interface ActionEvent {
	
	public static int NO_EFFCT=0;
	
	public static int JUDGE_BONUS=5;
	
	/**
	 * ���y�Q�[���Ŕ���������ꂽActionEvent�̎�ނ��󂯎��܂��B
	 * @return ActionEvent�Œ�`�����A�N�V�����^�C�v
	 */
	public int getEventType();
	
	
}
