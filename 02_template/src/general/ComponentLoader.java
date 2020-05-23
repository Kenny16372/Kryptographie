package general;

import configuration.Configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ComponentLoader {
    private static Map<String, URLClassLoader> classLoaderMap = new HashMap<>();

    public static URLClassLoader getClassLoader(String algorithm){
        algorithm = algorithm.toLowerCase();

        if(classLoaderMap.get(algorithm) != null){
            return classLoaderMap.get(algorithm);
        }

        File[] components = new File(Configuration.instance.componentDirectory).listFiles();

        if(components == null){
            return null;
        }

        for(File file: components){
            if(file.getName().substring(0, file.getName().length() - 4).equalsIgnoreCase(algorithm)){
                try {
                    URL[] urls = {file.toURI().toURL()};
                    URLClassLoader classLoader = new URLClassLoader(urls);
                    classLoaderMap.put(algorithm, classLoader);
                    return classLoader;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        System.err.println("Couldn't load algorithm " + algorithm);
        return null;
    }
}
