package network;

public class Participant {
    protected ParticipantType type;
    protected String name;

    Participant(String name, ParticipantType type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }
}
