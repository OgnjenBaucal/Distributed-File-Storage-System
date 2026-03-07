package storage;

import exceptions.EncryptionException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

public class AESUtil {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int AUTH_TAG_SIZE = 128;
    private static final int IV_SIZE = 12;

    public static SecretKey generateKey() throws EncryptionException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(KEY_SIZE);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Error while instancing the AES key generator algorithm", e);
        }
    }
    public static String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
    public static SecretKey stringToKey(String key) {
        return new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
    }
    
    public static GCMParameterSpec generateIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(AUTH_TAG_SIZE, iv);
    }
    public static String ivToString(GCMParameterSpec iv) {
        return Base64.getEncoder().encodeToString(iv.getIV());
    }
    public static GCMParameterSpec stringToIv(String iv) {
        return new GCMParameterSpec(AUTH_TAG_SIZE, Base64.getDecoder().decode(iv));
    }

    public static List<byte[]> encryptChunks(List<byte[]> chunks, SecretKey key, List<GCMParameterSpec> ivs) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            List<byte[]> encryptedChunks = new ArrayList<>();
            for (int i = 0; i < chunks.size(); i++) {
                cipher.init(Cipher.ENCRYPT_MODE, key, ivs.get(i));
                encryptedChunks.add(cipher.doFinal(chunks.get(i)));
            }
            return encryptedChunks;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            //throw new EncryptionException("Error while instancing the AES algorithm", e);
            throw new EncryptionException(e.getMessage(), e);
        } catch (Exception e) {
            //throw new EncryptionException("Error while encrypting file.", e);
            throw new EncryptionException(e.getMessage(), e);
        }
    }
    public static List<byte[]> decryptChunks(List<byte[]> chunks, SecretKey key, List<GCMParameterSpec> ivs) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            List<byte[]> decryptedChunks = new ArrayList<>();
            for (int i = 0; i < chunks.size(); i++) {
                cipher.init(Cipher.DECRYPT_MODE, key, ivs.get(i));
                decryptedChunks.add(cipher.doFinal(chunks.get(i)));
            }
            return decryptedChunks;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new EncryptionException("Error while instancing the AES algorithm", e);
        } catch (Exception e) {
            throw new EncryptionException("Error while decrypting file.", e);
        }
    }
}
