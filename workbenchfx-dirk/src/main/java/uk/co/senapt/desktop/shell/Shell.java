package uk.co.senapt.desktop.shell;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import uk.co.senapt.desktop.shell.ShellDialog.Type;
import uk.co.senapt.desktop.shell.model.Favorite;
import uk.co.senapt.desktop.shell.model.User;
import uk.co.senapt.desktop.shell.preferences.Category;
import uk.co.senapt.desktop.shell.preferences.EffectsPreferencesView;
import uk.co.senapt.desktop.shell.preferences.PreferenceView;
import uk.co.senapt.desktop.shell.skins.ShellSkin;
import uk.co.senapt.desktop.shell.util.LoggingDomain;

/**
 * Created by lemmi on 11.08.17.
 */
@Singleton
public class Shell extends Control {

    private final Executor executor = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r);
        thread.setName("Worker Execution Thread");
        thread.setDaemon(true);
        return thread;
    });

    public static Injector context;

    public static Injector getContext() {
        return context;
    }

    private final MenuDrawer menuDrawer;

    private final HomeScreen homeScreen;

    private final Dashboard dashboard;

    @Inject
    private Provider<EffectsPreferencesView> effectsPreferencesViewProvider;

    public Shell() {
        getStyleClass().add("shell");

        getStylesheets().add(Shell.class.getResource("shell.css").toExternalForm());
        getStylesheets().add(Shell.class.getResource("shell-icons.css").toExternalForm());

        menuDrawer = new MenuDrawer(this);
        homeScreen = new HomeScreen(this);
        dashboard = new Dashboard();

        addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (evt.getCode().equals(KeyCode.ESCAPE)) {
                setShowCalendar(false);
                setShowSearchField(false);
                setShowNavigation(false);
                setShowUserOptions(false);
                setShowModulesMenu(false);
            }
        });

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
                setShowMenuTray(false);
            }
        });

        showDialog.bind(dialogProperty().isNotNull());

        addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (evt.getCode().equals(KeyCode.ESCAPE)) {
                hideDialog();
            }
        });

        createUserMenuItems();

        blockingProperty().bind(Bindings.isNotEmpty(workers));

        persist(animateDialogs, "animate.dialogs");
        persist(animateMenus, "animate.menu");
        persist(animateTrays, "animate.trays");
        persist(showShadow, "show.shadow");
        persist(fadeInOut, "fade.in.out");
    }

    /*
     * Register the given boolean property for preferences / session state support.
     * First: the current value gets loaded from the preference store (or the current value of the property will be used).
     * Second: a listener gets attached to listen for property changes and storing the new value into the preference store.
     */
    private void persist(BooleanProperty property, String key) {
        property.setValue(Preferences.userNodeForPackage(getClass()).getBoolean(key, property.getValue()));
        property.addListener(it -> Preferences.userNodeForPackage(getClass()).putBoolean(key, property.getValue()));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ShellSkin(this);
    }

    public Executor getExecutor() {
        return executor;
    }

    private ObservableList<Worker<?>> workers = FXCollections.observableArrayList();

    protected void addWorker(Worker worker) {
        LoggingDomain.VIEW.debug("executing worker: " + worker.getTitle());

        workers.add(worker);

        worker.stateProperty().addListener(it -> {
            switch (worker.getState()) {
                case CANCELLED:
                case FAILED:
                case SUCCEEDED:
                    Platform.runLater(() -> workers.remove(worker));
                    break;
                default:
                    break;
            }
        });
    }

    public void execute(Task task) {
        addWorker(task);
        executor.execute(task);
    }

    private Dialog<Void> activitiesDialog;

    public void showProgressDialog() {
        if (activitiesDialog == null) {

            ActivitiesView<Worker<?>> activitiesView = new ActivitiesView<>();
            Bindings.bindContent(activitiesView.getWorkers(), workers);

            activitiesDialog = new Dialog<>();
            activitiesDialog.getDialogPane().setContent(activitiesView);
            activitiesDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            activitiesDialog.getDialogPane().getStylesheets().add(Shell.class.getResource("shell.css").toExternalForm());
            activitiesDialog.setTitle("Activities");
            activitiesDialog.setHeight(300);
            activitiesDialog.setWidth(400);
            activitiesDialog.initModality(Modality.NONE);
            activitiesDialog.initOwner(getScene().getWindow());
        }

        activitiesDialog.show();
    }

    @Inject
    private Provider<PreferenceView> preferenceViewProvider;

    public final PreferenceView getPreferencesView() {
        // preferences view is a singleton
        PreferenceView view = preferenceViewProvider.get();
        if (view.getCategories().isEmpty()) {
            Category effectsCategory = new Category("Effects");
            effectsCategory.setContentProvider(effectsPreferencesViewProvider);
            getPreferenceCategories().add(effectsCategory);
        }

        return view;
    }

    public void showPreferencesDialog() {
        ShellDialog<ButtonType> dialog = new ShellDialog<>(Type.INFORMATION);
        dialog.setContent(getPreferencesView());
        dialog.setTitle("Preferences");
        dialog.getButtonTypes().setAll(new ButtonType("Close", ButtonBar.ButtonData.OK_DONE));

        showDialog(dialog);
    }

    private final ObservableList<Category> preferenceCategories = FXCollections.observableArrayList();

    public final ObservableList<Category> getPreferenceCategories() {
        return preferenceCategories;
    }

    private void createUserMenuItems() {
        MenuItem exit = new MenuItem("Exit");
        exit.setAccelerator(KeyCombination.valueOf("Shortcut+q"));
        exit.setOnAction(evt -> {
            try {
                showConfirmation("Exit Senapt CRM", "Are you sure you want to exit the application?")
                        .thenAccept(buttonType -> {
                            if (buttonType == ButtonType.YES) {
                                Platform.exit();
                            }
                        });
            } catch (Exception e) {
                LoggingDomain.VIEW.error("an error occurred when trying to exit the application", e);
            }
        });

        MenuItem errorDialog = new MenuItem("Show Error");
        errorDialog.setOnAction(evt -> {
            showError("Error", "Something went really wrong!");
            setShowUserOptions(false);
        });

        MenuItem confirmationDialog = new MenuItem("Show Confirmation");
        confirmationDialog.setOnAction(evt -> {
            showConfirmation("Confirmation", "Are you sure you want to delete this customer?");
            setShowUserOptions(false);
        });

        MenuItem warningDialog = new MenuItem("Show Warning");
        warningDialog.setOnAction(evt -> {
            showWarning("Warning", "This action will have consequences!");
            setShowUserOptions(false);
        });

        MenuItem showProgressDialogItem = new MenuItem("Activities");
        showProgressDialogItem.setAccelerator(KeyCombination.valueOf("Shortcut+A"));
        showProgressDialogItem.setOnAction(evt -> showProgressDialog());
        getUserMenuItems().add(showProgressDialogItem);

        MenuItem preferencesItem = new MenuItem("Preferences");
        preferencesItem.setAccelerator(KeyCombination.valueOf("Shortcut+,"));
        preferencesItem.setOnAction(evt -> showPreferencesDialog());

        getUserMenuItems().setAll(exit, errorDialog, confirmationDialog, warningDialog, showProgressDialogItem, preferencesItem);
    }

    private final ObservableList<MenuItem> userMenuItems = FXCollections.observableArrayList();

    public final ObservableList<MenuItem> getUserMenuItems() {
        return userMenuItems;
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
        showError(title, message, null, null);
    }

    public final void showError(String title, String message, Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        showError(title, message, stringWriter.toString(), exception);
    }

    public final void showError(String title, String message, String details) {
        showError(title, message, details, null);
    }

    private final void showError(String title, String message, String details, Exception exception) {
        ShellDialog<String> dialog = new ShellDialog<>(Type.ERROR);
        dialog.setTitle(title);

        final Label messageLabel = new Label(message);

        if (StringUtils.isBlank(details)) {
            dialog.setContent(messageLabel);
        } else {
            TextArea textArea = new TextArea();
            textArea.setText(details);
            textArea.setWrapText(true);

            TitledPane titledPane = new TitledPane();
            titledPane.getStyleClass().add("error-details-titled-pane");
            titledPane.setText("Details");
            titledPane.setContent(textArea);
            titledPane.setPrefHeight(300);

            VBox content = new VBox(messageLabel, titledPane);
            content.getStyleClass().add("container");
            dialog.setContent(content);
        }

        dialog.setException(exception);
        showDialog(dialog);
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

    public final void showProperties(Object obj) {
        try {
            TableView<PojoProperty> tableView = new TableView<>();

            TableColumn<PojoProperty, String> keyColumn = new TableColumn<>("Key");
            keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
            keyColumn.setPrefWidth(250);

            TableColumn<PojoProperty, String> valueColumn = new TableColumn<>("Value");
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
            valueColumn.setPrefWidth(400);

            tableView.getColumns().setAll(keyColumn, valueColumn);

            final Map<String, String> map = BeanUtils.describe(obj);
            final List<String> keys = new ArrayList(map.keySet());
            Collections.sort(keys);

            tableView.getItems().setAll(keys.stream().map(key -> new PojoProperty(key, map.get(key))).collect(Collectors.toList()));

            showNode(Type.INFORMATION, "Properties", tableView);

        } catch (Exception e) {
            showError("Properties", "An exception occurred when trying to display properties of given object.", e);
        }
    }

    public static class PojoProperty {

        private String key;
        private String value;

        public PojoProperty(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    // show shadows

    private final BooleanProperty showShadow = new SimpleBooleanProperty(this, "showShadow", true);

    public final BooleanProperty showShadowProperty() {
        return showShadow;
    }

    public final boolean isShowShadow() {
        return showShadow.get();
    }

    public final void setShowShadow(boolean show) {
        showShadow.set(show);
    }

    // animation settings

    private final BooleanProperty animateMenus = new SimpleBooleanProperty(this, "animateMenus", true);

    public final BooleanProperty animateMenusProperty() {
        return animateMenus;
    }

    public final boolean isAnimateMenus() {
        return animateMenus.get();
    }

    public final void setAnimateMenu(boolean animate) {
        this.animateMenus.set(animate);
    }

    private final BooleanProperty animateDialogs = new SimpleBooleanProperty(this, "animateDialogs", false);

    public final BooleanProperty animateDialogsProperty() {
        return animateDialogs;
    }

    public final boolean isAnimateDialogs() {
        return animateDialogs.get();
    }

    public final void setAnimateDialogs(boolean animate) {
        this.animateDialogs.set(animate);
    }

    private final BooleanProperty animateTrays = new SimpleBooleanProperty(this, "animateTrays", true);

    public final boolean getAnimateTrays() {
        return animateTrays.get();
    }

    public final BooleanProperty animateTraysProperty() {
        return animateTrays;
    }

    public final void setAnimateTrays(boolean animateTrays) {
        this.animateTrays.set(animateTrays);
    }

    private final BooleanProperty fadeInOut = new SimpleBooleanProperty(this, "fadeInOut", true);

    public final BooleanProperty fadeInOutProperty() {
        return fadeInOut;
    }

    public final boolean isFadeInOut() {
        return fadeInOut.get();
    }

    public final void setFadeInOut(boolean animate) {
        this.fadeInOut.set(animate);
    }

    // blocking support

    private final BooleanProperty blocking = new SimpleBooleanProperty(this, "blocking");

    public final BooleanProperty blockingProperty() {
        return blocking;
    }

    public final void setBlocking(boolean blocking) {
        this.blocking.set(blocking);
    }

    public final boolean isBlocking() {
        return blocking.get();
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

    public void openCalendarModule() {
        throw new UnsupportedOperationException();
    }

    public boolean isCalendarModuleRegistered() {
        return false;
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

    // floating support

    private final BooleanProperty showFloatingElements = new SimpleBooleanProperty(this, "showFloatingElements", true);

    public final BooleanProperty showFloatingElementsProperty() {
        return showFloatingElements;
    }

    public final void setShowFloatingElements(boolean show) {
        this.showFloatingElements.set(show);
    }

    public final boolean isShowFloatingElements() {
        return showFloatingElements.get();
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

    public final Dashboard getDashboard() {
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

    private final BooleanProperty showMenuTray = new SimpleBooleanProperty(this, "showMenuTray");

    public final BooleanProperty showMenuTrayProperty() {
        return showMenuTray;
    }

    public final void setShowMenuTray(boolean showMenuTray) {
        this.showMenuTray.set(showMenuTray);
    }

    public final boolean getShowMenuTray() {
        return showMenuTray.get();
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
