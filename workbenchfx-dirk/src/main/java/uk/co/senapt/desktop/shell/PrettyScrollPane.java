package uk.co.senapt.desktop.shell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import java.util.Set;

/**
 * Created by lemmi on 23.08.17.
 */
public class PrettyScrollPane extends ScrollPane {

    private Region shadow = new Region();
    private ScrollBar vBar = new ScrollBar();
    private ScrollBar hBar = new ScrollBar();

    public PrettyScrollPane() {
        super();

        init();
    }

    public PrettyScrollPane(Node content) {
        super(content);
        init();
    }

    private void init() {
        skinProperty().addListener(it -> {
            // first bind, then add new scrollbars, otherwise the new bars will be found
            bindScrollBars();
            getChildren().addAll(vBar, hBar, shadow);
        });

        getStyleClass().add("pretty-scroll-pane");
        setFitToWidth(true);
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setHbarPolicy(ScrollBarPolicy.NEVER);

        vBar.setManaged(false);
        vBar.setOrientation(Orientation.VERTICAL);
        vBar.getStyleClass().add("vertical-scrollbar");
        vBar.visibleProperty().bind(vBar.visibleAmountProperty().lessThan(1));

        hBar.setManaged(false);
        hBar.setOrientation(Orientation.HORIZONTAL);
        hBar.getStyleClass().add("horizontal-scrollbar");
        hBar.visibleProperty().bind(hBar.visibleAmountProperty().lessThan(1));

        shadow.setManaged(false);
        shadow.getStyleClass().add("shadow");
        shadow.visibleProperty().bind(showShadowProperty());
        shadow.setMouseTransparent(true);
        shadow.visibleProperty().bind(vvalueProperty().greaterThan(0));

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

        vvalueProperty().addListener(it -> {
            if (lastOffset != computeOffset()) {
                requestLayout();
            }
        });
        showShadowProperty().addListener(it -> requestLayout());
    }

    private final BooleanProperty showShadow = new SimpleBooleanProperty(this, "showShadow", true);

    public final BooleanProperty showShadowProperty() {
        return showShadow;
    }

    public final boolean isShowShadow() {
        return showShadow.get();
    }

    public final void setShowShadow(boolean show) {
        showShadow.set(show);
    }

    private void bindScrollBars() {
        final Set<Node> nodes = lookupAll("ScrollBar");
        for (Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    bindScrollBars(vBar, bar);
                } else if (bar.getOrientation().equals(Orientation.HORIZONTAL)) {
                    bindScrollBars(hBar, bar);
                }
            }
        }
    }

    private void bindScrollBars(ScrollBar scrollBarA, ScrollBar scrollBarB) {
        scrollBarA.valueProperty().bindBidirectional(scrollBarB.valueProperty());
        scrollBarA.minProperty().bindBidirectional(scrollBarB.minProperty());
        scrollBarA.maxProperty().bindBidirectional(scrollBarB.maxProperty());
        scrollBarA.visibleAmountProperty().bindBidirectional(scrollBarB.visibleAmountProperty());
        scrollBarA.unitIncrementProperty().bindBidirectional(scrollBarB.unitIncrementProperty());
        scrollBarA.blockIncrementProperty().bindBidirectional(scrollBarB.blockIncrementProperty());
    }

    private final int SHADOW_HEIGHT = 30;

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        Insets insets = getInsets();
        double w = getWidth();
        double h = getHeight();
        final double prefWidth = vBar.prefWidth(-1);
        vBar.resizeRelocate(w - prefWidth - insets.getRight(), insets.getTop(), prefWidth, h - insets.getTop() - insets.getBottom());

        final double prefHeight = hBar.prefHeight(-1);
        hBar.resizeRelocate(insets.getLeft(), h - prefHeight - insets.getBottom(), w - insets.getLeft() - insets.getRight(), prefHeight);

        if (isShowShadow()) {
            double offset = computeOffset();
            shadow.resizeRelocate(-10, insets.getTop() - shadow.prefHeight(-1) - SHADOW_HEIGHT + offset, w + 20, shadow.prefHeight(-1) - 1);
            lastOffset = offset;
        }
    }

    private double lastOffset = 0;

    private double computeOffset() {
        return Math.min(getVvalue() * getContent().prefHeight(-1), SHADOW_HEIGHT);
    }
}
