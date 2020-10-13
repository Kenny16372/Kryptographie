package encryption;

import general.ComponentLoader;

import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class RSA {
    URLClassLoader classLoader;

    public RSA() {
        classLoader = ComponentLoader.getClassLoader("rsa");
    }

    public Map<String, Map<Character, String>> generateKeyPair() {
        // wrapper for default key length
        return this.generateKeyPair(48);
    }

    public Map<String, Map<Character, String>> generateKeyPair(int keyLength) {
        // schema of map: ("privateKey"/"publicKey" -> ('e'/'n' -> value))
        Map<String, Map<Character, String>> map = new HashMap<>();

        int counter = 1;
        boolean done = false;
        while (!done) {
            try {
                // get class: RSA
                Class<?> Rsa = Class.forName("RSA", true, classLoader);

                // create a new RSA object
                Object rsa = null;
                try {
                    rsa = Rsa.getDeclaredConstructor(int.class).newInstance(keyLength);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    counter++;
                    continue;
                }

                // get public and private keys
                Object publicKey = Rsa.getMethod("getPublicKey").invoke(rsa);
                Object privateKey = Rsa.getMethod("getPrivateKey").invoke(rsa);

                // insert keys into map
                map.put("publicKey", this.getKeyComponents(publicKey));
                map.put("privateKey", this.getKeyComponents(privateKey));

                done = true;
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
            }
        }

        System.out.println("BigInteger worked on the " + counter + ". try!");

        return map;
    }

    private Map<Character, String> getKeyComponents(Object key) {
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

}
