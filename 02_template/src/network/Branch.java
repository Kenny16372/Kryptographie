package network;

import com.google.common.eventbus.Subscribe;
import configuration.Configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Branch extends Participant {
    Branch(String name, int id) {
        super(name, ParticipantType.normal, id);
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
        ParticipantController.instance.removeParticipant(id);
    }
}