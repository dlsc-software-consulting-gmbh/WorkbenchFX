package uk.co.senapt.desktop.shell.skins;

import com.calendarfx.view.YearMonthView;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import uk.co.senapt.desktop.shell.Dashboard;
import uk.co.senapt.desktop.shell.HomeScreen;
import uk.co.senapt.desktop.shell.HorizontalMenu;
import uk.co.senapt.desktop.shell.MenuDrawer;
import uk.co.senapt.desktop.shell.ModuleListCell;
import uk.co.senapt.desktop.shell.ModulesManager;
import uk.co.senapt.desktop.shell.Shell;
import uk.co.senapt.desktop.shell.ShellDialog;
import uk.co.senapt.desktop.shell.ShellModule;
import uk.co.senapt.desktop.shell.UserAvatar;
import uk.co.senapt.desktop.shell.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

/**
 * Created by lemmi on 11.08.17.
 */
public class ShellSkin extends SkinBase<Shell> {

    private static final Duration ANIMATION_DURATION = Duration.millis(100);

    private HBox toolBar;
    private ToggleButton searchButton;
    private UserMenuButton userMenuButton;
    private DateAndTimeButton dateAndTimeButton;

    private UserMenu userMenu;
    private CalendarView calendarView;
    private SearchFieldPane searchFieldPane;
    private MenuDrawer menuDrawer;
    private HorizontalMenu<ShellModule> moduleSwitcher;

    private GlassPane glassPane;

    private HomeScreen homeScreen;
    private ModulePane modulePane;
    private Dashboard dashboard;
    private Node leftTray;
    private Node rightTray;

    private final DoubleProperty menuDrawerVisibility = new SimpleDoubleProperty();
    private final DoubleProperty userOptionsVisibility = new SimpleDoubleProperty();
    private final DoubleProperty calendarVisibility = new SimpleDoubleProperty();
    private final DoubleProperty searchFieldVisibility = new SimpleDoubleProperty();
    private final DoubleProperty leftTrayVisibility = new SimpleDoubleProperty();
    private final DoubleProperty rightTrayVisibility = new SimpleDoubleProperty();
    private final DoubleProperty dialogVisibility = new SimpleDoubleProperty();

    private final VBox floatingElementsContainer;

    private final Label dialogTitle;
    private final VBox dialogPane;
    private final StackPane dialogContentPane;
    private final ButtonBar dialogButtonBar;

    public ShellSkin(Shell shell) {
        super(shell);

        dialogPane = new VBox();
        dialogPane.setFillWidth(true);
        dialogPane.getStyleClass().add("dialog-pane");
        dialogPane.setVisible(false);

        HBox dialogHeader = new HBox();
        dialogHeader.setAlignment(Pos.CENTER_LEFT);
        dialogHeader.getStyleClass().add("dialog-header");

        dialogTitle = new Label("Dialog");

        dialogTitle.setMaxWidth(Double.MAX_VALUE);
        dialogTitle.getStyleClass().add("dialog-title");

        Button dialogCloseButton = new Button();
        dialogCloseButton.setOnAction(evt -> {
            shell.getDialog().getResult().complete(ButtonType.CANCEL);
            shell.hideDialog();
        });
        dialogCloseButton.getStyleClass().addAll("dialog-close-button", "dialog-close-icon");

        HBox.setHgrow(dialogTitle, Priority.ALWAYS);
        HBox.setHgrow(dialogCloseButton, Priority.NEVER);

        dialogHeader.getChildren().setAll(dialogTitle, dialogCloseButton);

        dialogContentPane = new StackPane();
        dialogContentPane.getStyleClass().add("dialog-content-pane");

        dialogButtonBar = new ButtonBar();

        VBox.setVgrow(dialogTitle, Priority.NEVER);
        VBox.setVgrow(dialogContentPane, Priority.ALWAYS);
        VBox.setVgrow(dialogButtonBar, Priority.NEVER);

        dialogPane.getChildren().setAll(dialogHeader, dialogContentPane, dialogButtonBar);


        moduleSwitcher = new HorizontalMenu<>();
        moduleSwitcher.getStyleClass().add("module-switcher");
        moduleSwitcher.setShowMenuButton(false);
        moduleSwitcher.setCellFactory(lv -> new ModuleListCell(shell, false));
        moduleSwitcher.setPopupCellFactory(lv -> new ModuleListCell(shell, true));
        moduleSwitcher.showPopupProperty().bindBidirectional(shell.showModulesMenuProperty());
        moduleSwitcher.selectedItemProperty().bindBidirectional(shell.selectedModuleProperty());
        Bindings.bindContent(moduleSwitcher.getItems(), getSkinnable().getActiveModules());

        createToolBar();

        floatingElementsContainer = new VBox();
        floatingElementsContainer.getStyleClass().add("floating-action-button-container");
        floatingElementsContainer.setManaged(false);
        floatingElementsContainer.setFillWidth(true);
        Bindings.bindContent(floatingElementsContainer.getChildren(), shell.getFloatingElements());

        shell.leftTrayProperty().addListener(it -> updateChildren());
        shell.rightTrayProperty().addListener(it -> updateChildren());

        dashboard = shell.getDashboard();

        homeScreen = shell.getHomeScreen();
        homeScreen.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> closeDrawers());

        modulePane = new ModulePane();

        glassPane = new GlassPane();
        glassPane.hideProperty().bind(Bindings.not(shell.showMenuDrawerProperty().or(shell.showLeftTrayProperty().or(shell.showRightTrayProperty()).or(shell.dialogProperty().isNotNull()))));

        menuDrawer = shell.getMenuDrawer();
        menuDrawer.setVisible(false);

        userMenu = new UserMenu();
        userMenu.setVisible(false);
        userMenu.minWidthProperty().bind(userMenuButton.widthProperty());

        calendarView = new CalendarView();
        calendarView.setVisible(false);

        searchFieldPane = new SearchFieldPane();
        searchFieldPane.setVisible(false);
        searchFieldPane.prefWidthProperty().bind(toolBar.widthProperty());

        updateChildren();

        shell.displayModeProperty().addListener(it -> {
            switch (shell.getDisplayMode()) {
                case DASHBOARD:
                    getChildren().removeAll(homeScreen, modulePane);
                    if (!getChildren().contains(dashboard)) {
                        getChildren().add(0, dashboard);
                    }
                    break;
                case HOME:
                    getChildren().removeAll(dashboard, modulePane);
                    if (!getChildren().contains(homeScreen)) {
                        getChildren().add(0, homeScreen);
                    }
                    break;
                case MODULE:
                    getChildren().removeAll(dashboard, homeScreen);
                    if (!getChildren().contains(modulePane)) {
                        getChildren().add(0, modulePane);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("unknown display mode");
            }
        });

        InvalidationListener listener = obs -> getSkinnable().requestLayout();
        userOptionsVisibility.addListener(listener);
        calendarVisibility.addListener(listener);
        searchFieldVisibility.addListener(listener);
        menuDrawerVisibility.addListener(listener);
        leftTrayVisibility.addListener(listener);
        rightTrayVisibility.addListener(listener);
        dialogVisibility.addListener(listener);

        shell.dialogProperty().addListener(it -> {
            final ShellDialog dialog = shell.getDialog();
            if (dialog != null) {
                showDialog();
            } else {
                hideDialog();
            }
        });

        dialogVisibility.addListener(it -> {
            if (dialogVisibility.get() == 0) {
                // free resources
                dialogContentPane.getChildren().clear();
            }
        });

        registerSlider(shell.showUserOptionsProperty(), userOptionsVisibility, () -> userMenu, ANIMATION_DURATION);
        registerSlider(shell.showCalendarProperty(), calendarVisibility, () -> calendarView, ANIMATION_DURATION);
        registerSlider(shell.showSearchFieldProperty(), searchFieldVisibility, () -> searchFieldPane, ANIMATION_DURATION);
        registerSlider(shell.showMenuDrawerProperty(), menuDrawerVisibility, () -> menuDrawer, ANIMATION_DURATION);
        registerSlider(shell.showLeftTrayProperty(), leftTrayVisibility, shell::getLeftTray, ANIMATION_DURATION);
        registerSlider(shell.showRightTrayProperty(), rightTrayVisibility, shell::getRightTray, ANIMATION_DURATION);
        registerSlider(shell.showDialogProperty(), dialogVisibility, () -> dialogPane, Duration.millis(300));
    }

    private void showDialog() {
        ShellDialog shellDialog = getSkinnable().getDialog();
        dialogTitle.textProperty().bind(shellDialog.titleProperty());
        dialogContentPane.getChildren().setAll(shellDialog.getContent());

        updateButtons(shellDialog.getButtonTypes());

        if (!getChildren().contains(dialogPane)) {
            getChildren().add(dialogPane);
        }
    }

    private final Map<ButtonType, Node> buttonNodes = new WeakHashMap<>();

    private void updateButtons(List<ButtonType> buttonTypes) {
        dialogButtonBar.getButtons().clear();

        boolean hasDefault = false;
        for (ButtonType cmd : buttonTypes) {
            Node button = buttonNodes.computeIfAbsent(cmd, dialogButton -> createButton(cmd));

            // keep only first default button
            if (button instanceof Button) {
                ButtonData buttonType = cmd.getButtonData();

                ((Button) button).setDefaultButton(!hasDefault && buttonType != null && buttonType.isDefaultButton());
                ((Button) button).setCancelButton(buttonType != null && buttonType.isCancelButton());
                ((Button) button).setOnAction(evt -> {
                    getSkinnable().getDialog().getResult().complete(cmd);
                    getSkinnable().hideDialog();
                });

                hasDefault |= buttonType != null && buttonType.isDefaultButton();
            }
            dialogButtonBar.getButtons().add(button);
        }
    }

    protected Node createButton(ButtonType buttonType) {
        final Button button = new Button(buttonType.getText());
        final ButtonData buttonData = buttonType.getButtonData();
        ButtonBar.setButtonData(button, buttonData);
        button.setDefaultButton(buttonData.isDefaultButton());
        button.setCancelButton(buttonData.isCancelButton());
        button.addEventHandler(ActionEvent.ACTION, ae -> {
            if (ae.isConsumed()) return;
//            if (dialog != null) {
//                dialog.setResultAndClose(buttonType, true);
//            }
        });

        return button;
    }

    private void hideDialog() {
    }

    private void registerSlider(ReadOnlyBooleanProperty showProperty, DoubleProperty visibilityProperty, Supplier<Node> nodeSupplier, Duration slideDuration) {
        showProperty.addListener(it -> {
            if (showProperty.get()) {
                slideInOut(1, visibilityProperty, nodeSupplier, slideDuration);
            } else {
                slideInOut(0, visibilityProperty, nodeSupplier, slideDuration);
            }
        });

        if (showProperty.get()) {
            visibilityProperty.set(1);
        }
    }

    private static void slideInOut(double visibility, DoubleProperty visibilityProperty, Supplier<Node> nodeSupplier, Duration slideDuration) {
        if (visibility == 1) {
            nodeSupplier.get().setVisible(true);
            nodeSupplier.get().setOpacity(0);
        } else {
            nodeSupplier.get().setOpacity(1);
        }

        KeyValue value1 = new KeyValue(visibilityProperty, visibility);
        KeyValue value2 = new KeyValue(nodeSupplier.get().opacityProperty(), visibility == 0 ? 0 : 1);
        KeyFrame frame = new KeyFrame(slideDuration, value1, value2);
        Timeline timeline = new Timeline(frame);
        timeline.setOnFinished(evt -> {
            if (visibility == 0) {
                nodeSupplier.get().setVisible(false);
            }
        });
        timeline.play();
    }

    private void updateChildren() {
        leftTray = getSkinnable().getLeftTray();
        rightTray = getSkinnable().getRightTray();
        getChildren().setAll(homeScreen, userMenu, calendarView, searchFieldPane, toolBar, floatingElementsContainer, glassPane, menuDrawer, leftTray, rightTray);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        if (dialogPane.isVisible() && getChildren().contains(dialogPane)) {

            double dialogPrefWidth = dialogPane.prefWidth(-1);
            double dialogPrefHeight = dialogPane.prefHeight(-1);

            final ShellDialog dialog = getSkinnable().getDialog();

            if (dialog == null) {
                dialogPrefWidth = dialogPane.getWidth();
                dialogPrefHeight = dialogPane.getHeight();
            } else if (dialog.isMaximize()) {
                dialogPrefWidth = contentWidth * .9;
                dialogPrefHeight = contentHeight * .9;
            }

            final double dialogTargetY = contentY + (contentHeight - dialogPrefHeight) / 2;
            dialogPane.resizeRelocate(contentX + (contentWidth - dialogPrefWidth) / 2, dialogTargetY + (contentHeight - dialogTargetY) * (1 - dialogVisibility.get()), dialogPrefWidth, dialogPrefHeight);
        }

        double prefHeightToolBar = toolBar.prefHeight(contentWidth);
        toolBar.resizeRelocate(contentX, contentY, contentWidth, prefHeightToolBar);
        toolBar.layout();

        final double menuDrawerPrefWidth = menuDrawer.prefWidth(-1);

        menuDrawer.resizeRelocate(-(1 - menuDrawerVisibility.get()) * menuDrawerPrefWidth, contentY, menuDrawerPrefWidth, contentHeight);

        glassPane.resizeRelocate(contentX, contentY, contentWidth, contentHeight);

        final double leftTrayPrefWidth = leftTray.prefWidth(-1);
        final double rightTrayPrefWidth = rightTray.prefWidth(-1);

        leftTray.resizeRelocate(-(1 - leftTrayVisibility.get()) * leftTrayPrefWidth, contentY, leftTrayPrefWidth, contentHeight);
        rightTray.resizeRelocate(contentWidth - (rightTrayVisibility.get() * rightTrayPrefWidth), contentY, rightTrayPrefWidth, contentHeight);

        contentY += prefHeightToolBar;
        contentHeight -= prefHeightToolBar;

        userMenu.relocate(userMenuButton.getLayoutX() - 2, contentY - (1 - userOptionsVisibility.get()) * userMenu.getHeight());

        final double calendarViewPrefHeight = calendarView.prefHeight(dateAndTimeButton.getWidth());
        final double calendarViewPrefWidth = calendarView.prefWidth(-1);

        calendarView.resizeRelocate(dateAndTimeButton.getLayoutX() + dateAndTimeButton.getWidth() - calendarViewPrefWidth + 1, contentY - (1 - calendarVisibility.get()) * calendarViewPrefHeight, Math.max(dateAndTimeButton.getWidth(), calendarViewPrefWidth), calendarViewPrefHeight);

        searchFieldPane.resizeRelocate(contentX, contentY - (1 - searchFieldVisibility.get()) * searchFieldPane.getHeight(), contentWidth, searchFieldPane.prefHeight(contentWidth));

        final double userMenuPrefHeight = userMenu.prefHeight(-1);
        final double userMenuPrefWidth = Math.max(userMenu.prefWidth(-1), userMenu.minWidth(-1));

        userMenu.resizeRelocate(userMenuButton.getLayoutX() + userMenuButton.getWidth() - userMenuPrefWidth - 1, contentY - (1 - userOptionsVisibility.get()) * userMenuPrefHeight, userMenuPrefWidth + 2, userMenuPrefHeight);

        homeScreen.resizeRelocate(contentX, contentY, contentWidth, contentHeight);

        modulePane.resizeRelocate(contentX, contentY, contentWidth, contentHeight);

        dashboard.resizeRelocate(contentX, contentY, contentWidth, contentHeight);

        final double gap = 30;
        final double favoritesPrefWidth = floatingElementsContainer.prefWidth(-1);
        final double favoritesPrefHeight = floatingElementsContainer.prefHeight(-1);
        floatingElementsContainer.resizeRelocate(contentWidth - favoritesPrefWidth - gap, contentY + contentHeight - favoritesPrefHeight - gap, favoritesPrefWidth, favoritesPrefHeight);
    }

    private void createToolBar() {
        ToggleButton menuDrawerButton = new ToggleButton();
        menuDrawerButton.selectedProperty().bindBidirectional(getSkinnable().showMenuDrawerProperty());

        Button homeButton = new Button();
        homeButton.setOnAction(evt -> getSkinnable().showHomeScreen());

        searchButton = new ToggleButton();
        searchButton.selectedProperty().bindBidirectional(getSkinnable().showSearchFieldProperty());
        searchButton.selectedProperty().addListener(it -> {
            updateSearchButtonIcon(searchButton);

            if (searchButton.isSelected()) {
                Platform.runLater(() -> searchFieldPane.getTextField().requestFocus());
            }
        });

        updateSearchButtonIcon(searchButton);

        Button dashboardButton = new Button();
        dashboardButton.setOnAction(evt -> getSkinnable().showDashboard());

        menuDrawerButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        homeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        searchButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        dashboardButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        menuDrawerButton.getStyleClass().addAll("element", "header-button", "toolbar-icon", "toolbar-menu-icon");
        homeButton.getStyleClass().addAll("element", "header-button", "toolbar-icon", "toolbar-minimize-icon");
        searchButton.getStyleClass().addAll("element", "header-button", "toolbar-icon", "toolbar-search-icon");
        dashboardButton.getStyleClass().addAll("element", "header-button", "toolbar-icon", "toolbar-dashboard-icon");

        dateAndTimeButton = new DateAndTimeButton();
        dateAndTimeButton.selectedProperty().bindBidirectional(getSkinnable().showCalendarProperty());

        userMenuButton = new UserMenuButton();
        userMenuButton.setSelected(true);
        userMenuButton.selectedProperty().bindBidirectional(getSkinnable().showUserOptionsProperty());

        ToggleButton moduleMenuButton = new ToggleButton();
        moduleMenuButton.selectedProperty().bindBidirectional(moduleSwitcher.showPopupProperty());

        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(menuDrawerButton, searchButton, dateAndTimeButton, userMenuButton, moduleMenuButton);

        HBox.setHgrow(menuDrawerButton, Priority.NEVER);
        HBox.setHgrow(homeButton, Priority.NEVER);
        HBox.setHgrow(moduleSwitcher, Priority.ALWAYS);
        HBox.setHgrow(dateAndTimeButton, Priority.NEVER);
        HBox.setHgrow(userMenuButton, Priority.NEVER);
        HBox.setHgrow(dashboardButton, Priority.NEVER);

        toolBar = new HBox(menuDrawerButton, new Separator(Orientation.VERTICAL), homeButton, new Separator(Orientation.VERTICAL), moduleSwitcher, new Separator(Orientation.VERTICAL), searchButton, new Separator(Orientation.VERTICAL), dateAndTimeButton, new Separator(Orientation.VERTICAL), userMenuButton, new Separator(Orientation.VERTICAL), dashboardButton);
        toolBar.getStyleClass().add("shell-toolbar");
        toolBar.setAlignment(Pos.CENTER);
    }

    private void updateSearchButtonIcon(ToggleButton searchButton) {
        if (searchButton.isSelected()) {
            searchButton.getStyleClass().remove("toolbar-search-icon");
            searchButton.getStyleClass().add("toolbar-search-close-icon");
        } else {
            Node graphic = searchButton.getGraphic();

            if (graphic == null) {

                searchButton.getStyleClass().add("toolbar-search-icon");
                searchButton.getStyleClass().remove("toolbar-search-close-icon");

            } else {
                final double opacity = graphic.getOpacity();

                RotateTransition rotateTransition = new RotateTransition(ANIMATION_DURATION, graphic);
                rotateTransition.setByAngle(90);
                rotateTransition.setOnFinished(evt -> {
                    searchButton.getStyleClass().add("toolbar-search-icon");
                    searchButton.getStyleClass().remove("toolbar-search-close-icon");
                    graphic.setRotate(0);
                    graphic.setOpacity(opacity);
                });

                FadeTransition fadeTransition = new FadeTransition(ANIMATION_DURATION, graphic);
                fadeTransition.setToValue(0);

                ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, fadeTransition);
                parallelTransition.play();
            }
        }
    }

    class DateAndTimeButton extends ToggleButton {

        private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);

        public DateAndTimeButton() {
            getStyleClass().addAll("element", "toolbar-label");
            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            Thread thread = new Thread(() -> {
                LocalDateTime dateTime = LocalDateTime.now();
                setText(formatter.format(dateTime));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // no op
                }
            });
            thread.setName("Date and Time Update Thread 1");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    class SearchFieldPane extends HBox {

        private TextField textField;

        public SearchFieldPane() {
            getStyleClass().add("search-field-pane");

            textField = new TextField();
            textField.setPromptText("Enter search term ...");
            HBox.setHgrow(textField, Priority.ALWAYS);

            getChildren().add(textField);
        }

        public TextField getTextField() {
            return textField;
        }
    }

    class CalendarView extends VBox {

        private DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
        private DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);

        public CalendarView() {
            getStyleClass().add("month-calendar");

            setFillWidth(true);

            Label timeLabel = new Label("time label");
            timeLabel.setMaxWidth(Double.MAX_VALUE);
            timeLabel.getStyleClass().add("time-label");

            Label dateLabel = new Label("date label");
            dateLabel.setMaxWidth(Double.MAX_VALUE);
            dateLabel.getStyleClass().add("date-label");

            YearMonthView monthView = new YearMonthView();
            monthView.setShowTodayButton(false);
            monthView.setShowYear(false);
            monthView.setShowMonth(false);
            monthView.setShowWeekNumbers(false);
            monthView.getWeekendDays().clear();

            Button gotoButton = new Button("Go to Calendar >");
            gotoButton.getStyleClass().add("goto-button");
            gotoButton.setMaxWidth(Double.MAX_VALUE);
            gotoButton.setOnAction(evt -> getSkinnable().openModule(ModulesManager.getInstance().getModule("Calendar").get()));

            if (ModulesManager.getInstance().getModule("Calendar").isPresent()) {
                getChildren().addAll(timeLabel, dateLabel, monthView, gotoButton);
            } else {
                getChildren().addAll(timeLabel, dateLabel, monthView);
            }

            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    LocalDateTime dateTime = LocalDateTime.now();
                    timeLabel.setText(timeFormatter.format(dateTime));
                    dateLabel.setText(dateFormatter.format(dateTime));
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // no op
                }
            });
            thread.setName("Date and Time Update Thread 2");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    class UserMenu extends VBox {

        public UserMenu() {
            getStyleClass().add("user-menu");

            Button exitButton = new Button("Exit");
            exitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            exitButton.getStyleClass().add("user-menu-button");
            exitButton.setOnAction(evt -> getScene().getWindow().hide());
            getChildren().add(exitButton);

            Button errorDialog = new Button("Show Error");
            errorDialog.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            errorDialog.getStyleClass().add("user-menu-button");
            errorDialog.setOnAction(evt -> {
                getSkinnable().showError("Error", "Something went really wrong!");
                getSkinnable().setShowUserOptions(false);
            });

            Button confirmationDialog = new Button("Show Confirmation");
            confirmationDialog.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            confirmationDialog.getStyleClass().add("user-menu-button");
            confirmationDialog.setOnAction(evt -> {
                getSkinnable().showConfirmation("Confirmation", "Are you sure you want to delete this customer?");
                getSkinnable().setShowUserOptions(false);
            });

            Button warningDialog = new Button("Show Warning");
            warningDialog.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            warningDialog.getStyleClass().add("user-menu-button");
            warningDialog.setOnAction(evt -> {
                getSkinnable().showWarning("Warning", "This action will have consequences!");
                getSkinnable().setShowUserOptions(false);
            });

            getChildren().addAll(errorDialog, confirmationDialog, warningDialog);
        }
    }

    class UserMenuButton extends HBox implements Toggle {

        private final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

        private UserAvatar userAvatar = new UserAvatar();
        private Label fullNameLabel = new Label();
        private Label titleLabel = new Label();
        private Label expandLabel = new Label();
        private VBox vbox = new VBox();

        public UserMenuButton() {
            vbox.getChildren().addAll(fullNameLabel, titleLabel);

            getChildren().addAll(userAvatar, vbox, expandLabel);

            setOnMouseClicked(evt -> setSelected(!isSelected()));

            userAvatar.userProperty().bind(getSkinnable().userProperty());

            fullNameLabel.getStyleClass().add("full-name-label");
            titleLabel.getStyleClass().add("title-label");
            expandLabel.getStyleClass().addAll("expand-label", "dropdown-icon-white");

            fullNameLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            titleLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            expandLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            fullNameLabel.setMouseTransparent(true);
            titleLabel.setMouseTransparent(true);
            expandLabel.setMouseTransparent(true);
            userAvatar.setMouseTransparent(true);

            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            getStyleClass().addAll("element", "user-button");

            getSkinnable().userProperty().addListener(it -> updateUser());
            updateUser();
        }

        private void updateUser() {
            User user = getSkinnable().getUser();
            if (user != null) {
                fullNameLabel.setText(user.getFullName());
                titleLabel.setText(user.getTitle());
            }
        }

        private BooleanProperty selected;

        public final BooleanProperty selectedProperty() {
            if (selected == null) {
                selected = new BooleanPropertyBase() {
                    @Override
                    protected void invalidated() {
                        final boolean selected = get();
                        final ToggleGroup tg = getToggleGroup();
                        // Note: these changes need to be done before selectToggle/clearSelectedToggle since
                        // those operations change properties and can execute user code, possibly modifying selected property again
                        pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, selected);
                        notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);
                        if (tg != null) {
                            if (selected) {
                                tg.selectToggle(UserMenuButton.this);
//                            } else if (tg.getSelectedToggle() == ToggleButton.this) {
//                                tg.clearSelectedToggle();
                            }
                        }
                    }

                    @Override
                    public Object getBean() {
                        return UserMenuButton.this;
                    }

                    @Override
                    public String getName() {
                        return "selected";
                    }
                };
            }
            return selected;
        }

        public final void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

        public final boolean isSelected() {
            return selectedProperty().get();
        }

        private final ObjectProperty<ToggleGroup> toggleGroup = new SimpleObjectProperty<>(this, "toggleGroup");

        @Override
        public ToggleGroup getToggleGroup() {
            return toggleGroup.get();
        }

        @Override
        public void setToggleGroup(ToggleGroup group) {
            this.toggleGroup.set(group);
        }

        @Override
        public ObjectProperty<ToggleGroup> toggleGroupProperty() {
            return toggleGroup;
        }
    }

    class ModulePane extends StackPane {

        public ModulePane() {
            getStyleClass().add("module-pane");

            final Shell shell = getSkinnable();
            shell.selectedModuleProperty().addListener(it -> {
                ShellModule module = shell.getSelectedModule();
                if (module != null) {
                    closeDrawers();

                    getChildren().setAll(module.getPane(getSkinnable()));
                } else {
                    getSkinnable().showHomeScreen();
                }
            });

            addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> closeDrawers());
        }
    }

    private void closeDrawers() {
        getSkinnable().setShowMenuDrawer(false);
        getSkinnable().setShowModulesMenu(false);
        getSkinnable().setShowCalendar(false);
        getSkinnable().setShowUserOptions(false);
        getSkinnable().setShowSearchField(false);
        getSkinnable().setShowLeftTray(false);
        getSkinnable().setShowRightTray(false);
    }

    class GlassPane extends StackPane {

        public GlassPane() {
            getStyleClass().add("glass-pane");

            setMouseTransparent(false);
            setOnMouseClicked(evt -> closeDrawers());
            setVisible(false);

            hideProperty().addListener(it -> {

                setVisible(true);

                FadeTransition fadeTransition = new FadeTransition();
                fadeTransition.setDuration(Duration.millis(200));
                fadeTransition.setNode(glassPane);
                fadeTransition.setFromValue(glassPane.isHide() ? .5 : 0);
                fadeTransition.setToValue(glassPane.isHide() ? 0 : .5);
                fadeTransition.setOnFinished(evt -> {
                    if (isHide()) {
                        setVisible(false);
                    }
                });
                fadeTransition.play();
            });
        }

        private final BooleanProperty hide = new SimpleBooleanProperty(this, "hide");

        public final BooleanProperty hideProperty() {
            return hide;
        }

        public final void setHide(boolean hide) {
            this.hide.set(hide);
        }

        public final boolean isHide() {
            return hide.get();
        }
    }
}
