package com.github.martinfrank.comicviewer;

import com.github.martinfrank.comicbrowser.xml.WebsiteStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComicLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComicLoader.class);
    private final List<WebsiteStructure> structures = new ArrayList<>();

    public ComicLoader(URL inputDirectory) {
        List<File> structureFiles = getFiles(inputDirectory);
        LOGGER.debug("structureFiles {}", structureFiles);
        createStructures(structureFiles);
    }

    public List<WebsiteStructure> getStructures() {
        return structures;
    }

    private void createStructures(List<File> structureFiles) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(WebsiteStructure.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            for (File inputXmlFile : structureFiles) {
                WebsiteStructure structure = (WebsiteStructure) unmarshaller.unmarshal(inputXmlFile);
                LOGGER.debug("successfully parsed structure {}", structure.getTitle());
                structures.add(structure);
            }
        } catch (JAXBException e) {
            LOGGER.debug("error parsing file{}", e);
        }

    }

    @SuppressWarnings("ConstantConditions")
    private List<File> getFiles(URL inputDirectory) {
        try {
            return Arrays.stream(new File(inputDirectory.getFile()).listFiles()).
                    filter(f -> f.getName().toLowerCase().endsWith(".xml")).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }
}
