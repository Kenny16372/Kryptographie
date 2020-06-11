import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class Application {
    public static void main(String... args) {
        BigInteger e = BigInteger.valueOf(12371);
        BigInteger n = new BigInteger("517815623413379");

        BigInteger cipher = new BigInteger("127881381553746");

        RSACracker rsaCracker = new RSACracker(e, n, cipher);

        try {
            BigInteger plainMessage = rsaCracker.execute(true);
            System.out.println("plainMessage : " + new String(cipher.toByteArray(), StandardCharsets.UTF_8));
        } catch (RSACrackerException rsae) {
            System.out.println(rsae.getMessage());
        }
    }
}