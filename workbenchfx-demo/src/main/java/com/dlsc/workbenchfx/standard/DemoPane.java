package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.standard.calendar.CalendarModule;
import com.dlsc.workbenchfx.standard.notes.NotesModule;
import com.dlsc.workbenchfx.standard.preferences.PreferencesModule;
import com.dlsc.workbenchfx.view.Dropdown;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class DemoPane extends StackPane {

    private final WorkbenchFx workbenchFx;

    public DemoPane() {
        workbenchFx = WorkbenchFx.of(
                new CalendarModule(),
                new NotesModule(),
                new PreferencesModule()
        ).toolBarControls (
                Dropdown.of(
                        new ImageView("com/dlsc/workbenchfx/jfx_logo.png"),
                        "ImageView",
                        "A dropdown with an Image",
                        new Label("Content 1"),
                        new Label("Content 2")
                ),
                Dropdown.of(
                        new FontAwesomeIconView(FontAwesomeIcon.ADDRESS_BOOK),
                        "FAIconView",
                        "A dropdown with an Icon",
                        new Label("Content 1"),
                        new Label("Content 2")
                )
        );
        getChildren().add(workbenchFx);
    }
}
