import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.Math;

class Merkle{
    //private String rootHash;
    public byte[] rootHash;
    private int arity;
    //private Vector<Transaction> transactionList;
    private Vector<byte[]> transactionsHashList;
    private Vector<Vector<byte[]>> tree;


    public Merkle(Vector<Transaction> vec){
        System.out.println("Merkel Constructor: "+vec.size());
        //this.transactionList = vec;
        this.arity = Main.arity;
        this.tree = new Vector<>();
        transactionsHashList = new Vector<>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        for(int i = 0; i < vec.size(); i+=Main.arity){
        	try{
        	for(int j = i; j < Main.arity && j < vec.size() ; j++) outputStream.write(vec.get(j).getHash());
        	
        	transactionsHashList.add(Crypto.sha256(outputStream.toByteArray()));
        	
        	outputStream.reset();
        	}catch(IOException e){
              System.out.println("Exception merkle");
            }
            
        }
        
        for(int i = 0; i < transactionsHashList.size(); i++){
        	System.out.println(transactionsHashList.get(i));
        }
        
        transactionsHashList.add(null);
        
        System.out.println(transactionsHashList.size());
        byte[] tempHash;
        double size = transactionsHashList.size();
        
        Vector<byte[]> temp = new Vector<>();
        
        while(transactionsHashList.size() > 2){
        	
        	
        	try{
        	for(int i = 0; i < Main.arity && transactionsHashList.get(0) != null; i++){ outputStream.write(transactionsHashList.remove(0));
        	//System.out.println(" Removed element at"+i);
		
        	}
		
		//System.out.println(transactionsHashList.size() + " 1 "+ temp.size()+"node : "+nodeId);
		//size -= Main.arity;
        	        	
        	tempHash = outputStream.toByteArray();
        	transactionsHashList.add(Crypto.sha256(tempHash));
        	outputStream.reset();
        	
        	if(transactionsHashList.get(0) == null){ 
        		transactionsHashList.remove(0);
        		transactionsHashList.add(null);
        	}
        	
        	//System.out.println(size + " 2 "+ temp.size());
        	 /*if(size <= 0){
        	 	int sizetmp = temp.size();
        	 	for(int i = 0; i < sizetmp; i++){
        	 		transactionsHashList.add(temp.remove(0));
        	 	}
        	 	size = Math.ceil(transactionsHashList.size()/Main.arity); 
			System.out.println(" here");        	 	
        	 } 
        	
        	System.out.println(size + " "+ temp.size() + " "+ transactionsHashList.size());
		if(temp.size() <= 0 && transactionsHashList.size() == 1) break;*/        	
        	}catch(IOException e){
              System.out.println("Exception merkle");
            }
        }
        
        rootHash = transactionsHashList.remove(0);
    }


    public byte[] getMerkleRootHash(){
      return rootHash;
    }
/*
    public void getHashofTransactions(){

      transactionsHashList = new Vector<>();

      for (int i =0; i< transactionList.size(); i++){
        // byte[] x = transactionList.get(i).getHash();
        // if(x != null){
        // System.out.println("1_Its not null?");
        // System.out.println(x);
        // System.out.println("1_printed");
        // }else{
        //   System.out.println("1_NULL");
        // }
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
            try{
                combinedBytes.write(vec.get(j));
            }catch(IOException e){
              System.out.println("Exception merkle");
            }
            

        }
        byte res[] = combinedBytes.toByteArray();
        nextlevel.add(res);
      }
      //If any transaction or hash remaining at the end 
      ByteArrayOutputStream combinedBytes = new ByteArrayOutputStream();
      for(int j = i*this.arity; j < vec.size(); j++){
        try{
          combinedBytes.write(vec.get(j));
        }catch(IOException e){
          System.out.println("Exception in Merkle ");
        }
        
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
      System.out.println("TransactionList Size: " + size);
      getHashofTransactions();

      // System.out.println("TransactionList Size2: " + transactionsHashList.size());
      // System.out.println("TransactionList Size2: " + transactionsHashList.get(size - 1));
      
      Vector<byte[]> levelNodes = this.transactionsHashList;
      tree.add(levelNodes);

      while(size > 1){
        levelNodes = getHashedParent(levelNodes);
        size = levelNodes.size();
      }

      // System.out.println("In Merkel: " + tree.size());
      // System.out.println("2: " + tree.get(0).size());
      byte[] x = tree.get(0).lastElement();
      // if(x != null){
      //   System.out.println("Its not null?");
      //   System.out.println(x);
      //   System.out.println("printed");
      // }else{
      //   System.out.println("NULL");
      // }
      // System.out.println("Last: " + x==null + "\n");

      this.rootHash = tree.get(tree.size()- 1).get(0);
      System.out.println("Hash root: " + this.rootHash);
    }
*/
}


  
