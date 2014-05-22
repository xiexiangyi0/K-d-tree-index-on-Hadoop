package kdtree;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import debug.Debug;
import shape.Rect;

public class KDTreeWritable extends KDTree implements Writable{
	public KDTreeWritable() {
		super(null);
	}

	public KDTreeWritable(KDTreeData[] data) {
		super(data);
    }

	@Override
	public void readFields(DataInput in) throws IOException {
		Debug.println("KDTreeWritable::readFields");
		int data_size = in.readInt();	
		Debug.println("data size " + data_size);
		Rect [] rect_array = new Rect[data_size];
		for(int i=0; i<data_size; i++) {
			rect_array[i] = new Rect();
			rect_array[i].readFields(in);
		}
		
		this.data = rect_array;
		
		root = readSubTree(in);
		
	}

	private KDTreeNode readSubTree(DataInput in) throws IOException {
		boolean is_null = in.readBoolean();
		if(is_null) {
			return null;
		} else {
			int idx = in.readInt();
			KDTreeNode left = readSubTree(in);
			KDTreeNode right = readSubTree(in);
			return new KDTreeNode(idx, left, right);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Debug.println("KDTreeWritable::write");
		//write data
		out.writeInt(data.length);
		Debug.println(data.length);
		for(int i=0; i<data.length; i++) {
			Rect r = (Rect) data[i];
			r.write(out);
			Debug.println(r.x + " " + r.y + " " + r.width + " " + r.height);
		}
		
		//write tree
		writeSubTree(root, out);
		
	}

	private void writeSubTree(KDTreeNode root, DataOutput out) throws IOException {
		if(root == null) {
			out.writeBoolean(true);
			Debug.println(true);
		} else {
			out.writeBoolean(false);
			out.writeInt(root.data_idx);
			Debug.println(false);
			Debug.println(root.data_idx);
		
			writeSubTree(root.left, out);
			writeSubTree(root.right, out);
		}
	}

}
