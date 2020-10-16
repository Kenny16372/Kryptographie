package parser;

import configuration.Configuration;
import encryption.Encryption;
import javafx.scene.control.TextArea;
import logging.Logger;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncryptionHandler extends Handler {

    public EncryptionHandler() {
        this.successor = new CrackingHandler();
        this.pattern = Pattern.compile("^\\s*(en|de)crypt\\s+message\\s+\"([^\"]*)\"\\s+using\\s+(\\S*)\\s+and\\s+keyfile\\s+(\\S*)\\s*$", Pattern.CASE_INSENSITIVE);
    }

    protected void handle(Matcher matcher, TextArea output) {
        boolean encrypt = true;

        // true: encryption; false: decryption
        encrypt = matcher.group(1).equals("en");

        String message = matcher.group(2);
        String algorithm = matcher.group(3);
        String keyFile = matcher.group(4);

        // logging
        Logger logger = null;
        if (Configuration.instance.debugMode) {
            logger = new Logger();
            logger.startLogging(encrypt, algorithm);
        }

        Encryption encryption = new Encryption();
        String returnedString;
        if (encrypt) {
            returnedString = encryption.encrypt(message, algorithm, keyFile);
        } else {
            returnedString = encryption.decrypt(message, algorithm, keyFile);
        }

        if(returnedString == null){
            output.setText("ERROR\nCan't use the RSA Cracker to " + (encrypt ? "en" : "de") + "crypt messages");
        }

        if (logger != null) {
            logger.close();
        }

        // display text
        output.setText(returnedString);

    }
}
