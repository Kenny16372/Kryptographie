package network;

public abstract class Participant {
    protected ParticipantType type;
    protected String name;
    protected int id;

    Participant(String name, ParticipantType type, int id){
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
