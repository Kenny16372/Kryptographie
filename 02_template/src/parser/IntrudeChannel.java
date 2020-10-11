package parser;

import com.google.common.eventbus.EventBus;
import javafx.scene.control.TextArea;
import network.*;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class IntrudeChannel {
    public static boolean handle(String text, TextArea output) {
        String errorText = "ERROR\nCouldn't parse input. Please use the following format:\nintrude channel [CHANNEL_NAME] by [INTRUDER]\n";

        output.clear();

        Scanner scanner = new Scanner(text);
        scanner.findInLine(Pattern.compile("^\\s*(channel)\\s+(\\w+)\\s+(by)\\s+(\\w+)\\s*$", Pattern.CASE_INSENSITIVE));

        MatchResult result = null;
        try {
            result = scanner.match();
        } catch (IllegalStateException e) {
            output.setText(errorText);
            return false;
        } finally {
            scanner.close();
        }

        if (result.groupCount() != 4) {
            output.setText(errorText);
            return false;
        }

        String channel = result.group(1);
        String channelName = result.group(2);
        String by = result.group(3);
        String intruderName = result.group(4);

        if (!channel.equalsIgnoreCase("channel") || !by.equalsIgnoreCase("by")) {
            output.setText(errorText);
            return false;
        }

        EventBus eventBus = Network.instance.getEventBus(channelName);

        if (eventBus == null) {
            output.setText("ERROR\nNo such channel\n");
            return false;
        }

        Participant participant = ParticipantController.instance.getParticipantByName(intruderName);

        if (participant == null) {
            output.setText("ERROR\nUnknown participant name\n");
            return false;
        }

        if (participant.getType() != ParticipantType.intruder) {
            output.setText("ERROR\nParticipant \"" + intruderName + "\" is not an intruder\n");
            return false;
        }

        Intruder intruder = (Intruder) participant;

        intruder.infiltrateEventBus(eventBus);

        return true;
    }
}
