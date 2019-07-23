package com.github.martinfrank.comicviewer;

import com.github.martinfrank.comicbrowser.xml.WebsiteStructure;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewerController.class);

    private final ComicLoader comicLoader;

    @FXML
    public TreeView<String> tree;


    public ViewerController(ComicLoader comicLoader) {
        this.comicLoader = comicLoader;

    }


    public void prev(ActionEvent event) {
        LOGGER.debug("event: prev");
    }

    public void next(ActionEvent event) {
        LOGGER.debug("event: next");
    }

    private void initTree() {
        TreeItem<String> rootItem = new TreeItem<>("Inbox");
        LOGGER.debug("tree {}", tree);
        for (WebsiteStructure structure : comicLoader.getStructures()) {
            LOGGER.debug("adding structure {}", structure.getTitle());
            TreeItem<String> item = new TreeItem<>(structure.getTitle());
            rootItem.getChildren().add(item);
        }
        tree.setRoot(rootItem);
    }

    public void init() {
        initTree();
    }
}
