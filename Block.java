import java.math.BigInteger; 
import java.util.*;
import java.security.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;


class Block{

	//Block header
	
	public byte[] blockHash; //sha256(nonce||prev||merkelRoot)

	public byte[] prevBlockHash;
	public BigInteger nonce;
	public String timeStamp;
	//private long timeStamp;

	public Merkle merkleTree; //Merkel tree how?
	
	public Block(Vector<Transaction> vec){	//This constructor is for genesis block only
		byte[] genesis = new byte[32]; //256 because hash size is 256bit
		this.prevBlockHash = genesis;
		this.merkleTree = new Merkle(vec);  //vec is the list of all transaction related to this block		
		//calculate hash of this block using prevHash + nonce + Txns + timeStamp
		addTimeStamp();
	}

	public Block(byte[] prevHash, Vector<Transaction> vec){
		this.prevBlockHash = prevHash;
		this.merkleTree = new Merkle(vec);  //vec is the list of all transaction related to this block		
		//calculate hash of this block using prevHash + nonce + Txns + timeStamp
		addTimeStamp();
	}

	public void addTimeStamp(){
		Timestamp tms = new Timestamp(System.currentTimeMillis());
		this.timeStamp = tms.toString();
	}

	public byte[] getBlockInFormOfbytes(){ //leaving nonce the part that needs to be taken as input to hash
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		//s.write()
		try{
			// System.out.println("Previous hash " + this.prevBlockHash);
			// if(prevBlockHash != null){
			// 	System.out.println("Not null " + prevBlockHash);
			// }
			s.write(prevBlockHash);
			// if(merkleTree.rootHash != null){
			// 	System.out.println("Not null " + prevBlockHash);
			// }else{
			// 	System.out.println("Its null");
			// }
			s.write(merkleTree.rootHash);
		}catch(IOException e){
			System.out.println("Exception in Block");

		}
		
		try{
			s.write(timeStamp.getBytes("UTF-8"));
		}catch(IOException e){
			System.out.println("Exception");
		}

		return s.toByteArray();
	}


	//Not needed for now. keep it by end we can remove this
	public byte[] longToByte(long inp){
		//try{
			ByteArrayOutputStream outOS = new ByteArrayOutputStream();
			DataOutputStream dataOS = new DataOutputStream(outOS);
			try{
				dataOS.writeLong(inp);
				byte[] bytes = outOS.toByteArray();
				dataOS.close();
				return bytes;
			}catch(IOException e){
				System.out.println("Exception");
			}
			
		return null;
		
	}



}
