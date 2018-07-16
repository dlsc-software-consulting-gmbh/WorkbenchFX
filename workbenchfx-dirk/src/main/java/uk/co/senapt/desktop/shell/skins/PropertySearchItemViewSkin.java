package uk.co.senapt.desktop.shell.skins;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import uk.co.senapt.desktop.shell.PropertySearchItemView;
import uk.co.senapt.desktop.shell.model.PropertyDefinition;
import uk.co.senapt.desktop.shell.model.SearchOperator;

import java.util.List;

public class PropertySearchItemViewSkin extends SkinBase<PropertySearchItemView> {

    private final ComboBox<PropertyDefinition> propertiesComboBox = new ComboBox<>();
    private final ComboBox<SearchOperator> operatorsComboBox = new ComboBox<>();
    private final EmptyBox propertyEmptyBox = new EmptyBox();
    private final EmptyBox operatorEmptyBox = new EmptyBox();
    private final EmptyBox editorEmptyBox = new EmptyBox();
    private final TextField defaultEditor = new TextField();
    private Node editor;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public PropertySearchItemViewSkin(PropertySearchItemView control) {
        super(control);

        defaultEditor.setDisable(true);
        editor = defaultEditor;

        propertiesComboBox.setItems(control.getAvailableProperties());
        propertiesComboBox.valueProperty().bindBidirectional(control.propertyProperty());
        propertiesComboBox.prefWidthProperty().bind(control.widthProperty().multiply(0.25));

        operatorsComboBox.prefWidthProperty().bind(control.widthProperty().multiply(0.25));
        operatorsComboBox.valueProperty().bindBidirectional(control.operatorProperty());
        operatorsComboBox.disableProperty().bind(Bindings.isNull(propertiesComboBox.valueProperty()));
        updateOperatorItems();

        control.propertyProperty().addListener(obs -> buildChildren());
        control.operatorProperty().addListener(obs -> buildChildren());
        control.propertyTypeProperty().addListener(obs -> updateOperatorItems());

        buildChildren();
    }

    private void updateOperatorItems () {
        if (getSkinnable().getPropertyType() == null) {
            operatorsComboBox.getItems().clear();
        }
        else {
            List<SearchOperator> operators = SearchOperator.getSupportedOperators(getSkinnable().getPropertyType());
            operatorsComboBox.getItems().setAll(operators);
            if (!operators.isEmpty()) {
                operatorsComboBox.setValue(operators.get(0));
            }
        }
    }

    private void buildChildren () {
        if (getSkinnable().isEmpty()) {
            getChildren().setAll(propertyEmptyBox, operatorEmptyBox, editorEmptyBox);
        }
        else {
            getChildren().remove(editor);
            editor = getSkinnable().getEditorFactory().call(getSkinnable());
            if (editor == null) {
                editor = defaultEditor;
            }

            if (getChildren().contains(propertiesComboBox)) {
                getChildren().add(editor);
            }
            else {
                getChildren().setAll(propertiesComboBox, operatorsComboBox, editor);
            }
        }
        getSkinnable().requestLayout();
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        final double gap = getSkinnable().getHgap();
        final double contentWidth = w - (gap * 2);
        final double c1Width = contentWidth * 0.3;
        final double c2Width = contentWidth * 0.3;
        final double c3Width = contentWidth * 0.4;

        if (getSkinnable().isEmpty()) {
            propertyEmptyBox.resizeRelocate(x, y, c1Width, h);
            operatorEmptyBox.resizeRelocate(x + c1Width + gap, y, c2Width, h);
            editorEmptyBox.resizeRelocate(x + c1Width + gap + c2Width + gap, y, c3Width, h);
        }
        else {
            propertiesComboBox.resizeRelocate(x, y, c1Width, h);
            operatorsComboBox.resizeRelocate(x + c1Width + gap, y, c2Width, h);
            editor.resizeRelocate(x + c1Width + gap + c2Width + gap, y, c3Width, h);
        }
    }

    private static class EmptyBox extends HBox {
        EmptyBox () {
            getStyleClass().add("empty-box");
        }
    }

}
