package MapReduceTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import shape.Rect;

//input (key, value) = count in each mapper, Rect
//output (key, value) = _, KDTree
class KdtreeIndexReducer extends MapReduceBase
	implements Reducer<IntWritable, Rect, IntWritable, KDTreeWritable>{

	@Override
	public void reduce(IntWritable key, Iterator<Rect> value,
			OutputCollector<IntWritable, KDTreeWritable> oc, Reporter rpt)
			throws IOException {
		// TODO Auto-generated method stub
		
		ArrayList<Rect> rlist = new ArrayList<Rect>();
		while(value.hasNext()) {
			rlist.add(value.next());
		}
		
		Rect [] rect = new Rect[rlist.size()];
		rlist.toArray(rect);
		
		KDTreeWritable kdtree = new KDTreeWritable(rect);
		
		oc.collect(key,  kdtree);
		
	}


}
