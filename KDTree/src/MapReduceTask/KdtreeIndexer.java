package MapReduceTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import IO.InputParser;
import KDIndex.SpatialObj;
import KDTree.KDTree;
import MapReduceTask.KdtreeIndexer.KdtreeIndexerMapper;

public class KdtreeIndexer {

  public static class KdtreeIndexerMapper extends Mapper<Object, Text, IntWritable,Text>{
    private IntWritable ID = new IntWritable();
    private Text res = new Text();
       
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
    	
    	String[] lines = value.toString().split("\n");
    	for(String line: lines){
    		int lakeID = InputParser.getIDFromLine(line);
			SpatialObj obj = InputParser.getObjFromLine(line);
			if (obj == null){
				continue;
			}
			ID.set(lakeID);
			res.set(line);
			context.write(ID,res);
    	}
      }
    }
  }
  
  public static class KdtreeIndexerReducer extends Reducer<IntWritable,Text,IntWritable,KdtreeWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(IntWritable key, Iterator<Text> value, Context context) throws IOException, InterruptedException {
    	
    	while(value.hasNext()){
    		String line = value.next().toString();
			SpatialObj obj = InputParser.getObjFromLine(line);
			if (obj == null){
				continue;
			}
			
    	    KdtreeWritable kd = new KdtreeWritable();
    	    
    	    if(!kd.insert(obj)){
			    continue;
		    } else {
		    	if(!kd.isEmpty()){
		          context.write(key, kd);
		        }
    	        else
			      continue;
    	}
	}
  }

  public static void main(String[] args) throws Exception {
	  
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
	      System.err.println("Usage: KdteeeIndexer <in> <out>");
	      System.exit(2);
    }
    
    Job job = new Job(conf, "KdtreeIndexer");
    job.setJarByClass(KdtreeIndexer.class);
    job.setMapperClass(KdtreeIndexerMapper.class);  //Mapper
    job.setCombinerClass(KdtreeIndexerReducer.class);  //Combiner
    job.setReducerClass(KdtreeIndexerReducer.class);   //Reducer
    job.setOutputKeyClass(Text.class);          //Output key
    job.setOutputValueClass(IntWritable.class); //Output Value
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));  //Input path
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));  //Output path
    System.exit(job.waitForCompletion(true) ? 0 : 1);   //System.exit(0);
  }
}
