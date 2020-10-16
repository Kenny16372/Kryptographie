package parser;

import configuration.Configuration;
import javafx.scene.control.TextArea;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowAlgorithmHandler extends Handler{

    public ShowAlgorithmHandler() {
        this.successor = new HelpHandler();
        this.pattern = Pattern.compile("^\\s*show\\s+algorithm\\s*$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void handle(Matcher matcher, TextArea output) {
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

    public static String[] getComponentNames() {
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
