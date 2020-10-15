package parser;

import encryption.Cracker;
import javafx.scene.control.TextArea;

import java.util.Scanner;
import java.util.regex.MatchResult;

public class CrackingHandler {
    public void handle(String command, TextArea output) {

        output.clear();

        try {
            Scanner scanner = new Scanner(command);
            scanner.findInLine("^(encrypted\\s+message)\\s+(\")([^\"]*)(\")\\s+(using)\\s+(\\S+)\\s*$");

            MatchResult result = scanner.match();
            scanner.close();

            if (result.groupCount() != 6) {
                throw new RuntimeException("Wrong format");
            }

            output.setText("This might take up to 30 seconds");

            int pos = 1;

            if (!result.group(pos++).replaceAll("\\s+", "").equals("encryptedmessage")) {
                throw new RuntimeException("Expected keyword \"encrypted message\"");
            }

            if (!result.group(pos++).equals("\"")) {
                throw new RuntimeException("Expected token (\")");
            }

            String message = result.group(pos++);

            if (!result.group(pos++).equals("\"")) {
                throw new RuntimeException("Expected token (\")");
            }

            if (!result.group(pos++).equals("using")) {
                throw new RuntimeException("Expected keyword \"using\"");
            }

            String algorithm = result.group(pos);

            Cracker cracker = new Cracker();

            // It would be possible to check the database for the key file name, but this would make the whole cracking process unnecessary, since
            // the decrypted message stands in the same table and no views were to be defined that only contain the encrypted message and the keyfile
            // Thus, the Cracker will be checking all key files inside the key file directory
            String plaintext = cracker.decrypt(message, algorithm, null, true);

            String resultText;
            boolean allFilesDone = Cracker.didFinishAllFiles();

            if(plaintext.contains("\n")){
                resultText = "The decrypted message is " + (allFilesDone ? "probably ": "") + "one of these:\n" + plaintext;
            } else {
                resultText = "The decrypted message " + (allFilesDone ? "probably ": "") + "is:\n" + plaintext;
            }

            output.setText(resultText);

        } catch (RuntimeException e) {
            String errorMessage = "Error parsing input\nMessage:\n" +
                    e.toString() +
                    "\nPlease use the format:\n" +
                    "crack encrypted message \"[message]\" using [algorithm]";
            output.setText(errorMessage);
        }
    }
}
