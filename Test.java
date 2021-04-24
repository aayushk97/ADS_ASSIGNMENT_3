import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
  
public class Test {
  
    // Initializing the final string variable
    private static final String SHA2_ALGORITHM = "SHA-256";
  
    // Creating a random salt value to prevent
    // attacks from the Rainbow table.
    // public static byte[] Creating_Random_Salt()
    // {
    //     byte[] salt = new byte[16];
    //     SecureRandom secure_random = new SecureRandom();
    //     secure_random.engineNextBytes(salt);
    //     return salt;
    // }
  
    // Creating hash value using input value
    // and salt using the SHA2 Algorithm.
    public static byte[] Creating_SHA2_Hash(String input, byte[] salt) throws Exception
    {
        ByteArrayOutputStream byte_Stream = new ByteArrayOutputStream();
  
        byte_Stream.write(salt);
        byte_Stream.write(input.getBytes());
        byte[] valueToHash = byte_Stream.toByteArray();
        MessageDigest messageDigest = MessageDigest.getInstance(SHA2_ALGORITHM);
        return messageDigest.digest(valueToHash);
    }
  
    public static void main(String args[])
        throws Exception
    {
  
        // Calling the function Creating_Random_Salt()
        // to generate a random salt value
        // byte[] salt = Creating_Random_Salt();
        // System.out.println("SALT_VALUE: " + DatatypeConverter.printHexBinary(salt));
        // String valueToHash = UUID.randomUUID().toString();
  
        // // Generating first hash with the salt
        // byte[] hash1 = Creating_SHA2_Hash(valueToHash, salt);
  
        // // Generating second hash with exact salt
        // // to check if we get the same hash.
        // byte[] hash2 = Creating_SHA2_Hash(valueToHash, salt);
  
        // // Print first and the second hash value
        // System.out.println("HASH1_VALUE: " + DatatypeConverter.printHexBinary(hash1));
        // System.out.println("HASH2_VALUE: "+ DatatypeConverter.printHexBinary(hash2));


        byte[] data1 = "0123456789".getBytes("UTF-8");
        byte[] data2 = "abcdefghijklmnopqrstuvxyz".getBytes("UTF-8");

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(data1);
        //messageDigest.update(data2);

        byte[] digest = messageDigest.digest();
        System.out.println(DatatypeConverter.printHexBinary(digest));
    }
}