package oto;

import java.awt.Image;

public class NoteInfomation implements Note{
	int type;
	double v,x;
	NoteInfomation(int type,double x,double v){
		this.type=type;
		this.v=v;
		this.x=x;
	}
	
	public Image getImage() {
		return null;
	}

	public int getType() {
		return type;
	}

	public double getVelocity() {
		return v;
	}

	public double getX() {
		return x;
	}

}
