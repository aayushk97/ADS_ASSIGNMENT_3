import java.math.BigInteger;
import java.util.*;
import java.security.*;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

public class Node implements Runnable{
	
	static final BigInteger ONE = new BigInteger("1");
	
	Vector<Block> bitcoinChain;

	public int m = 0;
	public Thread tId;
	public int nodeId;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public HashMap<PublicKey, Vector<UnspentTxn>> unspentTxns;

	public Vector<Transaction> validTransactions;
	
	private Queue<Block> blockReceiveQ;
	private Queue<Transaction> txnReceiveQ;

	public Node(int nodeId){
		//this.bitcoinChain = bitcoinChain;  //ideally it should probe all other nodes to get longest chain.
		
		//Generate keys for this node
		bitcoinChain = new Vector<>();
		this.nodeId = nodeId;
		
		int size = 256; 
		KeyPair pair = Crypto.generateKeyPair(size);
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();

		Queue<Block> blockReceiveQ = new LinkedList<>();
		Queue<Transaction> txnReceiveQ = new LinkedList<>();
		
		//initialize unspect queues
		unspentTxns = new HashMap<>();
		

		validTransactions = new Vector<>();
	}
	
	public void run(){
		System.out.println("Value of m: "+m);
		for(int i =0; i < Main.numNodes; i++){
			unspentTxns.put(Main.nodes.get(i).publicKey, new Vector<UnspentTxn>());
		}
		//Add a genesis block
		System.out.println("In running " + nodeId);
		mineGenesisBlock();
		System.out.println("BlockChain size for node: "+nodeId+" is :"+bitcoinChain.size());
		blockReceiveQ = Main.blockReceivingQueues.get(nodeId);
		txnReceiveQ = Main.txnReceivingQueues.get(nodeId);
		blockReceiveQ.clear();	
		Vector<UnspentTxn> myUnspent = unspentTxns.get(publicKey);
		if(myUnspent == null)
		System.out.println(nodeId+ ": Unspent size: "+nodeId+" is : 0");
		else
		System.out.println(nodeId + ": Unspent size: "+nodeId+" is :"+myUnspent.size());
		
		System.out.println(nodeId + ": Value of m: "+m);
		
		while(true && Main.numNodes > 1){
			//getBalanceOfEachNode();
			//System.out.println("Size of receive queue: "+txnReceiveQ.size());
			//we start validating the received transactions 
			if(txnReceiveQ.size() > 0){
				System.out.println(nodeId + ": Verifying transactions");
				
				int size = txnReceiveQ.size();
				while(!txnReceiveQ.isEmpty()){
					Transaction receivedTxn = txnReceiveQ.remove();
					boolean verified = verifyTransaction(receivedTxn);
				
					if(verified){
						validTransactions.add(receivedTxn);
						m++;		
				
					}
					size--;
				}
			}
			
			//first we check if max no of transactions are collected by the node
			if(validTransactions.size() >= Main.maxTransactionInBlock){
				
				System.out.println("Transcations are collected and block is being made");
				
				Vector<Transaction> vec = new Vector<>();
				
				//Add limited no of transactions to new block
				for(int i = 0; i < Main.maxTransactionInBlock; i++){
					Transaction txn = txnReceiveQ.remove();
					vec.add(txn);
				}
				
				Block blk = new Block(vec);
				
				boolean minedFirst = mineBlock(blk);
				
				if(minedFirst){
					broadCast(blk);
					bitcoinChain.add(blk);
					System.out.println("Current length block mined by node "+nodeId);
				}else{
			
					blockReceiveQ = Main.blockReceivingQueues.get(nodeId);
			
					blk = blockReceiveQ.remove();
			
					boolean verifiedBlock = verifyBlock(blk);
					while(!verifiedBlock){
						blk = blockReceiveQ.remove();
						verifiedBlock = verifyBlock(blk);
					}
			
					bitcoinChain.add(blk);
					System.out.println("current length block was mined by other node but inserted by node: "+nodeId);
					
					//we now validate the transactions in this block and add the validated ones to unspentTxns
					
			
		}
			
			}
			
			//perform a Transaction
			Random random = new Random();
			
			//generate a number between 1 to 10
			double x = 1+random.nextInt(10);
			boolean sent = false;
			if(x <= Main.probToSend*10){
				sent = doTransactions();
				if(sent) System.out.println("Transaction was sent by "+nodeId);
			}	
		}
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
		
		//Store all the created transaction
		Vector<Transaction> vec = new Vector<>();
		vec.add(txn);
		
		
		//Time to broadCast and add to it's own ledger
		System.out.println("Signature: "+ verifyTxnSignature(txn));
		
		//Add the transactions to block
		Block blk = new Block(vec);
		
		boolean minedFirst = mineBlock(blk);
		if(minedFirst){
			broadCast(blk);

			bitcoinChain.add(blk);
			System.out.println("Genesis block mined by node "+nodeId);
			
			//add this transaction to unspent trnasaction
			UnspentTxn myUnspentTxn = new UnspentTxn(getTxnBytes(txn), 0, publicKey, txn.outputTxns.get(0).amount);
			System.out.println("Amount in genesis mine block: "+txn.outputTxns.get(0).amount);
			Vector<UnspentTxn> myUnspent = new Vector<>();
			myUnspent.add(myUnspentTxn);
			unspentTxns.put(publicKey, myUnspent);
		}else{
			
			blockReceiveQ = Main.blockReceivingQueues.get(nodeId);
			
			blk = blockReceiveQ.remove();
			
			boolean verifiedBlock = verifyBlock(blk);
			while(!verifiedBlock){
				blk = blockReceiveQ.remove();
				verifiedBlock = verifyBlock(blk);
			}
			
			bitcoinChain.add(blk);
			System.out.println(nodeId + ": Genesis block was mined by other node but inserted by node: "+nodeId);
			Transaction txnInBlock = blk.transactionsInBlock.get(0);
			UnspentTxn unspentOthersTxn = new UnspentTxn(getTxnBytes(txnInBlock), 0,  txnInBlock.outputTxns.get(0).receiver, txnInBlock.outputTxns.get(0).amount); 
			// Vector<UnspentTxn> unspentOthers = new Vector<>();
			// unspentOthers.add(unspentOthersTxn);
			// unspentTxns.put(txnInBlock.outputTxns.get(0).receiver, unspentOthers);

			//unspentTxns.get(this.publicKey).add(unspentOthersTxn);			
		    unspentTxns.get(txnInBlock.outputTxns.get(0).receiver).add(unspentOthersTxn);

	
		}
	}
	
	public boolean verifyTransaction(Transaction receivedTransaction){
		
		 //find the hash of this transaction
		boolean verified = verifyTxnSignature(receivedTransaction);	
		if(!verified) return false;
		
		boolean validate = validateTransaction(receivedTransaction);
		
		return true;
	}
	
	public boolean validateTransaction(Transaction receivedTransaction){
		
		Vector<UnspentTxn> unspentTxnNode = unspentTxns.get(receivedTransaction.sender);
		
		double amountPresent = 0.0, amountTransferred = 0.0;
		
		//Get total unspent Transaction
		for(UnspentTxn t: unspentTxnNode){
			amountPresent += t.amount;
		} 
		
		//Get the amount that was sent in transaction
		for(int i = 0; i < receivedTransaction.outputTxns.size(); i++){
			amountTransferred += receivedTransaction.outputTxns.get(i).amount;
		}
		
		System.out.println("Amount Present :"+amountPresent+"Amount Transferred: "+amountTransferred+" received in node ID: "+nodeId);
		//The transaction is valid
		if(amountPresent >= amountTransferred){
			
			for(int j = 0; j < receivedTransaction.inputTxns.size(); j++){
				for(int i = 0; i < unspentTxnNode.size(); i++){
				//System.out.println("Hash of unspent: "+unspentTxnNode.get(i).txnHash+" Hash of input"+receivedTransaction.inputTxns.get(j).refTxn);
					if(unspentTxnNode.get(i).txnHash == receivedTransaction.inputTxns.get(j).refTxn){
						
		System.out.println(nodeId + ": Amount Removed");
						unspentTxnNode.remove(i);
					}
				}
			}
			
			unspentTxns.put(receivedTransaction.sender, unspentTxnNode);
			
			//Add new unspent transactions
			for(int i = 0; i < receivedTransaction.outputTxns.size(); i++){
				UnspentTxn unspent = new UnspentTxn(receivedTransaction.txHash, i, receivedTransaction.outputTxns.get(i).receiver, receivedTransaction.outputTxns.get(i).amount);
				
				Vector<UnspentTxn> unspentTxnNode2 = unspentTxns.get(receivedTransaction.outputTxns.get(i).receiver);
				
				unspentTxnNode2.add(unspent);
				
				unspentTxns.put(receivedTransaction.outputTxns.get(i).receiver, unspentTxnNode2);	
				//System.out.println("Added unspent");	
			}
			
			return true;
		}
		
		//else the transaction is not valid
		return false;
	}
	
	
	public PublicKey getPublickey(){
		return publicKey;
	}
	
	public boolean doTransactions(){
		//when transaction phase start just call this function it will randomly create a transaction 
		// will send fraction of money to other nodes
		Random rn = new Random();
		double totalAmount = 0;
		
		
		Transaction txn = new Transaction(this.publicKey);
		Vector<UnspentTxn> myUnspent = unspentTxns.get(publicKey);
		
		System.out.println("Unspent txns in node :"+nodeId+"is "+myUnspent.size());
		
		while(!myUnspent.isEmpty()){   //Assuming it will add all unspent txns and will keep the change
			UnspentTxn unstxn = myUnspent.remove(0);
			totalAmount+= unstxn.amount;
			//System.out.println("Amount: "+totalAmount+" in node :"+nodeId+"shown in  txn:"+unstxn.amount);
			txn.addInputToTxn(unstxn.txnHash, unstxn.indexInOutOfTxn);
		}
		
		System.out.println("Amount: "+totalAmount+" in node :"+nodeId);
		
		if(totalAmount > 0){
			int numTransaction =  1+rn.nextInt((int)(Main.numNodes*0.7));
			  //will return a random int these number of output will be included
			Set<Integer> alreadySent = new HashSet<>();
			/*if (numTransaction == 0){
				numTransaction = Main.numNodes/2;
				if(numTransaction == 0){
					System.out.println("Only one node network");
				}
			}*/
			while(numTransaction > 0 && totalAmount > 0){
				double fractionPay = rn.nextDouble();  //will return uniformely distributed [0,1]
				int randomNodeId = rn.nextInt(Main.numNodes);  //will return a random nodeId
				if(randomNodeId!= this.nodeId && !alreadySent.contains(randomNodeId)){
					double toPay = 0;
					if(totalAmount < Main.oneSatoshi){
						toPay = totalAmount;
					}else{
					 	toPay= fractionPay*totalAmount;
					}
					txn.addOutputToTxn(Main.nodes.get(randomNodeId).getPublickey(), toPay);
					System.out.println("The value of "+toPay+" transferred to "+randomNodeId+" by "+nodeId);
					totalAmount-= toPay;
					alreadySent.add(randomNodeId);
					numTransaction--;
				}
			}
			
			System.out.println(nodeId + " have Total amount: " + totalAmount);
			if(totalAmount > 0)
				txn.addOutputToTxn(this.publicKey, totalAmount);
			signTransaction(txn);
			broadCast(txn);
			
			//Add the unspent transaction to our hashMap
			for(int i = 0; i < txn.outputTxns.size(); i++){
				UnspentTxn unspent = new UnspentTxn(txn.txHash, i, txn.outputTxns.get(i).receiver, txn.outputTxns.get(i).amount);
				Vector<UnspentTxn> myUnspentOfNode = unspentTxns.get(txn.outputTxns.get(i).receiver);
				myUnspentOfNode.add(unspent);
				unspentTxns.put(txn.outputTxns.get(i).receiver, myUnspentOfNode);
				System.out.println("The value of "+txn.outputTxns.get(i).amount+" transferred by "+nodeId);
			}
			
			return true;

		}else{
			return false;
		}

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
  		//System.out.println("Size of the block" + Main.blockReceivingQueues.get(nodeId).size());
  		while(true){
  			//check if other nodes have not already finished mining blocks and broadcasted
  			if(Main.blockReceivingQueues.get(nodeId).size() != 0) return false;
  			
  			byte[] toBeHashed = concatTwoByteArray(nonce.toByteArray(), s);
  			try{

  				byte[] hash = Crypto.sha256(toBeHashed);
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
	        //System.out.println("Still mining current nonce: " + nonce);
	   
  		}
  		System.out.println("mining done by "+ nodeId + " !");
  		
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

		try{
			bytes.write(txn.timeStamp.getBytes("UTF-8"));
		}catch(IOException e){
			System.out.println("Exception");
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
  			if(i != nodeId){
  				Queue<Transaction> txnq = Main.txnReceivingQueues.get(i);
  				synchronized(txnq){
  					txnq.add(txn);
  					txnq.notify();
  				}
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
  	
  	public void getBalanceOfEachNode(){
  		for(int i = 0; i < Main.numNodes; i++){
  			Vector<UnspentTxn> unspentOfNode = unspentTxns.get(Main.nodes.get(i).getPublickey());
  			double amount = 0.0;
  			for(int j = 0; j < unspentOfNode.size(); j++) amount+= unspentOfNode.get(i).amount;
  			
  			System.out.println("The amount with node "+i+" is "+amount);
  		}
  	
  	}

}

class UnspentTxn{
	public byte[] txnHash; 
	public int indexInOutOfTxn;
	public PublicKey pk;
	public double amount;  //amount received.
	public UnspentTxn(byte[] txnHash, int indexInOutOfTxn, PublicKey pk, double amount){
		this.txnHash = txnHash;
		this.indexInOutOfTxn = indexInOutOfTxn;
		this.pk = pk;
		this.amount = amount;
	}
}

// class TransactionPool(){  //can votes from all nodes whether they are confirming this txn or not and Majority wins
// 							//when a node verify  the transaction in increase the confirmations by 1 
// 							//when at least 51% nodes confirms it should move to confirmed pool or can be taken as confirmed
// 							// by  checking
// 	Transaction txn;
// 	int confirmation;
// 	int rejections;
// 	boolean isValid;  //true after 51% confirmations
// 	public void confirm(){
// 		confirmation++;
// 		if()
// 	}
// }
