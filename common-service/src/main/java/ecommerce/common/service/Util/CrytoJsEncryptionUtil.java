package ecommerce.common.service.Util;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Irfan Zulkefly
 */
public class CrytoJsEncryptionUtil {
    private static final Logger logger = LoggerFactory.getLogger(CrytoJsEncryptionUtil.class);

    public CrytoJsEncryptionUtil() {
    }

    public static String encryptCryptoJsAES(String toEncrypt, String secret) {
        try {
//            int keySize = true;
//            int ivSize = true;
            byte[] key = new byte[32];
            byte[] iv = new byte[16];
            byte[] saltBytes = generateSalt(8);
            evpKDF(secret.getBytes(StandardCharsets.UTF_8), 256, 128, saltBytes, key, iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] cipherBytes = cipher.doFinal(toEncrypt.getBytes(StandardCharsets.UTF_8));
            byte[] sBytes = "Salted__".getBytes(StandardCharsets.UTF_8);
            byte[] b = new byte[sBytes.length + saltBytes.length + cipherBytes.length];
            System.arraycopy(sBytes, 0, b, 0, sBytes.length);
            System.arraycopy(saltBytes, 0, b, sBytes.length, saltBytes.length);
            System.arraycopy(cipherBytes, 0, b, sBytes.length + saltBytes.length, cipherBytes.length);
            byte[] base64b = Base64.getEncoder().encode(b);
            return new String(base64b);
        } catch (Exception var12) {
            logger.warn("Error while encryption [{}]" + ExceptionUtils.getRootCauseMessage(var12));
            throw new IllegalStateException(var12);
        }
    }

    public static String decryptCryptoJsAES(String toDecrypt, String secret) {
        try {
//            int keySize = true;
//            int ivSize = true;
            byte[] ctBytes = Base64.getDecoder().decode(toDecrypt.getBytes(StandardCharsets.UTF_8));
            byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);
            byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);
            byte[] key = new byte[32];
            byte[] iv = new byte[16];
            evpKDF(secret.getBytes(StandardCharsets.UTF_8), 256, 128, saltBytes, key, iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);
            return new String(recoveredPlaintextBytes);
        } catch (Exception var11) {
            logger.warn("Error while encryption [{}]" + ExceptionUtils.getRootCauseMessage(var11));
            throw new IllegalStateException(var11);
        }
    }

    private static byte[] generateSalt(int length) {
        Random r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        return salt;
    }

    private static byte[] evpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        return evpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }

    private static byte[] evpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        keySize /= 32;
        ivSize /= 32;
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;

        for(MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm); numberOfDerivedWords < targetKeySize; numberOfDerivedWords += block.length / 4) {
            if (block != null) {
                hasher.update(block);
            }

            hasher.update(password);
            block = hasher.digest(salt);
            hasher.reset();

            for(int i = 1; i < iterations; ++i) {
                block = hasher.digest(block);
                hasher.reset();
            }

            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4, Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));
        }

        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);
        return derivedBytes;
    }

    static {
        Security.setProperty("crypto.policy", "unlimited");
    }
}