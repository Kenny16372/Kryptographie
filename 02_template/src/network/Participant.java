package network;

import com.google.common.eventbus.Subscribe;
import encryption.Encryption;

public abstract class Participant {
    protected ParticipantType type;
    protected String name;
    protected int id;
    protected Encryption encryption = new Encryption();

    Participant(String name, ParticipantType type, int id) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Subscribe
    public void receiveMessage(String message, String algorithm, String keyfile, Participant participant01, Participant participant02) {
        System.out.println("Participant received new message");
    }

    public ParticipantType getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
