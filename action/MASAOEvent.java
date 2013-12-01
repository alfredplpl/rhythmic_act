package action;

public class MASAOEvent implements ActionEvent{
	int eventType;
	
	MASAOEvent(int eventType){
		this.eventType=eventType;
	}
	
	public int getEventType() {
		return eventType;
	}

}
