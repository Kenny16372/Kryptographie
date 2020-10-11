package parser;

import javafx.scene.control.TextArea;
import network.Network;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class CreateChannel {
    public static void handle(String text, TextArea output) {
        output.clear();
        Scanner scanner = new Scanner(text);
        scanner.findInLine(Pattern.compile("^\\s*(?:channel)\\s+(\\w+)\\s+(?:from)\\s+(\\w+)\\s+(?:to)\\s+(\\w+)\\s*$", Pattern.CASE_INSENSITIVE));

        MatchResult result = null;
        try {
            result = scanner.match();
        } catch (IllegalStateException e) {
            output.setText("ERROR\nCouldn't parse input. Please use the following format:\ncreate channel <CHANNEL_NAME> from <PARTICIPANT_1> to <PARTICIPANT_2>");
            return;
        } finally {
            scanner.close();
        }

        if (result.groupCount() != 3) {
            output.setText("ERROR\nCouldn't parse input. Please use the following format:\ncreate channel <CHANNEL_NAME> from <PARTICIPANT_1> to <PARTICIPANT_2>");
            return;
        }

        String channelName = result.group(1);
        String participant1 = result.group(2);
        String participant2 = result.group(3);

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

        channelName = createEventBus(channelName, participant1, participant2);

        output.setText(String.format("Channel %s from %s to %s successfully created", channelName, participant1, participant2));
    }

    public static String createEventBus(String channelName, String participant1, String participant2) {
        return Network.instance.createChannel(channelName, participant1, participant2);
    }
}
