import java.util.Scanner;

public class Cipher {
    private boolean debug;

    public Cipher(boolean debug) {
        this.debug = debug;
    }

    public String encrypt(String plainText, File keyFile) {
        int key = getKey(keyFile);

        StringBuilder stringBuilder = new StringBuilder();

        if (debug) {
            System.out.println("Encrypting message: " + plainText);
        }

        for (int i = 0; i < plainText.length(); i++) {
            int charCode = plainText.codePointAt(i) + key;

            charCode = charCode % (int) 'z';

            if (charCode < (int) 'A') {
                charCode = charCode + ((int) 'A') - 1;
            }

            char character = (char) (charCode);
            stringBuilder.append(character);
        }

        if (debug) {
            System.out.println("Ciphertext: " + stringBuilder.toString());
        }

        return stringBuilder.toString();
    }

    private int getKey(File keyFile) {
        int key = 0;
        try {
            Scanner scanner = new Scanner(keyFile);

            String keyString = scanner.findInLine("\\d+");
            key = Integer.parseInt(keyString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (debug) {
            System.out.println("Read key from file \"" + keyFile.getName() + "\": " + key);
        }

        return key;
    }

    public String decrypt(String cipherText, File keyFile) {
        int key = getKey(keyFile);

        StringBuilder stringBuilder = new StringBuilder();

        if (debug) {
            System.out.println("Decrypting message: " + cipherText);
        }

        for (int i = 0; i < cipherText.length(); i++) {
            int charCode = cipherText.codePointAt(i) - key;

            if (charCode < (int) 'A') {
                charCode -= 'A';
                charCode += ((int) 'z') + 1;
            }

            char character = (char) (charCode);
            stringBuilder.append(character);
        }

        if (debug) {
            System.out.println("Plaintext: " + stringBuilder.toString());
        }

        return stringBuilder.toString();
    }

}
