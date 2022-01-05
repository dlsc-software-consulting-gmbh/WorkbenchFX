package com.dlsc.workbenchfx.demo.modules.maps;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import javafx.scene.Node;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class MapsModule extends WorkbenchModule {

  private final ToolbarItem zoomIn;
  private final ToolbarItem zoomOut;
  private MapsView mapsView;

  public MapsModule() {
    super("Maps", FontAwesome.MAP_SIGNS);
    zoomIn = new ToolbarItem(new FontIcon(MaterialDesign.MDI_PLUS), event -> mapsView.zoomIn());
    zoomOut = new ToolbarItem(new FontIcon(MaterialDesign.MDI_MINUS), event -> mapsView.zoomOut());
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
