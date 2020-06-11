package company;

import com.google.common.eventbus.EventBus;

public class Network {
    private EventBus eventBus = new EventBus();

    public Branch newBranch(String name) {
        Branch branch = new Branch(name);
        this.eventBus.register(branch);
        return branch;
    }
}
