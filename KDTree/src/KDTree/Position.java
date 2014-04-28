package KDTree;

class Position {
	private static int nextId = 0;
	public int id;
	public int x;
	public int y;
	public Position(int xx, int yy) {
		x = xx;
		y = yy;
		id = nextId++;
	}
	public int getPosAtAix(int aix) {
		if(aix % 2 == 0) return x;
		else return y;
	}
	public int getDim() {
		return 2;
	}
	public int getDist2(Position pos) {
		int delta_x = pos.x - this.x;
		int delta_y = pos.y - this.y;
		int dist2 = delta_x*delta_x + delta_y*delta_y;
		return dist2;
	}
}

class Rect {
	private Position base;
	private Position length;
	
	public Rect(int x0, int y0, int lx, int ly) {
		base = new Position(x0, y0);
		length = new Position(lx, ly);
	}
	
	public int getBaseAtAix(int aix) {
		return base.getPosAtAix(aix);
	}
	
	public int getLengthAtAix(int aix) {
		return length.getPosAtAix(aix);
	}
	
	public boolean contains(Position point) {
		for(int i=0; i<base.getDim(); i++) {
			if(point.getPosAtAix(i) - base.getPosAtAix(i) > length.getPosAtAix(i)) {
				return false;
			}
		}
		return true;
	}
}
