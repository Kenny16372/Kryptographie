package configuration;

import encryption.RSA;
import encryption.Shift;

import java.util.HashMap;
import java.util.Map;

public class Algorithms {
    private Map<String, Class<?>> algorithms = new HashMap<>();

    public Algorithms(){
        // Classes must implement IEncryptionAlgorithm
        algorithms.put("rsa", RSA.class);
        algorithms.put("shift", Shift.class);
    }

    public Class<?> getAlgorithm(String name){
        return algorithms.get(name.toLowerCase());
    }
}
