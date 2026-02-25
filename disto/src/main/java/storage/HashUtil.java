package storage;
import exceptions.HashingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String hashSHA256(byte[] chunk) throws HashingException {
        MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("SHA-256"); } 
        catch (NoSuchAlgorithmException e) {
            throw new HashingException("Error while instancing the SHA-256 algorithm", e);
        }
        
        // Perform the hash computation
        byte[] encodedhash = digest.digest(chunk);

        // Convert byte array into a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedhash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
