package parser;

import configuration.Configuration;
import javafx.scene.control.TextArea;

import java.io.File;

public class ShowAlgorithm {

    public static void display(TextArea output) {
        StringBuilder algorithms = new StringBuilder();

        for (String component : getComponentNames()) {
            // remove crackers
            if (!component.contains("_cracker")) {
                algorithms.append(component);
                algorithms.append('\n');
            }
        }
        output.setText(algorithms.toString());
    }

    private static String[] getComponentNames() {
        // get folder of component directory
        File folder = new File(Configuration.instance.componentDirectory);

        // get files inside this folder
        File[] files = folder.listFiles();
        if (files != null) {
            // return filenames without .jar extension
            String[] componentNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                componentNames[i] = fileName.substring(0, fileName.length() - 4);
            }
            return componentNames;
        } else {
            return new String[0];
        }
    }
}
