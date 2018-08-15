package com.dlsc.workbenchfx.view;

import com.dlsc.workbenchfx.view.controls.GlassPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the master view, which is used to show all view parts.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public final class WorkbenchView extends StackPane implements View {
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
  public final void initializeSelf() {
    setId("workbench");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void initializeParts() {
    viewBox = new VBox();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void layoutParts() {
    viewBox.getChildren().addAll(toolbarView, contentView);
    getChildren().addAll(viewBox);
    VBox.setVgrow(contentView, Priority.ALWAYS);
    VBox.setVgrow(addModuleView, Priority.ALWAYS);
  }

  /**
   * Stacks the {@code overlay} on top of the current view, together with its {@code glassPane} in
   * the background and makes the {@code glassPane} hide, whenever the overlay is hidden.
   *
   * @param overlay   to be stacked on top of the view
   * @param glassPane to be added in the background of the {@code overlay}
   */
  final void addOverlay(Region overlay, GlassPane glassPane) {
    LOGGER.trace("addOverlay");
    overlay.setVisible(false);
    getChildren().addAll(glassPane, overlay);
    // make glass pane hide if overlay is not showing
    overlay.visibleProperty().addListener(observable -> glassPane.setHide(!overlay.isVisible()));
  }

  /**
   * Removes the {@code overlay} from the scene graph and removes the bindings created with the call
   * to {@link WorkbenchView#addOverlay(Region, GlassPane)}.
   *
   * @param overlay   to be removed from the scene graph
   * @param glassPane the {@code overlay}'s corresponding {@link GlassPane}
   */
  final void removeOverlay(Region overlay, GlassPane glassPane) {
    LOGGER.trace("removeOverlay");
    glassPane.hideProperty().unbind();
    getChildren().removeAll(glassPane, overlay);
  }

  /**
   * Makes the {@code overlay} visible.
   *
   * @param overlay to be made visible
   */
  final void showOverlay(Region overlay) {
    LOGGER.trace("showOverlay");
    overlay.setVisible(true);
  }

  /**
   * Makes the {@code overlay} <b>in</b>visible.
   *
   * @param overlay to be made <b>in</b>visible
   */
  final void hideOverlay(Region overlay) {
    LOGGER.trace("hideOverlay");
    overlay.setVisible(false);
  }

}
