public class Node implements Runnable{

	public Node(){

	}

	public boolean makeTransaction(double amount){

	}
	
	public byte[] getRawBlock(){
		
	}
  	public long mineBlock(Block blk){ 
  		byte[] s = blk.getRawBlock(); //Should return bytes of prevHash+Txns(merkel root)

  	}
  

}
