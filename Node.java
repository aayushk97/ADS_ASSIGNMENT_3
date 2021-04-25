public class Node implements Runnable{
	static final BigInteger ONE = new BigInteger("1");

	Vector<Block> bitcoinChain;

	public Node(Vector<Block> bitcoinChain){
		this.bitcoinChain = bitcoinChain;  //ideally it should probe all other nodes to get longest chain.
	}

	public boolean makeTransaction(double amount){

	}
	
	public Block prepareBlock(){
		//To create a block wtih available new transactions
	}
  	public void mineBlock(Block blk){ 
  		byte[] s = blk.getBlockInFormOfbytes(); //Should return bytes of prevHash+Txns(merkel root)
  		BigInteger nonce = new BigInteger("0");
  		while(true){
  			byte[] toBeHashed = concatTwoByteArray(nonce.toByteArray(), s);
  			try{
	            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
	            messageDigest.update(toBeHashed);
	            byte[] hash = messageDigest.digest();
	            if(isitRequiredhash(hash)){
	            	blk.nonce = nonce;
	            	blk.blockHash = hash;
	            	break;
	            }
	    		nonce = nonce.add(ONE);
	        }catch(UnsupportedEncodingException e){
	            System.out.println("Exception");
	        }
  		}
  	}

  	public byte[] concatTwoByteArray(byte[] one, byte[] two){
  		ByteArrayOutputStream combinedBytes = new ByteArrayOutputStream();
  		combinedBytes.write(one);
  		combinedBytes.write(two);
  		return combinedBytes.toByteArray();

  	}

  	public boolean isitRequiredhash(byte[] hash){
  		return Main.isFirstwbitsZero(hash);
  	}
}
