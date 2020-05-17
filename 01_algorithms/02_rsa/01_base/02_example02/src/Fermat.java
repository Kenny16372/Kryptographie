import java.math.BigInteger;

public class Fermat {
    // p         : 4062302197
    // q         : 3779177683
    // n = p * q : 15352161804504269551
    public static void main(String... args) {
        Fermat fermat = new Fermat();
        System.out.println("15352161804504269551 : " + fermat.factor(new BigInteger("15352161804504269551")));
    }

    public BigInteger factor(BigInteger n) {
        BigInteger a01 = BigMath.sqrt(n);
        BigInteger b01 = a01.multiply(a01).subtract(n);
        BigInteger b02 = BigMath.sqrt(b01);
        BigInteger a02 = a01.subtract(b02);

        BigInteger c = new BigInteger("30");
        BigInteger a03 = a01.subtract(b02).add(c);
        BigInteger result;

        while (!BigMath.sqrt(b01).pow(2).equals(b01) && a03.subtract(a02).compareTo(c) > -1) {
            a01 = a01.add(BigInteger.ONE);
            b01 = a01.multiply(a01).subtract(n);

            b02 = BigMath.sqrt(b01);
            a03 = a02;
            a02 = a01.subtract(b02);
        }

        if (BigMath.sqrt(b01).pow(2).equals(b01)) {
            result = a02;
        } else {
            boolean isSolved = false;
            BigInteger p = a02.add(BigMath.TWO);

            if (p.remainder(BigMath.TWO).intValue() == 0) {
                p = p.add(BigInteger.ONE);
            }

            while (!isSolved) {
                p = p.subtract(BigMath.TWO);
                if (n.remainder(p).equals(BigInteger.ZERO)) {
                    isSolved = true;
                }
            }

            result = p;
        }

        return result;
    }
}