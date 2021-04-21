class Block{
	private String currentHash;
	private String prevHash;
	private int noice;
	
	private Vector<Transcation> transactionList;
	
	public Block(String prevHash){
		this.prevHash = prevHash;
		this.transactionList = new Vector<Transaction>();
	
	}
	
	//a function to add the transactions in the vector: tommorow
	
}
