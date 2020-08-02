package network;

import com.google.common.eventbus.Subscribe;
import configuration.Configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public enum BranchController {
    instance;

    private Map<String, Branch> branchMap = new HashMap<>();

    public void createBranch(String name){
        Branch branch = new Branch(name);

        branchMap.put(name, branch);
    }

    public void removeBranch(String name){
        branchMap.remove(name);
    }

    public Branch getBranch(String name){
        return branchMap.get(name);
    }

    public Set<Branch> getBranches(String... branchNames){
        return Arrays.stream(branchNames).map(this::getBranch).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static class Branch extends Participant {
        private Branch(String name) {
            super(name, ParticipantType.normal);
        }

        public void storeKeys(Map<String, Map<Character, String>> keys) {
            // stores keys for this branch in the format "<n>\n<e>"
            // public key
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(Configuration.instance.keyFileDirectory + this.name + "_pub.txt"))) {
                bw.write("{\"n\":");
                bw.write(keys.get("publicKey").get('n') + ",");
                bw.write("\"e\":");
                bw.write(keys.get("publicKey").get('e'));
                bw.write("}");

            } catch (IOException e) {
                e.printStackTrace();
            }

            // private key
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(Configuration.instance.keyFileDirectory + this.name + "_pri.txt"))) {
                bw.write("{\"n\":");
                bw.write(keys.get("privateKey").get('n') + ",");
                bw.write("\"e\":");
                bw.write(keys.get("privateKey").get('e'));
                bw.write("}");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Subscribe
        public void inboundMessage(Message message){

        }

        public void remove(){
            BranchController.instance.removeBranch(name);
        }
    }
}
