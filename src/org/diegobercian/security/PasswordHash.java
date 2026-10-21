package org.diegobercian.security;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHash {
    private static final int ITERATIONS=600000;
    private static final SecureRandom RANDOM=new SecureRandom();
    private PasswordHash() { }
    public static String hash(char[] password) throws Exception {
        byte[] salt=new byte[16];RANDOM.nextBytes(salt);
        return "pbkdf2-sha256$"+ITERATIONS+"$"+Base64.getEncoder().encodeToString(salt)+"$"+Base64.getEncoder().encodeToString(derive(password,salt,ITERATIONS));
    }
    public static boolean verify(char[] password,String encoded) throws Exception {
        if(encoded==null || !encoded.startsWith("pbkdf2-sha256$")) return false;
        try {
            String[] parts=encoded.split("\\$");
            if(parts.length!=4) return false;
            int rounds=Integer.parseInt(parts[1]);
            if(rounds<600000 || rounds>2000000) return false;
            byte[] salt=Base64.getDecoder().decode(parts[2]), expected=Base64.getDecoder().decode(parts[3]);
            return salt.length==16 && expected.length==32 && MessageDigest.isEqual(expected,derive(password,salt,rounds));
        } catch (IllegalArgumentException e) {return false;}
    }
    private static byte[] derive(char[] password,byte[] salt,int rounds) throws Exception {
        PBEKeySpec spec=new PBEKeySpec(password,salt,rounds,256);
        try {return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();}
        finally {spec.clearPassword();}
    }
}
