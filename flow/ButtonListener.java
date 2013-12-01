package flow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import keyio.KeyTable;

/**
 * KeyTable�̎�����ł��B���̃N���X�͕ύX����\��Ȃ̂ŁA
 * �ڍׂɉ���͂��܂���B
 * @author �݂������ł�
 *
 */
final class ButtonListener implements KeyTable,KeyListener {
	boolean[] keytable;
	ButtonListener(){
		keytable=new boolean[0x1000];
		cnt=0;
	}
	
	public boolean isPressing(int keycode) {
		return keytable[keycode];
	}
	
	int cnt;
	public void keyPressed(KeyEvent e) {
		/*
		if(e.getKeyCode()==KeyTable.OTO_1 || e.getKeyCode()==KeyTable.OTO_2 || e.getKeyCode()==KeyTable.OTO_3 || e.getKeyCode()==KeyTable.OTO_4 || e.getKeyCode()==KeyTable.OTO_5){
			System.out.println("Press :" +e.getKeyChar());
			cnt++;
			System.out.println("keycount:"+cnt);
		}
		*/
		keytable[e.getKeyCode()]=true;
	}
	public void keyReleased(KeyEvent e) {
		keytable[e.getKeyCode()]=false;
		
	}
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	protected boolean isContinue(){
		return !keytable[EXIT];
	}
}