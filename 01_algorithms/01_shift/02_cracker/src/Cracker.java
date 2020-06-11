import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

public class Cracker {
    public String decrypt(String encryptedMessage) {
        // store System.in and System.out
        PrintStream outOld = System.out;
        InputStream inOld = System.in;

        // access System.in and System.out
        InputStream in = new ByteArrayInputStream(encryptedMessage.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        System.setIn(in);
        System.setOut(out);

        // run the application
        Application.main();

        System.setIn(inOld);
        System.setOut(outOld);

        Scanner scanner = new Scanner(outputStream.toString());

        return scanner.findAll("\\t(\\w+)[\\r\\n]+").map(MatchResult::group).map(String::trim).collect(Collectors.joining(", "));
    }
}
