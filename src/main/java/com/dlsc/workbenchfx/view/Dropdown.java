package com.dlsc.workbenchfx.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 29.12.17.
 */
public class Dropdown extends VBox implements View {
    private static final Logger LOGGER =
            LogManager.getLogger(Dropdown.class.getName());

    private final HBox buttonBox;
    private final VBox menuBox;

    private final Node iconView;
    private final VBox descriptionBox;
    private final Label titleLbl;
    private final Label subtitleLbl;
    private final Node[] contentNodes;
    private final FontAwesomeIconView arrowIcon;

    private final BooleanProperty expanded;

    public Dropdown(Node iconView, String title, String subtitle, Node... contentNodes) {
        this.expanded = new SimpleBooleanProperty();

        this.buttonBox = new HBox();
        this.menuBox = new VBox();
        this.descriptionBox = new VBox();

        this.iconView = iconView;
        if (this.iconView instanceof ImageView) {
            ImageView imageView = ((ImageView) this.iconView);
            // Calculate ratio
            double ratio = imageView.getImage().getWidth() / imageView.getImage().getHeight();

            // Bind the dimensions of the ImageView to the dropdown's height
            imageView.fitHeightProperty().bind(buttonBox.prefHeightProperty().subtract(15));
            imageView.fitWidthProperty().bind(buttonBox.prefHeightProperty().subtract(15).multiply(ratio));
        }
        this.titleLbl = new Label(title);
        this.subtitleLbl = new Label(subtitle);
        this.contentNodes = contentNodes;
        arrowIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);
        init();
        setupListeners();
        setupEventHandlers();
        setupBindings();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeSelf() {
        getStyleClass().add("dropdown");
        buttonBox.getStyleClass().add("buttonBox");

        iconView.getStyleClass().add("iconView");
        titleLbl.getStyleClass().add("titleLbl");
        subtitleLbl.getStyleClass().add("subtitleLbl");
        descriptionBox.getStyleClass().add("descriptionBox");
        arrowIcon.getStyleClass().add("arrowIcon");

        menuBox.getStyleClass().add("menuBox");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParts() {

        descriptionBox.getChildren().addAll(
                titleLbl,
                subtitleLbl
        );

        buttonBox.getChildren().addAll(
                iconView,
                descriptionBox,
                arrowIcon
        );

        for (Node contentNode : contentNodes) {
            contentNode.getStyleClass().add("contentNode");
            HBox contentBox = new HBox(contentNode);
            contentBox.getStyleClass().add("contentBox");
            menuBox.getChildren().add(contentBox);
        }

        getChildren().addAll(
                buttonBox,
                menuBox
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutParts() {
//        menuBox.relocate(buttonBox.getLayoutX() - 2, contentY - (1 - userOptionsVisibility.get()) * menuBox.getHeight());
        menuBox.relocate(100, 0);
//        menuBox.relocate(500, -50);
        menuBox.setVisible(false);
//        translateYProperty().bind(menuBox.heightProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindFieldsToModel() {

    }

    private void setupEventHandlers() {
        buttonBox.setOnMouseClicked(event -> expanded.set(!expanded.get()));
    }

    private void setupBindings() {
        menuBox.visibleProperty().bind(expanded);
        menuBox.prefWidthProperty().bind(widthProperty());
    }

    private void setupListeners() {
        expanded.addListener((observable, oldValue, newValue) ->
                arrowIcon.setIcon(newValue ? FontAwesomeIcon.ANGLE_UP : FontAwesomeIcon.ANGLE_DOWN)
        );
    }
}
