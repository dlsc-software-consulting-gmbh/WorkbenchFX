package com.dlsc.workbenchfx.modules.patient.view.util.numberrange;

import com.dlsc.workbenchfx.modules.patient.view.util.MaterialDesign;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * @author Dieter Holz
 */
public class NumberRangeControl extends Control {
  // needed for StyleableProperties
  private static final StyleablePropertyFactory<NumberRangeControl> FACTORY =
      new StyleablePropertyFactory<>(Control.getClassCssMetaData());

  @Override
  public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
    return FACTORY.getCssMetaData();
  }

  private static final Duration ANIMATION_DURATION = Duration.millis(400);

  // all properties
  private final StringProperty title = new SimpleStringProperty("TITLE");
  private final StringProperty unit = new SimpleStringProperty("UNIT");
  private final DoubleProperty minValue = new SimpleDoubleProperty(0);
  private final DoubleProperty maxValue = new SimpleDoubleProperty(1000);
  private final DoubleProperty value = new SimpleDoubleProperty();

  private final BooleanProperty animated = new SimpleBooleanProperty(true);
  private final DoubleProperty animatedValue = new SimpleDoubleProperty();

  private final BooleanProperty interactive = new SimpleBooleanProperty(false);

  private final DoubleProperty percentage = new SimpleDoubleProperty();
  private final DoubleProperty angle = new SimpleDoubleProperty();
  private final BooleanProperty outOfRange = new SimpleBooleanProperty();

  private final ObjectProperty<Color> originalBaseColor = new SimpleObjectProperty<>();
  private final ObjectProperty<Color> originalOutOfRangeColor = new SimpleObjectProperty<>();

  // all CSS Styleable properties
  private static final CssMetaData<NumberRangeControl, Color> BASE_COLOR_META_DATA =
      FACTORY.createColorCssMetaData("-base-color", s -> s.baseColor);

  private final StyleableObjectProperty<Color> baseColor =
      new SimpleStyleableObjectProperty<Color>(BASE_COLOR_META_DATA, this, "baseColor") {
        @Override
        protected void invalidated() {
          setStyle(BASE_COLOR_META_DATA.getProperty() + ": " + colorToCss(getBaseColor()));
          applyCss();
        }
      };

  private static final CssMetaData<NumberRangeControl, Color> OUT_OF_RANGE_COLOR_META_DATA =
      FACTORY.createColorCssMetaData("-out-of-range-color", s -> s.outOfRangeColor);

  private final StyleableObjectProperty<Color> outOfRangeColor =
      new SimpleStyleableObjectProperty<>(OUT_OF_RANGE_COLOR_META_DATA, this, "outOfRangeColor");


  // animations
  private final Timeline timeline = new Timeline();

  private final ObjectProperty<SkinType> skinType = new SimpleObjectProperty<>();

  public NumberRangeControl(SkinType skinType) {
    setSkinType(skinType);
    getStyleClass().add(getStyleClassName());
    setupValueChangedListeners();
    setupBindings();
    Platform.runLater(() -> {
      setOriginalBaseColor(getBaseColor());
      setOriginalOutOfRangeColor(getOutOfRangeColor());
    });
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return createSkin(getSkinType());
  }

  void setAngle(double cx, double cy, double x, double y) {
    double angle = angle(cx, cy, x, y);
    double percentage = angleToPercentage(angle);
    double value = percentageToValue(percentage, getMinValue(), getMaxValue());
    setValue(value);
  }

  private void setupValueChangedListeners() {
    skinType.addListener((observable, oldValue, newValue) -> {
      getChildren().clear();
      getStylesheets().clear();

      setSkin(createSkin(newValue));
    });

    valueProperty().addListener((observable, oldValue, newValue) -> {
      if (isAnimated()) {
        timeline.stop();
        timeline.getKeyFrames().setAll(new KeyFrame(ANIMATION_DURATION,
            new KeyValue(animatedValueProperty(),
                newValue,
                Interpolator.EASE_BOTH)));

        timeline.play();
      } else {
        setAnimatedValue(newValue.doubleValue());
      }
    });

    animatedValueProperty().addListener((observable, oldValue, newValue) -> {
      setPercentage(valueToPercentage(newValue.doubleValue(), getMinValue(), getMaxValue()));
      setAngle(percentageToAngle(getPercentage()));
    });

    outOfRangeProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        setBaseColor(getOriginalOutOfRangeColor());
      } else {
        setBaseColor(getOriginalBaseColor());
      }
    });

    originalBaseColorProperty().addListener((observable, oldValue, newValue) -> {
      if (!getOutOfRange()) {
        setBaseColor(newValue);
      }
    });

    originalOutOfRangeColorProperty().addListener((observable, oldValue, newValue) -> {
      if (getOutOfRange()) {
        setBaseColor(newValue);
      }
    });

    minValueProperty().addListener((observable, oldValue, newValue) -> {
      setPercentage(valueToPercentage(getValue(), newValue.doubleValue(), getMaxValue()));
      setAngle(percentageToAngle(getPercentage()));
    });

    maxValueProperty().addListener((observable, oldValue, newValue) -> {
      setPercentage(valueToPercentage(getValue(), getMinValue(), newValue.doubleValue()));
      setAngle(percentageToAngle(getPercentage()));
    });
  }


  private void setupBindings() {
    outOfRange.bind(Bindings.createBooleanBinding(
        () -> getValue() > getMaxValue() || getValue() < getMinValue(),
        value, minValue, maxValue));
  }

  private Skin<?> createSkin(SkinType skinType) {
    return skinType.getFactory().apply(this);
  }


  //some handy functions

  private double percentageToValue(double percentage, double minValue, double maxValue) {
    assert percentage >= 0.0 && percentage <= 100.0;
    assert minValue <= maxValue;

    return ((maxValue - minValue) * (percentage / 100.0)) + minValue;
  }

  private double valueToPercentage(double value, double minValue, double maxValue) {
    assert minValue <= maxValue;

    return Math.max(0.0, Math.min(100.0, (value - minValue) / ((maxValue - minValue) / 100.0)));
  }

  private double angleToPercentage(double angle) {
    return angle / 3.6;
  }

  private double percentageToAngle(double percentage) {
    return 3.60 * percentage;
  }

  private double angle(double cx, double cy, double x, double y) {
    double deltaX = x - cx;
    double deltaY = y - cy;
    double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    double nx = deltaX / radius;
    double ny = deltaY / radius;
    double theta = Math.toRadians(90) + Math.atan2(ny, nx);

    return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) :
        Math.toDegrees((theta)) + 360.0;
  }

  private String getStyleClassName() {
    String className = this.getClass().getSimpleName();

    return className.substring(0, 1).toLowerCase() + className.substring(1);
  }

  private String colorToCss(final Color color) {
    if (color != null) {
      return color.toString().replace("0x", "#");
    } else {
      return colorToCss(MaterialDesign.CYAN_300.getColor());
    }
  }


  // getter and setter for all properties

  public String getTitle() {
    return title.get();
  }

  public StringProperty titleProperty() {
    return title;
  }

  public void setTitle(String title) {
    this.title.set(title);
  }

  public String getUnit() {
    return unit.get();
  }

  public StringProperty unitProperty() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit.set(unit);
  }

  public double getMinValue() {
    return minValue.get();
  }

  public DoubleProperty minValueProperty() {
    return minValue;
  }

  public void setMinValue(double minValue) {
    this.minValue.set(minValue);
  }

  public double getMaxValue() {
    return maxValue.get();
  }

  public DoubleProperty maxValueProperty() {
    return maxValue;
  }

  public void setMaxValue(double maxValue) {
    this.maxValue.set(maxValue);
  }

  public double getValue() {
    return value.get();
  }

  public DoubleProperty valueProperty() {
    return value;
  }

  public void setValue(double value) {
    this.value.set(value);
  }

  public boolean isAnimated() {
    return animated.get();
  }

  public BooleanProperty animatedProperty() {
    return animated;
  }

  public void setAnimated(boolean animated) {
    this.animated.set(animated);
  }

  public double getAnimatedValue() {
    return animatedValue.get();
  }

  public DoubleProperty animatedValueProperty() {
    return animatedValue;
  }

  private void setAnimatedValue(double animatedValue) {
    this.animatedValue.set(animatedValue);
  }

  public boolean getInteractive() {
    return interactive.get();
  }

  public BooleanProperty interactiveProperty() {
    return interactive;
  }

  public void setInteractive(boolean interactive) {
    this.interactive.set(interactive);
  }

  public double getPercentage() {
    return percentage.get();
  }

  public DoubleProperty percentageProperty() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage.set(percentage);
  }

  public double getAngle() {
    return angle.get();
  }

  public DoubleProperty angleProperty() {
    return angle;
  }

  private void setAngle(double angle) {
    this.angle.set(angle);
  }

  public boolean getOutOfRange() {
    return outOfRange.get();
  }

  public BooleanProperty outOfRangeProperty() {
    return outOfRange;
  }

  public Color getBaseColor() {
    return baseColor.get();
  }

  public StyleableObjectProperty<Color> baseColorProperty() {
    return baseColor;
  }

  public void setBaseColor(Color baseColor) {
    this.baseColor.set(baseColor);
  }

  public Color getOutOfRangeColor() {
    return outOfRangeColor.get();
  }

  public StyleableObjectProperty<Color> outOfRangeColorProperty() {
    return outOfRangeColor;
  }

  public void setOutOfRangeColor(Color outOfRangeColor) {
    this.outOfRangeColor.set(outOfRangeColor);
  }

  public Color getOriginalBaseColor() {
    return originalBaseColor.get();
  }

  public ObjectProperty<Color> originalBaseColorProperty() {
    return originalBaseColor;
  }

  public void setOriginalBaseColor(Color originalBaseColor) {
    this.originalBaseColor.set(originalBaseColor);
  }

  public Color getOriginalOutOfRangeColor() {
    return originalOutOfRangeColor.get();
  }

  public ObjectProperty<Color> originalOutOfRangeColorProperty() {
    return originalOutOfRangeColor;
  }

  public void setOriginalOutOfRangeColor(Color originalOutOfRangeColor) {
    this.originalOutOfRangeColor.set(originalOutOfRangeColor);
  }

  private SkinType getSkinType() {
    return skinType.get();
  }

  public ObjectProperty<SkinType> skinTypeProperty() {
    return skinType;
  }

  private void setSkinType(SkinType skinType) {
    this.skinType.set(skinType);
  }
}

