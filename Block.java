class Block{

	//Block header
	public byte[] blockHash; //sha256(nonce||prev||merkelRoot)

	public byte[] prevBlockHash;
	public BigInteger nonce;
	//private long timeStamp;

	public Merkle merkleTree; //Merkel tree how?
	public Block(){
		//for genesis block
	}
	public Block(byte[] prevHash, Vector<Transaction> vec){
		this.prevHash = prevHash;
		this.merkleTree = new Merkle(vec);  //vec is the list of all transaction related to this block		
		//calculate hash of this block using prevHash + nonce + Txns + timeStamp
	}

	public byte[] getBlockInFormOfbytes(){ //leaving nonce the part that needs to be taken as input to hash
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		//s.write()
		s.write(prevBlockHash);
		s.write(merkleTree.rootHash);
		return s.toByteArray();
	}


	//Not needed for now. keep it by end we can remove this
	public byte[] longToByte(long inp){
		//try{
			ByteArrayOutputStream outS = new ByteArrayOutputStream();
			DataOutputStream dataOS = new DataoutputStream(outOS);
			dataOS.wirteLong(inp);
			byte[] bytes = outOS.toByteArray();
			dataOS.close();
			return bytes;
		//}catch()
		
	}



}
