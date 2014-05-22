package kdtree;

class KDTreeNode {
	int data_idx = -1;
	KDTreeNode left = null;
	KDTreeNode right = null;
	
	public KDTreeNode(int idx, KDTreeNode lc, KDTreeNode rc) {
		data_idx = idx;
		left = lc;
		right = rc;
	}


}
