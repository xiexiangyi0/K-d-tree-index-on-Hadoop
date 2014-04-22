package KDTree;

public class KDTreeMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Position [] posArr = new Position[10];
		for(int i=0; i<posArr.length; i++) {
			posArr[i] = new Position(i,10-i);
		}
		
		KDTree kdt = new KDTree(posArr);
	}

}
