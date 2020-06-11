package parser;

import application.Application;
import javafx.scene.control.TextArea;
import persistence.HSQLDB;

public class RegisterParticipant {
    public static void handle(String text, TextArea output){

        int indexFirstWhitespace =  text.indexOf(" ");
        if(indexFirstWhitespace == -1){
            return;
        }
        String command = text.substring(0, indexFirstWhitespace);
        String rest = text.substring(indexFirstWhitespace + 1).stripLeading();

        if(command.stripLeading().equals("participant")){
            getName(rest, output);
        }
    }

    private static void getName(String text, TextArea output){

        int indexFirstWhitespace =  text.indexOf(" ");
        if(indexFirstWhitespace == -1){
            return;
        }
        String name = text.substring(0, indexFirstWhitespace);
        String rest = text.substring(indexFirstWhitespace + 1).stripLeading();
        String type = rest.substring(10).stripLeading();

        HSQLDB.instance.setupConnection();
        int result = HSQLDB.instance.getCountName(name);
        HSQLDB.instance.shutdown();

        String messageCreate = "participant " + name + " with type " + type + " registered and postbox_" + name + " created";
        String messageExist = "participant " + name + " already exists, using existing postbox_" + name;

        if (result < 1) {
            output.setText(messageCreate);
            HSQLDB.instance.setupConnection();
            int typeID = HSQLDB.instance.getTypeID(type);
            HSQLDB.instance.insertDataTableParticipants(name, typeID);
            HSQLDB.instance.createTablePostbox(name);
            HSQLDB.instance.shutdown();
        } else output.setText(messageExist);
    }

}
