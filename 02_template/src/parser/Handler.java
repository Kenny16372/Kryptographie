package parser;

import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Handler {
    protected Handler successor;
    protected Pattern pattern;

    public void accept(String text, TextArea output){
        Matcher matcher = pattern.matcher(text);

        if(matcher.matches()){
            this.handle(matcher, output);
        } else {
            this.successor.accept(text, output);
        }
    }

    protected void handle(Matcher matcher, TextArea output){
        throw new UnsupportedOperationException();
    }
}