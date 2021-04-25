import java.security.PublicKey;
import java.io.ByteArrayOutputStream
class Output{
	public PublicKey receiver;
	public double amount;

	public Output(PublicKey receiver, double amount){
		this.receiver = receiver;
		this.amount = amount;
	}

	public byte[] getBytes(){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bytes.write(receiver.getEncoded());
		bytes.write(Main.getBytesFromDouble(amount));
		return bytes.toByteArray();
	}
}