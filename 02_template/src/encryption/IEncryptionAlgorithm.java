package encryption;

public interface IEncryptionAlgorithm {
    String encrypt(String cleartext, String keyFileName);
    String decrypt(String ciphertext, String keyFileName);
}
