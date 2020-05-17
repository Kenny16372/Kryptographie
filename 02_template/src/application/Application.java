package application;

import company.Branch;
import company.Network;
import persistence.HSQLDB;

public class Application {
    public static void main(String... args) {
        // hsqldb demo
        HSQLDB.instance.setupConnection();

        HSQLDB.instance.dropTableParticipants();
        HSQLDB.instance.dropTableTypes();

        HSQLDB.instance.createTableTypes();
        HSQLDB.instance.createTableParticipants();

        HSQLDB.instance.insertDataTableTypes("normal");
        HSQLDB.instance.insertDataTableTypes("intruder");

        HSQLDB.instance.insertDataTableParticipants("branch_hkg", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_cpt", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_sfo", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_syd", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_wuh", 1);
        HSQLDB.instance.insertDataTableParticipants("msa", 2);

        Network network = new Network();
        network.add(new Branch("branch_hkg"));
        network.add(new Branch("branch_cpt"));
        network.add(new Branch("branch_sfo"));
        network.add(new Branch("branch_syd"));
        network.add(new Branch("branch_wuh"));

        HSQLDB.instance.shutdown();
    }
}