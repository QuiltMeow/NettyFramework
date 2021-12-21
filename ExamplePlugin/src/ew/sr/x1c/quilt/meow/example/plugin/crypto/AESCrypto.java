package ew.sr.x1c.quilt.meow.example.plugin.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto {

    private static final SecretKeySpec KEY = new SecretKeySpec(new byte[]{
        0x08, 0x1C, 0x01, 0x3C, 0x1B, 0x41, 0x6B, 0x61,
        0x74, 0x73, 0x75, 0x6B, 0x69, 0x4A, 0x69, 0x61
    }, "AES");

    private static final IvParameterSpec IV = new IvParameterSpec(new byte[]{
        0x6E, 0x65, 0x6B, 0x6F, 0x73, 0x6D, 0x61, 0x6C,
        0x6C, 0x72, 0x75, 0x38, 0x30, 0x39, 0x32, 0x33
    });

    public static byte[] CBCEncrypt(byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, KEY, IV);
        return cipher.doFinal(message);
    }

    public static byte[] CBCDecrypt(byte[] encrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, KEY, IV);
        return cipher.doFinal(encrypt);
    }
}
