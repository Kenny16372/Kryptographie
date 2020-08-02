package network;

import com.google.common.eventbus.EventBus;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public enum Network {
    instance;

    private Map<String, Pair<EventBus, Set<BranchController.Branch>>> eventBusses = new HashMap<>();

    public String createEventBus(String eventBusIdentifier, String... listeners){
        Set<BranchController.Branch> branches = BranchController.instance.getBranches(listeners);

        if(eventBusIdentifier.isBlank()){
            eventBusIdentifier = branches.stream().map(BranchController.Branch::getName).map(String::toUpperCase).collect(Collectors.joining("_"));
        }

        EventBus eventBus = new EventBus(eventBusIdentifier);

        for(Object listener: branches){
            eventBus.register(listener);
        }

        eventBusses.put(eventBusIdentifier, new Pair<>(eventBus, branches));

        return eventBusIdentifier;
    }

    public boolean channelExists(String name){
        return eventBusses.containsKey(name);
    }

    public boolean connectionExists(String... participants){
        Set<BranchController.Branch> branches = BranchController.instance.getBranches(participants);

        return eventBusses.values().stream().map(Pair::getValue).anyMatch(branches::equals);
    }

    public void postMessage(String eventBusIdentifier, Message message){
        EventBus eventBus = eventBusses.get(eventBusIdentifier).getKey();

        if(eventBus != null){
            eventBus.post(message);
        }
    }

    public Participant createParticipant(String name, String type){
        return new Participant(name, ParticipantType.fromString(type));
    }
}
