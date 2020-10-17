package network;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import encryption.Cracker;
import gui.GUI;
import persistence.HSQLDB;

public class Intruder extends Participant {
    private final Cracker cracker = new Cracker();

    public Intruder(String name, int id) {
        super(name, ParticipantType.intruder, id);
    }

    // registers itself to a specified EventBus
    public void infiltrateEventBus(EventBus eventBus) {
        eventBus.register(this);
    }

    // intercept messages
    @Subscribe
    @Override
    public void receiveMessage(Message message) {
        String keyFile = message.getKeyFileName();

        HSQLDB.instance.insertDataTablePostbox(this.name, message.getIdSender(), "unknown");

        String decrypted = cracker.decrypt(message.toString(), message.getAlgorithm(), keyFile, false);

        String sender = ParticipantController.instance.getParticipant(message.getIdSender()).getName();

        if (Cracker.didFinishAllFiles()) {
            GUI.getOutputArea().appendText("intruder " + name + " cracked message from participant " + sender + " | " + decrypted + "\n");
            HSQLDB.instance.updateDataTablePostbox(this.name, decrypted);
        } else {
            GUI.getOutputArea().appendText("intruder " + name + " | crack message from participant " + sender + " failed\n");
        }
    }
}
