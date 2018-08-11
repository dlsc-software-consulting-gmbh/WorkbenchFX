package com.dlsc.workbenchfx.custom.calendar;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import java.util.Objects;
import javafx.scene.Node;

public class CalendarModule extends WorkbenchModule {

  private CalendarView calendarView;

  public CalendarModule() {
    super("François' Module", MaterialDesignIcon.CALENDAR);
  }

  @Override
  public Node activate() {
    if (Objects.isNull(calendarView)) {
      calendarView = new CalendarView();
    }
    calendarView.startClock();
    return calendarView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deactivate() {
    calendarView.stopClock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean destroy() {
    calendarView.stopClock();
    calendarView = null;
    return true;
  }
}
