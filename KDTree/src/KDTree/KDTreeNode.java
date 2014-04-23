package KDTree;

class KDTreeNode {
	private static int nextId = 0;
	private Position pos;
	private int id;
	
	private KDTreeNode left;
	private KDTreeNode right;
	
	public KDTreeNode(Position xy, KDTreeNode lc, KDTreeNode rc) {
		pos = xy;
		left = lc;
		right = rc;
		id = nextId;
		nextId++;
	}
	
	private void printSpace(int num) {
		for(int i=0; i<num*2; i++) {
			System.out.print(' ');
		}
	}
	public void print(int depth) {
		printSpace(depth);
		System.out.println("id = " + id + ", x = " + pos.x + ", y = " + pos.y);
		if(left != null) {
			printSpace(depth);
			System.out.println("left: ");
			left.print(depth+1);
		}
		
		if(right != null) {
			printSpace(depth);
			System.out.println("right: ");
			right.print(depth+1);
		}
	}
}
