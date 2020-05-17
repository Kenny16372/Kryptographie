package encryption;

import configuration.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSA implements IEncryptionAlgorithm{
    URLClassLoader classLoader;

    public RSA(){
        try {
            // load .jar file "RSA.jar"
            URL[] urls = new URL[]{new File(Configuration.instance.componentDirectory + "RSA.jar").toURI().toURL()};
            classLoader = new URLClassLoader(urls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Map<Character, String>> generateKeyPair(){
        // wrapper for default key length
        return this.generateKeyPair(48);
    }

    public Map<String, Map<Character, String>> generateKeyPair(int keyLength){
        // schema of map: (privateKey/publicKey -> (e/n -> value))
        Map<String, Map<Character, String>> map = new HashMap<>();
        try {
            // get class: RSA
            Class<?> Rsa = Class.forName("RSA", true, classLoader);

            // create a new RSA object
            Object rsa = Rsa.getDeclaredConstructor(int.class).newInstance(keyLength);

            // get public and private keys
            Object publicKey = Rsa.getMethod("getPublicKey").invoke(rsa);
            Object privateKey = Rsa.getMethod("getPrivateKey").invoke(rsa);

            // insert keys into map
            map.put("publicKey", this.getKeyComponents(publicKey));
            map.put("privateKey", this.getKeyComponents(privateKey));

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
        return map;
    }

    public String encrypt(String cleartext, String keyFileName){
        try {
            // get classes: Cipher, Key
            Class<?> Cipher = Class.forName("Cipher", true, classLoader);
            Class<?> Key = Class.forName("Key", true, classLoader);

            // create instance of Cipher
            Object cipher = Cipher.getDeclaredConstructor().newInstance();

            // encrypt cleartext; convert to Base64 for 1. displaying 2. safe network transmission
            return Base64.getEncoder().encodeToString((byte[]) Cipher.getMethod("encrypt", String.class, Key).invoke(cipher, cleartext, keyFromFile(keyFileName)));

        } catch (IllegalAccessException| NoSuchMethodException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }

        return null;
    }

    public String decrypt(String ciphertext, String keyFileName) {
        try {
            // get classes: Cipher, Key
            Class<?> Cipher = Class.forName("Cipher", true, classLoader);
            Class<?> Key = Class.forName("Key", true, classLoader);

            // create instance of Cipher
            Object cipher = Cipher.getDeclaredConstructor().newInstance();

            // turn Base64-encoded ciphertext back to byte[]
            byte[] content = Base64.getDecoder().decode(ciphertext);

            // decrypt ciphertext
            return (String) Cipher.getMethod("decrypt", byte[].class, Key).invoke(cipher, content, keyFromFile(keyFileName));

        } catch (IllegalAccessException | NoSuchMethodException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }

        return null;
    }

    private Map<Character, String> getKeyComponents(Object key){
        // schema: (n/e -> value)
        Map<Character, String> map = new HashMap<>();
        try {
            // get class: Key
            Class<?> Key = Class.forName("Key", true, classLoader);

            // store values for n and e in the map
            map.put('n', String.valueOf(Key.getMethod("getN").invoke(key)));
            map.put('e', String.valueOf(Key.getMethod("getE").invoke(key)));

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
        return map;
    }

    private Object keyFromFile(String filename){
        // open file to read from
        try(BufferedReader br = new BufferedReader(new FileReader(Configuration.instance.keyFileDirectory + filename))) {
            // get class: Key
            Class<?> Key = Class.forName("Key", true, classLoader);

            // load n and e from file; automatic conversion from Strint to BigInteger
            BigInteger n = new BigInteger(br.readLine());
            BigInteger e = new BigInteger(br.readLine());

            // return Key object
            return Key.getDeclaredConstructor(BigInteger.class, BigInteger.class).newInstance(n, e);

        } catch (IOException | NoSuchMethodException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
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
