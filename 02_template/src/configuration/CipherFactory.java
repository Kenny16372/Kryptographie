package configuration;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class CipherFactory {
    public List<Object> getCipher(Class<?> clazz, String algorithm) {
        if (clazz == null || algorithm == null) {
            return null;
        }

        List<Object> returnValue = new LinkedList<>();

        try {
            switch (algorithm.toLowerCase()) {
                case "rsa":
                case "shift":
                    returnValue.add(clazz.getDeclaredConstructor(boolean.class).newInstance(Configuration.instance.debugMode));
                    break;
                case "rsa_cracker":
                    // list of all possible keys
                    // since there is no way of knowing which keyfile was used to encrypt the message, we need to try all of them
                    List<Map<Character, BigInteger>> keys = this.loadKeys();

                    // the next lines instantiate the Objects with the key components ('e' and 'n') and if the debug mode is enabled
                    // returned is a list of Objects containing the Cracker instances
                    // these are inserted into the returnValue list
                    returnValue.addAll(keys.stream().map(map -> {
                        try {
                            return clazz.getDeclaredConstructor(boolean.class, BigInteger.class, BigInteger.class)
                                    .newInstance(Configuration.instance.debugMode, map.get('e'), map.get('n'));
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).filter(Objects::nonNull).collect(Collectors.toList()));
                    break;
                case "shift_cracker":
                    returnValue.add(clazz.getDeclaredConstructor().newInstance());    // TODO Debugmode
                    break;
                default:
                    System.out.println("No such algorithm: " + algorithm);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    private List<Map<Character, BigInteger>> loadKeys() {
        List<Map<Character, BigInteger>> keys = new ArrayList<>();

        File keyFileDir = new File(Configuration.instance.keyFileDirectory);

        FilenameFilter fileNameFilter = (dir, name) -> name.endsWith("_pub.txt");

        for(File keyFile: Objects.requireNonNull(keyFileDir.listFiles(fileNameFilter))) {
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

            keys.add(map);
        }

        return keys;
    }
}
