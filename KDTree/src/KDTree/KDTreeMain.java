package KDTree;

public class KDTreeMain {

	public static void main(String[] args) {

		Position [] posArr = new Position[10];
		for(int i=0; i<posArr.length; i++) {
			posArr[i] = new Position(i,10-i);
		}
		
		KDTree kdt = new KDTree(posArr);
		kdt.print();
		
		System.out.println("");
		System.out.println("");
		
		int x = 4;
		int y = 5;
		Position res = kdt.nearestNeighbour(x, y);
		
		System.out.println("("+x + ", " + y +
				") nearest neighbour in kd tree is " + 
				res.id+":(" + res.x + ", " + res.y + ")\n");
	}

}
