package parser;

import configuration.Algorithms;
import javafx.scene.control.TextArea;

import java.lang.reflect.InvocationTargetException;

public class EncryptionHandler implements IHandler {
    private TextArea output;

    public void handle(String command, TextArea output){
        this.output = output;

        boolean encrypt;    // true: encryption; false: decryption

        // get information, if text is to be encrypted or decrypted
        if(command.startsWith("encrypt")){
            encrypt = true;
        } else {            // doesn't need to be checked for "decrypt" again; this already happened in InputHandler
            encrypt = false;
        }
        command = command.substring("encrypt".length());

        // check for keyword "message"
        command = command.stripLeading();
        if(!command.startsWith("message")){
            System.out.println(command);
            throw new RuntimeException("Expected \"message\"");
        }
        command = command.substring("message".length()).stripLeading();

        // get message
        // starting at 1 because of leading "
        int endOfMessage = command.indexOf("\"", 1);
        if(endOfMessage == -1){
            throw new RuntimeException("Please use quotation marks around the message");
        }
        String message = command.substring(1, endOfMessage);

        // remove message from command
        command = command.substring(endOfMessage + 1);
        command = command.stripLeading();

        // check if next word is "using"
        if(!command.substring(0, command.indexOf(" ")).equals("using")){
            throw new RuntimeException("Expected \"using\"");
        }
        command = command.substring("using".length()).stripLeading();

        // get algorithm
        int endOfAlgorithm = command.indexOf(" ");
        String algorithm = command.substring(0, endOfAlgorithm);
        command = command.substring(endOfAlgorithm).stripLeading();
        System.out.println(algorithm);

        // get name of keyfile
        if(!command.substring(0, "and keyfile".length()).equals("and keyfile")){
            System.out.println(command);
            throw new RuntimeException("Expected \"and keyfile\"");
        }
        String keyfile = command.substring("and keyfile".length()).stripLeading();

        // see above for meaning of encrypt
        String result;
        if(encrypt){
            result = this.encrypt(message, algorithm, keyfile);
        } else{
            result = this.decrypt(message, algorithm, keyfile);
        }

        // display text
        this.output.clear();
        this.output.setText(result);
    }

    private String encrypt(String message, String algorithm, String keyfile){
        // get correct class depending on algorithm
        Algorithms algorithms = new Algorithms();
        Class<?> Encryption = algorithms.getAlgorithm(algorithm);
        try {
            // create object Encryption
            Object encryption = Encryption.getDeclaredConstructor().newInstance();

            // return encrypted String
            return (String) Encryption.getMethod("encrypt", String.class, String.class).invoke(encryption, message, keyfile);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
        return "Error";
    }

    private String decrypt(String message, String algorithm, String keyfile){
        // get correct class depending on algorithm
        Algorithms algorithms = new Algorithms();
        Class<?> Encryption = algorithms.getAlgorithm(algorithm);
        try {
            // create object Encryption
            Object encryption = Encryption.getDeclaredConstructor().newInstance();

            // return decrypted String
            return (String) Encryption.getMethod("decrypt", String.class, String.class).invoke(encryption, message, keyfile);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
        return "Error";
    }
}
