package com.dlsc.workbenchfx.view.controls;

import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

/**
 * TODO
 *
 * @author François Martin
 * @author Marco Sanfratello
 * @author Dirk Lemmermann
 */
public class PrettyScrollPane extends ScrollPane {

  private ScrollBar verticalScrollBar = new ScrollBar();
  private ScrollBar horizontalScrollBar = new ScrollBar();

  /**
   * TODO
   */
  public PrettyScrollPane() {
    super();
    init();
  }

  /**
   * TODO
   * @param content
   */
  public PrettyScrollPane(Node content) {
    super(content);
    init();
  }

  private void init() {
    skinProperty()
        .addListener(
            it -> {
              // first bind, then add new scrollbars, otherwise the new bars will be found
              bindScrollBars();
              getChildren().addAll(verticalScrollBar, horizontalScrollBar);
            });

    getStyleClass().add("pretty-scroll-pane");
    setFitToWidth(true);
    setVbarPolicy(ScrollBarPolicy.NEVER);
    setHbarPolicy(ScrollBarPolicy.NEVER);

    verticalScrollBar.setManaged(false);
    verticalScrollBar.setOrientation(Orientation.VERTICAL);
    verticalScrollBar.getStyleClass().add("vertical-scrollbar");
    verticalScrollBar.visibleProperty().bind(verticalScrollBar.visibleAmountProperty().lessThan(1));

    horizontalScrollBar.setManaged(false);
    horizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
    horizontalScrollBar.getStyleClass().add("horizontal-scrollbar");
    horizontalScrollBar.visibleProperty().bind(
        horizontalScrollBar.visibleAmountProperty().lessThan(1)
    );
  }

  private void bindScrollBars() {
    final Set<Node> nodes = lookupAll("ScrollBar");
    for (Node node : nodes) {
      if (node instanceof ScrollBar) {
        ScrollBar bar = (ScrollBar) node;
        if (bar.getOrientation().equals(Orientation.VERTICAL)) {
          bindScrollBars(verticalScrollBar, bar);
        } else if (bar.getOrientation().equals(Orientation.HORIZONTAL)) {
          bindScrollBars(horizontalScrollBar, bar);
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

  @Override
  protected void layoutChildren() {
    super.layoutChildren();

    Insets insets = getInsets();
    double w = getWidth();
    double h = getHeight();
    final double prefWidth = verticalScrollBar.prefWidth(-1);
    verticalScrollBar.resizeRelocate(
        w - prefWidth - insets.getRight(),
        insets.getTop(),
        prefWidth,
        h - insets.getTop() - insets.getBottom());

    final double prefHeight = horizontalScrollBar.prefHeight(-1);
    horizontalScrollBar.resizeRelocate(
        insets.getLeft(),
        h - prefHeight - insets.getBottom(),
        w - insets.getLeft() - insets.getRight(),
        prefHeight);
  }
}
