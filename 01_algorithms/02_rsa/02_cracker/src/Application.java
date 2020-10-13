import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Application {
    public static void main(String... args) {
        BigInteger e = BigInteger.valueOf(1998280363);
        BigInteger n = BigInteger.valueOf(1998369953);

        BigInteger cipher = new BigInteger("211092863");

        Cracker cracker = new Cracker(true, e, n);

        //BigInteger plainBigInt = cracker.execute(true);

        String plainMessage = cracker.decrypt("DJUFfw==");

        System.out.println("plainMessage : " + plainMessage);
    }
}