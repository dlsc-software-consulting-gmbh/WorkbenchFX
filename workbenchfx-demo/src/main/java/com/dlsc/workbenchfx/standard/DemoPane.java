package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.standard.calendar.CalendarModule;
import com.dlsc.workbenchfx.standard.notes.NotesModule;
import com.dlsc.workbenchfx.standard.preferences.PreferencesModule;
import javafx.scene.layout.StackPane;

public class DemoPane extends StackPane {

  public WorkbenchFx workbenchFx;

  public DemoPane() {
    workbenchFx = WorkbenchFx.of(
        new CalendarModule(),
        new NotesModule(),
        new PreferencesModule()
    );
    getChildren().add(workbenchFx);
  }

}
