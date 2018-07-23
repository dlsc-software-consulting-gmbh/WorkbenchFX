package com.dlsc.workbenchfx.extended.calendar;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import javafx.scene.Node;

public class CalendarModule extends WorkbenchModule {

  private CalendarView calendarView;

  public CalendarModule() {
    super("Calendar", FontAwesomeIcon.CALENDAR);
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
