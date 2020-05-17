package parser;

import javafx.scene.control.TextArea;

public interface IHandler {
    void handle(String command, TextArea output);
}
