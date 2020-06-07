package persistence;

import configuration.Configuration;

import java.sql.*;

public enum HSQLDB {
    instance;

    private Connection connection;

    public void setupConnection() {
        System.out.println("--- setupConnection");

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = Configuration.instance.driverName + Configuration.instance.databaseFile;
            connection = DriverManager.getConnection(databaseURL, Configuration.instance.username, Configuration.instance.password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private synchronized void update(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);

            if (result == -1) {
                System.out.println("error executing " + sqlStatement);
            }

            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    private int getNextID(String table) {
        int nextID = 0;

        try {
            String sqlStatement = "SELECT max(id) FROM " + table;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);

            while (resultSet.next()) {
                nextID = resultSet.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return nextID;
    }

    // Table types

    public void dropTableTypes() {
        System.out.println("--- dropTableTypes");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE types");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableTypes() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE types ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxTypes ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    public void insertDataTableTypes(String name) {
        int nextID = getNextID("types") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO types (").append("id").append(",").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    // Table participants

    public void dropTableParticipants() {
        System.out.println("--- dropTableParticipants");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE participants");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableParticipants() {
        System.out.println("--- createTableParticipants");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE participants ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("type_id TINYINT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxParticipants ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE participants ADD CONSTRAINT fkParticipants01 ");
        sqlStringBuilder03.append("FOREIGN KEY (type_id) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());
    }

    public void insertDataTableParticipants(String name, int typeID) {
        int nextID = getNextID("participants") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (").append("id").append(",").append("name").append(",").append("type_id").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'").append(",").append(typeID);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    // Table algorithms

    public void dropTableAlgorithms() {
        System.out.println("--- dropTableAlgorithms");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE algorithms");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableAlgorithms() {
        System.out.println("--- createTableAlgorithms");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE algorithms ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxTypes ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    public void insertDataTableAlgorithms(String name) {
        int nextID = getNextID("algorithms") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO algorithms (").append("id").append(",").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    // Table channel

    public void dropTableChannel() {
        System.out.println("--- dropTableChannel");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE channel");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableChannel() {
        System.out.println("--- createTableChannel");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE channel ( ");
        sqlStringBuilder01.append("name VARCHAR(25) NOT NULL").append(",");
        sqlStringBuilder01.append("participant_01 TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_02 TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (name)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE channel ADD CONSTRAINT fkChannel01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_01) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE channel ADD CONSTRAINT fkChannel02 ");
        sqlStringBuilder03.append("FOREIGN KEY (participant_02) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());
    }

    public void insertDataTableChannel(String name, int participant_01, int participant_02) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO channel (").append("name").append(",").append("participant_01").append(",").append("participant_02").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(name).append("'").append(participant_01).append(participant_02);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    public void shutdown() {
        System.out.println("--- shutdown");

        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
}