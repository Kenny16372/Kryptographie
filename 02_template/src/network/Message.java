package network;

public class Message {
    private String content;
    private boolean encrypted = false;

    public Message(String content) {
        this.content = content;
    }

    public void encrypt(String algorithm, String keyFile){
        this.encrypted = true;
    }

    public void decrypt(String algorithm, String keyFile){
        this.encrypted = false;
    }

    @Override
    public String toString(){
        return content;
    }
}
