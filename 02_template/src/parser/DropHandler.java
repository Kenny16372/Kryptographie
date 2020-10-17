package parser;


import javafx.scene.control.TextArea;
import network.Network;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropHandler extends Handler {

    public DropHandler() {
        this.successor = new IntrudeHandler();
        this.pattern = Pattern.compile("^\\s*drop\\s+channel\\s+(\\w+)\\s*$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {

        String channelName = matcher.group(1);

        if (Network.instance.dropChannel(channelName)) {
            output.setText("Channel " + channelName + " deleted");
        } else {
            output.setText("unknown channel " + channelName);
        }
    }
}
