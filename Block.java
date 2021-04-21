class Block{
	private String currentHash;
	private String prevHash;
	private int nonce;
	
	private Merkle merkleTree; //Merkel tree how?
	
	public Block(String prevHash){
		this.prevHash = prevHash;
		this.merkleTree = new Merkle();
		
		//calculate hash of this block using prevHash + nonce + Txns
	}
	
	//a function to add the transactions in the vector: tommorow
	
}
