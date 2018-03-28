package uk.co.senapt.desktop.shell.skins;

import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Pagination;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import uk.co.senapt.desktop.shell.ShellModule;
import uk.co.senapt.desktop.shell.HomeScreen;

/**
 * Created by lemmi on 22.08.17.
 */
public class HomeScreenSkin extends SkinBase<HomeScreen> {

    private static final int MODULE_BUTTONS_PER_PAGE = 15;

    private static final int COLUMNS_PER_ROW = 5;

    public HomeScreenSkin(HomeScreen screen) {
        super(screen);

        screen.getShell().getModules().addListener((Observable obs) -> buildLayout());
        buildLayout();

    }

    private void buildLayout () {
        Pagination pagination = new Pagination(getSkinnable().getShell().getModules().size() / MODULE_BUTTONS_PER_PAGE + 1);
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));
        pagination.setMaxPageIndicatorCount(100);
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

        AnchorPane anchor = new AnchorPane();
        AnchorPane.setTopAnchor(pagination, 0.0);
        AnchorPane.setRightAnchor(pagination, 10.0);
        AnchorPane.setBottomAnchor(pagination, 60.0);
        AnchorPane.setLeftAnchor(pagination, 10.0);
        anchor.getChildren().addAll(pagination);

        getChildren().setAll(anchor);
    }

    private Node createPage(int pageIndex) {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("modules-page");

        int position = pageIndex * MODULE_BUTTONS_PER_PAGE;
        int count = 0;
        int column = 0;
        int row = 0;

        while (count < MODULE_BUTTONS_PER_PAGE && position < getSkinnable().getShell().getModules().size()) {
            ShellModule module = getSkinnable().getShell().getModules().get(position);
            final Button button = new Button(module.getName());
            button.getStyleClass().add("module-button");
            button.getStyleClass().add(module.getIconClass());
            button.setContentDisplay(ContentDisplay.TOP);
            gridPane.add(button, column, row);

            if (module.isImplemented()) {
                button.getStyleClass().add("implemented");
            }

            button.setOnAction(evt -> getSkinnable().getShell().openModule(module));

            position++;
            count++;
            column++;

            if (column == COLUMNS_PER_ROW) {
                column = 0;
                row++;
            }
        }

        return gridPane;
    }



}
