import java.math.BigInteger;
import java.security.SecureRandom;

public class Application {
    private final static BigInteger one = new BigInteger("1");
    private final static SecureRandom random = new SecureRandom();
    public BigInteger privateKey;
    public BigInteger publicKey;
    public BigInteger n;
    BigInteger phi;
    BigInteger p;
    BigInteger q;

    public Application(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;

        phi = (p.subtract(one)).multiply(q.subtract(one)); // phi = (p - 1)*(q - 1)
        n = p.multiply(q);                                     // n = p * q

        publicKey = new BigInteger("65537");                  // e = common prime = 2^16 + 1
        privateKey = publicKey.modInverse(phi);                // d = (publicKey^-1) * mod(phi)
    }

    public Application(int bits) {
        p = BigInteger.probablePrime(bits, random);            // generating a big prime number (bits)
        q = BigInteger.probablePrime(bits, random);            // generating a big prime number (bits)
        phi = (p.subtract(one)).multiply(q.subtract(one)); // phi = (p - 1) * (q - 1)

        n = p.multiply(q);                                     // n = p * q
        publicKey = new BigInteger("65537");                  // e = common prime = 2^16 + 1
        privateKey = publicKey.modInverse(phi);                // d = (publicKey^-1) * mod(phi)
    }

    public static void main(String... args) {
        int bits = 32;
        Application application = new Application(bits);

        String plainText = "hello";
        BigInteger plainTextNumber = Utility.stringToNumber(plainText);

        System.out.println();

        System.out.println("p                        : " + application.p);
        System.out.println("q                        : " + application.q);
        System.out.println("n = p * q                : " + application.n);
        System.out.println("phi = (p - 1) * (q - 1)  : " + application.phi);
        System.out.println("publicKey                : " + application.publicKey + " (most common prime 2^16 + 1)");
        System.out.println("privateKey               : " + application.privateKey);

        System.out.println();

        System.out.println("plainText                : " + plainText);
        System.out.println("plainText (number)       : " + plainTextNumber);

        BigInteger encryptedMessage = application.encrypt(plainTextNumber);
        BigInteger decryptedMessage = application.decrypt(encryptedMessage);

        System.out.println("encryptedMessage         : " + encryptedMessage);
        System.out.println("decryptedMessage         : " + decryptedMessage);

        String result = Utility.numberToString(decryptedMessage);
        System.out.println("result                   : " + result);
    }

    public BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, n);                   // c = (message^publicKey) * mod n
    }

    public BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, n);                // c = (encrypted^privateKey) * mod m
    }
}