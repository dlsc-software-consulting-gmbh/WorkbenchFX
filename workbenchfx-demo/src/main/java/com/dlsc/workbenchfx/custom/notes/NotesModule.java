package com.dlsc.workbenchfx.custom.notes;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

public class NotesModule extends WorkbenchModule {

  public NotesModule() {
    super("Notes", MaterialDesignIcon.PENCIL_BOX_OUTLINE);
  }

  @Override
  public Node activate() {
    return new NotesView();
  }

}
