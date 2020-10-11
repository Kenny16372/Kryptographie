package parser;


import javafx.scene.control.TextArea;
import network.Network;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class DropHandler {
    public static boolean handle(String text, TextArea output) {
        output.clear();
        Scanner scanner = new Scanner(text);
        scanner.findInLine(Pattern.compile("^\\s*(channel)\\s+(\\w+)\\s*$", Pattern.CASE_INSENSITIVE));

        MatchResult result = null;
        try {
            result = scanner.match();
        } catch (IllegalStateException e) {
            output.setText("ERROR\nCouldn't parse input. Please use the following format:\ncreate channel <CHANNEL_NAME> from <PARTICIPANT_1> to <PARTICIPANT_2>");
            return false;
        } finally {
            scanner.close();
        }

        if (result.groupCount() != 2) {
            output.setText("ERROR\nCouldn't parse input. Please use the following format:\ncreate channel <CHANNEL_NAME> from <PARTICIPANT_1> to <PARTICIPANT_2>");
            return false;
        }

        String channel = result.group(1);
        String channelName = result.group(2);

        if (!channel.equalsIgnoreCase("channel")) {
            output.setText("ERROR\nExpected \"channel\"");
            return false;
        }
        if (Network.instance.dropChannel(channelName)) {
            output.setText("Channel " + channelName + " deleted");
        } else {
            output.setText("unknown channel " + channelName);
        }
        return true;
    }
}
