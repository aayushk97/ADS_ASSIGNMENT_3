import java.math.BigInteger;
import java.util.*;
import java.security.*;

public class Node implements Runnable{
	
	static final BigInteger ONE = new BigInteger("1");
	
	HashMap<byte[], Block> bitcoinChain;

	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public Node(Vector<Block> bitcoinChain){
		this.bitcoinChain = bitcoinChain;  //ideally it should probe all other nodes to get longest chain.
		
		//Generate keys for this node
		int size = 256; 
		KeyPair pair = Crypto.generateKeyPair(size);
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
		
		
	}
	
	public void run(){
	
		Transaction receivedTransaction;
		//First we need to validate the transaction?
		//Then we verify it
		boolean verified = verifyTransaction(receivedTransaction);
		
		//if verified the go forward else reject it
	}
	
	
	public boolean verifyTransaction(Transaction receivedTransaction){
		
		//find the hash of this transaction
		String data = receivedTransaction.prevHash + Crypto.getKeyInString(receivedTransaction.receiver) + Float.toString(receivedTransaction.amount);
	
		String hash = Crypto.sha256(data);
		
		//verify the signature 
		return Crypto.verifyECDSASign(reveivedTranasction.sender, hash, receivedTransaction.txHash);	
		
	
	}
	
	
	public boolean makeTransaction(double amount){

	}
	public Vector<Transaction> collectValidTransactions(){
		//Verify the available transaction and add them in block
	}

	public Block prepareBlock(){
		//To create a block wtih available new transactions
		Vector<Transaction> vec = collectValidTransactions();
		Block newBlock = new Block(bitcoinChain.lastElement().blockHash, vec );
	}

  	public Block mineBlock(){ 

  		Block blk = prepareBlock();
  		byte[] s = blk.getBlockInFormOfbytes(); //Should return bytes of prevHash+Txns(merkel root)
  		BigInteger nonce = new BigInteger("0");
  		while(true){
  			byte[] toBeHashed = concatTwoByteArray(nonce.toByteArray(), s);
  			try{
	            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
	            messageDigest.update(toBeHashed);
	            byte[] hash = messageDigest.digest();
	            if(isitRequiredhash(hash)){
	            	blk.nonce = nonce;
	            	blk.blockHash = hash;
	            	break;
	            }
	    		nonce = nonce.add(ONE);
	        }catch(UnsupportedEncodingException e){
	            System.out.println("Exception");
	        }
	        System.out.println("Still mining current nonce: " + nonce);
	   
  		}
  		return blk;
  	}
  	
  	public boolean verifyBlock(Block blockToVerify){
  		
  		byte[] blockHash = blockToVerify.blockHash;
  		
  		byte[] prevBlockHash = blockToVerify.prevBlockHash;
  		BigInteger nonce = blockToverify.nonce;
  		
  		
  	
  	}

  	public byte[] concatTwoByteArray(byte[] one, byte[] two){
  		ByteArrayOutputStream combinedBytes = new ByteArrayOutputStream();
  		combinedBytes.write(one);
  		combinedBytes.write(two);
  		return combinedBytes.toByteArray();

  	}

  	public boolean isitRequiredhash(byte[] hash){
  		return Main.isFirstwbitsZero(hash);
  	}
}
