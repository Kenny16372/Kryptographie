import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RSACracker extends Thread{
    private final BigInteger e;
    private final BigInteger n;
    private final BigInteger cipher;

    public RSACracker(BigInteger e, BigInteger n, BigInteger cipher) {
        this.e = e;
        this.n = n;
        this.cipher = cipher;
    }

    public BigInteger execute(boolean debug) throws RSACrackerException, InterruptedException {
        BigInteger p, q, d;

        List<BigInteger> factorList = factorize(n);

        if (factorList.size() != 2) {
            throw new RSACrackerException("cannot determine factors p and q");
        }

        p = factorList.get(0);
        q = factorList.get(1);

        if (debug) {
            System.out.println("Found factors for n: " + p + " and " + q);
        }

        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        d = e.modInverse(phi);
        return cipher.modPow(d, n);
    }

    public List<BigInteger> factorize(BigInteger n) throws InterruptedException {
        BigInteger two = BigInteger.valueOf(2);
        List<BigInteger> factorList = new LinkedList<>();

        if (n.compareTo(two) < 0) {
            throw new IllegalArgumentException("must be greater than one");
        }
        while (n.mod(two).equals(BigInteger.ZERO)) {
            factorList.add(two);
            n = n.divide(two);
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            BigInteger factor = BigInteger.valueOf(3);
            while (factor.multiply(factor).compareTo(n) <= 0) {
                // Thread interrupted
                if(interrupted()){
                    throw new InterruptedException();
                }
                if (n.mod(factor).equals(BigInteger.ZERO)) {
                    factorList.add(factor);
                    n = n.divide(factor);
                } else {
                    factor = factor.add(two);
                }

            }
            factorList.add(n);
        }

        return factorList;
    }
}