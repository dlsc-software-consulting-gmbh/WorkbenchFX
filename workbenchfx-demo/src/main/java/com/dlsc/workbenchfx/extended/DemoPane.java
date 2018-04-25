package com.dlsc.workbenchfx.extended;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.extended.calendar.CalendarModule;
import com.dlsc.workbenchfx.extended.notes.NotesModule;
import com.dlsc.workbenchfx.extended.preferences.PreferencesModule;
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
        );
        getChildren().add(workbenchFx);
    }

}
