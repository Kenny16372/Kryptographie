package parser;

import javafx.scene.control.TextArea;

public class ShowHandler {
    public void handle(String text, TextArea output) {
        if (text.stripLeading().equals("algorithm")) {
            ShowAlgorithm.display(output);
        }
        switch (text.stripLeading().toLowerCase()){
            case "algorithm":
                ShowAlgorithm.display(output);
                break;
            case "channel":
                ShowChannel.display(output);
                break;
        }
    }
}
