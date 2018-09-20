package com.dlsc.workbenchfx.modules.patient;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.modules.patient.model.FileCabinet;
import com.dlsc.workbenchfx.modules.patient.model.Translator;
import com.dlsc.workbenchfx.modules.patient.view.AllPatientsView;
import com.dlsc.workbenchfx.modules.patient.view.PatientSelectionView;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;

/**
 * @author Dieter Holz
 */
public class PatientModule extends WorkbenchModule {

  private FileCabinet fileCabinet;
  private Translator translator;
  private AllPatientsView patientView;

  private Workbench workbench;
  private ToolbarItem languageItem;
  private WorkbenchDialog selectPatientDialog;

  public PatientModule() {
    super("Patient Records", MaterialDesignIcon.ACCOUNT);
    getToolbarControlsLeft().addAll(
        new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.CONTENT_SAVE),
            event -> fileCabinet.save()),
        new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.PLUS)),
        new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.MINUS)),
        new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT),
            event -> workbench.showDialog(selectPatientDialog)
        )
    );
  }

  @Override
  public void init(Workbench workbench) {
    super.init(workbench);

    this.workbench = workbench;

    fileCabinet = new FileCabinet();
    translator = new Translator();

    // initialize all the elements used directly in workbench
    languageItem = createLanguageItem();

    selectPatientDialog = createSelectPatientDialog();

    patientView = new AllPatientsView(fileCabinet, translator, workbench);
  }

  @Override
  public Node activate() {
    workbench.getToolbarControlsRight().add(languageItem);

    return patientView;
  }

  @Override
  public void deactivate() {
    workbench.getToolbarControlsRight().remove(languageItem);
  }

  @Override
  public boolean destroy() {
    return true;
  }

  private ToolbarItem createLanguageItem() {
    MenuItem de = new MenuItem("de");
    de.setOnAction(event -> translator.translateToGerman());

    MenuItem en = new MenuItem("en");
    en.setOnAction(event -> translator.translateToEnglish());

    return new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.TRANSLATE),
        de,
        en);
  }

  private WorkbenchDialog createSelectPatientDialog() {
    PatientSelectionView dialogContent = new PatientSelectionView(fileCabinet, translator);
    WorkbenchDialog dialog = WorkbenchDialog
        .builder("Select Patient", dialogContent, WorkbenchDialog.Type.INPUT)
        .onResult(buttonType -> {
          if (buttonType.equals(ButtonType.OK)) {
            fileCabinet.setSelectedPatient(dialogContent.getSelected());
          }
        })
        .build();
    dialog.titleProperty().bind(translator.selectPatientLabelProperty());

    return dialog;
  }
}
