package action;

/**
 * Action�̃R���X�g���N�^�̑���ɗp�ӂ����N���X�ł��B
 * ����ɂ��AAction�͒��ڐ�������邱�ƂȂ�
 * Action�̒��ŏ�������������ی삳��܂��B
 * @version 1.1
 * @author �݂������ł�
 */
public final class ActionFactory {
	
	/**
	 * �R���X�g���N�^�̑���ɂȂ�܂��B
	 * @param stage ActionFactory�ɂ���ċK�肳���X�e�[�W�̎��
	 * @return�@Action���������Ă���C���X�^���X
	 * @throws Exception �ǂݍ��݂ɔ����������炩�̃G���[
	 */
	static public Action makeAction(int stage) throws Exception{
		Action act =null;
		
		act=new SuperMASAO(stage);
		//throw new Exception("�G���[�`�F�b�N");
		return act;
	}

}
