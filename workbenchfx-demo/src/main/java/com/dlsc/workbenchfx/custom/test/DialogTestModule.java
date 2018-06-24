package com.dlsc.workbenchfx.custom.test;

import com.dlsc.workbenchfx.module.WorkbenchModule;
import com.dlsc.workbenchfx.view.dialog.WorkbenchDialog;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.CheckListView;

public class DialogTestModule extends WorkbenchModule implements MapComponentInitializedListener {
  private int itemsCount = 1;

  private final Button confirmBtn = new Button("Confirmation Dialog");
  private final Button errorBtn = new Button("Error Dialog");
  private final Button errorExceptionBtn = new Button("Error Dialog with Exception");
  private final Button errorDetailsBtn = new Button("Error Dialog with Details");
  private final Button warningBtn = new Button("Warning Dialog");
  private final Button informationBtn = new Button("Information Dialog");
  private final Button customSmallBtn = new Button("Custom Small Dialog");
  private final Button customFullBtn = new Button("Custom Full Coverage Dialog");
  private final Button customFullMaxBtn = new Button("Custom Full Coverage Maximized Dialog");
  private final Button longTitleBtn = new Button("Long Title Dialog");
  private final Button longMessageBtn = new Button("Long Message Dialog");
  private final Button longTitleMessageBtn = new Button("Long Title & Message Dialog");

  private GoogleMapView mapView;
  private GoogleMap map;

  private final GridPane customPane = new GridPane();

  NullPointerException exception;
  private CheckListView<String> checkListView;

  public DialogTestModule() {
    super("Dialog Test", FontAwesomeIcon.QUESTION);
    initDialogParts();
    initException();
    layoutParts();
    setupEventHandlers();
  }

  private void initException() {
    // Provokes an exception to get a representative demo stacktrace to show in an error dialog
    Button btn = null;
    try {
      btn.setOnAction(event -> System.out.println("This will cause a NPE"));
    } catch (NullPointerException e) {
      exception = e;
    }
  }

  private void initDialogParts() {
    // create the data to show in the CheckListView
    final ObservableList<String> libraries = FXCollections.observableArrayList();
    libraries.addAll("WorkbenchFX", "PreferencesFX", "CalendarFX", "FlexGanttFX", "FormsFX");

    // Create the CheckListView with the data
    checkListView = new CheckListView<>(libraries);

    // initialize map for dialog
    mapView = new GoogleMapView();
    mapView.addMapInializedListener(this);
  }

  private void layoutParts() {
    customPane.add(confirmBtn, 0, 0);
    customPane.add(errorBtn, 0, 1);
    customPane.add(warningBtn, 0, 2);
    customPane.add(informationBtn, 0, 3);

    customPane.add(errorExceptionBtn, 1, 0);
    customPane.add(errorDetailsBtn, 1, 1);
    customPane.add(customSmallBtn, 1, 2);
    customPane.add(customFullBtn, 1, 3);
    customPane.add(customFullMaxBtn, 1, 4);

    customPane.add(longTitleBtn, 2, 0);
    customPane.add(longMessageBtn, 2, 1);
    customPane.add(longTitleMessageBtn, 2, 2);

    customPane.setAlignment(Pos.CENTER);
  }

  private void setupEventHandlers() {
    confirmBtn.setOnAction(event -> getWorkbench().showConfirmationDialog("Continue without saving?", "Are you sure you want to continue without saving your document?"));
    errorBtn.setOnAction(event -> getWorkbench().showErrorDialog("Button click failed!", "During the click of this button, something went horribly wrong."));
    errorExceptionBtn.setOnAction(event -> getWorkbench().showErrorDialog("Button click failed!", "During the click of this button, something went horribly wrong. Please forward the content below to anyone but the WorkbenchFX developers to track down the issue:", exception));
    errorDetailsBtn.setOnAction(event -> getWorkbench().showErrorDialog("Button click failed!", "During the click of this button, something went horribly wrong.", "Details about this exception are not present."));
    warningBtn.setOnAction(event -> getWorkbench().showWarningDialog("Reset settings?", "This will reset your device to its default factory settings."));
    informationBtn.setOnAction(event -> getWorkbench().showInformationDialog("Everything is fine", "You can relax, nothing wrong here."));
    longTitleBtn.setOnAction(event -> getWorkbench().showInformationDialog("Filming started 2 December 1939. The film recorded a loss of $104,000. Ikrandraco (\"Ikran dragon\") is a genus of pteranodontoid pterosaur known from Lower Cretaceous rocks in northeastern China. It is notable for its unusual skull, which features a crest on the lower jaw. Ikrandraco is based on IVPP V18199, a partial skeleton including the skull and jaws, several neck vertebrae, a partial sternal plate, parts of both wings, and part of a foot.", "You can relax, nothing wrong here."));
    longMessageBtn.setOnAction(event -> getWorkbench().showInformationDialog("Everything is fine", "Filming started 2 December 1939. The film recorded a loss of $104,000. Ikrandraco (\"Ikran dragon\") is a genus of pteranodontoid pterosaur known from Lower Cretaceous rocks in northeastern China. It is notable for its unusual skull, which features a crest on the lower jaw. Ikrandraco is based on IVPP V18199, a partial skeleton including the skull and jaws, several neck vertebrae, a partial sternal plate, parts of both wings, and part of a foot."));
    longTitleMessageBtn.setOnAction(event -> getWorkbench().showInformationDialog("In 2004, Bennett ruled that John Graham could be extradited to the United States for trial for the 1975 murder of Anna Mae Aquash, one of the most prominent members of the American Indian Movement. In 2007, she began proceedings on the Basi-Virk Affair where the Minister of Finance's politically appointed assistant was charged with the sale of benefits related to the province's sale of BC Rail, the publicly owned railway. The scandal came to public attention when news media filmed the RCMP conducting a search warrant inside the BC Legislature building.", "Filming started 2 December 1939. The film recorded a loss of $104,000. Ikrandraco (\"Ikran dragon\") is a genus of pteranodontoid pterosaur known from Lower Cretaceous rocks in northeastern China. It is notable for its unusual skull, which features a crest on the lower jaw. Ikrandraco is based on IVPP V18199, a partial skeleton including the skull and jaws, several neck vertebrae, a partial sternal plate, parts of both wings, and part of a foot."));
    customSmallBtn.setOnAction(event -> getWorkbench().showDialog(WorkbenchDialog.Type.INPUT,"Select your favorite libraries", checkListView));
    customFullBtn.setOnAction(event -> getWorkbench().showDialog(WorkbenchDialog.builder("Map Overview", mapView, ButtonType.CLOSE).setBlocking(true).build()));
    customFullMaxBtn.setOnAction(event -> getWorkbench().showDialog(WorkbenchDialog.builder("Map Overview", mapView, ButtonType.CLOSE).setMaximized(true).build()));
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

    markerOptions.position( new LatLong(47.4814072, 8.2116446) )
        .visible(Boolean.TRUE)
        .title("FHNW");

    Marker marker = new Marker( markerOptions );

    map.addMarker(marker);

  }

  @Override
  public Node activate() {
    return customPane;
  }
}
