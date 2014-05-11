package MapReduceTask;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

class KDTreeIndex {
	
	public static void run(String [] args) {
		String in_path = null;
		String out_path = null;
		if(args.length < 2) {
			in_path = "input";
			out_path = "output"; 
		} else {
			in_path = args[0];
			out_path = args[1];
		}
		
		JobConf conf = new JobConf();
		
		conf.setMapperClass(KDTreeIndexMapper.class);
		conf.setReducerClass(KDTreeIndexReducer.class);
		
		FileInputFormat.setInputPaths(conf, new Path(in_path));
		FileOutputFormat.setOutputPath(conf, new Path(out_path));
		try {
			JobClient.runJob(conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
