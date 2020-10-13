package parser;

import configuration.Configuration;
import encryption.Encryption;
import javafx.scene.control.TextArea;
import logging.Logger;

import java.util.Scanner;
import java.util.regex.MatchResult;

public class EncryptionHandler implements IHandler {

    public void handle(String command, TextArea output) {
        output.clear();

        Scanner scanner = new Scanner(command);
        scanner.findInLine("^\\s*(en|de)crypt\\s+(message)\\s+(\")([^\"]*)(\")\\s+(using)\\s+(\\S*)\\s+(and\\s+keyfile)\\s+(\\S*)\\s*$");

        MatchResult result = scanner.match();
        scanner.close();

        boolean encrypt = true;
        try {
            int pos = 1;

            // true: encryption; false: decryption; InputHandler assures that only commands starting with encrypt or decrypt are submitted
            encrypt = result.group(pos++).equals("en");

            if (!result.group(pos++).equals("message")) {
                throw new RuntimeException("Expected keyword \"message\"");
            }

            if (!result.group(pos++).equals("\"")) {
                throw new RuntimeException("Expected token (\")");
            }

            // get message
            String message = result.group(pos++);

            if (!result.group(pos++).equals("\"")) {
                throw new RuntimeException("Expected token (\")");
            }

            if (!result.group(pos++).equals("using")) {
                throw new RuntimeException("Expected keyword \"using\"");
            }

            // get algorithm
            String algorithm = result.group(pos++);

            if (!result.group(pos++).replaceAll("\\s+", "").equals("andkeyfile")) {
                throw new RuntimeException("Expected keyword \"and keyfile\"");
            }

            // get name of keyfile
            String keyfile = result.group(pos);

            // check if correct key was used
            if(encrypt){
                if(!keyfile.endsWith("pub.txt")){
                    output.appendText("Warning\nDid you mean to use a public key?");
                }
            } else{
                if(!keyfile.endsWith("pri.txt")){
                    output.appendText("Warning\nDid you mean to use a private key?");
                }
            }

            // logging
            Logger logger = null;
            if (Configuration.instance.debugMode) {
                logger = new Logger();
                logger.startLogging(encrypt, algorithm);
            }

            Encryption encryption = new Encryption();
            String returnedString;
            if (encrypt) {
                returnedString = encryption.encrypt(message, algorithm, keyfile);
            } else {
                returnedString = encryption.decrypt(message, algorithm, keyfile);
            }
            if (logger != null) {
                logger.close();
            }

            // display text
            output.setText(returnedString);

        } catch (RuntimeException e) {
            e.printStackTrace();
            String errorMessage = "Error parsing input\nMessage:\n" +
                    e.getMessage() +
                    "\nPlease use the format:\n" +
                    (encrypt ? "en" : "de") + "crypt message \"[message]\" using [algorithm] and keyfile [filename]";
            output.setText(errorMessage);
        }

    }
}
