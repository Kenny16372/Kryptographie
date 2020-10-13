import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Application {
    public static void main(String... args) {
        BigInteger e = new BigInteger("2464623548368437333");
        BigInteger n = new BigInteger("7393870650555390541");

        BigInteger cipher = new BigInteger("211092863");

        Cracker cracker = new Cracker(true, e, n);

        //BigInteger plainBigInt = cracker.execute(true);

        String plainMessage = cracker.decrypt("DJUFfw==");

        System.out.println("plainMessage : " + plainMessage);
    }
}