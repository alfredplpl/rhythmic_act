package oto;

import java.awt.Image;

/**
 * �ォ��U���Ă���G���󂯓n�����߂̃N���X���K�肵�܂��B
 * @author �݂������ł�
 *
 * @version 1.1
 */
public interface Note {
	
	/**
	 * �G�̃^�C�v���w�肵�܂��B�Ȃ��A�G�̔ԍ���EnemyType�ŋK�肳��܂��B
	 * @return EnemyType�ŋK�肳���萔
	 */
	public int getType();
	
	/**
	 * �����Ă��鑬�x���w�肵�܂��B���Ԃ̓t���[���P�ʂŁA
	 * �K���ȃ^�C�~���O�Ō�������悤�ɗ����Ă��Ă��������B
	 * 
	 * @return�@�����Ă��鑬�x
	 */
	public double getVelocity();
	
	/**
	 * �����Ă���G�̉摜��n���܂��B
	 * �Ȃ��A�n���͎̂Q�Ƃ����ŁA�i�f�B�[�v�j�R�s�[����K�v�͂���܂���B
	 * @return�@�G�̃C���[�W
	 */
	public Image getImage();
	
	/**
	 * �����Ă���I�u�W�F�N�g�̒��S��X���W��Ԃ��܂�
	 * @return �����Ă���I�u�W�F�N�g�̒��S��X���W
	 */
	public double getX();
}
