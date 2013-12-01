package oto;

import java.awt.Graphics;
import java.awt.Image;

import keyio.KeyTable;

import action.ActionEvent;

/**
 * �E��ɕ\������鉹�y�Q�[���̃C���^�[�t�F�[�X�ł��B
 * ���̃Q�[���́A�J�n���Ԃ���̌o�ߎ��ԂŃQ�[���𓮂����܂��B
 * ���̃C���^�[�t�F�[�X�����������C���X�^���X�𐶐�����Ƃ��́A
 * OtoFactory.makeOto()���g���܂��B
 * <s>�܂��Adraw()�̂��߂ɁA���̃C���^�[�t�F�[�X����������ہA
 * Image�^�̃o�b�t�@��p�ӂ��Ȃ���΂Ȃ�܂���B</s>
 *�@drawDirect��Graphics�������Ē��ڕ`�悵�Ă��������B
 * �摜�̏����������͕K�v����܂���B
 * �Ȃ��A���̃Q�[���ł́A�\�����E�����ς��Ƀ��[���i�m�[�g�������Ă��镔���j
 * ���g�p�����A
 * ���E�Ɏ኱(100�s�N�Z��)���x�̗]�T���������Ă��������B�B
 * 
 * @version 1.1
 * @author �݂������ł�
 *
 */
public interface Oto {
	/**
	 * ���y�Q�[���̕\�������̃T�C�Y�̈ꕔ�ł��B
	 * �������̃s�N�Z���T�C�Y��\���܂��B
	 */
	public static final int dispX=682;
	
	/**
	 * ���y�Q�[���̕\�������̃T�C�Y�̈ꕔ�ł��B
	 * �c�����̃s�N�Z���T�C�Y��\���܂��B
	 */
	public static final int dispY=384;
	
	/**
	 * ���̃��\�b�h�ɂ���āA���y�Q�[�����P�t���[���i�߂܂��B
	 * �����A���܂łɈ�x���Ăяo����Ă��Ȃ���ԂŌĂяo���ꂽ�ꍇ�A
	 * �Q�[���̊J�n���Ӗ����܂��B�������������̓R���X�g���N�^�ōs����
	 * ���������B
	 * keytable�ɂ��Ă�keyio.KeyTable���Q�Ƃ��Ă��������B
	 * @param keytable�@�L�[���͂��󂯎��܂��B
	 */
	public Note[] act(KeyTable keytable);
	
	/**
	 * �A�N�V�����Q�[���ɂ���Ĕ�����������C�x���g�������Ƃ�܂��B
	 * ������null�̏ꍇ�A�����������܂���B
	 * @param event�@�A�N�V�����Q�[���ɂ���Ĕ�����������C�x���g
	 */
	public void setEvent(ActionEvent[] event);
	
	/**
	 * ���y�Q�[�����ɔ����������y�Q�[���p�C�x���g��n���܂��B
	 * �����Ȃ������ꍇ�Anull��Ԃ��܂��B
	 * @return �A�N�V�����Q�[���ɔ���������C�x���g
	 */
	public OtoEvent[] getEvent();
	
	/**
	 * ���̃��\�b�h�́A�P�t���[���̊Ԃɐ��������_��Ԃ��܂��B
	 * ���_���������ꍇ�͂��̓��_���A
	 * ���ɐ����Ȃ������ꍇ��0��Ԃ��܂��B
	 * 
	 * @return ���O�ɏ����������y�Q�[���̓��_
	 */
	public int getFrameScore();
	
	
	/**
	 * ���̃Q�[���������Ă��邩�ǂ�����Ԃ��܂��B
	 * ���̃��\�b�h�̖߂�l���Q�[���S�̂̏I�������ƂȂ�܂��B
	 * BMS���I����Ă�����኱�̎���(4���ߒ��x)�̗]�T���������Ă��������B
	 * 
	 * @return ���̃Q�[���������Ă��邩�ǂ���
	 */
	public boolean isContinue();
	
	/**
	 * ���̃��\�b�h�ɂ���āA���O�ɏ����������y�Q�[���̗l�q
	 * ��\���摜�������܂��B
	 * �Ȃ��A���̃C���^�[�t�F�[�X����������
	 * �N���X�́A���̃��\�b�h���Ă΂ꂽ�Ƃ��ɕ`����s���Ă��������B
	 * @deprecated drawDirect���\�b�h�ɂ���Ēu�������Ă��������B����
	 * ���\�b�h�͎g���܂���B
	 * @return ���O�ɏ����������y�Q�[���̗l�q
	 */
	public Image draw();
	
	/**
	 * ���̃��\�b�h�ɂ���āA���O�ɏ����������y�Q�[���̗l�q
	 * �������̃O���t�B�b�N�R���e�L�X�g�ŕ`�悵�܂��B
	 * �Ȃ��A���̃C���^�[�t�F�[�X����������
	 * �N���X�́A���̃��\�b�h���Ă΂ꂽ�Ƃ��ɕ`����s���Ă��������B
	 * @param g �Q�[���`��̈�̃O���t�B�b�N�R���e�L�X�g
	 */
	public void drawDirect(Graphics g);
}
