package com.dlsc.workbenchfx.modules.patient.view.util.rectangularimageview;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;

/**
 * @author Dieter Holz
 */
public class RectangularImageView extends Control {
  private final StringProperty imageURL = new SimpleStringProperty(this, "imageURL");
  private final SimpleObjectProperty<Dimension2D> imageSize =
      new SimpleObjectProperty<>(this, "imageSize", new Dimension2D(410, 300));

  public RectangularImageView() {
    setMaxWidth(Region.USE_PREF_SIZE);
    setMinWidth(Region.USE_PREF_SIZE);
    setMaxHeight(Region.USE_PREF_SIZE);
    setMinHeight(Region.USE_PREF_SIZE);

    getStyleClass().add("rectangular-image-view");
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new RectangularImageViewSkin(this);
  }

  public String getImageURL() {
    return imageURL.get();
  }

  public StringProperty imageURLProperty() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL.set(imageURL);
  }

  public Dimension2D getImageSize() {
    return imageSize.get();
  }

  public SimpleObjectProperty<Dimension2D> imageSizeProperty() {
    return imageSize;
  }

  public void setImageSize(Dimension2D imageSize) {
    this.imageSize.set(imageSize);
  }
}
