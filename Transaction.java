import java.security.*;
import java.util.*;
import java.io.ByteArrayOutputStream;

class Transaction{
	public PublicKey sender;
	public byte[] txHash;
	public byte[] signature;

	public Vector<Input> inputTxns;
	public Vector<Output> outputTxns;

	public boolean coinbase;  //for mining reward 
	public Transaction(){
		this.coinbase = false;
		inputTxns = new Vector<>();
		outputTxns = new Vector<>();
	}
	public Transaction(boolean coinbase, PublicKey minorsPublickey){
		this.coinbase = true;
		inputTxns = new Vector<>();
		outputTxns = new Vector<>();

		Output out = new Output(minorsPublickey, Main.MINING_REWARD);
		outputTxns.add(out);
	}
	
	public int getTransactionID(){
		return transactionID;
	}
	
	public void addOutputToTxn(PublicKey receiver, double amt){
		Output out = new Output(receiver, amt);
		outputTxns.add(out);
	}

	public void addInputToTxn(byte[] refTxnHash, int indexInTheTxnsOutput){
		Input inp = new Input(refTxnHash, indexInTheTxnsOutput);
		inputTxns.add(inp);
	}

	public byte[] getHash(){ return txHash;}
	//transaction hash will be the hash of senderKey + receiverKey + amount
}
