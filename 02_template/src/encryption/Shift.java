package encryption;

import configuration.Configuration;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Shift implements IEncryptionAlgorithm{
    public URLClassLoader classLoader;

    public Shift(){
        try {
            // load jar: Shift.jar
            URL[] urls = {new File(Configuration.instance.componentDirectory + "Shift.jar").toURI().toURL()};
            classLoader = new URLClassLoader(urls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String cleartext, String keyFileName){
        // read keyfile to get key for encryption
        try(BufferedReader br = new BufferedReader(new FileReader(Configuration.instance.keyFileDirectory + keyFileName))) {
            // get class: CaesarCipher
            Class<?> Cipher = Class.forName("CaesarCipher", true, classLoader);

            // create new CaesarCipher object with key as parameter
            Object cipher = Cipher.getDeclaredConstructor(int.class).newInstance(Integer.parseInt(br.readLine()));

            // return encrypted message
            return (String) Cipher.getMethod("encrypt", String.class).invoke(cipher, cleartext);
        } catch (IOException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }

        return null;
    }

    public String decrypt(String ciphertext, String keyFileName){
        // read keyfile to get key for decryption
        try(BufferedReader br = new BufferedReader(new FileReader(Configuration.instance.keyFileDirectory + keyFileName))) {
            // get class: CaesarCipher
            Class<?> Cipher = Class.forName("CaesarCipher", true, classLoader);

            // create new CaesarCipher object with key as parameter
            Object cipher = Cipher.getDeclaredConstructor(int.class).newInstance(Integer.parseInt(br.readLine()));

            // return decrypted message
            return (String) Cipher.getMethod("decrypt", String.class).invoke(cipher, ciphertext);
        } catch (IOException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }

        return null;
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }
}
