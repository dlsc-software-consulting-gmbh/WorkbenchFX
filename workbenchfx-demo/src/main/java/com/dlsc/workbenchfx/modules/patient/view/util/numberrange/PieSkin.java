package com.dlsc.workbenchfx.modules.patient.view.util.numberrange;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Dieter Holz
 */
class PieSkin extends SkinBase<NumberRangeControl> implements CustomControlMixin {
  private static final String FONTS_CSS = "/fonts/fonts.css";
  private static final String STYLE_CSS =
      "/com/dlsc/workbenchfx/modules/patient/view/util/numberrange/pieStyle.css";

  private static final double ARTBOARD_WIDTH = 130;
  private static final double ARTBOARD_HEIGHT = 30;

  private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

  private static final double MINIMUM_WIDTH = 20;
  private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

  private static final double MAXIMUM_WIDTH = 800;
  private static final double MAXIMUM_HEIGHT = MAXIMUM_WIDTH / ASPECT_RATIO;

  // all parts
  private Circle border;
  private Arc pieSlice;
  private TextField valueField;

  private Pane drawingPane;

  private final ChangeListener<Number> resizeListener =
      (observable, oldValue, newValue) -> resize();


  // animations

  PieSkin(NumberRangeControl control) {
    super(control);
    init();
  }

  @Override
  public void dispose() {
    getSkinnable().widthProperty().removeListener(resizeListener);
    getSkinnable().heightProperty().removeListener(resizeListener);

    pieSlice.lengthProperty().unbind();
    valueField.textProperty().unbindBidirectional(getSkinnable().valueProperty());

    super.dispose();
  }

  @Override
  public void initializeSelf() {
    addStylesheetFiles(getSkinnable(), FONTS_CSS, STYLE_CSS);
  }

  @Override
  public void initializeParts() {
    double center = ARTBOARD_HEIGHT * 0.5;
    border = new Circle(center, center, center);
    border.getStyleClass().add("border");

    pieSlice = new Arc(center, center, center - 1, center - 1, 90, 0);
    pieSlice.getStyleClass().add("pieSlice");
    pieSlice.setType(ArcType.ROUND);

    valueField = new TextField();
    valueField.relocate(ARTBOARD_HEIGHT + 5, 2);
    valueField.setPrefWidth(ARTBOARD_WIDTH - ARTBOARD_HEIGHT - 5);
    valueField.getStyleClass().add("valueField");

    // always needed
    drawingPane = new Pane();
    drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
  }

  @Override
  public void layoutParts() {
    drawingPane.getChildren().addAll(border, pieSlice, valueField);
    getChildren().add(drawingPane);
  }

  @Override
  public void setupValueChangedListeners() {

    // always needed
    getSkinnable().widthProperty().addListener(resizeListener);
    getSkinnable().heightProperty().addListener(resizeListener);
  }

  @Override
  public void setupBindings() {
    pieSlice.lengthProperty().bind(
        Bindings.min(getSkinnable().angleProperty().multiply(-1.0), 0.0));
    valueField.textProperty().bindBidirectional(getSkinnable().valueProperty(),
        new NumberStringConverter());
  }

  private void resize() {
    Insets padding = getSkinnable().getPadding();
    double availableWidth = getSkinnable().getWidth() - padding.getLeft() - padding.getRight();
    double availableHeight = getSkinnable().getHeight() - padding.getTop() - padding.getBottom();

    double width =
        Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH),
            MINIMUM_WIDTH);

    double scalingFactor = width / ARTBOARD_WIDTH;

    if (availableWidth > 0 && availableHeight > 0) {
      drawingPane.relocate((getSkinnable().getWidth() - ARTBOARD_WIDTH) * 0.5,
          (getSkinnable().getHeight() - ARTBOARD_HEIGHT) * 0.5);
      drawingPane.setScaleX(scalingFactor);
      drawingPane.setScaleY(scalingFactor);
    }
  }

  // compute sizes

  @Override
  protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
    double horizontalPadding = leftInset + rightInset;

    return MINIMUM_WIDTH + horizontalPadding;
  }

  @Override
  protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
    double verticalPadding = topInset + bottomInset;

    return MINIMUM_HEIGHT + verticalPadding;
  }

  @Override
  protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
    double horizontalPadding = leftInset + rightInset;

    return ARTBOARD_WIDTH + horizontalPadding;
  }

  @Override
  protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
    double verticalPadding = topInset + bottomInset;

    return ARTBOARD_HEIGHT + verticalPadding;
  }

  @Override
  protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
    double horizontalPadding = leftInset + rightInset;

    return MAXIMUM_WIDTH + horizontalPadding;
  }

  @Override
  protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
    double verticalPadding = topInset + bottomInset;

    return MAXIMUM_HEIGHT + verticalPadding;
  }
}
