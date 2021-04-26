import java.security.*;

class Test4{
	public static void main(String[] args){
		String msg = "This is me";
		System.out.println("Print: " + Crypto.sha256(msg));
		
		
		
		int size = 256;
		
		KeyPair pair = Crypto.generateKeyPair(size);
		PrivateKey sk = pair.getPrivate();
		PublicKey pk = pair.getPublic();
		String afterSign;
		String x = Crypto.sha256(msg);
		System.out.println("KK: " + x);
		byte[] bytesMsg = Crypto.applyECDSASign(sk, x);
		
		boolean verified = Crypto.verifyECDSASign(pk, Crypto.sha256(msg), bytesMsg);
		
		System.out.println("The message after signing was: "+bytesMsg.toString()+" and was it verified? "+verified);
		
	}

	public static String toBitString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
        {
            sb.append(String.format("%02X", b[i] & 0xFF));
        }
        return sb.toString();
    }
}
