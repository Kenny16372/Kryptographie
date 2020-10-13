package network;

public class Message {
    private final String content;
    private final String algorithm;
    private final int idSender;
    // the keyfile will not be sent to increase the workload on the intruder

    public Message(String content, String algorithm, int idSender) {
        this.content = content;
        this.algorithm = algorithm;
        this.idSender = idSender;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getIdSender() {
        return idSender;
    }

    @Override
    public String toString() {
        return content;
    }
}
