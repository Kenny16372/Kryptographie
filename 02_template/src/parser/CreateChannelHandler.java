package parser;

import javafx.scene.control.TextArea;
import network.Network;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateChannelHandler extends Handler{

    public CreateChannelHandler() {
        this.successor = new DropHandler();
        this.pattern = Pattern.compile("^\\s*create\\s+channel\\s+(\\w+)\\s+from\\s+(\\w+)\\s+to\\s+(\\w+)\\s*$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {

        String channelName = matcher.group(1);
        String participant1 = matcher.group(2);
        String participant2 = matcher.group(3);

        // Check if participants are valid branches
        if (!Network.instance.participantNameUnused(participant1)) {
            if (!Network.instance.participantNameUnused(participant2)) {
                output.setText(String.format("Branches %s and %s don't exist", participant1, participant2));
            } else {
                output.setText(String.format("Branch %s doesn't exist", participant1));
            }
            return;
        }

        // check if participants are equal
        if (participant1.equalsIgnoreCase(participant2)) {
            output.setText(String.format("%s and %s are identical – cannot create channel on itself", participant1, participant2));

            if (!participant1.equals(participant2)) {
                output.appendText("\nBranch names are case-insensitive");
            }
            return;
        }

        if (Network.instance.channelExists(channelName)) {
            output.setText(String.format("Channel %s already exists", channelName));
            return;
        }

        if (Network.instance.connectionExists(participant1, participant2)) {
            output.setText(String.format("Communication channel between %s and %s already exists", participant1, participant2));
            return;
        }

        channelName = Network.instance.createChannel(channelName, participant1, participant2);

        output.setText(String.format("Channel %s from %s to %s successfully created", channelName, participant1, participant2));
    }
}
