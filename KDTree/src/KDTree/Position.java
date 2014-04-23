package KDTree;

class Position {
	public int x;
	public int y;
	public Position(int xx, int yy) {
		x = xx;
		y = yy;
	}
	public int getPosAtAix(int aix) {
		if(aix % 2 == 0) return x;
		else return y;
	}
}
