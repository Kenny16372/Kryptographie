package parser;

import encryption.Encryption;
import javafx.scene.control.TextArea;
import network.*;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendMessageHandler extends Handler{
    private final Encryption encryption = new Encryption();

    public SendMessageHandler() {
        this.successor = new EncryptionHandler();
        this.pattern = Pattern.compile("^\\s*send\\s+message\\s+\"([^\"]*)\"\\s+from\\s+(\\S+)\\s+to\\s+(\\S+)\\s+using\\s+(\\S*)\\s+and\\s+keyfile\\s+(\\S*)\\s*$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {
        output.clear();

        String message = matcher.group(1);
        String sender = matcher.group(2);
        String receiver = matcher.group(3);
        String algorithm = matcher.group(4);
        String keyfile = matcher.group(5);

        Set<Branch> branches = new HashSet<>();
        Branch branchFrom = null;
        Branch branchTo = null;

        Participant participantFrom = ParticipantController.instance.getParticipantByName(sender);

        if(participantFrom == null){
            output.setText("ERROR\nNo such participant: " + sender);
            return;
        }

        if (participantFrom.getType() != ParticipantType.normal) {
            output.appendText("ERROR\nCan't send message from " + participantFrom.getName() + "\n");
        } else {
            branchFrom = (Branch) participantFrom;
        }

        Participant participantTo = ParticipantController.instance.getParticipantByName(receiver);

        if(participantTo == null){
            output.setText("ERROR\nNo such participant: " + receiver);
            return;
        }

        if (participantTo.getType() != ParticipantType.normal) {
            output.appendText("ERROR\nCan't send message to " + participantTo.getName() + "\n");
        } else {
            branchTo = (Branch) participantTo;
        }

        if (branchFrom == null || branchTo == null) {
            return;
        }

        branches.add(branchFrom);
        branches.add(branchTo);

        String channelName = Network.instance.getChannelByBranches(branches);

        if (channelName == null) {
            output.appendText("ERROR\nNo valid channel from " + sender + " to " + receiver + "\n");
            return;
        }

        String encrypted = encryption.encrypt(message, algorithm, keyfile);

        if(encrypted == null){
            output.setText("ERROR\nEncryption failed. Please check the algorithm and the key file\n");
            return;
        }

        Network.instance.postMessage(channelName, message, encrypted, branchFrom.getId(), branchTo.getId(), algorithm, keyfile);
    }
}
