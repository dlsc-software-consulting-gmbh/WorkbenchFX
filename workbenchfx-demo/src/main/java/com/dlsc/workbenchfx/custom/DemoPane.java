package com.dlsc.workbenchfx.custom;

import com.dlsc.workbenchfx.WorkbenchFx;
import com.dlsc.workbenchfx.custom.calendar.CalendarModule;
import com.dlsc.workbenchfx.custom.notes.NotesModule;
import com.dlsc.workbenchfx.custom.preferences.PreferencesModule;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.module.TabControl;
import com.dlsc.workbenchfx.view.module.TileControl;
import java.util.function.BiFunction;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class DemoPane extends StackPane {

  public WorkbenchFx workbenchFx;

  BiFunction<WorkbenchFx, Module, Node> tabFactory = (workbench, module) -> {
    TabControl tabControl = new TabControl(module);
    tabControl.setOnClose(e -> workbench.closeModule(module));
    tabControl.setOnActive(e -> workbench.openModule(module));
    return tabControl;
  };

  BiFunction<WorkbenchFx, Module, Node> tileFactory = (workbench, module) -> {
    TileControl tileControl = new TileControl(module);
    tileControl.setOnActive(e -> workbench.openModule(module));
    return tileControl;
  };

  public DemoPane() {

    workbenchFx = WorkbenchFx.builder(
        new CalendarModule(),
        new NotesModule(),
        new PreferencesModule()
    ).modulesPerPage(20)
     .tabFactory(tabFactory)
     .tileFactory(tileFactory)
     .build();
    getChildren().add(workbenchFx);
  }

}
