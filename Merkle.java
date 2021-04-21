class Merkle{
  private String rootHash;
  private Vector<Transaction> transactionList;
  
  public Merkle(){
    
  }
  
  
  public String getMerkleRootHash(){
    return rootHash;
  }

}
