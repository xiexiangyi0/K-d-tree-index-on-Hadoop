package MapReduceTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import kdtree.KDTreeData;
import kdtree.KDTreeWritable;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;

import debug.Debug;
import shape.Rect;
import shape.RectRange;

class KDTreeRangeQuery {
	public static void run(String [] args) {
		String tree_path = null;
		String out_path = null;
		int x0 = Integer.MIN_VALUE, y0 = Integer.MIN_VALUE, 
				width = Integer.MAX_VALUE, height = Integer.MAX_VALUE;
		long rqid = 0;
		//pass range to mapper
		if (args.length < 1) {
			System.out.println("Please specify query range.");
			return;
		} else {
			String [] rq = args[0].split(",");
			
			if(rq.length < 5) {
				System.out.println("Invalid range query syntax.");
				return ;
			}
			
			rqid = Long.parseLong(rq[0]);
			
			x0 = Integer.parseInt(rq[1]);
			y0 = Integer.parseInt(rq[2]);
			width = Integer.parseInt(rq[3]);
			height = Integer.parseInt(rq[4]);
			
			if(args.length < 3) {
				tree_path = "tree";
				out_path = "output";
			} else {
				tree_path = args[1];
				out_path = args[2];
			}
		}
		
		JobConf conf = new JobConf(KDTreeRangeQuery.class);
		
		conf.setJobName("range_query");
		
		conf.setLong("rq_id", rqid);
		conf.setInt("x0", x0);
		conf.setInt("y0", y0);
		conf.setInt("width", width);
		conf.setInt("height", height);
		
		conf.setMapperClass(KDTreeRangeQueryMapper.class);
		conf.setReducerClass(KDTreeRangeQueryReducer.class);
		
		conf.setMapOutputKeyClass(LongWritable.class);
		conf.setMapOutputValueClass(Rect.class);
		
		conf.setOutputKeyClass(LongWritable.class);
		conf.setOutputValueClass(Text.class);
		
		conf.setInputFormat(SequenceFileInputFormat.class);
		
		FileInputFormat.setInputPaths(conf, new Path(tree_path));
		FileOutputFormat.setOutputPath(conf, new Path(out_path));
		try {
			JobClient.runJob(conf);
		} catch (IOException e) {
		
			e.printStackTrace();
		}

	}
}

class KDTreeRangeQueryMapper extends MapReduceBase 
	implements Mapper<LongWritable, KDTreeWritable, LongWritable, Rect> {
	
	long rq_id = 0;
	RectRange range = null;
	
	@Override
	public void configure(JobConf job) {
		rq_id = job.getLong("rq_id", 0);
		int x0 = job.getInt("x0", Integer.MIN_VALUE);
		int y0 = job.getInt("y0", Integer.MIN_VALUE);
		int width = job.getInt("width", Integer.MAX_VALUE);
		int height = job.getInt("height", Integer.MAX_VALUE);
		range = new RectRange(x0, y0, width, height);
		
	}

	@Override
	public void map(LongWritable treeid, KDTreeWritable kdtree,
			OutputCollector<LongWritable, Rect> oc, Reporter rpt)
			throws IOException {
		
		Debug.println("TreeId = " + treeid.get());
		
		LongWritable lw = new LongWritable(rq_id);
		ArrayList<KDTreeData> dlist = kdtree.rangeQuery(range);
		Debug.println("Find " + dlist.size() + " lakes.");
		for(int i=0; i<dlist.size(); i++ ) {
			Rect r = (Rect) dlist.get(i);
			Debug.println(r);
			oc.collect(lw, r);
		}
		
	}
}

class KDTreeRangeQueryReducer extends MapReduceBase
	implements Reducer<LongWritable, Rect, LongWritable, Text> {
	
	@Override
	public void reduce(LongWritable rqid, Iterator<Rect> rect_it,
			OutputCollector<LongWritable, Text> oc, Reporter rpt)
			throws IOException {
		int total = 0;
		while (rect_it.hasNext() ) {
			Rect r = rect_it.next();
			oc.collect(rqid, new Text(r.toString()));
			total++;
		}
		
		oc.collect(rqid, new Text("Summary: [" + total + "]  lakes are found."));
		Debug.println("Summary: [" + total + "]  lakes are found.");
		
	}
	
}