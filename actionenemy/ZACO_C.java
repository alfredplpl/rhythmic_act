package actionenemy;

import action.ImageKeeping;

public class ZACO_C extends Enemy{

	public ZACO_C(ImageKeeping ik, double x, double y,double vy) {
		super(ik, x, y, ssizex, ssizey, vy, "ZACO_C");
		state=Enemy.LEFT_MOVE;
		score=1000;
	}

	/**
	 * ‚ ‚ñ‚Ü‚è“®‚«‚Ü‚¹‚ñ
	 */
	@Override
	public void move(){
		y+=vy;
		rect.setRect(x-0.5*sizex,y+0.5*sizey,sizex,sizey);
		if(waitFrame>=0)waitFrame--;
	}
}
