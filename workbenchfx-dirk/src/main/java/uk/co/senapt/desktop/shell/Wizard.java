package uk.co.senapt.desktop.shell;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import uk.co.senapt.desktop.shell.skins.WizardSkin;

/**
 * Created by gdiaz on 25/09/2017.
 */
public class Wizard extends Control {

    public Wizard() {
        super();
        getStyleClass().add("wizard");

        steps.addListener((Observable obs) -> first());
        currentStep.bind(Bindings.valueAt(steps, currentStepIndex));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new WizardSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource("wizard.css").toExternalForm();
    }

    private final DoubleProperty contentLocation = new SimpleDoubleProperty(this, "contentLocation");

    public final DoubleProperty contentLocationProperty() {
        return contentLocation;
    }

    public final double getContentLocation() {
        return contentLocation.get();
    }

    private final ObservableList<WizardStep> steps = FXCollections.observableArrayList();

    public ObservableList<WizardStep> getSteps() {
        return steps;
    }

    public WizardStep addStep(String name, Node content) {
        WizardStep step = new WizardStep();
        step.setName(name);
        step.setContent(content);
        steps.add(step);
        return step;
    }

    private final ReadOnlyIntegerWrapper stepCount = new ReadOnlyIntegerWrapper(this, "stepCount");

    public final ReadOnlyIntegerWrapper stepCountProperty() {
        return stepCount;
    }

    public final int getStepCount() {
        return stepCount.get();
    }

    private final ReadOnlyObjectWrapper<WizardStep> currentStep = new ReadOnlyObjectWrapper<>(this, "currentStep");

    public final ReadOnlyObjectProperty<WizardStep> currentStepProperty() {
        return currentStep.getReadOnlyProperty();
    }

    public final WizardStep getCurrentStep() {
        return currentStep.get();
    }

    private final ReadOnlyIntegerWrapper currentStepIndex = new ReadOnlyIntegerWrapper(this, "currentStepIndex");

    public final ReadOnlyIntegerProperty currentStepIndexProperty() {
        return currentStepIndex.getReadOnlyProperty();
    }

    public final int getCurrentStepIndex() {
        return currentStepIndex.get();
    }

    public final WizardStep getPreviousStep() {
        if (currentStepIndex.get() > 0) {
            return steps.get(currentStepIndex.get() - 1);
        }
        return null;
    }

    public final WizardStep getNextStep() {
        if (currentStepIndex.get() < steps.size() - 1) {
            return steps.get(currentStepIndex.get() + 1);
        }
        return null;
    }

    public void next() {
        if (canMove() && !isLast(getCurrentStep())) {
            currentStepIndex.set(currentStepIndex.get() + 1);
        }
    }

    public void back() {
        if (canMove() && !isFirst(getCurrentStep())) {
            currentStepIndex.set(currentStepIndex.get() - 1);
        }
    }

    public void first() {
        if (canMove()) {
            currentStepIndex.set(0);
        }
    }

    public boolean isFirst(WizardStep step) {
        return !steps.isEmpty() && steps.get(0).equals(step);
    }

    public boolean isLast(WizardStep step) {
        return !steps.isEmpty() && steps.get(steps.size() - 1).equals(step);
    }

    public boolean isVisited(WizardStep step) {
        return getCurrentStep() != null && steps.indexOf(getCurrentStep()) >= steps.indexOf(step);
    }

    public boolean canMove() {
        if (!steps.isEmpty()) {
            if (currentStep.get() == null) {
                return true;
            }

            if (currentStep.get().isValid()) {
                return true;
            }
        }
        return false;
    }

}