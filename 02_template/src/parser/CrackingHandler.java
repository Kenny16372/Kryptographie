package parser;

import encryption.Cracker;
import javafx.scene.control.TextArea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrackingHandler extends Handler {

    public CrackingHandler() {
        this.successor = new CreateChannelHandler();
        this.pattern = Pattern.compile("^crack\\s+encrypted\\s+message\\s+\"([^\"]*)\"\\s+using\\s+(\\S+)\\s*$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {
        output.setText("This might take up to 30 seconds");
        String message = matcher.group(1);
        String algorithm = matcher.group(2);

        Cracker cracker = new Cracker();

        // It would be possible to check the database for the key file name, but this would make the whole cracking process unnecessary, since
        // the decrypted message stands in the same table and no views were to be defined that only contain the encrypted message and the keyfile
        // Thus, the Cracker will be checking all key files inside the key file directory
        String plaintext = cracker.decrypt(message, algorithm, null, true);

        String resultText;
        boolean allFilesDone = Cracker.didFinishAllFiles();

        if (plaintext.contains("\n")) {
            resultText = "The decrypted message is " + (!allFilesDone ? "maybe " : "") + "one of these:\n" + plaintext;
            if (!allFilesDone) {
                resultText += "\nCould not check all key files\n";
            }
        } else {
            resultText = "The decrypted message " + (!allFilesDone ? "probably " : "") + "is:\n" + plaintext;
            if (!allFilesDone) {
                resultText += "\nCould not check all key files\n";
            }
        }

        output.setText(resultText);
    }
}
