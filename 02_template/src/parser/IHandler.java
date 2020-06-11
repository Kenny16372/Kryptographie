package parser;

import javafx.scene.control.TextArea;

import java.sql.SQLException;

public interface IHandler {
    void handle(String command, TextArea output) throws SQLException;
}
