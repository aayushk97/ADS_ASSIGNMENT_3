import java.math.BigInteger; 
import java.util.*;
import java.security.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

class Block{

	//Block header
	public byte[] blockHash; //sha256(nonce||prev||merkelRoot)

	public byte[] prevBlockHash;
	public BigInteger nonce;
	//private long timeStamp;

	public Merkle merkleTree; //Merkel tree how?
	public Block(){
		//for genesis block
	}
	
	public Block(Vector<Transaction> vec){	//This constructor is for genesis block only
		byte[] genesis = new byte[32]; //256 because hash size is 256bit
		this.prevBlockHash = genesis;
		this.merkleTree = new Merkle(vec);  //vec is the list of all transaction related to this block		
		//calculate hash of this block using prevHash + nonce + Txns + timeStamp
	}

	public Block(byte[] prevHash, Vector<Transaction> vec){
		this.prevBlockHash = prevHash;
		this.merkleTree = new Merkle(vec);  //vec is the list of all transaction related to this block		
		//calculate hash of this block using prevHash + nonce + Txns + timeStamp
	}

	public byte[] getBlockInFormOfbytes(){ //leaving nonce the part that needs to be taken as input to hash
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		//s.write()
		try{
		s.write(prevBlockHash);
		s.write(merkleTree.rootHash);
		}catch(Exception e){
			throw new RuntimeException (e);
		}
		return s.toByteArray();
	}


	//Not needed for now. keep it by end we can remove this
	public byte[] longToByte(long inp){
		try{
			ByteArrayOutputStream outOS = new ByteArrayOutputStream();
			DataOutputStream dataOS = new DataOutputStream(outOS);
			dataOS.writeLong(inp);
			byte[] bytes = outOS.toByteArray();
			dataOS.close();
			return bytes;
		}catch(Exception e){
			throw new RuntimeException (e);
		}
	}



}
