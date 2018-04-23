package uk.co.senapt.desktop.shell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uk.co.senapt.desktop.shell.ShellDialog.Type;
import uk.co.senapt.desktop.shell.model.Favorite;
import uk.co.senapt.desktop.shell.model.User;
import uk.co.senapt.desktop.shell.skins.ShellSkin;

import java.util.concurrent.CompletableFuture;

/**
 * Created by lemmi on 11.08.17.
 */
public class Shell extends Control {

    private final MenuDrawer menuDrawer;

    private final HomeScreen homeScreen;

    private final Label dashboard;

    public Shell() {
        getStyleClass().add("shell");

        getStylesheets().add(Shell.class.getResource("shell.css").toExternalForm());
        getStylesheets().add(Shell.class.getResource("shell-icons.css").toExternalForm());

        //LoggingDomain.SHELL.info("Creating shell");

        menuDrawer = new MenuDrawer(this);
        homeScreen = new HomeScreen(this);
        dashboard = new Label("Dashboard not implemented, yet.");

        addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (evt.getCode().equals(KeyCode.ESCAPE)) {
                setShowCalendar(false);
                setShowSearchField(false);
                setShowNavigation(false);
                setShowUserOptions(false);
                setShowModulesMenu(false);
            }
        });

        User user = new User();
        user.setFullName("Michell Smith");
        user.setTitle("Sales Accountant");
        user.setUsername("msmith");
        user.setAvatar(new Image(Shell.class.getResource("woman.png").toExternalForm()));

        setUser(user);

        Label leftTray = new Label("Empty Left Tray");
        Label rightTray = new Label("Empty Right Tray");

        final String placeholderClass = "tray-placeholder";

        leftTray.getStyleClass().add(placeholderClass);
        rightTray.getStyleClass().add(placeholderClass);

        setLeftTray(leftTray);
        setRightTray(rightTray);

        leftTrayProperty().addListener(it -> {
            if (getLeftTray() == null) {
                throw new IllegalArgumentException("left tray can not be null");
            }
        });

        rightTrayProperty().addListener(it -> {
            if (getRightTray() == null) {
                throw new IllegalArgumentException("right tray can not be null");
            }
        });

        selectedModule.addListener(it -> {
            if (getSelectedModule() != null) {
                displayMode.set(DisplayMode.MODULE);
            }
        });

        displayMode.addListener(it -> {
            switch (getDisplayMode()) {
                case HOME:
                case DASHBOARD:
                    setSelectedModule(null);
                    break;
                case MODULE:
                default:
                    break;
            }
        });

        showLeftTray.addListener(it -> {
            if (isShowLeftTray()) {
                setShowMenuDrawer(false);
            }
        });

        showDialog.bind(dialogProperty().isNotNull());

        addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (evt.getCode().equals(KeyCode.ESCAPE)) {
                hideDialog();
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ShellSkin(this);
    }

    public void showDialog(ShellDialog dialog) {
        this.dialog.set(dialog);
    }

    public void hideDialog() {
        this.dialog.set(null);
    }

    private final CompletableFuture<ButtonType> showStandardDialog(Type type, String title, String message) {
        return showNode(type, title, new Label(message));
    }

    public final void showError(String title, String message) {
        ShellDialog<String> dialog = new ShellDialog<>(Type.ERROR);
        dialog.setTitle(title);
        dialog.setContent(new Label(message));
        showDialog(dialog);
    }

    public final void showError(String title, String message, Exception exception) {
        showError(title, message);

        // TODO: also display exception in expandable area
    }

    public final CompletableFuture<ButtonType> showWarning(String title, String message) {
        return showStandardDialog(Type.WARNING, title, message);
    }

    public final CompletableFuture<ButtonType> showConfirmation(String title, String message) {
        return showStandardDialog(Type.CONFIRMATION, title, message);
    }

    public final CompletableFuture<ButtonType> showInformation(String title, String message) {
        return showStandardDialog(Type.INFORMATION, title, message);
    }

    public final CompletableFuture<ButtonType> showNode(Type type, String title, Node node) {
        ShellDialog<ButtonType> dialog = new ShellDialog<>(type);
        dialog.setTitle(title);
        dialog.setContent(node);
        showDialog(dialog);
        return dialog.getResult();
    }

    // dialog support

    private final ReadOnlyObjectWrapper<ShellDialog> dialog = new ReadOnlyObjectWrapper<>(this, "dialog");

    public final ReadOnlyObjectProperty<ShellDialog> dialogProperty() {
        return dialog;
    }

    public final ReadOnlyBooleanWrapper showDialog = new ReadOnlyBooleanWrapper(this, "showDialog", false);

    public ReadOnlyBooleanProperty showDialogProperty() {
        return showDialog.getReadOnlyProperty();
    }

    public boolean isShowDialog() {
        return showDialog.get();
    }

    public final ShellDialog getDialog() {
        return dialog.get();
    }

    public void openModule(ShellModule module) {
        if (!getActiveModules().contains(module)) {
            getActiveModules().add(module);
        }
        setSelectedModule(module);
    }

    public void closeModule(ShellModule module) {
        int index = getActiveModules().indexOf(module);
        getActiveModules().remove(module);
        if (index > 0) {
            ShellModule newModule = getActiveModules().get(index - 1);
            setSelectedModule(newModule);
        } else if (!getActiveModules().isEmpty()) {
            ShellModule newModule = getActiveModules().get(0);
            setSelectedModule(newModule);
        }
    }

    public enum DisplayMode {
        HOME,
        DASHBOARD,
        MODULE
    }

    // display mode support

    private final ReadOnlyObjectWrapper<DisplayMode> displayMode = new ReadOnlyObjectWrapper<>(this, "displayMode");

    public final ReadOnlyObjectProperty<DisplayMode> displayModeProperty() {
        return displayMode.getReadOnlyProperty();
    }

    public final DisplayMode getDisplayMode() {
        return displayMode.get();
    }

    public void showHomeScreen() {
        displayMode.set(DisplayMode.HOME);
    }

    public void showDashboard() {
        displayMode.set(DisplayMode.DASHBOARD);
    }

    public final MenuDrawer getMenuDrawer() {
        return menuDrawer;
    }

    public final HomeScreen getHomeScreen() {
        return homeScreen;
    }

    public final Node getDashboard() {
        return dashboard;
    }

    private final ObservableList<Node> floatingElements = FXCollections.observableArrayList();

    public final ObservableList<Node> getFloatingElements() {
        return floatingElements;
    }

    // left tray support

    private final ObjectProperty<Node> leftTray = new SimpleObjectProperty<>(this, "leftTray");

    public final ObjectProperty<Node> leftTrayProperty() {
        return leftTray;
    }

    public final Node getLeftTray() {
        return leftTray.get();
    }

    public final void setLeftTray(Node leftTray) {
        this.leftTray.set(leftTray);
    }

    // left tray support

    private final ObjectProperty<Node> rightTray = new SimpleObjectProperty<>(this, "rightTray");

    public final ObjectProperty<Node> rightTrayProperty() {
        return rightTray;
    }

    public final void setRightTray(Node rightTray) {
        this.rightTray.set(rightTray);
    }

    public final Node getRightTray() {
        return rightTray.get();
    }

    // user support

    private final ObjectProperty<User> user = new SimpleObjectProperty<>(this, "user");

    public final ObjectProperty<User> userProperty() {
        return user;
    }

    public final void setUser(User user) {
        this.user.set(user);
    }

    public final User getUser() {
        return user.get();
    }

    // modules support

    private final ObservableList<ShellModule> modules = FXCollections.observableArrayList();

    public final ObservableList<ShellModule> getModules() {
        return modules;
    }

    // active modules support

    private final ObservableList<ShellModule> activeModules = FXCollections.observableArrayList();

    public final ObservableList<ShellModule> getActiveModules() {
        return activeModules;
    }

    // selected module support

    private final ObjectProperty<ShellModule> selectedModule = new SimpleObjectProperty<>(this, "selectedModule");

    public final ObjectProperty<ShellModule> selectedModuleProperty() {
        return selectedModule;
    }

    public final ShellModule getSelectedModule() {
        return selectedModule.get();
    }

    public final void setSelectedModule(ShellModule selectedModule) {
        this.selectedModule.set(selectedModule);
    }

    // show menu drawer support

    private final BooleanProperty showMenuDrawer = new SimpleBooleanProperty(this, "showMenuDrawer");

    public final BooleanProperty showMenuDrawerProperty() {
        return showMenuDrawer;
    }

    public final void setShowMenuDrawer(boolean showMenuDrawer) {
        this.showMenuDrawer.set(showMenuDrawer);
    }

    public final boolean isShowMenuDrawer() {
        return showMenuDrawer.get();
    }

    // show uk.co.senapt.desktop.ui.modules.calendar support

    private final BooleanProperty showCalendar = new SimpleBooleanProperty(this, "showCalendar");

    public BooleanProperty showCalendarProperty() {
        return showCalendar;
    }

    public void setShowCalendar(boolean showCalendar) {
        this.showCalendar.set(showCalendar);
    }

    public boolean isShowCalendar() {
        return showCalendar.get();
    }

    // show modules menu support

    private final BooleanProperty showModulesMenu = new SimpleBooleanProperty(this, "showModulesMenu");

    public final BooleanProperty showModulesMenuProperty() {
        return showModulesMenu;
    }

    public final void setShowModulesMenu(boolean showModulesMenu) {
        this.showModulesMenu.set(showModulesMenu);
    }

    public final boolean isShowModulesMenu() {
        return showModulesMenu.get();
    }

    // show search field support

    private final BooleanProperty showSearchField = new SimpleBooleanProperty(this, "showSearchField");

    public BooleanProperty showSearchFieldProperty() {
        return showSearchField;
    }

    public void setShowSearchField(boolean showSearchField) {
        this.showSearchField.set(showSearchField);
    }

    public boolean isShowSearchField() {
        return showSearchField.get();
    }

    // show user options

    private final BooleanProperty showUserOptions = new SimpleBooleanProperty(this, "showUserOptions");

    public BooleanProperty showUserOptionsProperty() {
        return showUserOptions;
    }

    public void setShowUserOptions(boolean showUserOptions) {
        this.showUserOptions.set(showUserOptions);
    }

    public boolean isShowUserOptions() {
        return showUserOptions.get();
    }

    // show navigation

    private final BooleanProperty showNavigation = new SimpleBooleanProperty(this, "showNavigation");

    public BooleanProperty showNavigationProperty() {
        return showNavigation;
    }

    public void setShowNavigation(boolean showNavigation) {
        this.showNavigation.set(showNavigation);
    }

    public boolean isShowNavigation() {
        return showNavigation.get();
    }

    // show left tray

    private final BooleanProperty showLeftTray = new SimpleBooleanProperty(this, "showLeftTray");

    public final BooleanProperty showLeftTrayProperty() {
        return showLeftTray;
    }

    public final void setShowLeftTray(boolean showLeftTray) {
        this.showLeftTray.set(showLeftTray);
    }

    public final boolean isShowLeftTray() {
        return showLeftTray.get();
    }

    // show right tray

    private final BooleanProperty showRightTray = new SimpleBooleanProperty(this, "showRightTray");

    public final BooleanProperty showRightTrayProperty() {
        return showRightTray;
    }

    public final void setShowRightTray(boolean showRightTray) {
        this.showRightTray.set(showRightTray);
    }

    public final boolean isShowRightTray() {
        return showRightTray.get();
    }

    // favorites

    private final ObservableList<Favorite> favorites = FXCollections.observableArrayList();

    public ObservableList<Favorite> getFavorites() {
        return favorites;
    }
}
