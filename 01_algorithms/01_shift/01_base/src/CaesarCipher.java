public class CaesarCipher {
    private final int key;

    public CaesarCipher(int key) {
        this.key = key;
    }

    public String encrypt(String plainText) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            char character = (char) (plainText.codePointAt(i) + key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    public String decrypt(String cipherText) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < cipherText.length(); i++) {
            char character = (char) (cipherText.codePointAt(i) - key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }
}
