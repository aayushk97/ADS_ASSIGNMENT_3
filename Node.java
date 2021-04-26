import java.math.BigInteger;
import java.util.*;
import java.security.*;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class Node implements Runnable{
	
	static final BigInteger ONE = new BigInteger("1");
	
	HashMap<byte[], Block> bitcoinChain;

	public Thread tId;
	public int nodeId;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	public Queue<UnspentTxn> myUnspent;

	public Node(int nodeId, Vector<Block> bitcoinChain){
		//this.bitcoinChain = bitcoinChain;  //ideally it should probe all other nodes to get longest chain.
		
		//Generate keys for this node
		int size = 256; 
		KeyPair pair = Crypto.generateKeyPair(size);
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
		myUnspent = new LinkedList<>();
	}
	
	public void run(){
	
		Transaction receivedTransaction;
		//First we need to validate the transaction?
		//Then we verify it
		boolean verified = verifyTransaction(receivedTransaction);
		
		//if verified the go forward else reject it
	}
	

	public Block mineGenesisBlock(){
		Transaction txn = createCoinbaseTxn();	//Only a coinbase txn will be added in the genesis block
		Vector<Transaction> vec = new Vector<>();
		vec.add(Transaction);
		Block blk = new Block(vec);
		mineBlock(blk);
		//Time to broadCast and add to it's own ledger
		broadCast(Block)

	}
	public boolean verifyTransaction(Transaction receivedTransaction){
		
		//find the hash of this transaction
		//Edit: Add time too?
		String data = receivedTransaction.prevHash + Crypto.getKeyInString(receivedTransaction.receiver) + Float.toString(receivedTransaction.amount);
	
		String hash = Crypto.sha256(data);
		
		//verify the signature 
		return Crypto.verifyECDSASign(reveivedTranasction.sender, hash, receivedTransaction.txHash);	
		
	
	}
	
	public Transaction createCoinbaseTxn(){
		Transaction coinbaseTxn = new Transaction(true, this.publicKey);
		return coinbaseTxn;
	}
	public Transaction makeTransaction(double amount){  // need to add transaction ouput one by one by receiver PubK and amount

	}
	public Vector<Transaction> collectValidTransactions(){
		//Verify the available transaction and add them in block
		
	}

	public Block prepareBlock(){
		//To create a block wtih available new transactions
		Vector<Transaction> vec = collectValidTransactions();
		Block newBlock = new Block(bitcoinChain.lastElement().blockHash, vec );
	}

  	public boolean mineBlock(Block blk){ //while mining it needs to keep checking whether next block has arrived

  		//Block blk = prepareBlock();
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
  		if(verifyBlockHash(blockToVerify)){
  			return true;  //Still transactions need to be verified
  		}else{
  			return false;
  		}
  	
  	}

  	public boolean verifyBlockHash(Block blk){

  		byte[] blockHash = blockToVerify.blockHash;
  		byte[] x = concatTwoByteArray(blk.nonce.toByteArray(), blk.getBlockInFormOfbytes());
  		byte[] calculatedHash = Crypto.sha256(x);
  		return Arrays.equals(blockHash, calculatedHash);
  	}
  	public byte[] concatTwoByteArray(byte[] one, byte[] two){
  		ByteArrayOutputStream combinedBytes = new ByteArrayOutputStream();
  		combinedBytes.write(one);
  		combinedBytes.write(two);
  		return combinedBytes.toByteArray();

  	}

  	public boolean verifyTxnSignature(Transaction txn){
  		byte[] rawData = getTxnBytes(Transaction txn);
  		return Crypto.verifyECDSASign(txn.sender, rawData, txn.signature);
  	}
  	public void signTransaction(Transaction txn){

        byte[] rawData = getTxnBytes(Transaction txn);
        byte[] signature = Crypto.applyECDSASign(this.privateKey, rawData);
        txn.txHash = Crypto.sha256(rawData);
        txn.signature = signature;

    }
  	public byte[] getTxnBytes(Transaction txn){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		int inpSize = txn.inputTxns.size();
		for(int i = 0; i < inpSize; i++){
			bytes.write(txn.inputTxns.get(i).refTxn);
		}

		int outSize = txn.outputTxns.size();
		for (int i = 0; i < outSize; i++ ){
			bytes.write(txn.outputTxns.get(i).getBytes())
		}

		return bytes.toByteArray();
	}

  	public boolean isitRequiredhash(byte[] hash){
  		return Main.isFirstwbitsZero(hash);
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