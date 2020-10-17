package configuration;

public enum Configuration {
    instance;

    // common
    public final String userDirectory = System.getProperty("user.dir") + "/02_template";
    public final String fileSeparator = System.getProperty("file.separator");

    // database
    public final String dataDirectory = userDirectory + fileSeparator + "hsqldb" + fileSeparator;
    public final String databaseFile = dataDirectory + "datastore.db";
    public final String driverName = "jdbc:hsqldb:";
    public final String username = "sa";
    public final String password = "";

    // component
    public String componentDirectory = userDirectory + fileSeparator + "component" + fileSeparator;

    // encryption
    public String keyFileDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public int defaultKeyLength = 64;

    // logging
    public String logDirectory = userDirectory + fileSeparator + "log" + fileSeparator;

    // debug
    public boolean debugMode = false;

    public void switchDebugMode() {
        this.debugMode = !this.debugMode;
    }
}