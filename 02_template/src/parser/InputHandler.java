package parser;

import javafx.scene.control.TextArea;

public class InputHandler implements IHandler{
    public void handle(String raw, TextArea output){
        // remove leading and trailing whitespace
        raw = raw.trim();

        // get main command
        int indexFirstWhitespace =  raw.indexOf(" ");
        if(indexFirstWhitespace == -1){
            return;
        }
        String command = raw.substring(0, indexFirstWhitespace);
        String rest = raw.substring(indexFirstWhitespace + 1).stripLeading();

        // switch between possible commands
        switch(command.toLowerCase()){
            case "show":
                ShowAlgorithm.handle(rest, output);
                break;
            case "encrypt": case "decrypt":
                // submitting raw to keep the information if text is to be encrypted or decrypted
                new EncryptionHandler().handle(raw, output);
                break;
            case "crack":
                new CrackingHandler().handle(rest, output);
            case "register":
                new RegisterParticipant().handle(rest, output);
        }
    }
}
