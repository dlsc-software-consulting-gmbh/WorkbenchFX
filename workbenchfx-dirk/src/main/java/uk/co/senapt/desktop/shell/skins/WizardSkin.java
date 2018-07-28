package uk.co.senapt.desktop.shell.skins;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import uk.co.senapt.desktop.shell.PrettyScrollPane;
import uk.co.senapt.desktop.shell.Wizard;
import uk.co.senapt.desktop.shell.WizardStep;

/**
 * Created by gdiaz on 25/09/2017.
 */
public class WizardSkin extends SkinBase<Wizard> {

    private static final PseudoClass VISITED = PseudoClass.getPseudoClass("visited");

    private final PrettyScrollPane scrollPane;

    private final BooleanBinding stepsBindings = Bindings.greaterThan(Bindings.size(getSkinnable().getSteps()), 1);

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public WizardSkin(Wizard control) {
        super(control);

        WizardProgressIndicator progress = new WizardProgressIndicator();
        WizardContent content = new WizardContent();
        control.contentLocationProperty().bind(content.layoutYProperty());
        WizardNavigationBar navigation = new WizardNavigationBar();

        VBox box = new VBox(progress, content, navigation);
        VBox.setVgrow(progress, Priority.NEVER);
        VBox.setVgrow(content, Priority.ALWAYS);
        VBox.setVgrow(navigation, Priority.NEVER);

        scrollPane = new PrettyScrollPane(box);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        getChildren().addAll(scrollPane);
    }

    private class WizardProgressIndicator extends Pane {

        private static final double SPACE_BETWEEN_ICON_NAME = 10;

        private final List<WizardStepSkin> steps = new ArrayList<>();

        WizardProgressIndicator() {
            final Wizard wizard = getSkinnable();
            getStyleClass().add("wizard-progress-indicator");
            wizard.getSteps().addListener((Observable obs) -> build());
            wizard.currentStepProperty().addListener(obs -> build());
            visibleProperty().bind(stepsBindings);
            managedProperty().bind(visibleProperty());
            build();
        }

        private void build() {
            final Wizard wizard = getSkinnable();

            getChildren().clear();
            steps.clear();

            if (!wizard.getSteps().isEmpty()) {
                WizardStepSkin previousStep = null;

                for (int i = 0; i < wizard.getSteps().size(); i++) {
                    WizardStep step = wizard.getSteps().get(i);
                    WizardStepSkin stepSkin = new WizardStepSkin(step, wizard.isVisited(step));

                    if (previousStep != null) {
                        previousStep.connectTo(stepSkin);
                        getChildren().add(previousStep.getConnector());
                    }

                    getChildren().add(stepSkin.getIcon());

                    if (stepSkin.getName().isManaged()) {
                        getChildren().add(stepSkin.getName());
                    }

                    steps.add(stepSkin);
                    previousStep = stepSkin;
                }
            }

            wizard.requestLayout();
        }

        @Override
        protected double computePrefHeight(double width) {
            if (!steps.isEmpty()) {

                WizardStepSkin step = steps.get(0);
                Label icon = step.getIcon();
                Label name = step.getName();
                double contentHeight = icon.prefHeight(-1) + name.prefHeight(-1) + SPACE_BETWEEN_ICON_NAME;
                return getInsets().getTop() + contentHeight + getInsets().getBottom();

            }

            return super.computePrefHeight(width);
        }

        @Override
        protected double computeMinHeight(double width) {
            return super.computePrefHeight(width);
        }

        @Override
        protected void layoutChildren() {
            if (steps.isEmpty()) {
                return;
            }

            double x = snappedLeftInset();
            double y = snappedTopInset();
            double w = snapSize(getWidth()) - x - snappedRightInset();
            double distanceBetweenSteps = 0;

            if (steps.size() > 1) {
                distanceBetweenSteps = w / (steps.size() - 1);
            }

            for (int i = 0; i < steps.size(); i++) {
                WizardStepSkin step = steps.get(i);
                positionIconAndName(step, i, x, y, w, distanceBetweenSteps);
                positionConnector(step, i, x, y, distanceBetweenSteps);
            }
        }

        private void positionIconAndName(WizardStepSkin step, int stepNumber, double parentX, double parentY, double parentWidth, double distanceBetweenSteps) {
            Label icon = step.getIcon();
            Label name = step.getName();

            double iconX;
            double iconW = icon.prefWidth(-1);
            double iconH = step.getIcon().prefHeight(-1);

            double nameX;
            double nameW = name.prefWidth(-1);
            double nameY = parentY + iconH + SPACE_BETWEEN_ICON_NAME;

            if (steps.size() == 1) {
                iconX = parentX + (parentWidth / 2) - (iconW / 2);
                nameX = parentX + (parentWidth / 2) - (nameW / 2);
            } else {
                final Wizard wizard = getSkinnable();

                if (wizard.isFirst(step.getStep())) {
                    iconX = parentX;
                    nameX = parentX;
                } else if (wizard.isLast(step.getStep())) {
                    iconX = parentX + parentWidth - iconW;
                    nameX = parentX + parentWidth - nameW;
                } else {
                    iconX = parentX + (stepNumber * distanceBetweenSteps) - (iconW / 2);
                    nameX = parentX + (stepNumber * distanceBetweenSteps) - (nameW / 2);
                }
            }

            icon.autosize();
            icon.relocate(iconX, parentY);

            if (name.isManaged()) {
                name.autosize();
                name.relocate(nameX, nameY);
            }
        }

        private void positionConnector(WizardStepSkin stepSkin, int stepIndex, double parentX, double parentY, double distanceBetweenSteps) {
            if (stepSkin.getConnector() != null) {
                Label icon = stepSkin.getIcon();
                Separator connector = stepSkin.getConnector();

                boolean isFirst = getSkinnable().isFirst(stepSkin.getStep());
                boolean isNextLast = (stepIndex + 1) == (getSkinnable().getSteps().size() - 1);

                double fixGap = 1;
                double iconW = icon.prefWidth(-1);
                double iconH = icon.prefHeight(-1);

                double connectorW = distanceBetweenSteps - (iconW * (isFirst? 1.0 : 0.5)) - (iconW * (isNextLast ? 1.0 : 0.5)) + (fixGap * 2);
                double connectorH = connector.prefHeight(-1);
                double connectorX = parentX + (stepIndex * distanceBetweenSteps) + (isFirst ? iconW : (iconW / 2)) - fixGap;
                double connectorY = parentY + (iconH / 2) - (connectorH / 2);

                connector.setId("");
                connector.setPrefWidth(connectorW);
                connector.toBack();
                connector.autosize();
                connector.relocate(connectorX, connectorY);
            }
        }
    }

    private class WizardContent extends Pagination {

        WizardContent() {
            final Wizard wizard = getSkinnable();

            setMaxPageIndicatorCount(1);

            final WizardStep currentStep = wizard.getCurrentStep();
            if (currentStep != null) {
                getChildren().add(currentStep.getContent());
            }

            pageCountProperty().bind(Bindings.size(wizard.getSteps()));

            setPageFactory(index -> wizard.getSteps().get(index).getContent());

            wizard.currentStepIndexProperty().addListener(it -> updateCurrentPage());
            updateCurrentPage();
        }

        private void updateCurrentPage() {
            final Wizard wizard = getSkinnable();
            setCurrentPageIndex(wizard.getCurrentStepIndex());
        }
    }

    private class WizardNavigationBar extends HBox {

        private final Button backBtn = new Button();
        private final Button nextBtn = new Button();
        private final HBox nextBox;
        private final HBox backBox;

        WizardNavigationBar() {
            final Wizard wizard = getSkinnable();

            backBox = new HBox(backBtn);
            nextBox = new HBox(nextBtn);

            backBox.setAlignment(Pos.CENTER_LEFT);
            nextBox.setAlignment(Pos.CENTER_RIGHT);

            HBox.setHgrow(backBox, Priority.ALWAYS);
            HBox.setHgrow(nextBox, Priority.ALWAYS);
            getChildren().addAll(backBox, nextBox);

            final Text backIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.LONG_ARROW_LEFT);
            final Text nextIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.LONG_ARROW_RIGHT);

            backIcon.getStyleClass().add("back-icon");
            nextIcon.getStyleClass().add("next-icon");

            backBtn.setAlignment(Pos.CENTER_LEFT);
            backBtn.getStyleClass().addAll("wizard-button", "back-button");
            backBtn.setGraphic(backIcon);
            backBtn.setOnAction(evt -> {
                wizard.back();
                scrollPane.setVvalue(0);
            });


            nextBtn.setAlignment(Pos.CENTER_RIGHT);
            nextBtn.getStyleClass().addAll("wizard-button", "next-button");
            nextBtn.setGraphic(nextIcon);
            nextBtn.setContentDisplay(ContentDisplay.RIGHT);
            nextBtn.setOnAction(evt -> {
                wizard.next();
                scrollPane.setVvalue(0);
            });

            getStyleClass().add("navigation-bar");
            visibleProperty().bind(stepsBindings);
            managedProperty().bind(visibleProperty());
            wizard.currentStepProperty().addListener(obs -> updateButtons());
            updateButtons();
        }

        private void updateButtons() {
            final Wizard wizard = getSkinnable();

            // remove all nodes / buttons that are not the back or next button, as in "custom wizard step buttons"
            nextBox.getChildren().removeIf(node -> node != nextBtn);

            WizardStep currentStep = wizard.getCurrentStep();
            if (currentStep != null) {
                nextBox.getChildren().addAll(currentStep.getButtons());
            }

            backBtn.setVisible(!wizard.isFirst(currentStep));
            nextBtn.setVisible(!wizard.isLast(currentStep));

            WizardStep previous = wizard.getPreviousStep();
            WizardStep next = wizard.getNextStep();

            if (previous == null || previous.getName() == null) {
                backBtn.setText("BACK");
            } else {
                backBtn.setText("BACK - " + previous.getName().toUpperCase());
            }

            if (next == null || next.getName() == null) {
                nextBtn.setText("NEXT");
            } else {
                nextBtn.setText("NEXT - " + next.getName().toUpperCase());
            }

            nextBtn.disableProperty().unbind();
            if (wizard.getCurrentStep() != null) {
                nextBtn.disableProperty().bind(wizard.getCurrentStep().validProperty().not());
            }
        }
    }

    private class WizardStepSkin {

        private Label icon;
        private Label name;
        private Separator connector;
        private WizardStep step;
        private boolean visited;

        WizardStepSkin(WizardStep step, boolean visited) {
            this.step = step;
            this.visited = visited;

            icon = new Label();
            icon.getStyleClass().add("step-icon");
            icon.pseudoClassStateChanged(VISITED, visited);

            name = new Label();
            name.getStyleClass().add("step-name");
            name.textProperty().bind(step.nameProperty());
            name.managedProperty().bind(Bindings.isNotEmpty(step.nameProperty()));
            name.pseudoClassStateChanged(VISITED, visited);
        }

        void connectTo(WizardStepSkin nextStep) {
            if (nextStep != null) {
                connector = new Separator();
                connector.getStyleClass().add("step-connector");
                connector.pseudoClassStateChanged(VISITED, nextStep.visited);
            }
        }

        WizardStep getStep() {
            return step;
        }

        Separator getConnector() {
            return connector;
        }

        public Label getIcon() {
            return icon;
        }

        public Label getName() {
            return name;
        }

    }

}
