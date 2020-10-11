package parser;

import javafx.scene.control.TextArea;

public class InputHandler implements IHandler {
    public void handle(String raw, TextArea output) {
        // remove leading and trailing whitespace
        raw = raw.trim();

        // get main command
        int indexFirstWhitespace = raw.indexOf(" ");

        String command, rest;
        if (indexFirstWhitespace == -1) {
            // no whitespace in input
            command = raw;
            rest = "";
        } else {
            // split at first whitespace
            command = raw.substring(0, indexFirstWhitespace);
            rest = raw.substring(indexFirstWhitespace + 1).stripLeading();
        }

        // switch between possible commands
        switch (command.toLowerCase()) {
            case "show":
                new ShowHandler().handle(rest, output);
                break;
            case "encrypt":
            case "decrypt":
                // submitting raw to keep the information if text is to be encrypted or decrypted
                new EncryptionHandler().handle(raw, output);
                break;
            case "crack":
                new CrackingHandler().handle(rest, output);
                break;
            case "register":
                new RegisterParticipant().handle(rest, output);
                break;
            case "create":
                new CreateChannel().handle(rest, output);
                break;
            case "drop":
                new DropHandler().handle(rest, output);
                break;
            case "send":
                new SendMessage().handle(rest, output);
                break;
            case "intrude":
                new IntrudeChannel().handle(rest, output);
                break;
            case "help":
            default:
                output.setText("Mögliche Befehle:\n" +
                        "\"show algorithm\": Zeigt Verschlüsselungsalgorithmen\n" +
                        "\"encrypt message \"[message]\" using [algorithm] and keyfile [filename]\": Verschlüsselt die Nachricht [message] mit dem Algorithmus [algorithm] " +
                        "und der Schlüsseldatei [filename]\n" +
                        "\"decrypt message \"[message]\" using [algorithm] and keyfile [filename]\": Entschlüsselt die Nachricht [message] mit dem Algorithmus [algorithm] " +
                        "und der Schlüsseldatei [filename]\n" +
                        "\"crack encrypted message \"[message]\" using [algorithm]\": Versucht, die Nachricht [message] mit dem Algorithmus [algorithm] zu knacken\n" +
                        "\"register participant [name] with type [normal | intruder]\": Registriert den Teilnehmer [name] mit dem Typ Normal bzw. Eindringling\n" +
                        "\"create channel [name] from [participant01] to [participant02]\": Erstellt einen Kommunikationschannel [name] zwischen [participant01] und [participant02]\n" +
                        "\"show channel\": Zeigt verfügbare Channel\n" +
                        "\"drop channel [name]\": Entfernt Channel [name]\n" +
                        "\"intrude channel [name] by [participant]\": [participant] dringt in Channel [name] ein\n" +
                        "\"send message \"[message]\" from [participant01] to [participant02] using [algorithm] and keyfile [name]\": Sendet die Nachricht [message] " +
                        "von [participant01] zu [participant02]; diese wird mit dem Algorithmus [algorithm] und der Schlüsseldatei [filename] verschlüsselt");
        }
    }
}
