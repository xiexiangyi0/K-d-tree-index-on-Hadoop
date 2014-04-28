package KDTree;

import java.util.Vector;

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
		
		int x0 = 2;
		int y0 = 4;
		int lx = 4;
		int ly = 2;
		
		Vector<Position> points = kdt.rangeQuery(x0, y0, lx, ly);
		System.out.println("Range query (" + x0 + ", " + y0 + ", " + lx + ", " + ly + ")");
		
		for(int i=0; i<points.size(); i++) {
			System.out.println("(" + points.get(i).x + ", " + points.get(i).y + ")" );
		}
	}

}
