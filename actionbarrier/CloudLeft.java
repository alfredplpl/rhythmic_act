package actionbarrier;

import action.ImageKeeping;

public class CloudLeft extends Barrier{
	public CloudLeft(ImageKeeping ik, double x, double y) {
		super(ik, x, y, ssizex, ssizey, "CloudLeft");
		hitVector=Barrier.HIT_TOP;
	}
}
