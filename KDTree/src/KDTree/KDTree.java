package kdtree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;

public class KDTree{
	protected KDTreeNode root = null;
	protected KDTreeData [] data = null;
	protected int k = 2;
	
	////////////////create//////////////////////
	public KDTree(KDTreeData [] data) {
		if(data == null || data.length == 0) return; 
		k = data[0].getDim();
		this.data = data;
		
		int [][] dataIdxInOrder = new int[k][data.length];
		
		//sort in two directions
		for(int j=0; j<k; j++ ) {
			for(int i=0; i<data.length; i++) {
				dataIdxInOrder[j][i] = i;
			}
			sortIdxInAix(dataIdxInOrder[j], j);
		}
		
		root = makeSubKDTree(dataIdxInOrder, 0);
	}
	
	//get the left most one if mids are the same
	int findMidIdx(int [] idxArr, int aix) {

		int idx = idxArr.length/2;
		if(idx == 0) return 0;
		idx--;
		while(idx>=0 && 
				data[idxArr[idx+1]].getPosAtAix(aix) == 
				data[idxArr[idx]].getPosAtAix(aix)) {
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
		int pivotDataIdx = idxArr[curAix][pivot];
		KDTreeData pivotData = data[pivotDataIdx];
		
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
				if(data[dataIdx].getPosAtAix(curAix) 
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
			
		return new KDTreeNode(pivotDataIdx, lc, rc);
	}
		

	private void sortIdxInAix(int [] idxArr, int aix) {
		Cmp cmp = new Cmp(data, aix);
		Integer [] idxArrObj = new Integer[idxArr.length];
		for(int i=0; i<idxArr.length; i++) {
			idxArrObj[i] = idxArr[i];
		}
		Arrays.sort(idxArrObj, cmp);
		for(int i=0; i<idxArrObj.length; i++) {
			idxArr[i] = idxArrObj[i].intValue();
		}
	}
	
	//sort idxArr
	private class Cmp implements Comparator<Integer> {
		private KDTreeData [] dataChunk;
		private int aix;
		public Cmp(KDTreeData [] dataChunk, int aix) {
			this.dataChunk = dataChunk;
			this.aix = aix;
		}
		@Override
		public int compare(Integer idx0, Integer idx1) {
			return dataChunk[idx0.intValue()].getPosAtAix(aix) 
					- dataChunk[idx1.intValue()].getPosAtAix(aix);
		}
		
	}
	
	///////////range query//////////////
	//acc is accumulator
	private ArrayList<KDTreeData> rangeQueryInNode (KDTreeNode pnode, KDTreeRange range, int depth, ArrayList<KDTreeData> acc) {
		if(pnode == null) return acc;
		int idx = pnode.data_idx;
		KDTreeData cur_data = data[idx];
		
		if(range.contains(cur_data)) {
			acc.add(cur_data);
		}
		
		int aix = depth % k;
		
		if(range.getLeftAtAix(aix) < cur_data.getPosAtAix(aix)) {
			acc = rangeQueryInNode(pnode.left, range, depth+1, acc);
		}
			
		if(range.getRightAtAix(aix) >= cur_data.getPosAtAix(aix)) {
			acc = rangeQueryInNode(pnode.right, range, depth+1, acc);
		}
		
		return acc;
	}
	
	public ArrayList<KDTreeData> rangeQuery(KDTreeRange range) {
		if(root == null) return null;
		ArrayList<KDTreeData> acc = new ArrayList<KDTreeData>();
		return rangeQueryInNode(root, range, 0, acc);
	}
}
