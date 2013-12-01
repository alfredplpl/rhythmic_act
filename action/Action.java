package action;

import java.awt.Graphics;
import java.awt.Image;

import keyio.KeyTable;

import oto.*;

/**
 * �����ɕ\�������A�N�V�����Q�[���̃C���^�[�t�F�[�X�ł��B
 * ���̃Q�[���́A�t���[���P�ʂŃQ�[���𓮂����܂��B
 * ���̃C���^�[�t�F�[�X�����������C���X�^���X�𐶐�����Ƃ��́A
 * ActionFactory.makeAction()���g���܂��B
 * <s>�܂��Adraw()�̂��߂ɁA���̃C���^�[�t�F�[�X����������ہA
 * Image�^�̃o�b�t�@��p�ӂ��Ȃ���΂Ȃ�܂���B</s>
 *�@drawDirect��Graphics�������Ē��ڕ`�悵�Ă��������B
 * �摜�̏����������͕K�v����܂���B
 * �Ȃ��A���̃A�N�V�����Q�[���̉��̉��͈͂́A
 * �����Ă���͈͂ł͂Ȃ��A�X�R�A�\�������x�ɂȂ�܂��B
 * 
 * @version 1.4
 * @author �݂������ł�
 *
 */
public interface Action {
	/**
	 * �A�N�V�����Q�[���̕\�������̃T�C�Y�̈ꕔ�ł��B
	 * �������̃s�N�Z���T�C�Y��\���܂��B
	 */
	public static final int dispX=1024;
	
	/**
	 * �A�N�V�����Q�[���̕\�������̃T�C�Y�̈ꕔ�ł��B
	 * �c�����̃s�N�Z���T�C�Y��\���܂��B
	 */
	public static final int dispY=384;
	
	/**
	 * ���̃Q�[���Ŏg�p����X�e�[�W�p�f�[�^�̃p�X�������܂��B
	 */
	static final String STAGE_PATH = "./stage/";
	
	/**
	 * ��Փx���Ⴂ�X�e�[�W��\���܂��B
	 */
	static final int EASY = 1;
	
	/**
	 * ��Փx�����ʂł���X�e�[�W��\���܂��B
	 */
	static final int NORMAL = 2;
	
	/**
	 * ��Փx�������X�e�[�W��\���܂��B
	 */
	static final int HARD = 3;
	
	/**
	 * �O���t�@�C���ł��邱�Ƃ������܂��B
	 */
	static final int EXTERNAL =10;
	
	/**
	 * ���̃��\�b�h�ɂ���āA�A�N�V�����Q�[�����P�t���[���i�߂܂��B
	 * �����A���܂łɈ�x���Ăяo����Ă��Ȃ���ԂŌĂяo���ꂽ�ꍇ�A
	 * �Q�[���̊J�n���Ӗ����܂��B�������������̓R���X�g���N�^�ōs����
	 * ���������B
	 * keytable�ɂ��Ă�keyio���Q�Ƃ��Ă��������B
	 * @param note �G�i���ʁj��\���܂��B
	 * @param keytable�@�L�[���͂��󂯎��܂��B
	 */
	public void act(Note[] note,KeyTable keytable);
	
	/**
	 * ���y�Q�[���ɂ���Ĕ�����������C�x���g�������Ƃ�܂��B
	 * ������null�̏ꍇ�A�����������܂���B
	 * @param event�@���y�Q�[���ɂ���Ĕ�����������C�x���g
	 */
	public void setEvent(OtoEvent[] event);
	
	/**
	 * �A�N�V�����Q�[�����ɔ����������y�Q�[���p�C�x���g��n���܂��B
	 * �����Ȃ������ꍇ�Anull��Ԃ��܂��B
	 * @return ���y�Q�[���ɔ���������C�x���g
	 */
	public ActionEvent[] getEvent();
	
	/**
	 * ���̃��\�b�h�́A�P�t���[���̊Ԃɐ��������_��Ԃ��܂��B
	 * ���_���������ꍇ�͂��̓��_���A
	 * ���ɐ����Ȃ������ꍇ��0��Ԃ��܂��B
	 * 
	 * @return ���O�ɏ��������A�N�V�����Q�[���̓��_
	 */
	public int getFrameScore();
	
	/**
	 * ���̃��\�b�h�ɂ���āA���O�ɏ��������A�N�V�����Q�[���̗l�q
	 * ��\���摜�������܂��B
	 * �Ȃ��A���̃C���^�[�t�F�[�X����������
	 * �N���X�́A���̃��\�b�h���Ă΂ꂽ�Ƃ��ɕ`����s���Ă��������B
	 * @deprecated drawDirect���\�b�h�ɂ���Ēu�������Ă��������B����
	 * ���\�b�h�͎g���܂���B
	 * @return ���O�ɏ��������A�N�V�����Q�[���̗l�q
	 */
	public Image draw();

	/**
	 * ���̃��\�b�h�ɂ���āA���O�ɏ��������A�N�V�����Q�[���̗l�q
	 * �������̃O���t�B�b�N�R���e�L�X�g�ŕ`�悵�܂��B
	 * �Ȃ��A���̃C���^�[�t�F�[�X����������
	 * �N���X�́A���̃��\�b�h���Ă΂ꂽ�Ƃ��ɕ`����s���Ă��������B
	 * @param g �Q�[���`��̈�̃O���t�B�b�N�R���e�L�X�g
	 */
	public void drawDirect(Graphics g);
	
	/**
	 * �Q�[�����Ɉړ�����������Ԃ��܂��B
	 * @return �ړ���������
	 */
	public double getWorldDistance();
}
