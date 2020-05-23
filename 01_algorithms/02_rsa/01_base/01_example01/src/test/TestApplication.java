import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestApplication {
    @Test
    public void execute() {
        String plainMessage = "morpheus";
        RSA rsa = new RSA(48);

        System.out.println("p                 : " + rsa.getP());
        System.out.println("q                 : " + rsa.getQ());
        System.out.println("n                 : " + rsa.getN());
        System.out.println("t                 : " + rsa.getT());
        System.out.println("e                 : " + rsa.getE());
        System.out.println("d                 : " + rsa.getD());
        System.out.println("isCoPrime e and t : " + rsa.isCoPrime(rsa.getE(), rsa.getT()));

        assertTrue(rsa.isCoPrime(rsa.getE(), rsa.getT()));

        Cipher cipher = new Cipher(false);
        String encryptedMessage = cipher.encrypt(plainMessage, new File(System.getProperty("user.dir") + "/data/SYD_pub.txt"));
        String decryptedMessage = cipher.decrypt(encryptedMessage, new File(System.getProperty("user.dir") + "/data/SYD_pri.txt"));

        System.out.println("plainMessage      : " + plainMessage);
        System.out.println("encryptedMessage  : " + encryptedMessage);
        System.out.println("decryptedMessage  : " + decryptedMessage);

        assertEquals(plainMessage, decryptedMessage);

        encryptedMessage = cipher.encrypt(plainMessage, new File(System.getProperty("user.dir") + "/data/SYD_pri.txt"));
        decryptedMessage = cipher.decrypt(encryptedMessage, new File(System.getProperty("user.dir") + "/data/SYD_pub.txt"));

        assertEquals(plainMessage, decryptedMessage);
    }
}