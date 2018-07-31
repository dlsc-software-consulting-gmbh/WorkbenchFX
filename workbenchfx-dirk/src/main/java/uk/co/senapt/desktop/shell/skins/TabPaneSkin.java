package uk.co.senapt.desktop.shell.skins;

import javafx.beans.Observable;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uk.co.senapt.desktop.shell.Tab;
import uk.co.senapt.desktop.shell.TabPane;

import java.util.ArrayList;
import java.util.List;

public class TabPaneSkin extends SkinBase<TabPane> {

    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    private final HBox header;
    private final VBox content;
    private TabView selected;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TabPaneSkin(TabPane control) {
        super(control);

        header = new HBox();
        header.getStyleClass().add("tab-header");

        content = new VBox();
        content.getStyleClass().add("tab-content");

        control.getTabs().addListener((Observable obs) -> buildTabs());
        control.fillHeaderProperty().addListener(obs -> buildTabs());

        buildTabs();
        getChildren().addAll(header, content);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        double headerHeight = header.prefHeight(-1);
        header.resizeRelocate(contentX, contentY, contentWidth, headerHeight);
        content.resizeRelocate(contentX, contentY + headerHeight, contentWidth, content.prefHeight(-1));
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double height = super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
        System.out.println("Computed Min height = " + height);
        return height;
        // TODO implement to avoid overlap components
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double height = super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        System.out.println("Computed Pref height = " + height);
        return height;
        // TODO implement to avoid overlap components
    }

    private void buildTabs () {
        List<TabView> tabs = new ArrayList<>();
        TabView first = null;

        for (Tab tab : getSkinnable().getTabs()) {
            TabView view = new TabView(tab);
            tabs.add(view);

            if (getSkinnable().isFillHeader()) {
                HBox.setHgrow(view, Priority.ALWAYS);
                view.setMaxWidth(Double.MAX_VALUE);
            }

            view.setMaxHeight(Double.MAX_VALUE);

            if (first == null) {
                first = view;
            }
        }

        header.getChildren().setAll(tabs);
        setSelected(first);
    }

    private void setSelected (TabView selected) {
        if (this.selected != null) {
            this.selected.pseudoClassStateChanged(SELECTED, false);
        }

        this.selected = selected;

        if (this.selected != null && this.selected.getContent() != null) {
            this.selected.pseudoClassStateChanged(SELECTED, true);
            content.getChildren().setAll(this.selected.getContent());
            VBox.setVgrow(this.selected.getContent(), Priority.ALWAYS);
        }
        else {
            content.getChildren().clear();
        }
    }

    private class TabView extends Label {

        private Tab tab;

        TabView (Tab tab) {
            this.tab = tab;

            setOnMouseClicked(evt -> setSelected(this));
            getStyleClass().add("tab");

            if (tab.getIcon() != null) {
                getStyleClass().add(tab.getIcon());
            }

            if (tab.getName() != null) {
                setText(tab.getName());
            }

            if (tab.getGraphic() != null) {
                setGraphic(tab.getGraphic());
            }

            setContentDisplay(ContentDisplay.RIGHT);
        }

        public Node getContent () {
            return tab.getContent();
        }

    }
}
