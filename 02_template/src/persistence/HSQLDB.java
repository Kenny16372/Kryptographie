package persistence;

import configuration.Configuration;

import java.sql.*;
import java.util.*;

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

    private synchronized ResultSet select(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);

            statement.close();

            if (result.isLast()) {
                return null;
            }

            return result;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return null;
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
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(", ");
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
        sqlStringBuilder.append("INSERT INTO types (").append("id").append(", ").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(", ").append("'").append(name.toLowerCase()).append("'");
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
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("name VARCHAR(50) NOT NULL").append(", ");
        sqlStringBuilder01.append("type_id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxParticipants ON participants (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE participants ADD CONSTRAINT fkParticipants01 ");
        sqlStringBuilder03.append("FOREIGN KEY (type_id) ");
        sqlStringBuilder03.append("REFERENCES types (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());
    }

    public int insertDataTableParticipants(String name, int typeID) {      // TODO: Escaping user input
        System.out.println("--- createParticipant " + name.toLowerCase());
        int nextID = getNextID("participants") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (").append("id").append(", ").append("name").append(", ").append("type_id").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(", ").append("'").append(name.toLowerCase()).append("'").append(", ").append(typeID);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
        return nextID;
    }

    public List<Map<String, String>> getAllParticipants() {

        ResultSet resultSet = select("SELECT participants.name AS Name, participants.id AS Id, types.name AS Type " +
                "FROM participants INNER JOIN types ON participants.type_id = types.id");

        List<Map<String, String>> participants = new ArrayList<>();

        if (resultSet == null) {
            return participants;
        }

        try {
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String type = resultSet.getString("Type");
                String id = Integer.toString(resultSet.getInt("Id"));

                Map<String, String> participant = new HashMap<>();

                participant.put("name", name);
                participant.put("type", type);
                participant.put("id", id);

                participants.add(participant);
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return new ArrayList<>();
        }

        return participants;
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
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(", ");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxTypes ON algorithms (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    public void insertDataTableAlgorithms(String name) {
        if (getAlgorithmId(name) != -1) {
            // algorithm is already stored in the table
            return;
        }

        int nextID = getNextID("algorithms") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO algorithms (").append("id").append(", ").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(", ").append("'").append(name.toLowerCase()).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    private int getAlgorithmId(String name) {
        String sql = "SELECT id FROM algorithms WHERE name='" +
                name.toLowerCase() +
                "' LIMIT 1";
        ResultSet resultSet = select(sql);

        if (resultSet == null) {
            return -1;
        }

        try {
            resultSet.next();

            return resultSet.getInt("id");
        } catch (SQLException e) {
            // resultSet is empty
            return -1;
        }
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
        sqlStringBuilder01.append("name VARCHAR(25) NOT NULL").append(", ");
        sqlStringBuilder01.append("participant_01 TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("participant_02 TINYINT NOT NULL").append(", ");
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

    public void insertDataTableChannel(String name, int participant01, int participant02) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO channel (").append("name").append(", ").append("participant_01").append(", ").append("participant_02").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(name).append("'").append(", ").append(participant01).append(", ").append(participant02);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    public Map<String, Set<Integer>> getAllChannels() {
        Map<String, Set<Integer>> channel = new HashMap<>();

        ResultSet resultSet = select("SELECT * from channel");

        if (resultSet == null) {
            return channel;
        }

        try {
            while (resultSet.next()) {
                String channelName = resultSet.getString("name");
                int participant1 = resultSet.getInt("participant_01");
                int participant2 = resultSet.getInt("participant_02");

                Set<Integer> participants = new HashSet<>();

                participants.add(participant1);
                participants.add(participant2);

                channel.put(channelName, participants);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public void dropChannel(String channelName) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DELETE FROM channel WHERE name='");
        sqlStringBuilder.append(channelName);
        sqlStringBuilder.append("'");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    // Table messages

    public void dropTableMessages() {
        System.out.println("--- dropTableMessages");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE messages");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableMessages() {
        System.out.println("--- createTableMessages");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE messages ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("participant_from_id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("participant_to_id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("plain_message VARCHAR(50) NOT NULL").append(", ");
        sqlStringBuilder01.append("algorithm_id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("encrypted_message VARCHAR(50) NOT NULL").append(", ");
        sqlStringBuilder01.append("keyfile VARCHAR(50) NOT NULL").append(", ");
        sqlStringBuilder01.append("timestamp INTEGER").append(", ");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE messages ADD CONSTRAINT fkMessages01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_from_id) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE messages ADD CONSTRAINT fkMessages02 ");
        sqlStringBuilder03.append("FOREIGN KEY (participant_to_id) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());

        StringBuilder sqlStringBuilder04 = new StringBuilder();
        sqlStringBuilder04.append("ALTER TABLE messages ADD CONSTRAINT fkMessages03 ");
        sqlStringBuilder04.append("FOREIGN KEY (algorithm_id) ");
        sqlStringBuilder04.append("REFERENCES algorithms (id) ");
        sqlStringBuilder04.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder04.toString());
        update(sqlStringBuilder04.toString());
    }

    public void insertDataTableMessages(int participantFromID, int participantToID, String plainMessage, String algorithmName, String encryptedMessage, String keyfile) {
        int algorithmId = getAlgorithmId(algorithmName);

        int nextID = getNextID("messages") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO messages (")
                .append("id")
                .append(", ")
                .append("participant_from_id")
                .append(", ")
                .append("participant_to_id")
                .append(", ")
                .append("plain_message")
                .append(", ")
                .append("algorithm_id")
                .append(", ")
                .append("encrypted_message")
                .append(", ")
                .append("keyfile")
                .append(", ")
                .append("timestamp")
                .append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(")
                .append(nextID)
                .append(", ")
                .append(participantFromID)
                .append(", ")
                .append(participantToID)
                .append(", ")
                .append("'")
                .append(plainMessage)
                .append("'")
                .append(", ")
                .append(algorithmId)
                .append(", ")
                .append("'")
                .append(encryptedMessage)
                .append("'")
                .append(", ")
                .append("'")
                .append(keyfile)
                .append("'")
                .append(", ")
                .append(System.currentTimeMillis() / 1000);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    // Table postbox_[participant_name]

    public void dropTablePostbox(String participantName) {
        System.out.println("--- dropTablePostbox_" + participantName.toLowerCase());

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE postbox_").append(participantName.toLowerCase());
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTablePostbox(String participantName) {
        System.out.println("--- createTablePostbox_" + participantName.toLowerCase());

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE postbox_").append(participantName.toLowerCase()).append(" (");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("participant_from_id TINYINT NOT NULL").append(", ");
        sqlStringBuilder01.append("message VARCHAR(50) NOT NULL").append(", ");
        sqlStringBuilder01.append("timestamp INTEGER").append(", ");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE postbox_").append(participantName.toLowerCase()).append(" ADD CONSTRAINT fkPostbox_" + participantName.toLowerCase() + " ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_from_id) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    public void insertDataTablePostbox(String participantName, int participantFromID, String message) {
        int nextID = getNextID("postbox_" + participantName.toLowerCase()) + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO postbox_").append(participantName.toLowerCase()).append(" (").append("id").append(", ").append("participant_from_id").append(", ").append("message").append(", ").append("timestamp").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(", ").append(participantFromID).append(", ").append("'").append(message).append("'").append(", ").append(System.currentTimeMillis() / 1000);
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