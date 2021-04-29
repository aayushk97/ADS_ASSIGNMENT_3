class Message{
	public int messageType; // 0 for request; 1 for send
	public int receiver;
	public int sender;
	public int chainSize;

	public Message(int messageType, int receiver, int sender, int chainSize){
		this.messageType = messageType;
		this.receiver = receiver;
		this.sender = sender;
		this.chainSize = chainSize;
	}
}

class RequestMessage extends Message{
	public RequestMessage(int messageType, int receiver, int sender, int chainSize){
		super(messageType, receiver, sender, chainSize);
	}
}

class ResponseMessage extends Message{
	private Vector<Block> bitcoinChain;

	public ResponseMessage(int messageType, int receiver, int sender, int chainSize,  Vector<Block> bitcoinChain){
		super(messageType, receiver, sender, chainSize);
		this.bitcoinChain = bitcoinChain;
	
	}
	
	public Vector<Block> getBlockChain(){
		return bitcoinChain;
	}

}


