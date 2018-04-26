package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.standard.calendar.CalendarModule;
import com.dlsc.workbenchfx.standard.notes.NotesModule;
import com.dlsc.workbenchfx.standard.preferences.PreferencesModule;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class DemoPane extends StackPane {

  private final WorkbenchFx workbenchFx;

  public DemoPane() {
    workbenchFx = WorkbenchFx.of(
        new CalendarModule(),
        new NotesModule(),
        new PreferencesModule()
    ).toolBarControls(
        Dropdown.of(
            "ImageView",
            new ImageView("com/dlsc/workbenchfx/user.png"),
            new CustomMenuItem(new Label("Content 1")),
            new CustomMenuItem(new Label("Content 2"))
        ),
        Dropdown.of(
            "FAIconView",
            new FontAwesomeIconView(FontAwesomeIcon.ADDRESS_BOOK),
            new CustomMenuItem(new Label("Content 1")),
            new CustomMenuItem(new MenuBar(
                new Menu("Menu 1"),
                new Menu("Menu 2")
            ))
        )
    );
    getChildren().add(workbenchFx);
  }
}
