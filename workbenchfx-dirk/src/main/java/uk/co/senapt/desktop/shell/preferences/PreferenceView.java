package uk.co.senapt.desktop.shell.preferences;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javax.inject.Inject;
import javax.inject.Singleton;
import uk.co.senapt.desktop.shell.Shell;
import uk.co.senapt.desktop.shell.skins.PreferenceViewSkin;

@Singleton
public class PreferenceView extends Control {

    private final Category root = new Category("root");

    @Inject
    public PreferenceView(Shell shell) {
        getStyleClass().add("preferences-view");

        setPrefSize(800, 500);

        root.setExpanded(true);

        Bindings.bindContent(categories, shell.getPreferenceCategories());
        Bindings.bindContent(root.getChildren(), categories);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PreferenceViewSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return PreferenceView.class.getResource("preferences.css").toExternalForm();
    }

    public final Category getRoot() {
        return root;
    }

    private final ObservableList<Category> categories = FXCollections.observableArrayList();

    public final ObservableList<Category> getCategories() {
        return categories;
    }
}
