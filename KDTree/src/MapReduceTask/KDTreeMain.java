package MapReduceTask;

import java.util.Arrays;

public class KDTreeMain {
    
	//KDTreeMain index src dest
	//TODO: KDTreeMain range ...
	//TODO: KDTreeMain knn ...
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Invalid syntax.");
			return;
		}
		
		String sub_cmd = args[0];
		String [] arguments = Arrays.copyOfRange(args, 1,  args.length);
	
		switch (sub_cmd) {
			case "index" : 
				KDTreeIndex.run(arguments);
				break;
			case "range" : 
				//break;
			case "knn" :
				//break;
			default :
				System.out.println("Unrecognized sub commands.");
		}
	}

}
