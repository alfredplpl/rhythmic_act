package oto;
import javax.media.*;

import java.io.*;
import java.net.URL;
import java.io.FileNotFoundException;

public class SoundPlayer extends Thread implements ControllerListener{
	long fadeOutTime = 3000000000L;
	float vol;
	Player player;
	GainControl gctrl;
	public void load(String str){
		set(str,1.0f);
	}
	public void set(final String str,float volume){
		try {
        	File file = new File(str);
            URL url = file.toURI().toURL();
            if(player != null){
            	finish();
            }
            player = Manager.createRealizedPlayer(url);
            vol = volume;
            player.addControllerListener(new ControllerAdapter() {
               	public void endOfMedia(EndOfMediaEvent ev) {
               		player.stop();
               		player.setMediaTime(new Time(0L));
               	}
            });
			
            gctrl = player.getGainControl();  //GainControlを得る
            gctrl.setLevel(vol);  //音量調節
        }
        catch (FileNotFoundException e) {
            // エラー処理
        } catch (IOException err) {
                System.out.println(err);
        } catch (Exception e){
        	
        }
	}
	public void setVolume(float vol){
		gctrl.setLevel(vol);
	}
    public void play() {
    	if(player.getMediaTime().getSeconds() != 0.0d){
    		player.setMediaTime(new Time(0L));
    	}
        player.start();
    }
    public void play(int startTime){
    	player.setMediaTime(new Time((long)startTime*1000000000L));
    	player.start();
    }
    public Boolean close(){
    	fadeOutTime = 3000000000L;
    	try{
    		this.start();
    		return true;
    	}catch(IllegalThreadStateException e){}
    	return false;
    }
    public Boolean close(long l){
    	fadeOutTime = l;
    	try{
    		this.start();
    		return true;
    	}catch(IllegalThreadStateException e){}
    	return false;
    }
    public void finish(){
    	if(player != null){
    		player.stop();
    		player.close();
    	}
    }
    public void run(){
    	long stTime,nowTime = 0;
    	stTime = System.nanoTime();
    	while(nowTime < fadeOutTime){
    		if(gctrl == null) gctrl = player != null ? player.getGainControl():null;
    		else gctrl.setLevel(vol * (fadeOutTime - nowTime) / fadeOutTime);
    		nowTime = System.nanoTime() - stTime;
    		try{
    			Thread.sleep(3L);
    		} catch(Exception e){
    			
    		}
    	}
    	if(player != null){
    		player.stop();
    		player.close();
    	}
    }
	public void controllerUpdate(ControllerEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	public Boolean isPlaying(){
		return player != null;
	}
	public void finalize(){
		if(player !=null){
			player.stop();
			player.close();
		}
	}
}