class Merkle{
    //private String rootHash;
    public byte[] rootHash;
    private int arity;
    private Vector<Transaction> transactionList;
    private Vector<byte[]> transactionsHashList;
    private Vector<Vector<byte[]>> tree;


    public Merkle(Vector<Transaction> vec){
        transcationList = vec;
        this.arity = Main.arity;
        this.tree = new Vector<>();
        buidMerkelTree();
    }


    public String getMerkleRootHash(){
      return rootHash;
    }

    public void getHashofTransactions(){

      transactionsHashList = new Vector<>();

      for (int i =0; i< transactionList.size(); i++){
        transactionsHashList.add(transactionList.get(i).getHash());
      }
    }

    public Vector<byte[]> getHashedParent(Vector<byte[]> vec){

      Vector<byte[]> nextlevel = new Vector<>();
      noOfparents = vec.size()/this.arity;

      for( int i = 0; i < noOfparents; i = i++){
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


  