package company;

import com.google.common.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private EventBus eventBus = new EventBus();
    private List branches = new ArrayList<Branch>();

    public void add(Branch branch){
        this.branches.add(branch);
    }
}
