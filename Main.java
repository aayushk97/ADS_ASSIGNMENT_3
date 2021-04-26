import java.util.Scanner;
import java.math.BigInteger; 

import java.util.*;

public class Main{
	public static final double MINING_REWARD = 50;  //Reward amount
	public static int w = 15;  //first w bits to be zero
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
            //Vector<Block> x = new Vector<>();  // will need to remove this and input appropriatively 
            nodes.add(new Node(i));
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

    public static String toHexString2(byte[] b){
        BigInteger number = new BigInteger(1, b);
        StringBuilder hashInHex = new StringBuilder(number.toString(16));
        //System.out.println("Length: " + hashInHex.length());
        while(hashInHex.length() < 64) hashInHex.insert(0, '0');
        //System.out.println("Length: " + hashInHex.length());

        return hashInHex.toString();
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
                System.out.println("3rd?");
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
                System.out.println("5th?");
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


    public static boolean isFirstwbitsZero2(byte[] hash){
        System.out.println("w: " + w + " h: " + hash.length);
        int i =0;
        System.out.println("...H11: i= "+ i + " " + toHexString(hash));
        System.out.println("...H22: i= "+ i + " " + toHexString2(hash));

    	for( i =0; i < w && i < hash.length*4; i++){
    		if(! isZero(hash, i)){
    			return false;
    		}
    	}

        System.out.println("...H: i= "+ i + " " + toHexString(hash));
        System.out.println("...H2: i= "+ i + " " + toHexString2(hash));
        System.out.println("...H3: i= "+ i + " " + Arrays.toString(hash));

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
