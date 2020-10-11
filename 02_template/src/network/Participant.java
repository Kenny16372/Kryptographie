package network;

import com.google.common.eventbus.Subscribe;

public abstract class Participant {
    protected ParticipantType type;
    protected String name;
    protected int id;

    Participant(String name, ParticipantType type, int id) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Subscribe
    public void receiveMessage(Message message) {
        System.out.println("Participant received message");
    }

    public ParticipantType getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
