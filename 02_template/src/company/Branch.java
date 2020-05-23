package company;

import configuration.Configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Branch {
    private String name;

    public Branch(String name){
        this.name = name;
    }

    public void storeKeys(Map<String, Map<Character, String>> keys){
        // stores keys for this branch in the format "<n>\n<e>"
        // public key
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(Configuration.instance.keyFileDirectory + this.name + "_pub.txt"))) {
            bw.write("{\"n\":");
            bw.write(keys.get("publicKey").get('n') + ",");
            bw.write("\"e\":");
            bw.write(keys.get("publicKey").get('e'));
            bw.write("}");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // private key
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(Configuration.instance.keyFileDirectory + this.name + "_pri.txt"))) {
            bw.write("{\"n\":");
            bw.write(keys.get("privateKey").get('n') + ",");
            bw.write("\"e\":");
            bw.write(keys.get("privateKey").get('e'));
            bw.write("}");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
}
