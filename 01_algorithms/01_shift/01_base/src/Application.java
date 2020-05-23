import java.io.*;
import java.nio.charset.StandardCharsets;

public class Application {
    public static void main(String... args) {
        try {

            String text;

            Cipher cipher = new Cipher(true);

            // encryption
            BufferedReader inputDataFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(Configuration.instance.inputDataFile)));
            BufferedWriter encryptedDataFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Configuration.instance.encryptedDataFile), StandardCharsets.UTF_8));
            while ((text = inputDataFileReader.readLine()) != null) {
                System.out.println("plainText       : " + text);
                String string = cipher.encrypt(text, new File(Configuration.instance.dataDirectory + "key.txt"));
                System.out.println("encryptedString : " + string);
                encryptedDataFileWriter.write(string + "\n");
                encryptedDataFileWriter.flush();
            }

            // decryption
            BufferedReader encryptedDataFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(Configuration.instance.encryptedDataFile), StandardCharsets.UTF_8));
            while ((text = encryptedDataFileReader.readLine()) != null) {
                System.out.println("decryptedString : " + cipher.decrypt(text, new File(Configuration.instance.dataDirectory + "key.txt")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
