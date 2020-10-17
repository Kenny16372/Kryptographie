package parser;

import com.google.common.eventbus.EventBus;
import javafx.scene.control.TextArea;
import network.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntrudeHandler extends Handler {

    public IntrudeHandler() {
        this.successor = new RegisterHandler();
        this.pattern = Pattern.compile("^\\s*intrude\\s+channel\\s+(\\w+)\\s+by\\s+(\\w+)\\s*$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {
        String channelName = matcher.group(1);
        String intruderName = matcher.group(2);

        EventBus eventBus = Network.instance.getEventBus(channelName);

        if (eventBus == null) {
            output.setText("ERROR\nNo such channel\n");
        }

        Participant participant = ParticipantController.instance.getParticipantByName(intruderName);

        if (participant == null) {
            output.setText("ERROR\nUnknown participant name\n");
        }

        if (participant.getType() != ParticipantType.intruder) {
            output.setText("ERROR\nParticipant \"" + intruderName + "\" is not an intruder\n");
        }

        Intruder intruder = (Intruder) participant;

        intruder.infiltrateEventBus(eventBus);

        output.setText(intruder.getName() + " successfully intruded " + channelName);
    }
}
