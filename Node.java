import java.math.BigInteger;
import java.util.*;
import java.security.*;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

public class Node implements Runnable{
	
	static final BigInteger ONE = new BigInteger("1");
	
	HashMap<byte[], Block> bitcoinChain;


	public Thread tId;
	public int nodeId;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	public Queue<UnspentTxn> myUnspent;

	private Queue<Block> blockReceiveQ;
	private Queue<Transaction> txnReceiveQ;

	public Node(int nodeId){
		//this.bitcoinChain = bitcoinChain;  //ideally it should probe all other nodes to get longest chain.
		
		//Generate keys for this node
		bitcoinChain = new HashMap<>();
		this.nodeId = nodeId;
		int size = 256; 
		KeyPair pair = Crypto.generateKeyPair(size);
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
		myUnspent = new LinkedList<>();

		Queue<Block> blockReceiveQ = Main.blockReceivingQueues.get(nodeId);
		Queue<Transaction> txnReceiveQ = Main.txnReceivingQueues.get(nodeId);

	}
	
	public void run(){
	
		// Transaction receivedTransaction;
		// //First we need to validate the transaction?
		// //Then we verify it
		// boolean verified = verifyTransaction(receivedTransaction);
		
		//if verified the go forward else reject it
		System.out.println("In running " + nodeId);
		mineGenesisBlock();
		System.out.println("Mined genesis block");
	}
	
	public void start(){
		//will start the thread and call initialization method.
		System.out.println("Starting Thread: " + nodeId);
		if(tId == null){
			tId = new Thread(this);
			tId.start();
		}
	}

	public void mineGenesisBlock(){
		Transaction txn = createCoinbaseTxn();	//Only a coinbase txn will be added in the genesis block
		signTransaction(txn);
		Vector<Transaction> vec = new Vector<>();
		vec.add(txn);
		Block blk = new Block(vec);
		mineBlock(blk);
		//Time to broadCast and add to it's own ledger
		System.out.println("Signature: "+ verifyTxnSignature(txn));
		broadCast(blk);

		bitcoinChain.put(blk.blockHash, blk);
	}
	public boolean verifyTransaction(Transaction receivedTransaction){
		
		// //find the hash of this transaction
		// //Edit: Add time too?
		// String data = receivedTransaction.prevHash + Crypto.getKeyInString(receivedTransaction.receiver) + Float.toString(receivedTransaction.amount);
	
		// String hash = Crypto.sha256(data);
		
		// //verify the signature 
		// return Crypto.verifyECDSASign(reveivedTranasction.sender, hash, receivedTransaction.txHash);	

		//Let's assume transaction is verified for now
		return true;
	}
	
	public Transaction createCoinbaseTxn(){
		Transaction coinbaseTxn = new Transaction(true, this.publicKey);
		return coinbaseTxn;
	}
	public Transaction makeTransaction(double amount){  // need to add transaction ouput one by one by receiver PubK and amount
		return null;
	}


	public Vector<Transaction> collectValidTransactions(){
		//Verify the available transaction and add them in block
		return null;
	}

	public Block prepareBlock(){
		//To create a block wtih available new transactions
		Vector<Transaction> vec = collectValidTransactions();
		//Block newBlock = new Block(bitcoinChain.lastElement().blockHash, vec );
		return null;
	}

  	public boolean mineBlock(Block blk){ //while mining it needs to keep checking whether next block has arrived

  		//Block blk = prepareBlock();
  		byte[] s = blk.getBlockInFormOfbytes(); //Should return bytes of prevHash+Txns(merkel root)
  		BigInteger nonce = new BigInteger("0");
  		while(true){
  			byte[] toBeHashed = concatTwoByteArray(nonce.toByteArray(), s);
  			try{
	            // MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
	            // messageDigest.update(toBeHashed);

	            //byte[] hash = messageDigest.digest();

  				byte[] hash = Crypto.sha256(toBeHashed);
	            // BigInteger number = new BigInteger(1, hash);
	            // System.out.println("Nuber: " + number);  //1461179

	            if(isitRequiredhash(hash)){
	            	blk.nonce = nonce;
	            	blk.blockHash = hash;
	            	System.out.println("Hash: " + Main.toHexString(hash));
	            	break;
	            }
	    		nonce = nonce.add(ONE);
	        }catch(Exception e){
	            System.out.println("Exception");
	        }
	        System.out.println("Still mining current nonce: " + nonce);
	   
  		}
  		System.out.println("Mined done!");
  		return true;
  	}
  	
  	public boolean verifyBlock(Block blockToVerify){
  		if(verifyBlockHash(blockToVerify)){
  			return true;  //Still transactions need to be verified
  		}else{
  			return false;
  		}
  	
  	}

  	public boolean verifyBlockHash(Block blk){

  		byte[] blockHash = blk.blockHash;
  		byte[] x = concatTwoByteArray(blk.nonce.toByteArray(), blk.getBlockInFormOfbytes());
  		byte[] calculatedHash = Crypto.sha256(x);
  		return Arrays.equals(blockHash, calculatedHash);
  	}
  	public byte[] concatTwoByteArray(byte[] one, byte[] two){
  		ByteArrayOutputStream combinedBytes = new ByteArrayOutputStream();
  		try{
  			combinedBytes.write(one);
  			combinedBytes.write(two);
  		}catch(IOException e){
  			System.out.println("Exception: in concatTwoByteArray ");
  		}
  		
  		return combinedBytes.toByteArray();

  	}

  	public boolean verifyTxnSignature(Transaction txn){
  		byte[] rawData = getTxnBytes(txn);
  		return Crypto.verifyECDSASign(txn.sender, rawData, txn.signature);
  	}
  	public void signTransaction(Transaction txn){

        byte[] rawData = getTxnBytes(txn);
        byte[] signature = Crypto.applyECDSASign(this.privateKey, rawData);
        txn.txHash = Crypto.sha256(rawData);
        txn.signature = signature;

    }
  	public byte[] getTxnBytes(Transaction txn){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		int inpSize = txn.inputTxns.size();
		for(int i = 0; i < inpSize; i++){
			try{
				bytes.write(txn.inputTxns.get(i).refTxn);

			}catch(IOException e){
				System.out.println("Exception");
			}
		}

		int outSize = txn.outputTxns.size();
		for (int i = 0; i < outSize; i++ ){
			try{
				bytes.write(txn.outputTxns.get(i).getBytes());
				
			}catch(IOException e){
				System.out.println("Exception");
			}
		}

		return bytes.toByteArray();
	}

  	public boolean isitRequiredhash(byte[] hash){
  		return Main.isFirstwbitsZero(hash);
  	}

  	public boolean hasNewBlockArrived(){
  		synchronized(blockReceiveQ){
  			//check if queue is empty
  			return !blockReceiveQ.isEmpty();
  		}
  	}

  	public int noOfNewTransactions(){
  		synchronized(txnReceiveQ){
  			//check if queue is empty.  do I need to notify?? does this block any thread
  			return txnReceiveQ.size();
  		}
  	}

  	public void broadCast(Transaction txn){
  		for(int i =0; i < Main.numNodes; i++){
  			Queue<Transaction> txnq = Main.txnReceivingQueues.get(i);
  			synchronized(txnq){
  				txnq.add(txn);
  				txnq.notify();
  			}
  		}
  	}

  	public void broadCast(Block blk){
  		for(int i =0; i < Main.numNodes; i++){
  			if(i != nodeId){
  				Queue<Block> recvq = Main.blockReceivingQueues.get(i);
	  			synchronized(recvq){
	  				recvq.add(blk);
	  				recvq.notify();
	  			}
  			}
  			
  		}
  	}

}

class UnspentTxn{
	public byte[] txnHash; 
	public int indexInOutOfTxn;
	public PublicKey pk;
	public UnspentTxn(byte[] txnHash, int indexInOutOfTxn, PublicKey pk){
		this.txnHash = txnHash;
		this.indexInOutOfTxn = indexInOutOfTxn;
		this.pk = pk;
	}
}