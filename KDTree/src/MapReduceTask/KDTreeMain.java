package MapReduceTask;

import java.util.Arrays;

public class KDTreeMain {
    
	//KDTreeMain index src dest
	//KDTreeMain range_query r tree output
	//TODO: KDTreeMain knn ...
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Invalid syntax.");
			return;
		}
		
		String sub_cmd = args[0];
		String [] arguments = Arrays.copyOfRange(args, 1,  args.length);
	
		if(sub_cmd.equals("index")) {
				KDTreeIndex.run(arguments);
		} else if(sub_cmd.equals("range_query")) {
				KDTreeRangeQuery.run(arguments);
		} else if(sub_cmd.equals("knn")) {
		} else {
				System.out.println("Unrecognized sub commands.");
		}
	}

}
