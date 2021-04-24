class Transaction{
	private int transactionID;
	public PublicKey sender;
	public PublicKey receiver;
	private float amount;

	public byte[] prevHash;
	public byte[] txHash;


	public Transaction(float amt, ){
		
	}
	
	public int getTransactionID(){
		return transactionID;
	}
	
	public byte[] getHash(){ return txtHash}
	//transaction hash will be the hash of senderKey + receiverKey + amount
}
