package application;

import persistence.HSQLDB;

public class Application {
    public static void main(String... args) {
        // hsqldb demo
        HSQLDB.instance.setupConnection();

        HSQLDB.instance.dropTableParticipants();
        HSQLDB.instance.dropTableTypes();
        HSQLDB.instance.dropTableAlgorithms();
        HSQLDB.instance.dropTableChannel();
        HSQLDB.instance.dropTableMessages();

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

        HSQLDB.instance.insertDataTableChannel("hkg_wuh", 1, 5);
        HSQLDB.instance.insertDataTableChannel("hkg_cpt", 1, 2);
        HSQLDB.instance.insertDataTableChannel("cpt_syd", 2, 4);
        HSQLDB.instance.insertDataTableChannel("syd_sfo", 4, 3);

        HSQLDB.instance.shutdown();
    }
}