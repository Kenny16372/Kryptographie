package parser;

import javafx.scene.control.TextArea;
import network.Participant;
import network.ParticipantController;
import persistence.HSQLDB;

import java.util.Map;

public class RegisterParticipant {
    public static void handle(String text, TextArea output) {

        int indexFirstWhitespace = text.indexOf(" ");
        if (indexFirstWhitespace == -1) {
            return;
        }
        String command = text.substring(0, indexFirstWhitespace);
        String rest = text.substring(indexFirstWhitespace + 1).stripLeading();

        if (command.stripLeading().equals("participant")) {
            getName(rest, output);
        }
    }

    private static void getName(String text, TextArea output) {

        int indexFirstWhitespace = text.indexOf(" ");
        if (indexFirstWhitespace == -1) {
            return;
        }
        String name = text.substring(0, indexFirstWhitespace);
        String rest = text.substring(indexFirstWhitespace + 1).stripLeading();
        String type = rest.substring(10).stripLeading();

        boolean mustCreateParticipant = true;

        Map<Integer, Participant> participants = ParticipantController.instance.getParticipantMap();
        for (Map.Entry<Integer, Participant> participant : participants.entrySet()) {
            if(participant.getValue().getName().equals(name)){
                mustCreateParticipant = false;
                break;
            }
        }

        String messageCreate = "participant " + name + " with type " + type + " registered and postbox_" + name + " created";
        String messageExist = "participant " + name + " already exists, using existing postbox_" + name;

        if (mustCreateParticipant) {
            output.setText(messageCreate);
            ParticipantController.instance.createParticipant(name, type);
            ParticipantController.instance.createPostbox(name);
        } else output.setText(messageExist);
    }

}
