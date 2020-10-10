package application;

import network.Network;
import network.Participant;
import network.ParticipantController;
import persistence.HSQLDB;

import java.util.List;
import java.util.Map;

public class Application {
    public static void main(String... args) {
        // hsqldb demo
        HSQLDB.instance.setupConnection();

        List<Map<String, String>> participants = HSQLDB.instance.getAllParticipants();
        for(Map<String, String> participant: participants){
            HSQLDB.instance.dropTablePostbox(participant.get("name"));
            System.out.println(participant.get("name"));
        }

        HSQLDB.instance.dropTableChannel();
        HSQLDB.instance.dropTableMessages();
        HSQLDB.instance.dropTableAlgorithms();
        HSQLDB.instance.dropTableParticipants();
        HSQLDB.instance.dropTableTypes();

        HSQLDB.instance.createTableTypes();
        HSQLDB.instance.createTableParticipants();
        HSQLDB.instance.createTableAlgorithms();
        HSQLDB.instance.createTableChannel();
        HSQLDB.instance.createTableMessages();

        HSQLDB.instance.insertDataTableTypes("normal");
        HSQLDB.instance.insertDataTableTypes("intruder");

        HSQLDB.instance.insertDataTableParticipants("branch_hkg", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_cpt", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_sfo", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_syd", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_wuh", 1);
        HSQLDB.instance.insertDataTableParticipants("msa", 2);

        HSQLDB.instance.createTablePostbox("branch_hkg");
        HSQLDB.instance.createTablePostbox("branch_cpt");
        HSQLDB.instance.createTablePostbox("branch_sfo");
        HSQLDB.instance.createTablePostbox("branch_syd");
        HSQLDB.instance.createTablePostbox("branch_wuh");
        HSQLDB.instance.createTablePostbox("msa");

        HSQLDB.instance.insertDataTableChannel("hkg_wuh", 1, 5);
        HSQLDB.instance.insertDataTableChannel("hkg_cpt", 1, 2);
        HSQLDB.instance.insertDataTableChannel("cpt_syd", 2, 4);
        HSQLDB.instance.insertDataTableChannel("syd_sfo", 4, 3);

        HSQLDB.instance.shutdown();

        Network.instance.startup();
    }
}