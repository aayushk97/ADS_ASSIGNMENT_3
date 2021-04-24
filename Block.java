class Block{
	private String blockHash;
	private String prevBlockHash;

	private int nonce;
	private long timeStamp;

	private Merkle merkleTreeRoot; //Merkel tree how?
	
	private HashMap<PublicKey, Account> accountOfAll;  //As all nodes keep track of everyOne's account by annonimity

	public Block prev  // reference of previous block to form a linked list.
	public Block next // reference of next block?? is it possible?

	public Block(String prevHash){
		this.prevHash = prevHash;
		this.merkleTree = new Merkle();
		
		//calculate hash of this block using prevHash + nonce + Txns
	}
	
	//a function to add the transactions in the vector: tommorow
	
}
