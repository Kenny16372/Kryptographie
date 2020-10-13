package network;

import com.google.common.eventbus.Subscribe;
import configuration.Configuration;
import encryption.RSA;
import gui.GUI;
import persistence.HSQLDB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Branch extends Participant {
    Branch(String name, int id) {
        super(name, ParticipantType.normal, id);

        createKeys();
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
    @Override
    public void receiveMessage(Message message) {
        if(message.getIdSender() == id){
            return;
        }

        String decrypted = encryption.decrypt(message, this.name + "_pri.txt");
        HSQLDB.instance.insertDataTablePostbox(this.name, message.getIdSender(), decrypted);
        GUI.getOutputArea().appendText("Branch " + name + " received a new message: " + decrypted + "\n");
    }

    private void createKeys(){
        RSA rsa = new RSA();
        Map<String, Map<Character, String>> keys = rsa.generateKeyPair();
        storeKeys(keys);
    }

    public void remove() {
        ParticipantController.instance.removeParticipant(id);
    }
}
