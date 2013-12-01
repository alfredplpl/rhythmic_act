package actionbarrier;

import action.ImageKeeping;

public class CloudMid  extends Barrier{
	public CloudMid(ImageKeeping ik, double x, double y) {
		super(ik, x, y, ssizex, ssizey, "CloudMid");
		hitVector=Barrier.HIT_TOP;
	}
}
