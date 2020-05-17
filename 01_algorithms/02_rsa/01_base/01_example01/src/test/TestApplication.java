import org.junit.jupiter.api.Test;

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

        Cipher cipher = new Cipher();
        byte[] encryptedMessage = cipher.encrypt(plainMessage, rsa.getPublicKey());
        String decryptedMessage = cipher.decrypt(encryptedMessage, rsa.getPrivateKey());

        System.out.println("plainMessage      : " + plainMessage);
        System.out.println("encryptedMessage  : " + Base64.getEncoder().encodeToString(encryptedMessage));
        System.out.println("decryptedMessage  : " + decryptedMessage);

        assertEquals(plainMessage, decryptedMessage);

        encryptedMessage = cipher.encrypt(plainMessage, rsa.getPrivateKey());
        decryptedMessage = cipher.decrypt(encryptedMessage, rsa.getPublicKey());

        assertEquals(plainMessage, decryptedMessage);
    }
}