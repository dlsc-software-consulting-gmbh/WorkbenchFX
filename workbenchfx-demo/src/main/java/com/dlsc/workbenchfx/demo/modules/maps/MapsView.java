package com.dlsc.workbenchfx.demo.modules.maps;

import com.dlsc.gmapsfx .GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.javascript.object.LatLong;
import com.dlsc.gmapsfx.javascript.object.MapOptions;
import com.dlsc.gmapsfx.javascript.object.Marker;
import com.dlsc.gmapsfx.javascript.object.MarkerOptions;
import javafx.scene.layout.StackPane;

public class MapsView extends StackPane implements MapComponentInitializedListener {

  private GoogleMapView mapView;
  private GoogleMap map;

  public MapsView() {
    getStyleClass().add("module-background");
    // initialize map
    mapView = new GoogleMapView();
    mapView.addMapInitializedListener(this);
    getChildren().add(mapView);
  }

  @Override
  public void mapInitialized() {
    //Set the initial properties of the map.
    MapOptions mapOptions = new MapOptions();

    mapOptions.center(new LatLong(47.4814072, 8.2116446))
        .overviewMapControl(false)
        .panControl(false)
        .rotateControl(false)
        .scaleControl(false)
        .streetViewControl(false)
        .zoomControl(false)
        .zoom(17);

    map = mapView.createMap(mapOptions);

    //Add a marker to the map
    MarkerOptions markerOptions = new MarkerOptions();

    markerOptions.position(new LatLong(47.4814072, 8.2116446))
        .visible(Boolean.TRUE)
        .title("FHNW");

    Marker marker = new Marker(markerOptions);

    map.addMarker(marker);
  }

  public void zoomIn() {
    map.setZoom(map.getZoom()+1);
  }

  public void zoomOut() {
    map.setZoom(map.getZoom()-1);
  }

}
