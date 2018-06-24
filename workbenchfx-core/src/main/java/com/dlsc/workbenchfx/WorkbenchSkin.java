package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.view.ContentPresenter;
import com.dlsc.workbenchfx.view.ContentView;
import com.dlsc.workbenchfx.view.HomePresenter;
import com.dlsc.workbenchfx.view.HomeView;
import com.dlsc.workbenchfx.view.ToolbarPresenter;
import com.dlsc.workbenchfx.view.ToolbarView;
import com.dlsc.workbenchfx.view.WorkbenchPresenter;
import com.dlsc.workbenchfx.view.WorkbenchView;
import com.dlsc.workbenchfx.view.dialog.WorkbenchDialog;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the Skin which is made for the {@link Workbench}.
 * It creates all the Views and Presenters which are needed and sets the stylesheets.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class WorkbenchSkin extends SkinBase<Workbench> {
  private static final Logger LOGGER =
      LogManager.getLogger(WorkbenchSkin.class.getName());

  // Views
  private ToolbarView toolbarView;
  private ToolbarPresenter toolbarPresenter;

  private HomeView homeView;
  private HomePresenter homePresenter;

  private ContentView contentView;
  private ContentPresenter contentPresenter;

  private WorkbenchView workbenchView;
  private WorkbenchPresenter workbenchPresenter;

  private Label dialogTitle;
  private VBox dialogPane;
  private StackPane dialogContentPane;
  private ButtonBar dialogButtonBar;
  private final Map<ButtonType, Node> buttonNodes = new WeakHashMap<>();

  /**
   * Creates a skin for a given {@link Workbench}.
   * Contains all views and presenters and sets also the default stylesheet.
   *
   * @param workbench for which this skin is created
   */
  public WorkbenchSkin(Workbench workbench) {
    super(workbench);

    initViews(workbench);

    getChildren().add(workbenchView);
    Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

    initDialog(workbench);

    initDialogBindings(workbench);
  }

  private void initDialogBindings(Workbench workbench) {
    workbench.dialogProperty().addListener(it -> {
      final WorkbenchDialog dialog = workbench.getDialog();
      if (dialog != null) {
        showDialog();
      } else {
        hideDialog();
      }
    });
  }

  private void initViews(Workbench model) {
    toolbarView = new ToolbarView();
    toolbarPresenter = new ToolbarPresenter(model, toolbarView);

    homeView = new HomeView();
    homePresenter = new HomePresenter(model, homeView);

    contentView = new ContentView();
    contentPresenter = new ContentPresenter(model, contentView);

    workbenchView = new WorkbenchView(toolbarView, homeView, contentView);
    workbenchPresenter = new WorkbenchPresenter(model, workbenchView);
  }


}
