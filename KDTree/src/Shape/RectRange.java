package shape;

import kdtree.KDTreeData;
import kdtree.KDTreeRange;

public class RectRange implements KDTreeRange{
	
	//rect aix : 
	// 0 -> x0
	// 1 -> y0
	// 2 -> x1 -> x0 + width
	// 3 -> y1 -> y0 + height
	
	Rect rect;
	
	public RectRange (int x0, int y0, int width, int height) {
		rect = new Rect(x0, y0, width, height);
	}

	@Override
	public boolean contains(KDTreeData cur_data) {
		int x0 = cur_data.getPosAtAix(0);
		int y0 = cur_data.getPosAtAix(1);
		int x1 = cur_data.getPosAtAix(2);
		int y1 = cur_data.getPosAtAix(3);
		int rx0 = rect.getPosAtAix(0);
		int ry0 = rect.getPosAtAix(1);
		int rx1 = rect.getPosAtAix(2);
		int ry1 = rect.getPosAtAix(3);
		return x0 <= rx1 && x1 >= rx0 &&
				y0 <= ry1 && y1 >= ry0;
	}

	@Override
	public int getLeftAtAix(int aix) {
		switch (aix) {
			case 0 : return Integer.MIN_VALUE;
			case 1 : return Integer.MIN_VALUE;
			case 2 : return rect.getPosAtAix(0);
			case 3 : return rect.getPosAtAix(1);
			default: return Integer.MIN_VALUE;
		}
	}

	@Override
	public int getRightAtAix(int aix) {
		switch (aix) {
			case 0 : return rect.getPosAtAix(2);
			case 1 : return rect.getPosAtAix(3);
			case 2 : return Integer.MAX_VALUE;
			case 3 : return Integer.MAX_VALUE;
			default: return Integer.MAX_VALUE;
		}
	}


}
