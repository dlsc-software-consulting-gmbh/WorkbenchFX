package com.dlsc.workbenchfx.custom.test;

import static com.dlsc.workbenchfx.model.WorkbenchDialog.Type;

import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckListView;

public class DialogTestModule extends WorkbenchModule implements MapComponentInitializedListener {
  private static final Logger LOGGER = LogManager.getLogger(DialogTestModule.class.getName());
  private int itemsCount = 1;

  private final Button confirmBtn = new Button("Confirmation Dialog");
  private final Button errorBtn = new Button("Error Dialog");
  private final Button errorExceptionBtn = new Button("Error Dialog with Exception");
  private final Button errorDetailsBtn = new Button("Error Dialog with Details");
  private final Button warningBtn = new Button("Warning Dialog");
  private final Button informationBtn = new Button("Information Dialog");
  private final Button customSmallBtn = new Button("Custom Small Dialog");
  private final Button customFullBtn = new Button("Custom Full Coverage Dialog (blocking)");
  private final Button customFullMaxBtn = new Button("Custom Full Coverage Maximized Dialog");
  private final Button longTitleBtn = new Button("Long Title Dialog");
  private final Button longMessageBtn = new Button("Long Message Dialog");
  private final Button longTitleMessageBtn = new Button("Long Title & Message Dialog");
  private final Button noButtonsBtn = new Button("No Buttons Dialog");
  private final Button conditionalBtn = new Button("Conditional Button Dialog");
  private final Button settingsBtn = new Button("Settings Dialog With 3 Buttons");

  private GoogleMapView mapView;
  private GoogleMap map;

  private final GridPane customPane = new GridPane();

  NullPointerException exception;
  private CheckListView<String> checkListView;
  private CheckBox checkBox = new CheckBox("I accept the Terms and Conditions");
  private WorkbenchDialog favoriteLibrariesDialog;

  public DialogTestModule() {
    super("Dialog Test", MaterialDesignIcon.HELP);
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

    // initialize favorites dialog separately
    favoriteLibrariesDialog =
        WorkbenchDialog.builder("Select your favorite libraries", checkListView, Type.INPUT)
            .onResult(buttonType -> {
              if (ButtonType.CANCEL.equals(buttonType)) {
                System.err.println("Dialog was cancelled!");
              } else {
                System.err.println("Chosen favorite libraries: " +
                    checkListView.getCheckModel().getCheckedItems().stream().collect(
                        Collectors.joining(", ")));
              }
            }).build();
  }

  private void layoutParts() {
    customPane.add(confirmBtn, 0, 0);
    customPane.add(errorBtn, 0, 1);
    customPane.add(warningBtn, 0, 2);
    customPane.add(informationBtn, 0, 3);
    customPane.add(settingsBtn, 0, 4);

    customPane.add(errorExceptionBtn, 1, 0);
    customPane.add(errorDetailsBtn, 1, 1);
    customPane.add(customSmallBtn, 1, 2);
    customPane.add(customFullBtn, 1, 3);
    customPane.add(customFullMaxBtn, 1, 4);

    customPane.add(longTitleBtn, 2, 0);
    customPane.add(longMessageBtn, 2, 1);
    customPane.add(longTitleMessageBtn, 2, 2);
    customPane.add(noButtonsBtn, 2, 3);
    customPane.add(conditionalBtn, 2, 4);


    customPane.setAlignment(Pos.CENTER);
  }

  private void setupEventHandlers() {
    Consumer<ButtonType> printResult = System.out::println;
    confirmBtn.setOnAction(
        event -> getWorkbench().showConfirmationDialog("Continue without saving?",
            "Are you sure you want to continue without saving your document?", printResult));
    errorBtn.setOnAction(event -> getWorkbench().showErrorDialog("Button click failed!",
        "During the click of this button, something went horribly wrong.", printResult));
    errorExceptionBtn.setOnAction(event -> getWorkbench().showErrorDialog("Button click failed!",
        "During the click of this button, something went horribly wrong. Please forward the content below to anyone but the WorkbenchFX developers to track down the issue:",
        exception, printResult));
    errorDetailsBtn.setOnAction(event -> getWorkbench().showErrorDialog("Button click failed!",
        "During the click of this button, something went horribly wrong.",
        "Details about this exception are not present.", printResult));
    warningBtn.setOnAction(event -> getWorkbench().showWarningDialog("Reset settings?",
        "This will reset your device to its default factory settings.", printResult));
    //informationBtn.setOnAction(event -> getWorkbench().showInformationDialog("Everything is fine", "You can relax, nothing wrong here.", printResult));

    informationBtn.setOnAction(event -> getWorkbench().showDialog(
        WorkbenchDialog.builder("title", "message", ButtonType.OK)
            .blocking(false)
            .onResult(printResult)
            .build()));
    longTitleBtn.setOnAction(event -> getWorkbench().showInformationDialog(
        "Filming started 2 December 1939. The film recorded a loss of $104,000. Ikrandraco (\"Ikran dragon\") is a genus of pteranodontoid pterosaur known from Lower Cretaceous rocks in northeastern China. It is notable for its unusual skull, which features a crest on the lower jaw. Ikrandraco is based on IVPP V18199, a partial skeleton including the skull and jaws, several neck vertebrae, a partial sternal plate, parts of both wings, and part of a foot.",
        "You can relax, nothing wrong here.", printResult));
    longMessageBtn.setOnAction(event -> getWorkbench().showInformationDialog("Everything is fine",
        "Filming started 2 December 1939. The film recorded a loss of $104,000. Ikrandraco (\"Ikran dragon\") is a genus of pteranodontoid pterosaur known from Lower Cretaceous rocks in northeastern China. It is notable for its unusual skull, which features a crest on the lower jaw. Ikrandraco is based on IVPP V18199, a partial skeleton including the skull and jaws, several neck vertebrae, a partial sternal plate, parts of both wings, and part of a foot.",
        printResult));
    longTitleMessageBtn.setOnAction(event -> getWorkbench().showInformationDialog(
        "In 2004, Bennett ruled that John Graham could be extradited to the United States for trial for the 1975 murder of Anna Mae Aquash, one of the most prominent members of the American Indian Movement. In 2007, she began proceedings on the Basi-Virk Affair where the Minister of Finance's politically appointed assistant was charged with the sale of benefits related to the province's sale of BC Rail, the publicly owned railway. The scandal came to public attention when news media filmed the RCMP conducting a search warrant inside the BC Legislature building.",
        "Filming started 2 December 1939. The film recorded a loss of $104,000. Ikrandraco (\"Ikran dragon\") is a genus of pteranodontoid pterosaur known from Lower Cretaceous rocks in northeastern China. It is notable for its unusual skull, which features a crest on the lower jaw. Ikrandraco is based on IVPP V18199, a partial skeleton including the skull and jaws, several neck vertebrae, a partial sternal plate, parts of both wings, and part of a foot.",
        printResult));
    customSmallBtn.setOnAction(event -> {
      getWorkbench().showDialog(
          favoriteLibrariesDialog
      );
    });
    customFullBtn.setOnAction(event -> getWorkbench().showDialog(
        WorkbenchDialog.builder("Map Overview (blocking)", mapView, ButtonType.CLOSE).blocking(
            true).build()));
    customFullMaxBtn.setOnAction(event -> {
      getWorkbench().showDialog(WorkbenchDialog.builder("Map Overview", mapView, ButtonType.FINISH)
          .maximized(true)
          .onResult(buttonType -> System.err.println("Dialog result: " + buttonType))
          .build()
      );
    });
    noButtonsBtn.setOnAction(event -> {
      getWorkbench().showDialog(WorkbenchDialog.builder("This dialog has no buttons",
          "Click outside of the dialog to close it.", Type.INFORMATION)
          .showButtonsBar(false)
          .onResult(buttonType -> System.err.println("Dialog result: " + buttonType))
          .build()
      );
    });
    conditionalBtn.setOnAction(event -> {
      WorkbenchDialog dialog =
          WorkbenchDialog.builder("Check the box to continue", checkBox, ButtonType.OK)
              .build();
      dialog.setOnShown(event1 -> {
        dialog.getButton(ButtonType.OK).ifPresent(button -> {
          button.disableProperty().bind(checkBox.selectedProperty().not());
        });
      });
      getWorkbench().showDialog(dialog);
    });
    CheckBox settingsBox = new CheckBox("Insert your Preferences(FX) window.\n"
        + "Check the box to simulate a change.");
    settingsBtn.setOnAction(event -> {
      WorkbenchDialog dialog = WorkbenchDialog.builder(
          "Settings with 3 buttons", settingsBox,
          ButtonType.OK, ButtonType.CANCEL, ButtonType.APPLY
      ).onResult(buttonType -> {
        if (ButtonType.OK.equals(buttonType)) {
          // Do your OK stuff
          LOGGER.trace("OK pressed: SettingsBox.isSelected() = " + settingsBox.isSelected());
        }
        if (ButtonType.CANCEL.equals(buttonType)) {
          LOGGER.trace("CANCEL pressed: SettingsBox.isSelected() = " + settingsBox.isSelected());
        }
      }).build();
      dialog.setOnShown(event1 -> {
        dialog.getButton(ButtonType.APPLY).ifPresent(button -> {
          button.disableProperty().bind(settingsBox.selectedProperty().not());
          button.addEventFilter(ActionEvent.ACTION, event2 -> {
            // Do your APPLY stuff
            button.disableProperty().unbind(); // Unbind (because we're doing our "saving")
            // Bind again ("saving" is done and the new state is bound)
            if (settingsBox.isSelected()) {
              button.disableProperty().bind(settingsBox.selectedProperty());
            } else {
              button.disableProperty().bind(settingsBox.selectedProperty().not());
            }
            event2.consume();
          });
        });
      });
      getWorkbench().showDialog(dialog);
    });
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

  @Override
  public Node activate() {
    return customPane;
  }
}
