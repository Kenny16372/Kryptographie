package encryption;

import configuration.CipherFactory;
import configuration.Configuration;
import general.ComponentLoader;
import network.Message;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Encryption {

    public String decrypt(Message message, String keyFile) {
        return decrypt(message.toString(), message.getAlgorithm(), keyFile);
    }

    public String encrypt(String cleartext, String algorithm, String keyFileName) {
        return handle(cleartext, algorithm, keyFileName, true);
    }

    public String decrypt(String ciphertext, String algorithm, String keyFileName) {
        return handle(ciphertext, algorithm, keyFileName, false);
    }

    private String handle(String cipherText, String algorithm, String keyFileName, boolean encrypt) {
        try {
            // get class: Cipher
            Class<?> Cipher = Class.forName("Cipher", true, ComponentLoader.getClassLoader(algorithm));

            // create instance of Cipher
            CipherFactory factory = new CipherFactory();
            List<Object> cipher = factory.getCipher(Cipher, algorithm);

            if (cipher == null) {
                return null;
            }

            // get keyfile
            File keyFile = new File(Configuration.instance.keyFileDirectory + keyFileName);

            if (!keyFile.exists()) {
                return null;
            }

            // decrypt ciphertext
            return (String) Cipher.getMethod((encrypt ? "en" : "de") + "crypt", String.class, File.class).invoke(cipher.get(0), cipherText, keyFile);

        } catch (IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }

        return null;
    }
}
