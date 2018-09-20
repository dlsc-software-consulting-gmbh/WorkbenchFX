package com.dlsc.workbenchfx.demo.modules.calendar;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import java.util.Objects;

public class CalendarModule extends WorkbenchModule {

  private CalendarFxView calendarView;

  public CalendarModule() {
    super("Calendar", MaterialDesignIcon.CALENDAR);
  }

  @Override
  public Node activate() {
    if (Objects.isNull(calendarView)) {
      calendarView = new CalendarFxView();
    }
    calendarView.start();
    return calendarView;
  }

  @Override
  public void deactivate() {
    calendarView.stop();
  }

  @Override
  public boolean destroy() {
    calendarView = null;
    return true;
  }
}
