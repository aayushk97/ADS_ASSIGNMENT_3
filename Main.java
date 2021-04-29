import java.util.Scanner;
import java.math.BigInteger; 

import java.util.*;

public class Main{
	
	public static final double MINING_REWARD = 50;  //Reward amount
	public static int w = 15;  //first w bits to be zero
	public static int numNodes;
	public static Vector<Node> nodes;

	public static int arity = 2; //default
	public static double oneSatoshi = 0.005; //min amount of bitcoin that can be transferred
						                      //different from actual value
	
	public static int maxTransactionInBlock = 50;   
	public static double probToSend = 0.2;
	
	static Vector<Queue<Block>> blockReceivingQueues;
	static Vector<Queue<Transaction>> txnReceivingQueues;
	static Vector<Queue<Message>> messagePassingQ;  

	public static void main(String[] args){
		nodes = new Vector<Node>();
	
    		Scanner in = new Scanner(System.in);
    		
    		System.out.println("Enter the number of nodes in network");
    		numNodes = in.nextInt();

    		//initialize the queues of each node 
    		blockReceivingQueues = new Vector<>();
    		txnReceivingQueues = new Vector<>();
    	    messagePassingQ = new Vector<>();
    		for(int i = 0; i < numNodes; i++){
    			blockReceivingQueues.add(new LinkedList<>());
    			txnReceivingQueues.add(new LinkedList<>());	
                messagePassingQ.add(new LinkedList<>());
        	}
        	
        	//initialize each node
        	for(int i = 0 ; i < numNodes; i++ ){
            		nodes.add(new Node(i));
        	}
        	
        	//initialize bitcoin chain in all nodes
        	for(int i = 0; i < numNodes; i++){
        		nodes.get(i).initializeBitcoinChain();
        	}
        	
        	//mine the genesis block
        	nodes.get(0).mineGenesisBlock();
        	
        	//add the genesis block to rest of them
        	for(int i = 1 ; i < numNodes; i++ ){
            		nodes.get(i).addGenesisBlock();
        	}
        	
            for(int i = 0 ; i < numNodes; i++ ){
                    nodes.get(i).start();
            }

        	for(int i =0; i < numNodes; i++){
			try{
				nodes.get(i).tId.join();
			}catch(InterruptedException e){
				System.out.println("InterruptedException");
			}
				
		}

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

    public static boolean isFirstwbitsZero(byte[] hash){
        //There was some problem in last isFirstwbitsZero..this is working fine
        int fullBytes = w/8;
        int r = w%8;
        for(int i = 0 ; i < fullBytes; i++){
            if(hash[i] != 0){
                return false;
            }
        }
        //aByte & 0xff
        switch(r){
            case 0:
                return true;
            case 1:
                if(hash[fullBytes] <= 0x7f){
                    return true;
                }else{
                    return false;
                }
            case 2:
                if((hash[fullBytes] & 0xff) <= 0x3f){
                    System.out.println(hash[fullBytes]);
                    return true;
                }else{
                    return false;
                }
            case 3:
                //System.out.println("3rd?");
                if((hash[fullBytes] & 0xff) <= 0x1f){
                    System.out.println(hash[fullBytes]);
                    return true;
                }else{
                    return false;
                }
            case 4:
                if((hash[fullBytes] & 0xff) <= 0x0f){
                    System.out.println(hash[fullBytes]);
                    return true;
                }else{
                    return false;
                }
            case 5:
                //System.out.println("5th?");
                if((hash[fullBytes] & 0xff) <= 0x07){
                    System.out.println(hash[fullBytes]);
                    return true;
                }else{
                    return false;
                }
            case 6:
                if((hash[fullBytes] & 0xff) <= 0x03){
                    System.out.println(hash[fullBytes]);
                    return true;
                }else{
                    return false;
                }
            case 7:
                if((hash[fullBytes] & 0xff) <= 0x01){
                    System.out.println(hash[fullBytes]);
                    return true;
                }else{
                    return false;
                }
            default:
                System.out.println("It should never come here");
                return false;
        }
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
