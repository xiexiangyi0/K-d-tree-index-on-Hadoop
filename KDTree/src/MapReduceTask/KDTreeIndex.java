package MapReduceTask;

import io.InputParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;






import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;

import quadIndex.SpatialObj;
import debug.Debug;
import shape.Rect;
import kdtree.KDTreeWritable;

class KDTreeIndex {
	
	public static void run(String [] args) {
		String in_path = null;
		String out_path = null;
		if(args.length < 2) {
			in_path = "input";
			out_path = "tree"; 
		} else {
			in_path = args[0];
			out_path = args[1];
		}
		
		JobConf conf = new JobConf(KDTreeIndex.class);
		
		conf.setJobName("index");
		
		conf.setMapperClass(KDTreeIndexMapper.class);
		conf.setReducerClass(KDTreeIndexReducer.class);
		
		conf.setMapOutputKeyClass(LongWritable.class);
		conf.setMapOutputValueClass(Rect.class);
		
		conf.setOutputKeyClass(LongWritable.class);
		conf.setOutputValueClass(KDTreeWritable.class);
		
		conf.setOutputFormat(SequenceFileOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(in_path));
		FileOutputFormat.setOutputPath(conf, new Path(out_path));
		try {
			JobClient.runJob(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

//input (key, value) = lineno, line
//output (key, value) = count to number of calls of map, Rect
class KDTreeIndexMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, LongWritable, Rect>{
	
	static int inst_count = 0;
	
	static enum IndexCounters { SIZE_E, COUNT_E };
	
	final static int sizeof_boolean = 8;
	final static int sizeof_int = 8;
	// refer to QuadTreeWritable
	final static int max_block_size = 1024; //63*1024*1024;
	
	synchronized LongWritable getTreeId(Rect rect, Reporter rpt) {
		Counters.Counter size_cc = rpt.getCounter(IndexCounters.SIZE_E); 
		Counters.Counter count_cc = rpt.getCounter(IndexCounters.COUNT_E);
		//size of data plus tree node
		int dsize = rect.size() + sizeof_boolean*2 + sizeof_int;
		// new data size + original value + data_length size
		if(dsize + size_cc.getValue() + sizeof_int> max_block_size) {
			size_cc.setValue(dsize);
			count_cc.increment(1);
			
			return new LongWritable (count_cc.getValue());
			
		} else {
			
			size_cc.increment(dsize);
			
			return new LongWritable (count_cc.getValue());
			
		}
	}

	@Override
	public void map(LongWritable no, Text content,
			OutputCollector<LongWritable, Rect> oc, Reporter rpt)
			throws IOException {
		
		String line = content.toString();
		SpatialObj obj = InputParser.getObjFromLine(line);
		Rect rect = new Rect(obj);
		
		LongWritable tid = getTreeId(rect, rpt);
		
		Debug.println("TreeId = " + tid.get());
		
		oc.collect(tid, rect);
		
	}

}

//input (key, value) = count in each mapper, Rect
//output (key, value) = _, KDTree
class KDTreeIndexReducer extends MapReduceBase
	implements Reducer<LongWritable, Rect, LongWritable, KDTreeWritable>{

	static int count = 0;
	@Override
	public void reduce(LongWritable key, Iterator<Rect> value,
			OutputCollector<LongWritable, KDTreeWritable> oc, Reporter rpt)
			throws IOException {
		//Debug.println("reduce = " + count);
		count++;
		ArrayList<Rect> rlist = new ArrayList<Rect>();
		while(value.hasNext()) {
			Rect r = value.next();
			rlist.add(new Rect(r));
		}
		
		Rect [] rect_list = new Rect[rlist.size()];
		rect_list = rlist.toArray(rect_list);
		
		KDTreeWritable kdtree = new KDTreeWritable(rect_list);
		
		oc.collect(key,  kdtree);
		
	}


}

