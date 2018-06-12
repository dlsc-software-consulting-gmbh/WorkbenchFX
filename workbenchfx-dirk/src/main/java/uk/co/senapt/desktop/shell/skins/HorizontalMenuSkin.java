package uk.co.senapt.desktop.shell.skins;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import uk.co.senapt.desktop.shell.HorizontalMenu;
import uk.co.senapt.desktop.shell.PrettyListView;

import java.util.Set;

/**
 * Created by gdiaz on 10/10/2017.
 */
public class HorizontalMenuSkin<T> extends SkinBase<HorizontalMenu> {

    private static final int FADE_DURATION = 200;

    private final BooleanProperty menuButtonRequired = new SimpleBooleanProperty(this, "menuButtonRequired");

    private final PrettyListView<T> mainList;
    private final PrettyListView<T> popupList;
    private final ToggleButton menuButton;
    private final Region leftFader;
    private final Region rightFader;
    private final Label leftScroll;
    private final Label rightScroll;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public HorizontalMenuSkin(HorizontalMenu<T> control) {
        super(control);

        this.mainList = new PrettyListView<>();
        this.mainList.setItems(control.getItems());
        this.mainList.cellFactoryProperty().bind(control.cellFactoryProperty());
        this.mainList.setOrientation(Orientation.HORIZONTAL);
        this.mainList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.mainList.getSelectionModel().select(control.getSelectedItem());
        this.mainList.getSelectionModel().selectedItemProperty().addListener(obs -> control.setSelectedItem(mainList.getSelectionModel().getSelectedItem()));

        control.selectedItemProperty().addListener(obs -> mainList.getSelectionModel().select(control.getSelectedItem()));

        this.mainList.getStyleClass().add("main-list");
        this.mainList.setManaged(false);
        this.mainList.skinProperty().addListener(it -> installMainListScrollListener());

        this.menuButton = new ToggleButton();
        this.menuButton.visibleProperty().bind(control.showMenuButtonProperty().and(menuButtonRequired));
        this.menuButton.selectedProperty().bindBidirectional(control.showPopupProperty());
        this.menuButton.getStyleClass().add("menu-button");
        this.menuButton.setManaged(false);
        this.menuButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.menuButton.getStyleClass().addAll("dropdown-icon-white");

        this.popupList = new PrettyListView<>();
        this.popupList.setItems(control.getItems());
        this.popupList.getSelectionModel().select(control.getSelectedItem());
        this.popupList.selectionModelProperty().bind(mainList.selectionModelProperty());
        this.popupList.cellFactoryProperty().bind(control.popupCellFactoryProperty());
        this.popupList.getStyleClass().add("popup-list");
        this.popupList.setManaged(false);
        this.popupList.visibleProperty().bind(menuButton.selectedProperty());
        this.popupList.skinProperty().addListener(it -> updatePopupListPrefHeight());
        this.popupList.getItems().addListener((Observable it) -> updatePopupListPrefHeight());
        this.popupList.minWidthProperty().bind(this.menuButton.widthProperty().add(3));
        control.showPopupProperty().addListener(obs -> updatePopupListPrefHeight());

        this.leftFader = new Region();
        this.leftFader.setManaged(false);
        this.leftFader.setMouseTransparent(true);
        this.leftFader.getStyleClass().add("left-fader");
        this.leftFader.setVisible(false);

        this.rightFader = new Region();
        this.rightFader.setManaged(false);
        this.rightFader.setMouseTransparent(true);
        this.rightFader.getStyleClass().add("right-fader");
        this.rightFader.setVisible(false);

        this.leftScroll = new Label();
        this.leftScroll.getStyleClass().add("left-scroll");
        this.leftScroll.setOnMouseClicked(evt -> scrollLeft());
        this.leftScroll.setVisible(false);

        this.rightScroll = new Label();
        this.rightScroll.getStyleClass().add("right-scroll");
        this.rightScroll.setOnMouseClicked(evt -> scrollRight());
        this.rightScroll.setVisible(false);

        getChildren().addAll(mainList, popupList, leftFader, rightFader, leftScroll, rightScroll, menuButton);
    }

    private double lastValue;

    private Timeline timeline;

    private void scroll(double delta) {
        VirtualFlow virtualFlow = (VirtualFlow) mainList.lookup("VirtualFlow");
        if (virtualFlow != null) {

            if (timeline != null) {
                timeline.stop();
            }

            DoubleProperty scrollValue = new SimpleDoubleProperty(this, "scrollValue");
            scrollValue.addListener(it -> {
                double currentValue = scrollValue.get();
                virtualFlow.scrollPixels(currentValue - lastValue);
                lastValue = currentValue;
            });

            KeyValue keyValue = new KeyValue(scrollValue, delta, Interpolator.EASE_BOTH);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);

            timeline = new Timeline(keyFrame);
            timeline.setOnFinished(evt -> lastValue = 0);
            timeline.play();
        }
    }

    private void scrollLeft() {
        scroll(-mainList.getWidth() / 2);
    }

    private void scrollRight() {
        scroll(mainList.getWidth() / 2);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        double menuButtonWidth = 0;

        if (menuButton.isVisible()) {
            menuButtonWidth = menuButton.prefWidth(-1);
        }

        double leftScrollWidth = leftScroll.prefWidth(-1);
        double rightScrollWidth = rightScroll.prefWidth(-1);

        leftScroll.resizeRelocate(contentX, contentY, leftScrollWidth, contentHeight);
        rightScroll.resizeRelocate(contentX + contentWidth - menuButtonWidth - rightScrollWidth, contentY, rightScrollWidth, contentHeight);

        mainList.resizeRelocate(contentX + leftScrollWidth, contentY, contentWidth - leftScrollWidth - rightScrollWidth - menuButtonWidth, contentHeight);
        menuButton.resizeRelocate(mainList.getLayoutX() + mainList.getWidth() + rightScrollWidth, contentY, menuButtonWidth, contentHeight);

        leftFader.resizeRelocate(mainList.getLayoutX(), contentY, leftFader.prefWidth(-1), contentHeight);
        rightFader.resizeRelocate(mainList.getLayoutX() + mainList.getWidth() - rightFader.prefWidth(-1), contentY, rightFader.prefWidth(-1), contentHeight);

        popupList.resizeRelocate(
                menuButton.getLayoutX() + menuButton.getWidth() - popupList.prefWidth(-1),
                contentY + contentHeight,
                popupList.prefWidth(-1) + 1, popupList.prefHeight(-1));
    }

    private void installMainListScrollListener() {
        final Set<Node> nodes = mainList.lookupAll("VirtualScrollBar");
        for (Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar vsb = (ScrollBar) node;
                if (vsb.getOrientation().equals(Orientation.HORIZONTAL)) {
                    vsb.valueProperty().addListener(it2 -> updateChildren(vsb));

                    Platform.runLater(() -> updateChildren(vsb));
//                    updateChildren(vsb);

                    // when the user resizes the window then the module menu button might have to show up (or hidden)
                    mainList.widthProperty().addListener(it3 -> updateChildren(vsb));

                    // when the list of activated modules has changed we also need to update the module switcher
                    mainList.getItems().addListener((Observable it4) -> updateChildren(vsb));
                }
            }
        }
    }

    private void updatePopupListPrefHeight() {
        VirtualFlow virtualFlow = (VirtualFlow) popupList.lookup("VirtualFlow");
        if (virtualFlow != null) {
            final IndexedCell cell = virtualFlow.getFirstVisibleCell();
            if (cell != null) {
                popupList.setPrefHeight(Math.min(8, popupList.getItems().size()) * cell.prefHeight(popupList.getWidth()));
                getSkinnable().requestLayout();
            }
        }
    }

    private ParallelTransition transitions;

    private void updateChildren(ScrollBar vsb) {
        if (transitions != null) {
            transitions.stop();
        }

        boolean showLeftFader = true;
        boolean showRightFader = true;

        if (vsb.getValue() == 0) {
            showLeftFader = false;
        }

        VirtualFlow flow = (VirtualFlow) mainList.lookup("VirtualFlow");

        final IndexedCell firstVisibleCell = flow.getFirstVisibleCell();
        if (firstVisibleCell != null) {
            if (firstVisibleCell.getIndex() == 0) {
                showLeftFader = false;
            }
        }

        final IndexedCell lastVisibleCell = flow.getLastVisibleCell();
        if (lastVisibleCell != null) {
            if (lastVisibleCell.getIndex() == mainList.getItems().size() - 1) {
                showRightFader = false;
            }
        } else {
            showRightFader = false;
        }

        FadeTransition menuButtonTransition;

        if (showRightFader || showLeftFader) {
            menuButtonRequired.set(true);
            menuButtonTransition = createFadeTransition(true, menuButton);
            getSkinnable().requestLayout();
        } else {
            menuButtonRequired.set(false);
            menuButtonTransition = createFadeTransition(false, menuButton);
            getSkinnable().requestLayout();
            getSkinnable().setShowPopup(false);
        }

        FadeTransition showLeftFaderTransition = createFadeTransition(showLeftFader, leftFader);
        FadeTransition showRightFaderTransition = createFadeTransition(showRightFader, rightFader);

        FadeTransition showLeftScrollTransition = createFadeTransition(showLeftFader, leftScroll);
        FadeTransition showRightScrollTransition = createFadeTransition(showRightFader, rightScroll);

        transitions = new ParallelTransition();

        if (menuButtonTransition == null) {
            transitions = new ParallelTransition(showLeftFaderTransition, showRightFaderTransition, showLeftScrollTransition, showRightScrollTransition);
        } else {
            transitions = new ParallelTransition(menuButtonTransition, showLeftFaderTransition, showRightFaderTransition, showLeftScrollTransition, showRightScrollTransition);
        }
        transitions.play();
    }

    private FadeTransition createFadeTransition(boolean showElement, Region element) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(FADE_DURATION), element);
        if (showElement) {
            fadeTransition.setFromValue(element.getOpacity());
            fadeTransition.setToValue(1);
            if (element != menuButton) {
                element.setVisible(true);
            }
        } else {
            fadeTransition.setFromValue(element.getOpacity());
            fadeTransition.setToValue(0);
            fadeTransition.setOnFinished(evt -> {
                if (element != menuButton) {
                    element.setVisible(false);
                }
            });
        }
        return fadeTransition;
    }

}
