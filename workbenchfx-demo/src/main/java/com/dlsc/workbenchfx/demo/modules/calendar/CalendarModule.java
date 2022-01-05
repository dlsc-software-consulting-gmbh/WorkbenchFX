package com.dlsc.workbenchfx.demo.modules.calendar;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.Objects;

public class CalendarModule extends WorkbenchModule {

  private CalendarFxView calendarView;

  public CalendarModule() {
    super("Calendar", MaterialDesign.MDI_CALENDAR);
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
