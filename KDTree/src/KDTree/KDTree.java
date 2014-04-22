package KDTree;

import java.util.Arrays;

class KDTree {
	private KDTreeNode root;
	private Position [] dataChunk;
	
	//these two arrays store the idx of data in dataChunk,
	// in the order of x direction and y direction separately
	private int [] posXArr;
	private int [] posYArr;

	
	public KDTree(Position [] posArr) {
		this.dataChunk = posArr;
		posXArr = new int[posArr.length];
		posYArr = new int[posArr.length];

		int [] dataSet = new int[posArr.length];
		
		//TODO: sort in two directions
		for(int i=0; i<posArr.length; i++) {
			posXArr[i] = i;
			posYArr[i] = posArr.length-i-1;
			dataSet[i] = i;
		}
		
		
		System.out.println("Construct root\n");
		root = makeSubKDTree(dataSet, 0);
		
	}
	
	//TODO: get the left most one if mids are the same
	int findMidIdx(int [] dataArr) {
		return dataArr.length/2;
	}
	
	//dataArr should be sorted before passed in
	// dataArr stores the idx of data in dataChunck
	private KDTreeNode makeSubKDTree(int [] dataArr, int depth) {
		if(dataArr.length == 0) return null;
		
		//mark elements in set
		// inSet tells us weather an element is in dataArr
		boolean [] inSet = new boolean[dataChunk.length];
		for(int i=0; i<inSet.length; i++) {
			inSet[i] = false;
		}
		for(int i=0; i<dataArr.length; i++) {
			inSet[dataArr[i]] = true;
		}
		
		//next depth's sorted array
		int [] sortedArr = null;
		if((depth+1) % 2 == 0) {
			sortedArr = posXArr;
		} else {
			sortedArr = posYArr;
		}
		
		
		//fill the data needed to be passed to next depth construction
		//find the mid elements
		int pivot = findMidIdx(dataArr);
		int pivotDataIdx = dataArr[pivot];
		
		inSet[pivotDataIdx] = false;

		Position pivotData = dataChunk[pivotDataIdx];
		int target = depth%2==0?pivotData.x:pivotData.y;
		
		System.out.println("Pivot idx "+pivot+", Pivot data idx "+pivotDataIdx);
		int [] leftData = new int [pivot];
		int [] rightData = new int [dataArr.length-pivot-1];
		int leftIdx = 0;
		int rightIdx = 0;
		
		//traverse on sortedArr, which means traverse elements in order of next depth's direction
		//fill two sub array if the elements is in current dataArr
		for(int i=0; i<sortedArr.length; i++) {
			int idx = sortedArr[i];
			if(!inSet[idx]) continue;
			Position data = dataChunk[idx];
			int toCmp = depth%2==0?data.x:data.y;
			if(toCmp < target) {
				System.out.println("Fill left array at "+leftIdx+": "+idx);
				leftData[leftIdx] = idx;
				leftIdx++;
			} else {
				System.out.println("Fill right array at "+rightIdx+": "+idx);
				rightData[rightIdx] = idx;
				rightIdx++;
			}
		}
		
		//Recursion
		System.out.println("Construct left tree, depth " + (depth+1));
		KDTreeNode lc = makeSubKDTree(leftData, depth+1);
		System.out.println("Construct left tree, depth " + (depth+1));
		KDTreeNode rc = makeSubKDTree(rightData, depth+1);
		
		return new KDTreeNode(pivotData, lc, rc);
	}
}
