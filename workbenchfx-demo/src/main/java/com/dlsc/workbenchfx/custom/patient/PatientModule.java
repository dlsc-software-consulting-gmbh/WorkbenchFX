package com.dlsc.workbenchfx.custom.patient;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.custom.patient.model.FileCabinet;
import com.dlsc.workbenchfx.custom.patient.view.PatientView;
import com.dlsc.workbenchfx.module.Module;
import com.dlsc.workbenchfx.view.controls.Dropdown;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

/**
 * @author Dieter Holz
 */
public class PatientModule extends Module {

    private FileCabinet fileCabinet;
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

        patientView = new PatientView(fileCabinet, workbench);
    }

    @Override
    public Node activate() {

        dropdown = Dropdown.of(new FontAwesomeIconView(FontAwesomeIcon.LANGUAGE),
                               new MenuItem("de"),
                               new MenuItem("en"));
        workbench.addToolbarControlRight(dropdown);

        return patientView;
    }

    @Override
    public void deactivate() {
        workbench.removeToolbarControlRight(dropdown);
    }

    @Override
    public boolean destroy() {
        deactivate();

        return true;
    }
}
