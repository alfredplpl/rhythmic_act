package action;

/**
 * �C���[�W��ǂݍ��ނ̂ɕK�v�ȏ���񋟂���N���X�ł��B
 * @author �݂������ł�
 *
 */
public interface LoadImage {
	/**
	 * �ꖇ������̉������̃T�C�Y��Ԃ��܂��B
	 * @return�@�ꖇ������̉������̃T�C�Y
	 */
	public int getSizeX();
	
	/**
	 * �ꖇ������̏c�����̃T�C�Y��Ԃ��܂��B
	 * @return�@�ꖇ������̏c�����̃T�C�Y
	 */
	public int getSizeY();
	
	/**
	 * �e��Ԃɂ�����C���[�W������Ԃ��܂��B
	 * @return�@�e��Ԃɂ�����C���[�W����
	 */
	public int[] getFrameSize();
}
