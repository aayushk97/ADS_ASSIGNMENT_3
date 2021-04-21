class Block{
	private String currentHash;
	private String prevHash;
	private int nonce;
	
	private Vector<Transcation> transactionList;
	private Merkle merkleTree; //Merkel tree how?
	
	public Block(String prevHash){
		this.prevHash = prevHash;
		this.transactionList = new Vector<Transaction>();
		
		//calculate hash of this block using prevHash + nonce + Txns
	}
	
	//a function to add the transactions in the vector: tommorow
	
}
