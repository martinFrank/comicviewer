package com.github.martinfrank.comicviewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);
    private final ClassLoader classLoader;

    public ResourceManager(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public URL getGuiRoot() throws MalformedURLException {
        return getResourceURL("viewer.fxml");
    }

    public URL getInput() throws MalformedURLException {
        //FIXME properly handle directory! use a properties file?
        try {
            File launchingDir = new File(ResourceManager.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI());
            File directory = new File(launchingDir, "structures");
            LOGGER.debug("parent: {}, {}", directory.getAbsoluteFile(), directory.exists());
            if (directory.exists()) {
                return directory.toURI().toURL();
            } else {
                directory = new File(launchingDir.getParentFile().getParent(), "structures");
                LOGGER.debug("parent2: {}, {}", directory.getAbsoluteFile(), directory.exists());
                return directory.toURI().toURL();
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new File("structures").toURI().toURL();
    }

    private URL getResourceURL(String str) throws MalformedURLException {
        URL url = classLoader.getResource(str);
        if (url != null) {
            return new File(url.getPath()).toURI().toURL();
        }
        throw new MalformedURLException("url=null");
    }


}
