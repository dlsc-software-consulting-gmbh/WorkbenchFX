package com.dlsc.workbenchfx.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    public Dropdown(Node iconView, String title, String subtitle, Node... contentNodes) {
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

        for (Node content : contentNodes) {
            content.getStyleClass().add("contentNode");
            menuBox.getChildren().add(content);
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

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindFieldsToModel() {

    }

}
