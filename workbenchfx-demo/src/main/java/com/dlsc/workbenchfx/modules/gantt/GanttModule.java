package com.dlsc.workbenchfx.modules.gantt;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import java.util.Objects;
import javafx.scene.Node;

public class GanttModule extends WorkbenchModule {

  private GanttView ganttView;

  public GanttModule() {
    super("Gantt Chart", MaterialDesignIcon.CHART_GANTT);
  }

  @Override
  public Node activate() {
    if (Objects.isNull(ganttView)) {
      ganttView = new GanttView();
    }
    return ganttView;
  }

  @Override
  public boolean destroy() {
    ganttView = null;
    return true;
  }
}
