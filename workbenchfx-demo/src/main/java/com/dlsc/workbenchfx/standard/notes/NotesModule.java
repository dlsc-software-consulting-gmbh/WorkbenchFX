package com.dlsc.workbenchfx.standard.notes;

import com.dlsc.workbenchfx.module.Module;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class NotesModule extends Module {

  public NotesModule() {
    super("Notes", FontAwesomeIcon.PENCIL_SQUARE);
  }

  @Override
  public Node activate() {
    return new NotesView();
  }

}
