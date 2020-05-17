import java.math.BigInteger;

public class Utility {
    public static BigInteger stringToNumber(String message) {
        StringBuilder stringBuilder = new StringBuilder("1");

        for (int i = 0; i < message.length(); ++i) {
            int asc = message.charAt(i);

            if (String.valueOf(asc).length() <= 2) {
                stringBuilder.append("0");
            }

            stringBuilder.append(asc);
        }

        return new BigInteger(stringBuilder.toString());
    }

    public static String numberToString(BigInteger number) {
        StringBuilder stringBuilder = new StringBuilder();

        String string = number.toString();
        string = string.substring(1);

        for (int i = 0; i < string.length(); i += 3) {
            String blockString = string.substring(i, i + 3);
            int block = Integer.parseInt(blockString);
            stringBuilder.append((char) block);
        }

        return stringBuilder.toString();
    }
}