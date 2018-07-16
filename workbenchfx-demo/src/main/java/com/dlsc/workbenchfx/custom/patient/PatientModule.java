package com.dlsc.workbenchfx.custom.patient;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.custom.patient.model.FileCabinet;
import com.dlsc.workbenchfx.custom.patient.model.Translator;
import com.dlsc.workbenchfx.custom.patient.view.PatientView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.Dropdown;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

/**
 * @author Dieter Holz
 */
public class PatientModule extends WorkbenchModule {

    private FileCabinet fileCabinet;
    private Translator  translator;
    private PatientView patientView;


    private Workbench workbench;
    private Dropdown dropdown;

    public PatientModule(){
        super("Patient Records", FontAwesomeIcon.USER);
    }

    @Override
    public void init(Workbench workbench) {
        super.init(workbench);
        this.workbench = workbench;

        fileCabinet = new FileCabinet();
        translator  = new Translator();

        patientView = new PatientView(fileCabinet, translator, workbench);
    }

    @Override
    public Node activate() {

        MenuItem de = new MenuItem("de");
        de.setOnAction(event -> translator.translateToGerman());

        MenuItem en = new MenuItem("en");
        en.setOnAction(event -> translator.translateToEnglish());

        dropdown = Dropdown.of(new FontAwesomeIconView(FontAwesomeIcon.LANGUAGE),
                               de,
                               en);
        workbench.getToolbarControlsRight().add(dropdown);

        return patientView;
    }

    @Override
    public void deactivate() {
        workbench.getToolbarControlsRight().remove(dropdown);
    }

    @Override
    public boolean destroy() {
        deactivate();

        return true;
    }
}
