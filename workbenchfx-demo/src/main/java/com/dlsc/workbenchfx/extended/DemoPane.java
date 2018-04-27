package com.dlsc.workbenchfx.extended;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.extended.calendar.CalendarModule;
import com.dlsc.workbenchfx.extended.notes.NotesModule;
import com.dlsc.workbenchfx.extended.preferences.PreferencesModule;
import com.dlsc.workbenchfx.view.controls.Dropdown;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class DemoPane extends StackPane {

  public WorkbenchFx workbenchFx;

  public DemoPane() {
    workbenchFx = WorkbenchFx.of(
        new PreferencesModule(),
        new PreferencesModule(),
        new PreferencesModule(),
        new PreferencesModule(),
        new PreferencesModule(),
        new CalendarModule(),
        new CalendarModule(),
        new CalendarModule(),
        new CalendarModule(),
        new CalendarModule(),
        new NotesModule(),
        new NotesModule(),
        new NotesModule(),
        new NotesModule(),
        new NotesModule()
    ).toolBarControls(
        Dropdown.of(
            "ImageView",
            new ImageView("com/dlsc/workbenchfx/user.png"),
            new CustomMenuItem(new Label("Content 1")),
            new CustomMenuItem(new Label("Content 2"))
        )
    );
    getChildren().add(workbenchFx);
  }

}
