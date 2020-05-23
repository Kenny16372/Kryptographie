import java.math.BigInteger;

public class Test {
    public static void main(String... args){
        Cracker cracker = new Cracker(true, BigInteger.valueOf(1608930529), BigInteger.valueOf(1609012117));

        System.out.println(cracker.decrypt("SEb7JA=="));
    }
}
