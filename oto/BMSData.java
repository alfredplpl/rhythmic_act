package oto;

/**
 * BMS�̊�{�I�ȏ����i�[����N���X���K�肷��C���^�[�t�F�[�X�ł��B
 * �����̃f�[�^�́A������A���邢�͐����^�������݂����A
 * ���y�f�[�^�Ȃǂ̋�̓I�ȃf�[�^�͊܂܂�܂���B
 * 
 * @author �݂������ł�
 *
 */
public interface BMSData {
	/**
	 * BMS�̃^�C�g����Ԃ��܂��B
	 * @return BMS�̃^�C�g��
	 */
	public String getTitle();
	
	/**
	 * BMS�̐���Җ���Ԃ��܂��B
	 * @return BMS�̐���Җ���
	 */
	public String getArtist();
	
	/**
	 * BMS�̃W��������Ԃ��܂��B
	 * @return BMS�̃W������
	 */
	public String getGenre();
	
	/**
	 * BMS�̃v���C���x����Ԃ��܂��B
	 * @return BMS�̃v���C���x��
	 */
	public int getPlayLevel();
	
	/**
	 * BMS��BPM(�P��������Ɏl��������ł�)��Ԃ��܂��B
	 * @return BMS��BPM
	 */
	public int getBPM();
	
	/**
	 * BMS�t�@�C�����w���p�X��Ԃ��܂��B
	 * OtoFactory�ȂǂŎg���܂��B
	 * @return BMS�t�@�C�����w���p�X
	 */
	public String getPath();
	public String getPath2();
	public int getStartTime();
}
