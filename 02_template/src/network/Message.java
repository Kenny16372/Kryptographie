package network;

public class Message {
    private final String content;
    private final String algorithm;
    private final int idSender;
    private final String keyFile;

    public Message(String content, String algorithm, int idSender, String keyFile) {
        this.content = content;
        this.algorithm = algorithm;
        this.idSender = idSender;
        this.keyFile = keyFile;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getIdSender() {
        return idSender;
    }

    public String getKeyFileName() {
        return keyFile;
    }

    @Override
    public String toString() {
        return content;
    }
}
