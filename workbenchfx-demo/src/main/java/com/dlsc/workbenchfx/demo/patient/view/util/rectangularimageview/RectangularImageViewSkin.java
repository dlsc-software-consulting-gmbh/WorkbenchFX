package com.dlsc.workbenchfx.modules.patient.view.util.rectangularimageview;

import com.dlsc.workbenchfx.modules.patient.view.util.ImageCache;
import javafx.beans.binding.Bindings;
import javafx.geometry.Dimension2D;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

/**
 * @author Dieter Holz
 */
class RectangularImageViewSkin extends SkinBase<RectangularImageView> implements ImageCache {
  private ImageView imageView;
  private Rectangle clip;

  RectangularImageViewSkin(RectangularImageView control) {
    super(control);

    clip = new Rectangle(getWidthToFit(), getHeightToFit());

    imageView = new ImageView();
    imageView.setSmooth(true);
    imageView.setClip(clip);
    imageView.imageProperty().bind(
        Bindings.createObjectBinding(() -> getImage(getSkinnable().getImageURL()),
            getSkinnable().imageURLProperty()));

    getSkinnable().imageSizeProperty().addListener((observable, oldValue, newValue) -> {
      clip.setWidth(getWidthToFit());
      clip.setHeight(getHeightToFit());
      getSkinnable().requestLayout();
    });

    getChildren().add(imageView);
  }

  @Override
  protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
    return getHeightToFit() + topInset + bottomInset;
  }

  @Override
  protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
    return getWidthToFit() + leftInset + topInset;
  }

  @Override
  protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
    Image currentImage = imageView.getImage();

    if (imageView.getImage() != null && currentImage.getWidth() > 0 &&
        currentImage.getHeight() > 0) {
      Dimension2D bestFit =
          shouldFitIn(currentImage.getWidth(), currentImage.getHeight(), getWidthToFit(),
              getHeightToFit());

      imageView.setFitWidth(bestFit.getWidth());
      imageView.setFitHeight(bestFit.getHeight());
      imageView.relocate(contentX + (contentWidth - bestFit.getWidth()) * 0.5,
          contentY + (contentHeight - bestFit.getHeight()) * 0.5);

      clip.relocate((bestFit.getWidth() - clip.getWidth()) * 0.5,
          (bestFit.getHeight() - clip.getHeight()) * 0.5);
    }
  }

  private Dimension2D shouldFitIn(double originalWidth, double originalHeight, double toFitWidth, double toFitHeight) {
    double fitRatio = toFitWidth / toFitHeight;
    double originalRatio = originalWidth / originalHeight;

    if (fitRatio > originalRatio) {
      double widthFactor = toFitWidth / originalWidth;
      return new Dimension2D(toFitWidth, originalHeight * widthFactor);
    } else {
      double heightFactor = toFitHeight / originalHeight;
      return new Dimension2D(originalWidth * heightFactor, toFitHeight);
    }
  }

  private double getHeightToFit() {
    return getSkinnable().getImageSize().getHeight();
  }

  private double getWidthToFit() {
    return getSkinnable().getImageSize().getWidth();
  }

}
