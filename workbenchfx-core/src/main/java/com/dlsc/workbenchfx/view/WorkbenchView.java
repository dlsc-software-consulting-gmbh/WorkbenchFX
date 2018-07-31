package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.view.controls.GlassPane;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the master view, which is used to show all view parts.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchView extends StackPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchView.class.getName());
  final ToolbarView toolbarView;
  final AddModuleView addModuleView;
  final ContentView contentView;
  VBox viewBox;

  /**
   * Displays all of the view parts, representing the master view.
   *
   * @param toolbarView the {@link ToolbarView} to be shown
   * @param addModuleView the {@link AddModuleView} to be shown
   * @param contentView the {@link ContentView} to be shown
   */
  public WorkbenchView(
      ToolbarView toolbarView,
      AddModuleView addModuleView,
      ContentView contentView) {
    this.toolbarView = toolbarView;
    this.addModuleView = addModuleView;
    this.contentView = contentView;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {
    setId("workbench");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    viewBox = new VBox();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    viewBox.getChildren().addAll(toolbarView, contentView);
    getChildren().addAll(viewBox);
    VBox.setVgrow(contentView, Priority.ALWAYS);
  }

  /**
   * Stacks the {@code overlay} on top of the current view, together with its {@code glassPane} in
   * the background and makes the {@code glassPane} hide, whenever the overlay is hidden.
   *
   * @param overlay   to be stacked on top of the view
   * @param glassPane to be added in the background of the {@code overlay}
   */
  public void addOverlay(Node overlay, GlassPane glassPane) {
    LOGGER.trace("addOverlay");
    overlay.setVisible(false);
    getChildren().addAll(glassPane, overlay);
    // make glass pane hide if overlay is not showing
    glassPane.hideProperty().bind(overlay.visibleProperty().not());
  }

  /**
   * Removes the {@code overlay} from the scene graph and removes the bindings created with the call
   * to {@link WorkbenchView#addOverlay(Node, GlassPane)}.
   *
   * @param overlay   to be removed from the scene graph
   * @param glassPane the {@code overlay}'s corresponding {@link GlassPane}
   */
  public void removeOverlay(Node overlay, GlassPane glassPane) {
    LOGGER.trace("removeOverlay");
    glassPane.hideProperty().unbind();
    getChildren().removeAll(glassPane, overlay);
  }

  /**
   * Makes the {@code overlay} visible.
   *
   * @param overlay to be made visible
   */
  public void showOverlay(Node overlay) {
    LOGGER.trace("showOverlay");
    Region overlay2 = (Region)overlay;
    LOGGER.trace("Overlay LayoutX: " + overlay.getLayoutX() + " TranslateX: " +
        overlay.getTranslateX());
    if (overlay2.getWidth() == 0) {
      overlay2.widthProperty().addListener(observable -> {
        if (overlay2.getWidth() > 0) {
          overlay2.setTranslateX(-(overlay2.getWidth()));
          slideIn(overlay2);
        }
      });
    } else {
      slideIn(overlay2);
    }

    overlay.setVisible(true);

//    overlay2.setTranslateX(-(overlay2.getWidth()));
    LOGGER.trace("Overlay LayoutX: " + overlay.getLayoutX() + " TranslateX: " +
        overlay.getTranslateX());

  }

  /**
   * Makes the {@code overlay} <b>in</b>visible.
   *
   * @param overlay to be made <b>in</b>visible
   */
  public void hideOverlay(Node overlay) {
    LOGGER.trace("hideOverlay");
    slideOut((Region)overlay);
  }

  private void slideIn(Region overlay) {
    TranslateTransition openNav=new TranslateTransition(new Duration(200), overlay);
      openNav.setFromX(overlay.getTranslateX());
      openNav.setToX(0);
      LOGGER.trace("Open Nav");
      openNav.play();
  }

  private void slideOut(Region overlay) {
    TranslateTransition closeNav=new TranslateTransition(new Duration(200), overlay);
        LOGGER.trace("Close Nav");
        closeNav.setToX(-(overlay.getWidth()));
        closeNav.play();
        closeNav.setOnFinished(event -> {
          overlay.setVisible(false);
          LOGGER.trace("Overlay LayoutX: " + overlay.getLayoutX() + " TranslateX: " +
              overlay.getTranslateX());
        });
      }

}
