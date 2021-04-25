import java.util.Scanner;

public class main{
	public static final double MINING_REWARD = 50;  //Reward amount
	public static w = 4;  //first w bits to be zero
	public static int numNodes;
	public static Vector<Node> nodes;
	
	static Vector<Queue<Block>> blockReceivingQueues;
	static Vector<Queue<Transaction>> txnReceivingQueues;
	
	public static void main(String[] args){
	
	
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
    public static String toBitString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
        {
            sb.append(String.format("%02X", b[i] & 0xFF));
        }
        return sb.toString();
    }

    public static boolean isFirstwbitsZero(byte[] hash){
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
}
