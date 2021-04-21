class Merkle{
  private String rootHash;
  private Vector<Transaction> transactionList;
  
  public Merkle(){
      transcationList = new Vector<Transaction>();
  }
  
  
  public String getMerkleRootHash(){
    return rootHash;
  }
  
  public bool addTransactionToMerkle(Transaction txn){
    
  }

}
