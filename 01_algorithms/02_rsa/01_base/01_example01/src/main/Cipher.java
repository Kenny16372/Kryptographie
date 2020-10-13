import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;

public class Cipher {
    private boolean debug;

    public Cipher(boolean debug) {
        this.debug = debug;
    }

    private BigInteger crypt(BigInteger message, Key key) {
        return message.modPow(key.getE(), key.getN());
    }

    public String encrypt(String plainMessage, File keyFile) {
        Key key = loadKey(keyFile);

        if (debug) {
            System.out.println("Encrypting message: " + plainMessage);
        }

        byte[] bytes = plainMessage.getBytes();
        BigInteger crypt = crypt(new BigInteger(bytes), key);
        String cipherText = Base64.getEncoder().encodeToString(crypt.toByteArray());

        if (debug) {
            System.out.println("Ciphertext: " + cipherText);
        }

        return cipherText;
    }

    public String decrypt(String cipher, File keyFile) {
        Key key = loadKey(keyFile);

        if (debug) {
            System.out.println("Decrypting message: " + cipher);
        }

        byte[] value = Base64.getDecoder().decode(cipher);

        BigInteger bigInteger = new BigInteger(value);
        System.out.println(bigInteger);

        String res = crypt(bigInteger, key).toString();

        System.out.println(res);

        String msg = new String(crypt(new BigInteger(Base64.getDecoder().decode(cipher)), key).toByteArray());

        if (debug) {
            System.out.println("Plaintext: " + msg);
        }

        return msg;
    }

    private Key loadKey(File keyFile) {
        BigInteger e = null;
        BigInteger n = null;

        try (BufferedReader br = new BufferedReader(new FileReader(keyFile))) {

            String filecontent = br.readLine();

            filecontent = filecontent.substring(1, filecontent.length() - 1);

            String[] parts = filecontent.split(",");

            for (String part : parts) {
                // name of key part at index 0; value at index 1
                String[] mapping = part.split(":");

                // save it to the correct variable
                if (mapping[0].contains("e")) {
                    e = new BigInteger(mapping[1]);
                } else if (mapping[0].contains("n")) {
                    n = new BigInteger(mapping[1]);
                }
            }

            if (e == null || n == null) {
                return null;
            }

            if (debug) {
                System.out.println("Loaded key \"" + keyFile.getName() + "\":");
                System.out.println("\te: " + e.toString());
                System.out.println("\tn: " + n.toString());
            }

            return new Key(n, e);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}