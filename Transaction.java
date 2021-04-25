import java.security.*;
import java.util.*;

class Transaction{
	private int transactionID;
	public PublicKey sender;
	public PublicKey receiver;
	private float amount;

	public byte[] prevHash;
	public byte[] txHash;

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

	public void signTransaction(PublicKey publicKey){

	}
	
	public byte[] getHash(){ return txHash;}
	//transaction hash will be the hash of senderKey + receiverKey + amount
}
