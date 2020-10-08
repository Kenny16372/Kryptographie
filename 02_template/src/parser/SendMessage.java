package parser;

import encryption.Encryption;
import javafx.scene.control.TextArea;
import network.*;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SendMessage {
    private static final Encryption encryption = new Encryption();

    public static boolean handle(String text, TextArea output){
        String errorText = "ERROR\nCouldn't parse input. Please use the following format:\nsend message \"[content]\" from [sender] to [receiver]" +
                "using [algorithm] and keyfile [keyfile]\n";

        output.clear();

        Scanner scanner = new Scanner(text);
        scanner.findInLine(Pattern.compile("^\\s*(message)\\s+(\")([^\"]*)(\")\\s+(from)\\s+(\\S+)\\s+(to)\\s+(\\S+)\\s+(using)\\s+(\\S*)\\s+(and\\s+keyfile)\\s+(\\S*)\\s*$", Pattern.CASE_INSENSITIVE));

        MatchResult result = null;
        try {
            result = scanner.match();
        } catch (IllegalStateException e){
            output.setText(errorText);
            return false;
        } finally {
            scanner.close();
        }

        if(result.groupCount() != 12){
            output.setText(errorText);
            return false;
        }

        String messageKeyword = result.group(1);
        String firstQuotationMark = result.group(2);
        String message = result.group(3);
        String secondQuotationMark = result.group(4);
        String from = result.group(5);
        String sender = result.group(6);
        String to = result.group(7);
        String receiver = result.group(8);
        String using = result.group(9);
        String algorithm = result.group(10);
        String andKeyfile = result.group(11);
        String keyfile = result.group(12);

        if( !messageKeyword.equalsIgnoreCase("message") ||
            !firstQuotationMark.equals("\"") ||
            !secondQuotationMark.equals("\"") ||
            !from.equalsIgnoreCase("from") ||
            !to.equalsIgnoreCase("to") ||
            !using.equalsIgnoreCase("using") ||
            !andKeyfile.replaceAll("\\s", "").equalsIgnoreCase("andkeyfile")) {

                output.setText(errorText);
                return false;
        }

        Set<Branch> branches = new HashSet<>();
        Branch branch1 = null;
        Branch branch2 = null;

        Participant participant1 = ParticipantController.instance.getParticipantByName(sender);
        if(participant1.getType() != ParticipantType.normal){
            output.appendText("ERROR\nCan't send message from " + participant1.getName() + "\n");
        } else {
            branch1 = (Branch) participant1;
        }

        Participant participant2 = ParticipantController.instance.getParticipantByName(receiver);
        if(participant2.getType() != ParticipantType.normal){
            output.appendText("ERROR\nCan't send message to " + participant2.getName() + "\n");
        } else {
            branch2 = (Branch) participant2;
        }

        if(branch1 == null || branch2 == null){
            return false;
        }

        branches.add(branch1);
        branches.add(branch2);

        String channelName = Network.instance.getChannelByBranches(branches);

        if(channelName == null){
            output.appendText("ERROR\nNo valid channel from " + sender + " to " + receiver + "\n");
            return false;
        }

        String encrypted = encryption.encrypt(message, algorithm, keyfile);

        Network.instance.postMessage(channelName, message, encrypted, branch1.getId(), branch2.getId(), algorithm, keyfile);

        return true;
    }
}
