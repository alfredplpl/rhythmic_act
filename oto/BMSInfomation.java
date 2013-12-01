package oto;

public class BMSInfomation implements BMSData{
	int bpm,playLevel,startTime;
	String artist,genre,path,title,path2;
	void print(){
		System.out.println("ジャンル："+genre);
		System.out.println("タイトル："+title);
		System.out.println("アーティスト："+artist);
		System.out.println("レベル："+playLevel);
		System.out.println("BPM："+bpm);
		System.out.println("Path:"+path);
	}
	/*BMSInfomation retBMS(){
		BMSInfomation bms = new BMSInfomation();
		bms.setArtist(artist.substring(0));
		bms.setBPM(bpm);
		bms.setGenre(genre.substring(0));
		bms.setPath(path.substring(0));
		bms.setPath2(path2 != null ? path2.substring(0) : null);
		bms.setPlayLevel(playLevel);
		bms.setStartTime(startTime);
		bms.setTitle(title.substring(0));
		return bms;
	}*/
	
	void setArtist(String artist){
		this.artist = artist;
	}
	void setBPM(int bpm){
		this.bpm = bpm;
	}
	void setGenre(String genre){
		this.genre = genre;
	}
	void setPath(String path){
		this.path = path;
	}
	void setPlayLevel(int playLevel){
		this.playLevel = playLevel;
	}
	void setTitle(String title){
		this.title = title;
	}
	void setPath2(String p){
		path2 = p;
	}
	void setStartTime(int st){
		startTime = st;
	}
	
	public String getArtist(){
		return artist;
	}
	
	public int getBPM(){
		return bpm;
	}
	
	public String getGenre(){
		return genre;
	}
	
	public String getPath(){
		return path;
	}
	public String getPath2(){
		return path2;
	}
	public int getStartTime(){
		return startTime;
	}
	public int getPlayLevel(){
		return playLevel;
	}
	
	public String getTitle(){
		return title;
	}
}
