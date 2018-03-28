package uk.co.senapt.desktop.shell;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

import static java.util.Collections.singletonList;


public class FavoritesButton extends AnimatedNodesList {

    public static final String FLOATING_ACTION_BUTTON = "floating-action-button";
    public static final String FLOATING_ACTION_BUTTON_ACTIVE = "floating-action-button-active";
    public static final String FLOATING_ACTION_BUTTON_CHILD = "floating-action-button-child";

    public FavoritesButton() {
        getStyleClass().addAll("favorites-button");

        setAlignment(Pos.CENTER);

        Button button1 = new Button();
        Label label1 = new Label();
        button1.getStyleClass().add("fab-favorites-icon");
        button1.setGraphic(label1);
        button1.getStyleClass().addAll(FLOATING_ACTION_BUTTON);

        Button button2 = new Button();
        Label label2 = FontAwesomeIconFactory.get().createIconLabel(FontAwesomeIcon.USER, "", "24", "16", ContentDisplay.GRAPHIC_ONLY);
        button2.setGraphic(label2);
        button2.getStyleClass().add(FLOATING_ACTION_BUTTON_CHILD);

        Button button3 = new Button();
        Label label3 = FontAwesomeIconFactory.get().createIconLabel(FontAwesomeIcon.CALENDAR, "", "24", "16", ContentDisplay.GRAPHIC_ONLY);
        button3.setGraphic(label3);
        button3.getStyleClass().add(FLOATING_ACTION_BUTTON_CHILD);

        Button button4 = new Button();
        Label label4 = FontAwesomeIconFactory.get().createIconLabel(FontAwesomeIcon.PENCIL, "", "24", "16", ContentDisplay.GRAPHIC_ONLY);
        button4.setGraphic(label4);
        button4.getStyleClass().add(FLOATING_ACTION_BUTTON_CHILD);

        Button button5 = new Button();
        Label label5 = FontAwesomeIconFactory.get().createIconLabel(FontAwesomeIcon.TRASH, "", "24", "16", ContentDisplay.GRAPHIC_ONLY);
        button5.setGraphic(label5);
        button5.getStyleClass().add(FLOATING_ACTION_BUTTON_CHILD);

        addAnimatedNode(button1,
                (expanded) -> singletonList(new KeyValue(label1.rotateProperty(),
                        expanded ? 45 : 0,
                        Interpolator.EASE_BOTH)));

        addAnimatedNode(button2);
        addAnimatedNode(button3);
        addAnimatedNode(button4);
        addAnimatedNode(button5);


        setRotate(180);
    }
}