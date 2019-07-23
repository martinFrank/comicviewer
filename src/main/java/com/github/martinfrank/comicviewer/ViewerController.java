package com.github.martinfrank.comicviewer;

import com.github.martinfrank.comicbrowser.xml.Page;
import com.github.martinfrank.comicbrowser.xml.WebsiteStructure;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ViewerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewerController.class);

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 1;
    private static final long KEEP_ALIVE_TIME = 1L;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private final BlockingQueue<Runnable> pool = new LinkedBlockingQueue<>();

    private ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, pool);

    private final StructureLoader comicLoader;

    @FXML
    public TreeView<Page> tree;

    @FXML
    public ImageView imageView;


    public ViewerController(StructureLoader comicLoader) {
        this.comicLoader = comicLoader;

    }


    public void prev(ActionEvent event) {
        TreeItem<Page> treeItem = tree.getSelectionModel().getSelectedItem();
        if (treeItem != null) {
            LOGGER.debug("move to the next from " + treeItem.getValue());
        }

    }

    public void next(ActionEvent event) {
        LOGGER.debug("event: next");
    }

    private void initTree() {
        Page rootPage = new Page();
        rootPage.setDetail("root");
        TreeItem<Page> rootItem = new TreeItem<>(rootPage);
        LOGGER.debug("tree {}", tree);
        for (WebsiteStructure structure : comicLoader.getStructures()) {
            LOGGER.debug("adding structure {}", structure.getTitle());
            Page titlePage = new Page();
            titlePage.setDetail(structure.getTitle());
            TreeItem<Page> item = new TreeItem<>(titlePage);
            for (Page page : structure.getPages()) {
                TreeItem<Page> pageItem = new TreeItem<>(page);
                item.getChildren().add(pageItem);
            }
            rootItem.getChildren().add(item);
        }
        tree.setRoot(rootItem);
        tree.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> setNext(newValue.getValue()));
    }

    private void setNext(Page value) {
        String imageUrl = value.getUrl();
        LOGGER.debug("image url to catch: {}", imageUrl);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            imageView.setImage(new Image(imageUrl));
        }
    }

    private void doIt() {
        if (threadPoolExecutor.getActiveCount() == 0) {
            LOGGER.debug("do it!!");
        } else {
            LOGGER.debug("still busy, skipping request");
        }
    }

    public void init() {
        initTree();
    }

    public void stop() {
        threadPoolExecutor.shutdown();
    }
}
