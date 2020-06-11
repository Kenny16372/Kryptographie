package parser;

import javafx.scene.control.TextArea;

public class CreateChannel {
    public static void handle(String text, TextArea output) {

        int indexFirstWhitespace = text.indexOf(" ");
        if (indexFirstWhitespace == -1) {
            return;
        }
        String command = text.substring(0, indexFirstWhitespace);
        String rest = text.substring(indexFirstWhitespace + 1).stripLeading();

        if (command.stripLeading().equals("channel")) {
            //Do something
        }
    }
}
