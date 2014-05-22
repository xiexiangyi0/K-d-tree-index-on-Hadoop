package kdtree;

public interface KDTreeRange {

	boolean contains(KDTreeData cur_data);

	int getLeftAtAix(int aix);

	int getRightAtAix(int aix);

}
