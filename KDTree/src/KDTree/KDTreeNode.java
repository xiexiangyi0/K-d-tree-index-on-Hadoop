/*Author: Xiangyi Xie*/

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
}
