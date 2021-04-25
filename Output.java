import java.security.PublicKey;

class Output{
	public PublicKey receiver;
	public double amount;

	public Output(PublicKey receiver, double amount){
		this.receiver = receiver;
		this.amount = amount;
	}

}