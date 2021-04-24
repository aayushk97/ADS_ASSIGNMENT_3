class Block{

	//Block header
	private String blockHash;

	private String prevBlockHash;
	private int nonce;
	private long timeStamp;
	private Merkle merkleTreeRoot; //Merkel tree how?

	public Block(String prevHash, Vector<Transaction>){
		this.prevHash = prevHash;
		this.merkleTree = new Merkle();
		
		//calculate hash of this block using prevHash + nonce + Txns
	}
}
