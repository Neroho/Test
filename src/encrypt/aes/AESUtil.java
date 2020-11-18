package encrypt.aes;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class AESUtil {
    public enum CipherMode {
        CBC("CBC"),
        ECB("ECB"),
        CTR("CTR"),
        OCB("OCB"),
        CFB("CFB");
        private String code;
        private CipherMode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
    public enum PaddingMode {
        Nopadding("Nopadding"),
        PKCS5Padding("PKCS5Padding"),
        ISO10126Padding("ISO10126Padding");
        //ZeroPadding("ZeroPadding");//java 不支持ZeroPadding
        private String padding;
        private PaddingMode(String padding) {
            this.padding = padding;
        }
        public String getPadding() {
            return padding;
        }
    }
    private static final String	UTF_8	= "UTF-8";
    private static final String alogrithm = "AES";
    private CipherMode cipherMode;
    private PaddingMode paddingMode;
    private SecretKeySpec skeySpec;
    private IvParameterSpec ivParamSpec;
    private Cipher cipher;
    private String AES_CIPHER_PADDING_MODE;
    private boolean simple;


    public AESUtil(CipherMode cipherMode, PaddingMode paddingMode, String key, String iv, boolean simple)
            throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        super();
        if (simple) {
            this.simple = simple;
            KeyGenerator keyGenerator = KeyGenerator.getInstance(alogrithm, new BouncyCastleProvider());
            keyGenerator.init(128, new SecureRandom(key.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] raw = secretKey.getEncoded();
            skeySpec = new SecretKeySpec(raw, alogrithm);
            cipher = Cipher.getInstance(alogrithm, new BouncyCastleProvider());
        } else {
            if (cipherMode == null || paddingMode == null) {
                throw new UnsupportedOperationException("CipherMode and PaddingMode must not be null");
            }
            this.cipherMode = cipherMode;
            this.paddingMode = paddingMode;

            byte[] raw = hexStringToBytes(getMD5Str(key));
            skeySpec = new SecretKeySpec(raw, alogrithm);
            AES_CIPHER_PADDING_MODE = alogrithm + "/" + cipherMode.getCode() + "/" + paddingMode.getPadding();
            cipher = Cipher.getInstance(AES_CIPHER_PADDING_MODE, new BouncyCastleProvider());
            if (this.cipherMode.equals(CipherMode.ECB)) {

            } else {
                ivParamSpec = new IvParameterSpec(hexStringToBytes(getMD5Str(iv)));
            }
        }

    }

    public AESUtil(CipherMode cipherMode, PaddingMode paddingMode, String key, String iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        this(cipherMode, paddingMode, key, iv, false);
    }

    public String encrypt(String source) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        if (simple) {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        } else {
            if (this.cipherMode.equals(CipherMode.ECB)) {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParamSpec);
            }

            if (this.paddingMode.equals(PaddingMode.Nopadding)) {
                int len = source.getBytes(UTF_8).length;
                int m = len % 16;
                if (m != 0) {
                    for (int i = 0; i < 16 - m; i++) {
                        source += " ";
                    }
                }
            }
        }
        byte[] encrypted = cipher.doFinal(source.getBytes(UTF_8));
        return bytesToHexString(encrypted);
    }

    public String decrypt(String source) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        if (simple || this.cipherMode.equals(CipherMode.ECB)) {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParamSpec);
        }

        byte[] encrypted1 = hexStringToBytes(source);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, UTF_8);
    }

    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static String bytesToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String getMD5Str(String strIn) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(strIn.getBytes(UTF_8));
        byte[] byteArray = messageDigest.digest();
        return bytesToHexString(byteArray);
    }

    public static String getAlogrithm() {
        return alogrithm;
    }
    public CipherMode getCipherMode() {
        return cipherMode;
    }
    public PaddingMode getPaddingMode() {
        return paddingMode;
    }

}