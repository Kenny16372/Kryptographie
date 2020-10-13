import java.math.BigInteger;
import java.util.Base64;

public class Cracker {
    private BigInteger e;
    private BigInteger n;
    private boolean debug;

    public Cracker(boolean debug, BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;
        this.debug = debug;
    }

    public String decrypt(String encrypted) {

        if (debug) {
            System.out.println("Ciphertext: " + encrypted);
            System.out.println("Received keys: ");
            System.out.println("\te: " + e);
            System.out.println("\tn: " + n);
        }

        BigInteger encryptedBigInt = new BigInteger(Base64.getDecoder().decode(encrypted));

        RSACracker rsaCracker = new RSACracker(e, n, encryptedBigInt);
        try {
            BigInteger bint = rsaCracker.execute(debug);

            String result = new String(bint.toByteArray());

            if (debug) {
                System.out.println("Decrypted message: " + result);
            }

            return result;
        } catch (RSACrackerException ex) {
            ex.printStackTrace();
        } catch (InterruptedException interruptedException) {
            if(debug){
                System.out.println("Time limit for cracking reached. Aborting");
            }
            return null;
        }
        return null;
    }
}
