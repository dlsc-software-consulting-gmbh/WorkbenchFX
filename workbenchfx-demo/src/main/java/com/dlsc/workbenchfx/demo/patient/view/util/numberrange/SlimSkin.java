package com.dlsc.workbenchfx.modules.patient.view.util.numberrange;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

/**
 * @author Dieter Holz
 */
class SlimSkin extends SkinBase<NumberRangeControl> {
  private static final String FONTS_CSS = "/fonts/fonts.css";
  private static final String STYLE_CSS =
      "/com/dlsc/workbenchfx/modules/patient/view/util/numberrange/slimStyle.css";

  private static final double ARTBOARD_WIDTH = 250;
  private static final double ARTBOARD_HEIGHT = 265;

  private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

  private static final double MINIMUM_WIDTH = 80;
  private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

  private static final double MAXIMUM_WIDTH = 800;
  private static final double MAXIMUM_HEIGHT = MAXIMUM_WIDTH / ASPECT_RATIO;

  private static final String FORMAT = "%.0f";
  private static final long BLINK_RATE = 500_000_000L;

  private static final int CIRCLE_CENTER_X = 125;
  private static final int CIRCLE_CENTER_Y = 150;
  private static final int RADIUS = 100;
  private static final int START_ANGLE = 90;

  // all parts
  private Line separator;
  private Text titleLabel;
  private Text valueLabel;
  private Text unitLabel;
  private Circle barBackground;
  private Arc bar;

  private Circle thumb;

  private Pane drawingPane;

  // animations
  private AnimationTimer timer = new AnimationTimer() {
    private long lastTimerCall;

    @Override
    public void handle(long now) {
      if (now > lastTimerCall + BLINK_RATE) {
        performPeriodicTask();
        lastTimerCall = now;
      }
    }
  };

  private final ChangeListener<Boolean> outOfRangeListener = (observable, oldValue, newValue) -> {
    if (newValue) {
      timer.start();
    } else {
      timer.stop();
      bar.setVisible(true);
      separator.setVisible(true);
    }
  };

  private final ChangeListener<Number> angleListener =
      (observable, oldValue, newValue) -> relocateThumb(newValue.doubleValue());

  private final ChangeListener<Number> resizeListener =
      (observable, oldValue, newValue) -> resize();


  SlimSkin(NumberRangeControl control) {
    super(control);
    initializeSelf();
    initializeParts();
    layoutParts();
    setupEventHandlers();
    setupValueChangedListeners();
    setupBindings();

    relocateThumb(getSkinnable().getAngle());
  }

  private void initializeSelf() {
    String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
    getSkinnable().getStylesheets().add(fonts);

    String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
    getSkinnable().getStylesheets().add(stylesheet);
  }

  private void initializeParts() {
    double cx = ARTBOARD_WIDTH * 0.5;

    separator = new Line(25, 15, 225, 15);
    separator.getStyleClass().add("separator");
    separator.setStrokeLineCap(StrokeLineCap.ROUND);

    titleLabel = createCenteredText(cx, 19, "title");
    titleLabel.setTextOrigin(VPos.TOP);

    valueLabel = createCenteredText(cx, 150, "value");

    unitLabel = createCenteredText(cx, 188, "unit");
    unitLabel.setTextOrigin(VPos.TOP);

    barBackground = new Circle(CIRCLE_CENTER_X, CIRCLE_CENTER_Y, RADIUS);
    barBackground.getStyleClass().add("barBackground");

    bar = new Arc(CIRCLE_CENTER_X, CIRCLE_CENTER_Y, RADIUS, RADIUS, START_ANGLE, 0);
    bar.getStyleClass().add("bar");
    bar.setType(ArcType.OPEN);

    thumb = new Circle(7);
    thumb.getStyleClass().add("thumb");

    // always needed
    drawingPane = new Pane();
    drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
  }

  private void layoutParts() {
    drawingPane.getChildren().addAll(barBackground, bar, separator, titleLabel, valueLabel,
        unitLabel, thumb);

    getChildren().add(drawingPane);
  }

  private void setupEventHandlers() {
    thumb.setOnMouseDragged(event -> {
      getSkinnable().setAnimated(false);
      getSkinnable().setAngle(CIRCLE_CENTER_X, CIRCLE_CENTER_Y, event.getX(), event.getY());
      getSkinnable().setAnimated(true);
    });
  }

  @Override
  public void dispose() {
    getSkinnable().outOfRangeProperty().removeListener(outOfRangeListener);
    getSkinnable().angleProperty().removeListener(angleListener);
    getSkinnable().widthProperty().removeListener(resizeListener);
    getSkinnable().heightProperty().removeListener(resizeListener);

    titleLabel.textProperty().unbind();
    unitLabel.textProperty().unbind();
    valueLabel.textProperty().unbind();
    bar.lengthProperty().unbind();
    thumb.visibleProperty().unbind();

    super.dispose();
  }

  private void setupValueChangedListeners() {
    getSkinnable().outOfRangeProperty().addListener(outOfRangeListener);
    getSkinnable().angleProperty().addListener(angleListener);

    // always needed
    getSkinnable().widthProperty().addListener(resizeListener);
    getSkinnable().heightProperty().addListener(resizeListener);
  }

  private void setupBindings() {
    titleLabel.textProperty().bind(getSkinnable().titleProperty());
    unitLabel.textProperty().bind(getSkinnable().unitProperty());
    valueLabel.textProperty().bind(getSkinnable().animatedValueProperty().asString(FORMAT));
    bar.lengthProperty().bind(Bindings.min(getSkinnable().angleProperty().multiply(-1.0), -1.0));
    thumb.visibleProperty().bind(getSkinnable().interactiveProperty());
  }

  private void performPeriodicTask() {
    bar.setVisible(!bar.isVisible());
    separator.setVisible(!separator.isVisible());
  }

  private void relocateThumb(double angle) {
    Point2D p = pointOnCircle(CIRCLE_CENTER_X, CIRCLE_CENTER_Y, RADIUS, angle);

    thumb.setCenterX(p.getX());
    thumb.setCenterY(p.getY());
  }

  // some useful helper-methods

  private Text createCenteredText(String styleClass) {
    return createCenteredText(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, styleClass);
  }

  private Text createCenteredText(double cx, double cy, String styleClass) {
    Text text = new Text();
    text.getStyleClass().add(styleClass);
    text.setTextOrigin(VPos.CENTER);
    text.setTextAlignment(TextAlignment.CENTER);
    double width = cx > ARTBOARD_WIDTH * 0.5 ? ((ARTBOARD_WIDTH - cx) * 2.0) : cx * 2.0;
    text.setWrappingWidth(width);
    text.setBoundsType(TextBoundsType.VISUAL);
    text.setY(cy);

    return text;
  }

  private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
    return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
        cY + (radius * Math.cos(Math.toRadians(angle - 180))));
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
