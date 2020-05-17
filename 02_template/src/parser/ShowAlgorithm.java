package parser;

import configuration.Configuration;

import java.io.File;

public class ShowAlgorithm {
    public static void handle(String text){
        if(text.stripLeading().equals("algorithm")){
            display();
        }
    }

    private static void display(){
        for(String component: getComponentNames()){
            System.out.println(component);
        }
    }

    private static String[] getComponentNames(){
        // get folder of component directory
        File folder = new File(Configuration.instance.componentDirectory);
        // get files inside this folder
        File[] files = folder.listFiles();
        if(files != null) {
            // return filenames without .jar extension
            String[] componentNames = new String[files.length];
            for(int i = 0; i < files.length; i++){
                String fileName = files[i].getName();
                componentNames[i] = fileName.substring(0, fileName.length() - 4);
            }
            return componentNames;
        }
        else {
            return new String[0];
        }
    }
}
