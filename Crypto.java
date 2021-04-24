import java.math.BigInteger; 
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;

//Have test this class by signing any string??

public class Crypto{
	
	
	public static String sha256(String input){  //SHA is not encryption algorithm
		
		byte[] hashInBytes = new byte[32];
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		hashInBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
		
		BigInteger number = new BigInteger(1, hashInBytes);
		
		StringBuilder hashInHex = new StringBuilder(number.toString(16));
		
		while(hashInHex.length() < 32) hashInHex.insert(0, '0');
		
		return hashInHex.toString();
		
	}
	
	public static void generateKeyPair(int keySize, PrivateKey privateKey, PublicKey publicKey){
		//create a key pair generator object
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
		
		//initialize the key pair generator object
		keyPairGen.initialize(keySize);
		
		//generate the key pair
		KeyPair pair = keyPairGen.generateKeyPair();
		
		privateKey = pair.getPrivate();
		publicKey = pair.getPublic();
	
	}
	
	public static byte[] applyDSASign(PrivateKey privateKey, String input){
		Signature signature = Signature.getInstance("SHA256withDSA");
		signature.initSign(privateKey);
		
		byte[] bytes = input.getBytes();
		
		//Add data to the signature
		signature.update(bytes);
		
		//calculate the signature
		byte[] signatureResult = signature.sign();
		
		return signatureResult;
	
	}
	
	public static boolean verifyDSASign(PublicKey publicKey, String message, byte[] messageSignature){
		Signature signature = Signature.getInstance("SHA256withDSA");
		//initialize the signature
		signature.initVerify(publicKey);
		signature.update(message.getBytes());
		
		//verify the signature
		return signature.verify(messageSignature);
	}

}
