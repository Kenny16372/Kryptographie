package encryption;

import configuration.CipherFactory;
import configuration.Configuration;
import general.ComponentLoader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Cracker {
    public String decrypt(String cipherText, String algorithm) {
        algorithm = algorithm.toLowerCase() + "_cracker";

        try {
            // get class: Cipher
            Class<?> Cipher = Class.forName("Cracker", true, ComponentLoader.getClassLoader(algorithm));
            Method decrypt = Cipher.getMethod("decrypt", String.class);

            CipherFactory factory = new CipherFactory();
            List<Object> ciphers = factory.getCipher(Cipher, algorithm);

            ExecutorService executor = Executors.newFixedThreadPool(ciphers.size());

            // decrypt ciphertext
            if(ciphers.size() == 0){
                System.out.println("ERROR\nCouldn't decrypt ciphertext");
                return null;
            }

            // create task runners
            List<Decryption> taskRunners = ciphers.stream().map(cipher -> new Decryption(decrypt, cipher, cipherText)).collect(Collectors.toList());

            // create futures
            List<FutureTask<String>> futures = taskRunners.stream().map(FutureTask::new).collect(Collectors.toList());

            // execute task runners
            for(FutureTask<String> futureTask: futures){
                executor.execute(futureTask);
            }

            // wait 30 seconds or until the task runners complete
            executor.shutdown();
            executor.awaitTermination(30L, TimeUnit.SECONDS);
            executor.shutdownNow();
            executor.awaitTermination(100L, TimeUnit.MILLISECONDS);

            // return the cracked passwords as comma separated string
            return futures.stream().filter(FutureTask::isDone).filter(future -> !future.isCancelled()).map(future -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.joining(", "));

        } catch (NoSuchMethodException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class Decryption implements Callable<String> {
        private final Method decrypt;
        private final Object cipher;
        private final String cipherText;

        public Decryption(Method decrypt, Object cipher, String cipherText) {
            this.decrypt = decrypt;
            this.cipher = cipher;
            this.cipherText = cipherText;
        }

        @Override
        public String call(){
            try {
                return (String) decrypt.invoke(cipher, cipherText);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
