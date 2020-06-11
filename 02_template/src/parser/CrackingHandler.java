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
            scanner.findInLine("^(encrypted\\s+message)\\s+(\")([^\"]*)(\")\\s+(using)\\s+(\\S+)(?:\\s*$)|(?:(and\\s+keyfile)\\s+(\\S+)\\s*$)");

            MatchResult result = scanner.match();
            scanner.close();

            if (result.groupCount() <= 6) {
                throw new RuntimeException("Wrong format");
            }

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

            String algorithm = result.group(pos++);

            String keyFileName = null;
            if (result.group(pos) != null) {
                if (!(result.group(pos++).replaceAll("\\s+", "").equals("andkeyfile"))) {
                    throw new RuntimeException("Expected keyword \"and keyfile\"");
                }

                keyFileName = result.group(pos);
            }

            Cracker cracker = new Cracker();

            String plaintext = cracker.decrypt(message, algorithm, keyFileName);

            String resultText = plaintext.indexOf(',') == -1 ? plaintext : "The plaintext word probably is one of these: " + plaintext;
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
