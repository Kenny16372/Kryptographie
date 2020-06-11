package encryption;

import configuration.CipherFactory;
import configuration.Configuration;
import general.ComponentLoader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class Encryption {

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
            Object cipher = factory.getCipher(Cipher, algorithm);

            // get keyfile
            File keyFile = new File(Configuration.instance.keyFileDirectory + keyFileName);

            // decrypt ciphertext
            return (String) Cipher.getMethod((encrypt ? "en" : "de") + "crypt", String.class, File.class).invoke(cipher, cipherText, keyFile);

        } catch (IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }

        return null;
    }
}
