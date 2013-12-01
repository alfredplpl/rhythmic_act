package oto;

public class SoundData extends BMSInfomation{
	final int dataMax = 576;
	WaveEngine waveEngine;
	Mp3Data mp3Engine,newMp3;
	VisibleObject visibleObject,newVo;
	int commandNum;
	boolean data[];//false:wav true:mp3
	long[] syousetsuTimeNs;
	SoundData(){
		waveEngine = new WaveEngine(dataMax);
		mp3Engine = new Mp3Data();
		newMp3 = mp3Engine;
		data = new boolean[dataMax];
		for(int i=0;i<dataMax;i++){
			data[i] = false;
		}
		visibleObject = new VisibleObject();
		newVo = visibleObject;
		commandNum = 0;

		
	}
	void daleteVisibleObjectDat(){
		visibleObject = null;
		newVo = null;
	}
	void nextVisibleObject(){
		visibleObject = visibleObject.getNext();
	}
	void print(){
		VisibleObject Vo = visibleObject;
		for(int i=0;i<commandNum;i++,Vo = Vo.next){
			int[] a;
			if(Vo.getChannel()>=11 && Vo.getChannel()<=19){
				System.out.println("Syousetsu:"+Vo.getSyousetsu());
				System.out.println("DatLength:"+Vo.getDatLength());
				System.out.println("Channel:"+(int)Vo.getChannel());
				a = Vo.getObjectDat();
				for(int j=0;j<a.length;j++){
					System.out.print("["+j+":"+a[j]+"]");
				}
				System.out.println();
			}
		}
	}
	
	
	void setVisibleObject(int left,String right){
		newVo = newVo.setVisibleObject(left, right);
		commandNum++;
	}
	
	void setSound(int label,String filePath){
		String str = filePath.substring(filePath.length()-3);
		if("wav".equals(str) || "WAV".equals(str)){
			data[label] = false;
			waveEngine.load(""+label,"..\\" + filePath);
		}else if("mp3".equals(str) || "MP3".equals(str)){
			data[label] = true;
			newMp3 = newMp3.create(label,filePath);
		}
		
	}
	void playSound(int label){
		if(data[label]){
			Mp3Data dat;
			for(dat = mp3Engine;
			(!dat.cmpLabel(label)) && dat.next != null;dat = dat.next){}
				dat.getSound().play();
			
		}else waveEngine.play(""+label);
	}
	void closeSound(){
		for(Mp3Data dat = mp3Engine;dat.getNext() != null;dat = dat.getNext()){
			dat.getSound().close((long)2e9);
		}
		waveEngine.close();
	}
}

class Mp3Data{
	SoundPlayer mp3Engine;
	Mp3Data next;
	int label;//01~ZF : 1~575
	Mp3Data create(int label,String filePath){
		this.label = label;
		mp3Engine = new SoundPlayer();
		mp3Engine.set(filePath,1.0f);
		next = new Mp3Data();
		return next;
	}
	boolean cmpLabel(int label){
		if(this.label == label){
			return true;
		} else return false;
	}
	Mp3Data getNext(){
		return next;
	}
	SoundPlayer getSound(){
		return mp3Engine;
	}
}

class VisibleObject{
	VisibleObject next;
	boolean flug;
	short syousetsu;
	char channel;
	int objectDat[],datLength;
	long startTimeNs;
	VisibleObject(){
		flug = false;
	}
	boolean isContinue(){
		return flug;
	}
	VisibleObject setVisibleObject(int left,String right){
		flug = true;
		String str = right;
		syousetsu = (short)(left / 100);
		channel = (char)(left % 100);
		datLength = (short)(right.length()/2);
		objectDat = new int[datLength];
		for(int i=0;i<right.length()/2;i++){
			objectDat[i] = StrToInt.strToInt36(str.substring(0, 2));
			//System.out.println(str.substring(0, 2));
			//System.out.println((int)channel);
			
			str = str.substring(2);
		}
		next = new VisibleObject();
		return next;
	}
	int getDatLength(){
		return datLength;
	}
	int[] getObjectDat(){
		return objectDat;
	}
	short getSyousetsu(){
		return syousetsu;
	}
	char getChannel(){
		return channel;
	}
	VisibleObject getNext(){
		return next;
	}
}