package network;

import com.google.common.eventbus.EventBus;
import javafx.util.Pair;
import parser.ShowAlgorithm;
import persistence.HSQLDB;

import java.util.*;
import java.util.stream.Collectors;

public enum Network {
    instance;

    // channels: Identifier -> (EventBus, Branch)
    private Map<String, Pair<EventBus, Set<Branch>>> channels = new HashMap<>();

    public void startup() {
        ParticipantController.instance.startup();

        loadChannels();
        updateAlgorithms();
    }

    private void loadChannels() {
        Map<String, Set<Integer>> channels = HSQLDB.instance.getAllChannels();

        for (String channelName : channels.keySet()) {
            Set<Integer> branchIds = channels.get(channelName);
            Set<Branch> branches = new HashSet<>();

            for (int id : branchIds) {
                Participant participant = ParticipantController.instance.getParticipant(id);

                if (participant.type.equals(ParticipantType.normal)) {
                    branches.add((Branch) participant);
                }
            }

            EventBus eventBus = new EventBus(channelName);

            for(Branch branch: branches){
                eventBus.register(branch);
            }

            this.channels.put(channelName, new Pair<>(eventBus, branches));
        }
    }

    private void updateAlgorithms() {
        for (String name : ShowAlgorithm.getComponentNames()) {
            if (!name.endsWith("_cracker")) {
                HSQLDB.instance.insertDataTableAlgorithms(name);
            }
        }
    }

    public Map<String, Set<Branch>> getChannels() {
        Map<String, Set<Branch>> result = new HashMap<>();

        for (String channel : channels.keySet()) {
            Set<Branch> branches = channels.get(channel).getValue();

            result.put(channel, branches);
        }

        return result;
    }

    public String createChannel(String channelName, String... listeners) {
        Set<Participant> participants = ParticipantController.instance.getParticipants(listeners);
        Set<Branch> branches = participants.stream().map(participant -> (Branch) participant).collect(Collectors.toSet());

        if (channelName.isBlank()) {
            channelName = branches.stream().map(Branch::getName).map(String::toUpperCase).sorted().collect(Collectors.joining("_"));
        }

        EventBus eventBus = new EventBus(channelName);

        for (Object listener : branches) {
            eventBus.register(listener);
        }

        channels.put(channelName, new Pair<>(eventBus, branches));

        List<Branch> branchArray = new ArrayList<>(branches);
        HSQLDB.instance.insertDataTableChannel(channelName, branchArray.get(0).getId(), branchArray.get(1).getId());

        return channelName;
    }

    public String getChannelByBranches(Set<Branch> branches) {
        for (String channel : channels.keySet()) {
            Pair<EventBus, Set<Branch>> pair = channels.get(channel);

            if (pair.getValue().equals(branches)) {
                return channel;
            }
        }
        return null;
    }

    public boolean dropChannel(String channelName) {
        boolean result = channels.get(channelName) != null;

        if (!result) {
            return false;
        }

        channels.remove(channelName);
        HSQLDB.instance.dropChannel(channelName);

        return true;
    }

    // check if channel specified by name has an underlying EventBus including non-null branches
    public boolean channelExists(String name) {
        Pair<EventBus, Set<Branch>> eventBus = channels.get(name);
        if (eventBus != null) {
            Set<Branch> branches = eventBus.getValue();

            return eventBus.getKey() != null && branches != null && branches.size() > 0;
        }

        return false;
    }

    // check if a channel with exactly these branches already exists
    public boolean connectionExists(String... participantNames) {
        // get set of participants from names
        Set<Participant> participants = ParticipantController.instance.getParticipants(participantNames);

        // Pair of EventBus and set of participants -> set of participants -> is there an exact match?
        return channels.values().stream().map(Pair::getValue).anyMatch(participants::equals);
    }

    public boolean participantNameUnused(String name) {
        return ParticipantController.instance.getParticipantByName(name) != null;
    }

    // wrapper for sending messages to a channel
    public void postMessage(String channelName, String plaintext, String encrypted, int fromId, int toId, String algorithm, String keyfile) {
        Message message = new Message(encrypted, algorithm, fromId);
        EventBus eventBus = channels.get(channelName).getKey();

        if (eventBus != null) {
            eventBus.post(message);
        }

        HSQLDB.instance.insertDataTableMessages(fromId, toId, plaintext, algorithm, encrypted, keyfile);
    }

    // intruder use only; official way to access EventBuses is via their identifier
    public EventBus getEventBus(String identifier) {
        Pair<EventBus, Set<Branch>> eventBus = channels.get(identifier);

        if (eventBus != null) {
            return channels.get(identifier).getKey();
        }

        return null;
    }
}
