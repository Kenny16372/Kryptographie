import java.io.File;
import java.math.BigInteger;
import java.util.Base64;

public class Application {
    public static void main(String... args) {
        String plainMessage = "abc";

        Cipher cipher = new Cipher(true);
        String encryptedMessage = cipher.encrypt(plainMessage, new File(System.getProperty("user.dir") + "/data/SYD_pub.txt"));

        byte[] real = Base64.getDecoder().decode(encryptedMessage);

        BigInteger bint = new BigInteger(real);

        System.out.println(bint);

        cipher.decrypt(encryptedMessage, new File(System.getProperty("user.dir") + "/data/SYD_pri.txt"));
    }
}