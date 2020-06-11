package configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class CipherFactory {
    public Object getCipher(Class<?> clazz, String algorithm, File keyFile) {
        if (clazz == null || algorithm == null) {
            return null;
        }
        try {
            switch (algorithm.toLowerCase()) {
                case "rsa":
                case "shift":
                    return clazz.getDeclaredConstructor(boolean.class).newInstance(Configuration.instance.debugMode);
                case "rsa_cracker":
                    Map<Character, BigInteger> map = this.loadKey(keyFile);
                    return clazz.getDeclaredConstructor(boolean.class, BigInteger.class, BigInteger.class)
                            .newInstance(Configuration.instance.debugMode, map.get('e'), map.get('n'));
                case "shift_cracker":
                    return clazz.getDeclaredConstructor().newInstance();
                default:
                    throw new RuntimeException("No such algorithm: " + algorithm);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getCipher(Class<?> clazz, String algorithm) {
        return this.getCipher(clazz, algorithm, null);
    }

    private Map<Character, BigInteger> loadKey(File keyFile) {
        Map<Character, BigInteger> map = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(keyFile))) {

            String filecontent = br.readLine();

            filecontent = filecontent.substring(1, filecontent.length() - 1);

            String[] parts = filecontent.split(",");

            for (String part : parts) {
                // name of key part at index 0; value at index 1
                String[] mapping = part.split(":");

                // save it to the correct variable
                if (mapping[0].contains("e")) {
                    map.put('e', new BigInteger(mapping[1]));
                } else if (mapping[0].contains("n")) {
                    map.put('n', new BigInteger(mapping[1]));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return map;
    }
}
