package actionbarrier;

import action.ImageKeeping;

public class CloudRight extends Barrier{
	public CloudRight(ImageKeeping ik, double x, double y) {
		super(ik, x, y, ssizex, ssizey, "CloudRight");
		hitVector=Barrier.HIT_TOP;
	}

}
