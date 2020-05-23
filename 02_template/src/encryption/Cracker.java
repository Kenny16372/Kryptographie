package encryption;

import configuration.CipherFactory;
import configuration.Configuration;
import general.ComponentLoader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class Cracker {
    public String decrypt(String cipherText, String algorithm, String keyFileName){
        algorithm = algorithm + "_cracker";

        try {
            // get class: Cipher
            Class<?> Cipher = Class.forName("Cracker", true, ComponentLoader.getClassLoader(algorithm));

            File keyFile = null;

            if(keyFileName != null){
                // get keyfile
                keyFile = new File(Configuration.instance.keyFileDirectory + keyFileName);
            }

            CipherFactory factory = new CipherFactory();
            Object cipher = factory.getCipher(Cipher, algorithm, keyFile);

            // decrypt ciphertext
            return (String) Cipher.getMethod("decrypt", String.class).invoke(cipher, cipherText);

        } catch (IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }

        return null;
    }
}
