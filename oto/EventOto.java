package oto;

public class EventOto implements OtoEvent {
	int combo;
	EventOto(){
		reset();
	}
	public void count(){
		combo++;
	}
	public void reset(){
		combo = 0;
	}
	@Override
	public int getEventType() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		if(combo>99) return OtoEvent.COMBO100;
		if(combo>29) return OtoEvent.COMBO30;
		return OtoEvent.NONEEFFECTIVE;
	}

}
