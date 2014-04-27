package KDTree;

/*I/O on fs for run test k-d tree index.*/
import java.io.*;

interface ISerialize<T extends java.io.Serializable> {
	public boolean write(T t, int block);
	public T read(int block);
}

/*
 * Java IO Serializer 
 * On native file System
 */
class nativeSerializer<T extends java.io.Serializable> implements ISerialize<T> {
	//private Class<? extends Serializable> obj;
	private FileInputStream fileIn;
	private ObjectInputStream in;
		
	private FileOutputStream fileOut;
	private ObjectOutputStream out;
	
	public nativeSerializer(){
		//obj = o.getClass();
	}
	public boolean write(T t, int block) {
		try{
			fileOut = new FileOutputStream(String.valueOf(block));
			out = new ObjectOutputStream(fileOut);
			out.writeObject(t);
			out.close();
			fileOut.close();
			return true;
        }catch(Exception ex){
        	//System.out.println(ex.toString()+"!!!write error!!!");
        	return false;
        }
	}

	@SuppressWarnings("unchecked")
	public T read(int block) {
		try{
			//T t = (T) obj.newInstance();
			//System.out.println("Reading block: "+String.valueOf(block));
			fileIn = new FileInputStream(String.valueOf(block));
			in = new ObjectInputStream(fileIn);
			T t = (T) in.readObject();
			in.close();
			fileIn.close();
			return t;
		} catch(Exception ex){
			//System.out.println(ex.toString()+"!!!read error!!!");
			return null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {  
			fileIn.close();
			fileOut.close();
			in.close();
			out.close();
		    super.finalize();
	}  
}
