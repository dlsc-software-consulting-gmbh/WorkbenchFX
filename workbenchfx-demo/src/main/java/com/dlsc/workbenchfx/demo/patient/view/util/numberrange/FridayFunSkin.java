package com.dlsc.workbenchfx.modules.patient.view.util.numberrange;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

/**
 * @author Dieter Holz
 */
public class FridayFunSkin extends SkinBase<NumberRangeControl> implements CustomControlMixin {
  private static final double ARTBOARD_SIZE = 300;
  private static final double MINIMUM_SIZE = 75;
  private static final double MAXIMUM_SIZE = 800;

  //declare all parts
  private Circle backgroundCircle;
  private Arc bar;
  private Circle thumb;
  private Text valueDisplay;
  private Group ticks;
  private List<Text> tickLabels;


  // needed for resizing
  private Pane drawingPane;

  private final ChangeListener<Number> angleListener =
      (observable, oldValue, newValue) -> relocateThumb(newValue.doubleValue());

  private final ChangeListener<Number> minMaxListener =
      (observable, oldValue, newValue) -> Platform.runLater(this::updateTickLabels);


  public FridayFunSkin(NumberRangeControl control) {
    super(control);
    init();

    relocateThumb(getSkinnable().getAngle());
  }

  @Override
  public void dispose() {
    getSkinnable().angleProperty().removeListener(angleListener);
    getSkinnable().minValueProperty().removeListener(minMaxListener);
    getSkinnable().maxValueProperty().removeListener(minMaxListener);

    valueDisplay.textProperty().unbind();
    bar.lengthProperty().unbind();

    thumb.setOnMouseDragged(null);

    super.dispose();
  }

  @Override
  protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
    super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
    resize();
  }

  @Override
  public void initializeSelf() {
    addStylesheetFiles(getSkinnable(), "/fonts/fonts.css",
        "/com/dlsc/workbenchfx/modules/patient/view/util/numberrange/fridayFunStyle.css");
  }

  @Override
  public void initializeParts() {
    double center = ARTBOARD_SIZE * 0.5;
    int width = 15;
    double radius = center - width;

    backgroundCircle = new Circle(center, center, radius);
    backgroundCircle.getStyleClass().add("backgroundCircle");

    bar = new Arc(center, center, radius, radius, 90.0, 0.0);
    bar.getStyleClass().add("bar");
    bar.setType(ArcType.OPEN);

    thumb = new Circle(center, center + center - width, 13);
    thumb.getStyleClass().add("thumb");

    valueDisplay = createCenteredText(center, center, "valueDisplay");

    ticks = createTicks(center, center, 60, 360.0, 6, 33, 0, "tick");

    tickLabels = new ArrayList<>();

    int labelCount = 8;
    for (int i = 0; i < labelCount; i++) {
      double r = 95;
      double nextAngle = i * 360.0 / labelCount;

      Point2D p = pointOnCircle(center, center, r, nextAngle);
      Text tickLabel = createCenteredText(p.getX(), p.getY(), "tickLabel");

      tickLabels.add(tickLabel);
    }
    updateTickLabels();

    drawingPane = new Pane();
    drawingPane.getStyleClass().add("numberRange");
    drawingPane.setMaxSize(ARTBOARD_SIZE, ARTBOARD_SIZE);
    drawingPane.setMinSize(ARTBOARD_SIZE, ARTBOARD_SIZE);
    drawingPane.setPrefSize(ARTBOARD_SIZE, ARTBOARD_SIZE);
  }

  @Override
  public void layoutParts() {
    // add all parts to drawingPane
    drawingPane.getChildren().addAll(backgroundCircle, bar, thumb, valueDisplay, ticks);
    drawingPane.getChildren().addAll(tickLabels);

    getChildren().add(drawingPane);
  }

  @Override
  public void setupEventHandlers() {
    thumb.setOnMouseDragged(event -> {
      double center = ARTBOARD_SIZE * 0.5;
      getSkinnable().setAnimated(false);
      getSkinnable().setAngle(center, center, event.getX(), event.getY());
      getSkinnable().setAnimated(true);
    });
  }

  @Override
  public void setupValueChangedListeners() {
    getSkinnable().angleProperty().addListener(angleListener);
    getSkinnable().minValueProperty().addListener(minMaxListener);
    getSkinnable().maxValueProperty().addListener(minMaxListener);
  }

  private void updateTickLabels() {
    int labelCount = tickLabels.size();
    double step = (getSkinnable().getMaxValue() - getSkinnable().getMinValue()) / labelCount;

    for (int i = 0; i < labelCount; i++) {
      Text tickLabel = tickLabels.get(i);
      tickLabel.setText(String.format("%.1f", getSkinnable().getMinValue() + (i * step)));
    }
  }

  private void relocateThumb(double angle) {
    double center = ARTBOARD_SIZE * 0.5;
    Point2D thumbCenter = pointOnCircle(center, center, center - 15, angle);
    thumb.setCenterX(thumbCenter.getX());
    thumb.setCenterY(thumbCenter.getY());
  }

  @Override
  public void setupBindings() {
    valueDisplay.textProperty().bind(getSkinnable().valueProperty().asString("%.1f"));
    bar.lengthProperty().bind(getSkinnable().angleProperty().multiply(-1.0));
  }

  private void resize() {
    Insets padding = getSkinnable().getPadding();
    double availableWidth = getSkinnable().getWidth() - padding.getLeft() - padding.getRight();
    double availableHeight = getSkinnable().getHeight() - padding.getTop() - padding.getBottom();

    double width =
        Math.max(Math.min(Math.min(availableWidth, availableHeight), MAXIMUM_SIZE), MINIMUM_SIZE);

    double scalingFactor = width / ARTBOARD_SIZE;

    if (availableWidth > 0 && availableHeight > 0) {
      drawingPane.relocate((getSkinnable().getWidth() - ARTBOARD_SIZE) * 0.5,
          (getSkinnable().getHeight() - ARTBOARD_SIZE) * 0.5);
      drawingPane.setScaleX(scalingFactor);
      drawingPane.setScaleY(scalingFactor);
    }
  }

  private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
    return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
        cY + (radius * Math.cos(Math.toRadians(angle - 180))));
  }

  private Text createCenteredText(String styleClass) {
    return createCenteredText(ARTBOARD_SIZE * 0.5, ARTBOARD_SIZE * 0.5, styleClass);
  }

  private Text createCenteredText(double cx, double cy, String styleClass) {
    Text text = new Text();
    text.getStyleClass().add(styleClass);
    text.setTextOrigin(VPos.CENTER);
    text.setTextAlignment(TextAlignment.CENTER);
    double width = cx > ARTBOARD_SIZE * 0.5 ? ((ARTBOARD_SIZE - cx) * 2.0) : cx * 2.0;
    text.setWrappingWidth(width);
    text.setBoundsType(TextBoundsType.VISUAL);
    text.setY(cy);
    text.setX(cx - (width / 2.0));

    return text;
  }

  private Group createTicks(double cx, double cy, int numberOfTicks, double overallAngle, double tickLength, double indent, double startingAngle, String styleClass) {
    Group group = new Group();

    double degreesBetweenTicks = overallAngle == 360 ?
        overallAngle / numberOfTicks :
        overallAngle / (numberOfTicks - 1);
    double outerRadius = Math.min(cx, cy) - indent;
    double innerRadius = Math.min(cx, cy) - indent - tickLength;

    for (int i = 0; i < numberOfTicks; i++) {
      double angle = 180 + startingAngle + i * degreesBetweenTicks;

      Point2D startPoint = pointOnCircle(cx, cy, outerRadius, angle);
      Point2D endPoint = pointOnCircle(cx, cy, innerRadius, angle);

      Line tick = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
      tick.getStyleClass().add(styleClass);
      group.getChildren().add(tick);
    }

    return group;
  }

}
