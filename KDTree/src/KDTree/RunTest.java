/*Run test for k-d tree index.*/
import java.util.HashMap;
import java.util.Random;

public class RunTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int testnum = 1500;
		KDTree<Long> b = new KDTree<Long>();
		Random r = new Random();
	
		HashMap<Integer,Long> hm = new HashMap<Integer,Long>();
		Long random_value;
		for(int i = 1; i< testnum; i++){
			random_value = r.nextLong();
			b.insert(i, random_value);
			hm.put(i, random_value);
		}
		
		/*b.DebugPrint();*/
		/**/
		for(int i=1; i<testnum; i++){
			Long v1 = b.find(i) ;
			Long v2 =  hm.get(i);
			if(v1 == null || v1.compareTo(v2) != 0)
				System.out.println("["+String.valueOf(i) +"] ERROR: "+v1.toString() +" <> "+ v2.toString());
			else System.out.println("PASS. [KEY]:"+ String.valueOf(i) + " [VALUE]:"+Long.toHexString(v1));
		}
	}

}
