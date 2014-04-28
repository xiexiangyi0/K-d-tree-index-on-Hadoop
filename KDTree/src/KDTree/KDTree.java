package KDTree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;


class KDTree {
	private KDTreeNode root;
	private Position [] dataChunk;
	
	private int k;
	
	public KDTree(Position [] posArr) {
		this.dataChunk = posArr;
		k = posArr[0].getDim();
		int [][] dataIdxInOrder = new int[k][posArr.length];
		
		//sort in two directions
		for(int i=0; i<posArr.length; i++) {
			dataIdxInOrder[0][i] = i;
			dataIdxInOrder[1][i] = i;
		}
		
		sortIdxInAix(dataIdxInOrder[0], 0);
		sortIdxInAix(dataIdxInOrder[1], 1);
		root = makeSubKDTree(dataIdxInOrder, 0);
		
	}
	
	//get the left most one if mids are the same
	int findMidIdx(int [] idxArr, int aix) {
		int idx = idxArr.length/2;
		if(idx == 0) return 0;
		idx--;
		while(idx>0 && 
				dataChunk[idxArr[idx+1]].getPosAtAix(aix) == 
				dataChunk[idxArr[idx]].getPosAtAix(aix)) {
			idx--;
		}
		return idx+1;
	}
	
	//idxArr is a k * n array
	//  idxArr[i] stores array of idx in order of i-th direction
	//  time complexity T(n) = 2*T(n/2)+k*n => T(n) = nlogn
	private KDTreeNode makeSubKDTree(int [][] idxArr, int depth) {
		if(idxArr[0].length == 0) return null;
		
		int n = idxArr[0].length;
		int curAix = depth%k;
		
		int pivot = findMidIdx(idxArr[curAix], curAix);
		//System.out.println(pivot);
		int pivotDataIdx = idxArr[curAix][pivot];

		Position pivotData = dataChunk[pivotDataIdx];
		
		int [][] leftData = new int[k][pivot];
		int [][] rightData = new int[k][n-pivot-1];
		
		//for each direction, we need to generate new sorted array in order
		//  time complexity k*n
		for(int i=0; i<k; i++) {
			int leftIdx = 0;
			int rightIdx = 0;
			for(int j=0; j<n; j++) {
				int dataIdx = idxArr[i][j];
				if(dataIdx == pivotDataIdx) {
					continue;
				}
				if(dataChunk[dataIdx].getPosAtAix(curAix) 
						< pivotData.getPosAtAix(curAix)) {
					leftData[i][leftIdx++] = dataIdx;
				} else {
					rightData[i][rightIdx++] = dataIdx;
				}
			}
		}
		
		//Recursion
		//  time complexity 2*T(n/2)
		KDTreeNode lc = makeSubKDTree(leftData, depth+1);
		KDTreeNode rc = makeSubKDTree(rightData, depth+1);
		
		return new KDTreeNode(pivotData, lc, rc);
	}
	
	public void print() {
		root.print(0);
	}

	private void sortIdxInAix(int [] idxArr, int aix) {
		Cmp cmp = new Cmp(dataChunk, aix);
		Integer [] idxArrObj = new Integer[idxArr.length];
		for(int i=0; i<idxArr.length; i++) {
			idxArrObj[i] = idxArr[i];
		}
		Arrays.sort(idxArrObj, cmp);
		for(int i=0; i<idxArrObj.length; i++) {
			idxArr[i] = idxArrObj[i].intValue();
		}
	}
	
	private Position findNN(KDTreeNode node, Position target, Position best, int depth) {
		if(node == null) return best;
		Position cur_pos = node.getData();
		if(cur_pos.getDist2(target) < best.getDist2(target)) {
			best = cur_pos;
		}	
		
		int aix = depth % k;
		
		int cur_cor = cur_pos.getPosAtAix(aix);
		int target_cor = target.getPosAtAix(aix); 
		
		int cur_dist2 = (target_cor-cur_cor)*(target_cor-cur_cor);
		
		if(target_cor < cur_cor ) {
			//target is on the left part
			best = findNN(node.getLeft(), target, best, depth+1);
			int best_dist2 = best.getDist2(target);
			if(best_dist2 >= cur_dist2) {
				//intersect with right part
				best = findNN(node.getRight(), target, best, depth+1);
			}
			
		} else {
			//target is on the right part
			best = findNN(node.getRight(), target, best, depth+1);
			int best_dist2 = best.getDist2(target);
			if(best_dist2 >= cur_dist2) {
				//intersect with left part
				best = findNN(node.getLeft(), target, best, depth+1);
			}
		}
		
		return best;
		
	}
	
	public Position nearestNeighbour(int x, int y) {
		if (root == null) return null;
		
		Position pos = root.getData();
		
		Position target = new Position(x, y);
		
		return findNN(root, target, pos, 0);
	}
	
	//acc is accumulator
	private Vector<Position> rangeQueryInNode(KDTreeNode pnode, Rect range, int depth, Vector<Position> acc) {
		if(pnode == null) return acc;
		Position cur_pos = pnode.getData();
		if(range.contains(cur_pos)) {
			acc.add(cur_pos);
		}
		
		int aix = depth % k;
		
		if(range.getBaseAtAix(aix) < cur_pos.getPosAtAix(aix)) {
			acc = rangeQueryInNode(pnode.getLeft(), range, depth+1, acc);
			if(range.getBaseAtAix(aix) + range.getLengthAtAix(aix) >= cur_pos.getPosAtAix(aix)) {
				acc = rangeQueryInNode(pnode.getRight(), range, depth+1, acc);
			}
		} else {
			acc = rangeQueryInNode(pnode.getRight(), range, depth+1, acc);
			if(range.getBaseAtAix(aix) - range.getLengthAtAix(aix) < cur_pos.getPosAtAix(aix)) {
				acc = rangeQueryInNode(pnode.getLeft(), range, depth+1, acc);
			}
		}
		return acc;
	}
	
	public Vector<Position> rangeQuery(int x0, int y0, int lx, int ly) {
		if(root == null) return null;
		Rect range = new Rect(x0, y0, lx, ly);
		Vector<Position> acc = new Vector<Position>();
		return rangeQueryInNode(root, range, 0, acc);
	}

}

//sort idxArr
class Cmp implements Comparator<Integer> {
	private Position [] dataChunk;
	private int aix;
	public Cmp(Position [] dataChunk, int aix) {
		this.dataChunk = dataChunk;
		this.aix = aix;
	}
	@Override
	public int compare(Integer idx0, Integer idx1) {
		return dataChunk[idx0.intValue()].getPosAtAix(aix) 
				- dataChunk[idx1.intValue()].getPosAtAix(aix);
	}
	
}
