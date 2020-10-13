package logging;

import configuration.Configuration;
import javafx.scene.control.TextArea;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Logger {
    private PrintStream out = null;
    private File logFile = null;
    private PrintStream redirection = null;

    public void startLogging(boolean encrypt, String algorithm) {
        this.out = System.out;

        long timestamp = System.currentTimeMillis() / 1000L;
        try {
            File logDir = new File(Configuration.instance.logDirectory);
            if(!logDir.exists()){
                if(!logDir.mkdir()){
                    System.err.println("Couldn't create log directory");
                }
            }

            this.logFile = new File(Configuration.instance.logDirectory + (encrypt ? "en" : "de") + "crypt_" + algorithm.toUpperCase() + "_" + timestamp + ".log");

            if (!this.logFile.createNewFile()) {
                System.err.println("Couldn't create log file: " + this.logFile.getName());
            }

            this.redirection = new PrintStream(this.logFile);

            System.setOut(this.redirection);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (this.redirection != null) {
            this.redirection.close();
        }

        this.redirection = null;
        this.logFile = null;

        System.setOut(this.out);

        this.out = null;
    }

    public static void displayLatestLogFile(TextArea output) {
        try (BufferedReader br = new BufferedReader(new FileReader(Configuration.instance.logDirectory + getLatestFile()))) {
            output.setText(br.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLatestFile() {
        File[] logFiles = new File(Configuration.instance.logDirectory).listFiles();
        Map<Long, String> map = new HashMap<>();
        long max = 0L;

        if (logFiles == null) {
            return null;
        }

        for (File file : logFiles) {
            String filename = file.getName();

            int indexLastUnderscore = filename.lastIndexOf('_');

            long value = Long.parseLong(filename.substring(indexLastUnderscore + 1, filename.length() - 4));

            max = Math.max(max, value);

            map.put(value, filename);
        }

        return map.get(max);
    }
}
