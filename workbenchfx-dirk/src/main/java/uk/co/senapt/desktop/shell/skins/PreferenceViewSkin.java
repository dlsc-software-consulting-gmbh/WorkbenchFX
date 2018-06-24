package uk.co.senapt.desktop.shell.skins;

import javafx.scene.control.SkinBase;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import uk.co.senapt.desktop.shell.preferences.Category;
import uk.co.senapt.desktop.shell.preferences.PreferenceView;

public class PreferenceViewSkin extends SkinBase {

    private final BorderPane borderPane;

    public PreferenceViewSkin(PreferenceView view) {
        super(view);

        TreeView<Category> treeView = new TreeView(view.getRoot());
        treeView.setShowRoot(false);
        treeView.setCellFactory(v -> new TreeCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty && item != null) {
                    setText(item.getName());
                } else {
                    setText("");
                }
            }
        });

        borderPane = new BorderPane();
        borderPane.setLeft(treeView);
        getChildren().setAll(borderPane);

        treeView.getSelectionModel().selectedItemProperty().addListener(it -> updateView(treeView.getSelectionModel().getSelectedItem()));
        final TreeItem<Category> treeItem = view.getRoot().getChildren().get(0);
        treeView.getSelectionModel().select(treeItem);
    }

    private void updateView(TreeItem<Category> selectedCategory) {
        if (selectedCategory == null) {
            borderPane.setCenter(null);
        } else {
            borderPane.setCenter(selectedCategory.getValue().getContentProvider().get());
        }
    }
}
