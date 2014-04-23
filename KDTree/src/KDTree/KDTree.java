package KDTree;

import java.util.Arrays;
import java.util.Comparator;


class KDTree {
	private KDTreeNode root;
	private Position [] dataChunk;
	
	public KDTree(Position [] posArr) {
		this.dataChunk = posArr;
		int [][] dataIdxInOrder = new int[2][posArr.length];
		
		//TODO: sort in two directions
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
		
		int k = idxArr.length;
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
