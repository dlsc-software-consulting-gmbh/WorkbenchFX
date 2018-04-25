package com.dlsc.workbenchfx.standard;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.standard.calendar.CalendarModule;
import com.dlsc.workbenchfx.standard.notes.NotesModule;
import com.dlsc.workbenchfx.standard.preferences.PreferencesModule;
import com.dlsc.workbenchfx.view.Dropdown;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class DemoPane extends StackPane {

    private final WorkbenchFx workbenchFx;
    private final List<Dropdown> dropdowns;

    public DemoPane() {
        dropdowns = new ArrayList<>();
        initDropdowns();

        workbenchFx = WorkbenchFx.of(
                dropdowns,
                new CalendarModule(),
                new NotesModule(),
                new PreferencesModule()
        );
        getChildren().add(workbenchFx);
    }

    private void initDropdowns() {
        dropdowns.add(
                WorkbenchFx.createDropdown(
                        new ImageView("com/dlsc/workbenchfx/jfx_logo.png"),
                        "ImageView",
                        "A dropdown with an Image",
                        new Label("Content 1"),
                        new Label("Content 2")
                )
        );
        dropdowns.add(
                WorkbenchFx.createDropdown(
                        new FontAwesomeIconView(FontAwesomeIcon.ADDRESS_BOOK),
                        "FAIconView",
                        "A dropdown with an Icon",
                        new Label("Content 1"),
                        new Label("Content 2")
                )
        );
    }
}
