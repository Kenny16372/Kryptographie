import java.math.BigInteger;

public class Application {
    public static void main(String... args) {
        BigInteger e = BigInteger.valueOf(12371);
        BigInteger n = new BigInteger("517815623413379");

        BigInteger cipher = new BigInteger("127881381553746");

        RSACracker rsaCracker = new RSACracker(e, n, cipher);

        try {
            BigInteger plainMessage = rsaCracker.execute();
            System.out.println("plainMessage : " + plainMessage);
        } catch (RSACrackerException rsae) {
            System.out.println(rsae.getMessage());
        }
    }
}