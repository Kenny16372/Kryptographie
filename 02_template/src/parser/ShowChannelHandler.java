package parser;

import javafx.scene.control.TextArea;
import network.Branch;
import network.Network;
import network.Participant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ShowChannelHandler extends Handler{

    public ShowChannelHandler() {
        this.successor = new ShowAlgorithmHandler();
        this.pattern = Pattern.compile("^\\s*show\\s+channel\\s*$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {
        // Channel name -> Branch names
        Map<String, Set<String>> channels = getChannels();

        if (channels.size() == 0) {
            output.setText("No channels available");
            return;
        }

        for (String channelName : channels.keySet()) {
            Set<String> branchNames = channels.get(channelName);

            output.appendText(channelName + " | ");
            output.appendText(String.join(" and ", branchNames));
            output.appendText("\n");
        }
    }

    private static Map<String, Set<String>> getChannels() {
        Map<String, Set<Branch>> channels = Network.instance.getChannels();

        Map<String, Set<String>> channelNames = new HashMap<>();

        for (String channel : channels.keySet()) {
            Set<Branch> branches = channels.get(channel);

            Set<String> branchNames = branches.stream().map(Participant::getName).collect(Collectors.toSet());

            channelNames.put(channel, branchNames);
        }

        return channelNames;
    }
}
