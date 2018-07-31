package com.dlsc.workbenchfx.custom.notes;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class NotesModule extends WorkbenchModule {

  public NotesModule() {
    super("Notes", FontAwesomeIcon.PENCIL_SQUARE);
  }

  @Override
  public Node activate() {
    return new NotesView();
  }

}
