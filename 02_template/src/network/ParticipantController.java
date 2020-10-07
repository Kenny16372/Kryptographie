package network;

import persistence.HSQLDB;

import javax.print.DocFlavor;
import java.util.*;
import java.util.stream.Collectors;

public enum ParticipantController {
    instance;

    private Map<Integer, Participant> participantMap = new HashMap<>();

    public boolean createParticipant(String name, int id, String type){
        Participant participant;

        if(participantMap.get(id) != null){
            System.out.println("Tried creating duplicate participant " + name);
            return false;
        }

        switch (type){
            case "normal":
                participant = new Branch(name, id);
                break;
            case "intruder":
                participant = new Intruder(name, id);
                break;
            default:
                return false;
        }

        participantMap.put(id, participant);

        return true;
    }

    public void startup(){
        List<Map<String, String>> participants = HSQLDB.instance.getAllParticipants();

        for(Map<String, String> participant: participants){
            createParticipant(participant.get("name"), Integer.parseInt(participant.get("id")), participant.get("type"));
        }
    }

    public void removeParticipant(int id){
        participantMap.remove(id);
    }

    public Participant getParticipant(int id){
        return participantMap.get(id);
    }

    public Participant getParticipantByName(String name){
        return participantMap.values().stream().filter(participant -> participant.name.equals(name)).findFirst().orElse(null);
    }

    // returns a set of participant objects corresponding to the given participant names
    public Set<Participant> getParticipants(String... participantNames){
        return Arrays.stream(participantNames).map(this::getParticipantByName).filter(Objects::nonNull).collect(Collectors.toSet());
    }

}