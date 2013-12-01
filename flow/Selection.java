package flow;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;

import javax.sound.sampled.*;

import action.ActionSelect;
import action.SelectAction;

import keyio.KeyTable;
import oto.BMSData;
import oto.OtoSelect;
import oto.SelectOto;
import oto.SoundPlayer;

public class Selection{
	OtoSelect osel;
	ActionSelect asel;
	TimeCounter tc;
	
	boolean isContinue;
	
	static final String bgm="./testbgm/air.mp3";
	SoundPlayer mp3;
	
	Selection(){
		isContinue=true;
		osel=new SelectOto();
		asel = new SelectAction();
		tc= new TimeCounter();
		/*
		mp3=new SoundPlayer();
		mp3.load(bgm);
		mp3.play();
		*/
	}
	
	public boolean isContinue(){
		return (osel.isContinue() || asel.isContinue()) && tc.isContinue();
	}
	
	public void act(KeyTable key){
		osel.act(key);
		asel.act(key);
		tc.act(key);
		
		//if(!isContinue())mp3.close(1000000000L);
	}
	
	public void draw(Graphics g){
		osel.draw(g.create(0, 0, OtoSelect.dispX, OtoSelect.dispY));
		asel.draw(g.create(0, OtoSelect.dispY ,ActionSelect.dispX, ActionSelect.dispY));
		tc.draw(g.create(OtoSelect.dispX/2-50,OtoSelect.dispY-50,100,100));
	}
	
	public int getDifficulty(){
		return asel.getDifficulty();
	}
	
	public BMSData getBMS(){
		return osel.getSelectedBMS();
	}

}
