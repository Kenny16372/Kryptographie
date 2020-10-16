package parser;

import javafx.scene.control.TextArea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpHandler extends Handler{

    public HelpHandler() {
        this.successor = null;
        this.pattern = Pattern.compile(".*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {
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
