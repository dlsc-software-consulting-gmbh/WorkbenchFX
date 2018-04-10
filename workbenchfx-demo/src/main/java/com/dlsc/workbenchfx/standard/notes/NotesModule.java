package com.dlsc.workbenchfx.standard.notes;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;
import com.dlsc.workbenchfx.model.module.AbstractModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.Node;

public class NotesModule extends AbstractModule {

  public NotesModule() {
    super("Notes", FontAwesomeIcon.PENCIL_SQUARE);
  }

  @Override
  public Node init(WorkbenchFxModel workbench) {
    return new NotesView();
  }

}
