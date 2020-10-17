package parser;

import javafx.scene.control.TextArea;
import network.Participant;
import network.ParticipantController;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterHandler extends Handler{

    public RegisterHandler() {
        this.successor = new ShowChannelHandler();
        this.pattern = Pattern.compile("^\\s*register\\s+participant\\s+(\\S+)\\s+with\\s+type\\s+(normal|intruder)\\s*$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {

        String participantName = matcher.group(1);
        String type = matcher.group(2);

        boolean participantExists = ParticipantController.instance.getParticipantByName(participantName) != null;

        if (!participantExists) {
            ParticipantController.instance.createParticipant(participantName, type);
            ParticipantController.instance.createPostbox(participantName);
        } else {
            output.setText("Participant " + participantName + " already exists");
            return;
        }

        output.setText("Successfully created participant " + participantName + " with type " + type);
    }

}
