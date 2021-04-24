class Merkle{
  private String rootHash;
  private Vector<Transaction> transactionList;
  
  public Merkle(Vector<Transaction> vec){
      transcationList = vec;
      buidMerkelTree();
  }
  
  
  public String getMerkleRootHash(){
    return rootHash;
  }
  
  public bool addTransactionToMerkle(Transaction txn){
    
  }

}
