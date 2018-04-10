package com.dlsc.workbenchfx.standard.calendar;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class CalendarModule extends AbstractModule {

  public CalendarModule() {
    super("Calendar", FontAwesomeIcon.CALENDAR);
  }

  @Override
  public Node activate() {
    return new CalendarView();
  }

}
