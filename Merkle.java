import java.util.*;
import java.io.ByteArrayOutputStream;


class Merkle{
    //private String rootHash;
    public byte[] rootHash;
    private int arity;
    private Vector<Transaction> transactionList;
    private Vector<byte[]> transactionsHashList;
    private Vector<Vector<byte[]>> tree;


    public Merkle(Vector<Transaction> vec){
        this.transactionList = vec;
        this.arity = Main.arity;
        this.tree = new Vector<>();
        buidMerkelTree();
    }


    public byte[] getMerkleRootHash(){
      return rootHash;
    }

    public void getHashofTransactions(){

      transactionsHashList = new Vector<>();

      for (int i =0; i< transactionList.size(); i++){
        transactionsHashList.add(transactionList.get(i).getHash());
      }
    }

    public Vector<byte[]> getHashedParent(Vector<byte[]> vec){
	try{
      Vector<byte[]> nextlevel = new Vector<>();
      int noOfparents = vec.size()/this.arity;
      int i = 0;
      for( i = 0; i < noOfparents; i = i++){
        ByteArrayOutputStream combinedBytes = new ByteArrayOutputStream();
        for(int j = i*this.arity ; j < i + this.arity; j++){
            combinedBytes.write(vec.get(j));

        }
        byte res[] = combinedBytes.toByteArray();
        nextlevel.add(res);
      }
      //If any transaction or hash remaining at the end 
      ByteArrayOutputStream combinedBytes = new ByteArrayOutputStream();
      for(int j = i*this.arity; j < vec.size(); j++){
        combinedBytes.write(vec.get(j));
      }
      byte res[] = combinedBytes.toByteArray();
      nextlevel.add(res);

      return nextlevel;
      }catch(Exception e){
			throw new RuntimeException (e);
	}

    }
    private void buidMerkelTree(){
      int size = transactionList.size();
      getHashofTransactions();
      Vector<byte[]> levelNodes = this.transactionsHashList;
      tree.add(levelNodes);

      while(size > 1){
        levelNodes = getHashedParent(levelNodes);
        size = levelNodes.size();
      }
      this.rootHash = tree.get(tree.size()- 1).get(0);
    }
}


  
