package actionbarrier;

import action.ImageKeeping;

/**
 * Barrierの実装例です。
 * @author みそかつおでん
 *
 */
public class Block extends Barrier{

	public Block(ImageKeeping ik,double x,double y) {
		super(ik, x, y,ssizex,ssizey,"Block");
		
	}

}
