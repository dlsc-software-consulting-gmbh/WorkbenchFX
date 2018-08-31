package com.dlsc.workbenchfx.modules.maps;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.Node;

public class MapsModule extends WorkbenchModule {

  private final ToolbarItem zoomIn;
  private final ToolbarItem zoomOut;
  private MapsView mapsView;

  public MapsModule() {
    super("Maps", FontAwesomeIcon.MAP_SIGNS);
    zoomIn = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.PLUS), event -> mapsView.zoomIn());
    zoomOut = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.MINUS), event -> mapsView.zoomOut());
    getToolbarControlsLeft().addAll(zoomIn, zoomOut);
  }

  @Override
  public void init(Workbench workbench) {
    super.init(workbench);
    mapsView = new MapsView();
  }

  @Override
  public Node activate() {
    return mapsView;
  }

}
