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
