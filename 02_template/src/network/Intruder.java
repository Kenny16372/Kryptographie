package network;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import encryption.Cracker;
import gui.GUI;

public class Intruder extends Participant {
    private Cracker cracker = new Cracker();

    public Intruder(String name, int id) {
        super(name, ParticipantType.intruder, id);
    }

    // registers itself to a specified EventBus
    public void infiltrateEventBus(EventBus eventBus) {
        eventBus.register(this);
    }

    // intercept messages
    @Subscribe
    public void handleMessage(Message message) {
        String decrypted = cracker.decrypt(message.toString(), message.getAlgorithm());
        GUI.getOutputArea().appendText("Intruder " + name + " intercepted a message from " + ParticipantController.instance.getParticipant(message.getIdSender()).getName() +
                ":\n" + "it is one of the following\n" + decrypted + "\n");
    }
}
