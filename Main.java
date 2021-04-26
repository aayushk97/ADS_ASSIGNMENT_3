import java.util.Scanner;
import java.util.*;

public class Main{
	public static final double MINING_REWARD = 50;  //Reward amount
	public static int w = 16;  //first w bits to be zero
	public static int numNodes;
	public static Vector<Node> nodes;

    public static int arity = 2; //default
	
	static Vector<Queue<Block>> blockReceivingQueues;
	static Vector<Queue<Transaction>> txnReceivingQueues;
	// static int n;  //number of nodes
	public static void main(String[] args){
	   nodes = new Vector<Node>();
	
    	Scanner in = new Scanner(System.in);
    	//create a new block chain class
    	//create new nodes 
    	System.out.println("Enter the number of nodes in network");
    	numNodes = in.nextInt();

    	//initialize the queues of each node 
    	blockReceivingQueues = new Vector<>();
    	txnReceivingQueues = new Vector<>();
    	
    	for(int i = 0; i < numNodes; i++){
    		blockReceivingQueues.add(new LinkedList<>());
    		txnReceivingQueues.add(new LinkedList<>());	
        }
        for(int i = 0 ; i < numNodes; i++ ){
            Vector<Block> x = new Vector<>();  // will need to remove this and input appropriatively 
            nodes.add(new Node(i, x));
            nodes.get(i).start();
        }

	//create a node
	//create genesis block using node and add it to blockchain


	}


	public static String toHexString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
        {
            sb.append(String.format("%02X", b[i] & 0xFF));
        }
        return sb.toString();
    }
    // public static String toBitString(byte[] b)
    // {
    //     StringBuilder sb = new StringBuilder();
    //     for (int i = 0; i < b.length; i++)
    //     {
    //         sb.append(String.format("%02X", b[i] & 0xFF));
    //     }
    //     return sb.toString();
    // }

    public static boolean isFirstwbitsZero(byte[] hash){
        System.out.println("w: " + w + " h: " + hash.length);
    	for(int i =0; i < w && i < hash.length*4; i++){
    		if(! isZero(hash, i)){
    			return false;
    		}
    	}

    	return true;
    }

    public static boolean isZero(byte[] arr, int i) {
        int index = i / 8;  //index of the byteArray in which this bit falls
        int bitPosition = i % 8;  

        return (arr[index] >> bitPosition & 1) != 1;
    }
    
    public static byte[] getBytesFromDouble(Double value) {
        byte[] bytes = new byte[Double.BYTES];
        Long x = Double.doubleToLongBits(value);
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte)((x >> ((7 - i) * 8)) & 0xff);
        }
        return bytes;
    }
}
