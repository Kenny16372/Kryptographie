package network;

public enum ParticipantType {
    normal, intruder;

    public static ParticipantType fromString(String typeString){
        switch (typeString.toLowerCase()){
            case "normal":
                return normal;
            case "intruder":
                return intruder;
            default:
                return null;
        }
    }
}
