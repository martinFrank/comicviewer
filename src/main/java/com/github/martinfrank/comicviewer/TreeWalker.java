package com.github.martinfrank.comicviewer;

import com.github.martinfrank.comicbrowser.xml.Page;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeWalker {


    public static void moveDown(TreeView<Page> treeView) {
        TreeItem<Page> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            item.setExpanded(true);
            TreeItem<Page> next = item;
            if (treeView.getRoot().equals(next)) {
                next = treeView.getRoot().getChildren().get(0);
                treeView.getSelectionModel().select(next);
                next.setExpanded(true);
                return;
            }
            if (!item.getChildren().isEmpty()) {
                next = item.getChildren().get(0);
                next.setExpanded(true);
                treeView.getSelectionModel().select(next);
                return;
            }
            int index = item.getParent().getChildren().indexOf(item) + 1;
            if (index == item.getParent().getChildren().size()) {
                next = getNextParent(treeView, next);
            } else {
                next = item.getParent().getChildren().get(index);
            }
            treeView.getSelectionModel().select(next);
        }
    }

    private static TreeItem<Page> getNextParent(TreeView<Page> treeView, TreeItem<Page> next) {
        TreeItem<Page> parent = next.getParent();
        if (treeView.getRoot().equals(parent)) {
            return treeView.getRoot();
        } else {
            int index = parent.getParent().getChildren().indexOf(parent) + 1;
            if (index == parent.getParent().getChildren().size()) {
                return getNextParent(treeView, parent);
            } else {
                return parent.getParent().getChildren().get(index);
            }
        }
    }

    public static void moveUp(TreeView<Page> treeView) {
        TreeItem<Page> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            TreeItem<Page> prev = item;
            if (treeView.getRoot().equals(item)) {
                prev = getLast(treeView);
            } else if (item.getParent().getChildren().indexOf(item) == 0) {
                prev = item.getParent();
            } else {
                int index = item.getParent().getChildren().indexOf(item) - 1;
                prev = item.getParent().getChildren().get(index);
                while (prev.getChildren().size() > 0) {
                    prev.setExpanded(true);
                    int prevIndex = prev.getChildren().size() - 1;
                    prev = prev.getChildren().get(prevIndex);
                }
            }
            treeView.getSelectionModel().select(prev);
        }

    }

    private static TreeItem<Page> getLast(TreeView<Page> treeView) {
        int lastOfRootIndex = treeView.getRoot().getChildren().size() - 1;
        TreeItem<Page> item = treeView.getRoot().getChildren().get(lastOfRootIndex);
        while (item.getChildren().size() > 0) {
            int lastIndex = item.getChildren().size() - 1;
            item = item.getChildren().get(lastIndex);
        }
        return item;
    }

}
