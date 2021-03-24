package cn.xzxy.lewy.netty.furtherimpl.assembly.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    public AES(String password) throws Throwable {
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
        encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    public AES(SecretKey key) throws Throwable {
        encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    public byte[] encrypt(byte[] content) throws Throwable {
        return encryptCipher.doFinal(content);
    }

    public byte[] decrypt(byte[] content) throws Throwable {
        return decryptCipher.doFinal(content);
    }

    public byte[] passwordBytes(String passwordStr) {
        int length = passwordStr.length();
        if (length % 2 == 0) {
            throw new IllegalArgumentException("password str length must be even");
        }
        int len = length / 2;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) Integer.parseInt(passwordStr.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

}
