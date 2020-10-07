package network;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class Intruder extends Participant {

    public Intruder(String name, int id) {
        super(name, ParticipantType.intruder, id);
    }

    // registers itself to a specified EventBus
    public void infiltrateEventBus(EventBus eventBus){
        eventBus.register(this);
    }

    // intercept messages
    @Subscribe
    public void handleMessage(Message message){
        System.out.println(message);
    }
}
