import java.io.*;
import java.nio.charset.StandardCharsets;

public class Application {
    public static void main(String... args) {
        try {
            int key = 5;

            String text = "";

            CaesarCipher caesarCipher = new CaesarCipher(key);

            // encryption
            BufferedReader inputDataFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(Configuration.instance.inputDataFile)));
            BufferedWriter encryptedDataFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Configuration.instance.encryptedDataFile), StandardCharsets.UTF_8));
            while ((text = inputDataFileReader.readLine()) != null) {
                System.out.println("plainText       : " + text);
                String string = caesarCipher.encrypt(text);
                System.out.println("encryptedString : " + string);
                encryptedDataFileWriter.write(string + "\n");
                encryptedDataFileWriter.flush();
            }

            // decryption
            BufferedReader encryptedDataFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(Configuration.instance.encryptedDataFile), StandardCharsets.UTF_8));
            while ((text = encryptedDataFileReader.readLine()) != null) {
                System.out.println("decryptedString : " + caesarCipher.decrypt(text));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
