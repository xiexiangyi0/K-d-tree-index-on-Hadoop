package shape;

import kdtree.KDTreeData;
import quadIndex.Rectangle;
import quadIndex.SpatialObj;

public class Rect extends Rectangle implements KDTreeData{

	public Rect(int x0, int y0, int width, int height) {
		super(x0, y0, width, height);
	}

	public Rect() {
		super(0, 0, 0, 0);
	}
	public Rect(SpatialObj obj) {
		super(0, 0, 0, 0);
		
		Rectangle rect = obj.getMBR();
		
		x = rect.x;
		y = rect.y;
		height = rect.height;
		width = rect.width;
		
	}

	@Override
	public int getDim() {
		return 4;
	}

	@Override
	public int getPosAtAix(int cor) {
		switch (cor) {
			case 0 : return (int) x;
			case 1 : return (int) y;
			case 2 : return (int) (x+width);
			case 3 : return (int) (y+height);
			default : return -1;
		}
	}
	
}
