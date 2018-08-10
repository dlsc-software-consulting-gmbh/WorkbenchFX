# WorkbenchFX
[![Build Status](https://travis-ci.com/FHNW-IP5-IP6/WorkbenchFX.svg?token=8WqsSGJvE4SAqmHHx2Z7&branch=develop)](https://travis-ci.com/FHNW-IP5-IP6/WorkbenchFX)

**The one and only library to build large JavaFX Applications!**

![screenshot of an application created with WorkbenchFX](docs/images/workbenchFX_in_use.png) 

# Table of Contents
- [What is WorkbenchFX?](#what-is-workbenchfx)
- [Advantages](#advantages)
- [Main Components](#main-components)
- [Documentation](#documentation)
- [Basic Structure](#basic-structure)
  - [Workbench Concept](#workbench-concept)
  - [Module Lifecycle](#module-lifecycle)
- [Demos](#demos)
- [Getting Started](#getting-started)
  - [Extending the `WorkbenchModule`](#extending-the-workbenchmodule)
  - [Creating the `Workbench`](#creating-the-workbench)
  - [Optionals](#optionals)
    - [`WorkbenchBuilder`](#workbenchbuilder)
    - [`Workbench`](#workbench)
    - [`WorkbenchModule`](#workbenchmodule)
- [Using the Components](#using-the-components)
  - [ToolbarItem](#toolbaritem)
  - [Dialog](#dialog)
    - [Predefined Dialogs](#predefined-dialogs)
    - [Custom Dialog](#custom-dialog)
  - [Prevent module from closing](#prevent-module-from-closing)
  - [Drawer](#drawer)
  - [Custom Overlay](#custom-overlay)
- [Restyling](#restyling)
- [Team](#team)

# What is WorkbenchFX?
TODO: description

# Advantages
- Less error-prone
- Less code needed
- Easy to learn
- Easy to understand
- Easy to use, especially for developers which have not much experience in working with JavaFX
- A well designed, adaptable UI, inspired by the material design standards
- Multiple, independent `Workbench modules`, displayed in Tabs combine into one great application
- The `jdk8` branch works well with `JPRO`
- `SceneBuilder` support

# Main Components
The most important components are noted in the picture and the corresponding table below:

![screenshot of the addModulePage](docs/images/components/addModulePage.png)

Nr. | Component           | Description
--- | ------------------- | -----------
 _  | `Workbench module`  | The complete workbench application consists of multiple modules. It contains the title, an icon and the content to be displayed in the `Workbench`
 2  | `Tile`              | Using the `Workbench module`, a `Tile` will be created. It is basically a button which allows to open a module or switch to it, if it's already open.
 3  | `Tab`               | Together with the `Tile`, a `Tab` will be generated. It will be displayed in the `Tabbar` as long as the module is open. The `Tab` is used to navigate through the open modules and to close them
 4  | `Tabbar`            | The upper section of the window, where the `Tabs` of the current open modules are displayed. On the right end of the bar, the `Add button` is displayed
 5  | `Add button`        | The button used to open a new module. It opens an overview of all available modules.
 6  | `Pagination`        | Stores all the `Pages` on which the `Tiles` are displayed
 7  | `Page`              | When having more modules as are defined in the `modulesPerPage()` attribute, the `Workbench` creates multiple `Pages` on which the `Tiles` are displayed
 8  | `Pagination dots`   | are only displayed when having multiple `Pages` and can be used for navigating through them
 9  | `Toolbar`           | It contains `Toolbar items`. If the bar does not contain any items, the `Toolbar` will be hidden automatically.
10  | `Toolbar item`      | Depending on the attributes defined, the item adapts the behaviour of either a JavaFX `Label`, `Button` or `MenuButton`
11  | `Menu button`       | It opens the `Navigation drawer`. The position of the button varies depending on the amount of items in the `Toolbar` and the `Navigation drawer`. If the `Navigation drawer` does not contain any items, the button will not be displayed at all. If any items are in the `Toolbar`, it will be displayed on the left side of the `Toolbar`, otherwise on the left side of the `Tabbar`

![screenshot of the navigationDrawer](docs/images/components/navigationDrawer.png)

Nr. | Component           | Description
--- | ------------------- | -----------
12  | `Navigation drawer` | It displays a logo which can be set in the stylesheet and the defined items. The default hover behaviour over its items can be adjusted using the method call `setMenuHoverBehavior()` on the drawer. It can be closed by clicking on the `Glass pane`
13  | `Glass pane`        | The `Glass pane` prevents click events on the components below. Clicking on the `Glass pane` often closes the showing `Drawer` or `Dialog` unless its `blocking` attribute prevents it from closing

![screenshot of the drawer](docs/images/components/drawer.png)

Nr. | Component           | Description
--- | ------------------- | -----------
14  | `Drawer`            | It is possible to use the optional `showDrawer()` call on the `Workbench` to create additional drawers with custom content. All four window sides are supported

![screenshot of the dialog](docs/images/components/dialog.png)

Nr. | Component           | Description
--- | ------------------- | -----------
15  | `Workbench dialog`  | When calling `showDialog()` on the `Workbench`, there is the possibility to create a custom dialog, or using a variety of predefined dialogs like `error-, confirmation-, information-dialog, etc.`

![screenshot of the moduleToolbar](docs/images/components/moduleToolbar.png)

Nr. | Component           | Description
--- | ------------------- | -----------
16  | `Module toolbar`    | Calling `getToolbarControlsLeft()` or `getToolbarControlsRight()` on a `Workbench module` gives access to its `Toolbar items`. Adding them will automatically generate a unique toolbar for it

For further information about the several components we refer to the `Javadoc`

# Documentation
TODO: Rewrite documentation

This project uses the `asciidoctor` plugin to generate the necessary documentation. Run the following *maven* task:
```Maven
process-resources
```
Afterwards, the documentation is located in the `target/generated-docs/` subdirectory.

# Basic Structure
## Workbench Concept
WorkbenchFX uses the builder pattern to create the application, since one can use plenty of optional features.
The minimal usage requires only the definition of a custom extension from the `WorkbenchModule`.
Afterwards one can define further functionality calling the equivalent methods.

For better illustration, the basic concept of creating a workbench application is shown below:
```Java
Workbench workbench = 
    Workbench.builder( // Using the static method call
        new CustomWorkbenchModule() // Extension of the WorkbenchModule
        ...
    )
    // .toolbarRight(...) // optional usage of additional features eg. navigationDrawer(), modulesPerPage(), etc.
    .build(); // The build call finishes and returns the workbench
```

Note:
- The result of the `build()` call is a `Control` which can be set in a scene.
- It is also possible to use the default constructor `new Workbench()` and add the `WorkbenchModules` and features afterwards. But it is recommended to use the builder pattern, since it is much easier to create the `Workbench`. The default constructor comes in use when the API is used with `SceneBuilder`.

## Module Lifecycle
The full documentation about the module lifecycle can be found in the subdirectory *docs/index.adoc#workbenchmodule-lifecycle*

The abstract class `WorkbenchModule` contains four different methods which can be overridden:

Method         | Description
-------------- | -----------
`init()`       | Gets called when the module is being opened from the overview for the first time
`activate()`   | Gets called whenever the currently displayed content is being switched to this module
`deactivate()` | Gets called whenever this module is the currently displayed content and the content is being switched to another module
`destroy()`    | Gets called when this module is explicitly being closed by the user in the toolbar

When extending a custom module, it is only required to override the `activate()` method.

Overriding the methods don't require any workbench related coding.
When overriding one of the methods, only the module related stuff needs to be programmed.

Note:
- A fifth method exists: `close()`. This method differs from the others, because it ignores the lifecycle and closes the module without calling `deactivate()` or `destroy()`. More about this method is written in the chapter about [prevent module from closing](#prevent-module-from-closing).
- For further information we refer to our documentation or the `Javadoc`

# Demos
We created several demos to visualize the capabilities of `WorkbenchFX` in the `workbench-fx-demo` folder:

File                | Description
------------------- | -----------
`StandardDemo.java` | Shows the simplest usage of `WorkbenchFX` with only three modules and no optional features  
`CustomDemo.java`   | A workbench application which uses all features, to demonstrate the full capability of `WorkbenchFX`
`FXMLDemo.java`     | A proof of concept, if the API also works with `SceneBuilder` and a `FXML` file

# Getting started
## Extending the `WorkbenchModule`
An extension of the abstract class `WorkbenchModule` is required in order to create an application:

```Java
public class CustomModule extends WorkbenchModule {
  
}
```

It is then required to call the `super()` constructor and hand over a `String` as name and either an `Image`, `FontawesomeIcon` or `MaterialDesignIcon` as icon for the module:

```Java
public CustomModule() {
  super("My first Workbench module", MaterialDesignIcon.THUMB_UP); // A name and an icon is required
}
```

Cheatsheets for using the icons are available at:
- [materialdesignicons.com](https://materialdesignicons.com/)
- [fontawesome.com](https://fontawesome.com/v4.7.0/)


Furthermore, overriding the `activate()` method is also required.
This method will be called when clicking on the `Tile` to open the module (further information about the [Module Lifecycle](#module-lifecycle)):

```Java
@Override
public Node activate() {
  return new Label("Hello World"); // return here the actual content to display
}
```

The minimal implementation of a custom `WorkbenchModule` finally looks like the code snippet below.
Returning a *Hello World Label* represents the view which will be displayed in the final application.
For further information we refer to the `Javadoc`.

```Java
public class CustomModule extends WorkbenchModule {
  public CustomModule() {
      super("My first Workbench module", MaterialDesignIcon.THUMB_UP); // A name and an icon is required
  }
  @Override
  public Node activate() {
      return new Label("Hello World"); // return here the actual content to display
  }
}
```

## Creating the `Workbench`
After extending the `WorkbenchModule`, the application can be created.
To do this, one can access the `WorkbenchBuilder` by calling `Workbench.builder()`, setting the previously written module and build the `Workbench` by calling the `build()` method:

```Java
// Creating the Workbench
Workbench customWorkbench = Workbench.builder( // Accessing the WorkbenchBuilder
    new CustomModule() // Adding the CustomModule
).build(); // Building the Workbench
```

It then can be set into a scene:

```Java
public class CustomDemo extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    
    // Creating the Workbench
    Workbench customWorkbench = Workbench.builder( // Accessing the WorkbenchBuilder
        new CustomModule() // Adding the CustomModule
    ).build(); // Building the Workbench
    
    Scene myScene = new Scene(customWorkbench);
    primaryStage.setScene(myScene);
    primaryStage.setWidth(700);
    primaryStage.setHeight(450);
    primaryStage.show();
  }
}
```

This code snippet results to the following application:

![custom workbench](docs/images/customWorkbench.png)

The default implementation comes with a clickable `Tile` to open the module.
Opening the module, creates a `Tab` with the defined Icon and text and the content returned in the activate() method is displayed in the center.
By clicking on the `+` button, one gets back to the `AddModulePage`.
Closing the opened module is achieved through clicking on the close button in the `Tab`.

## Optionals
### `WorkbenchBuilder`
These optionals are called after adding the custom modules to the builder:

```Java
Workbench workbench = Workbench.builder(...)
.modulesPerPage(6) // call the optionals
.build();
```

The following parameters are optionally available to further configure the application:

Method (WorkbenchBuilder) | Description
------------------ | -----------
`modulesPerPage()` | Define the amount of `Tiles` which are displayed at the `AddModulePage`
`navigationDrawerItems()` | Allows setting multiple `MenuItems` which are then displayed in the `NavigationDrawer`. The button to open and close the drawer appears alway in the top-left corner
`toolbarLeft()` | Allows setting multiple `ToolbarItems` on the left side of the toolbar on top of the `Tabs`
`toolbarRight()` | Allows setting multiple `ToolbarItems` on the right side of the toolbar on top of the `Tabs`

When the default layout of `Page`, `Tab`, `Tile` or `NavigationDrawer` doesn't fulfill the desired requirements, it is possible to replace them:

Method (WorkbenchBuilder) | Description
------------------ | -----------
`navigationDrawer()` | Allows setting a custom implementation of the `NavigationDrawer` control, which will then be used
`pageFactory()` | Requires a `Callback` function which takes a `Workbench` and then returns a custom implementation of a `Page` control
`tabFactory()` | Requires a `Callback` function which takes a `Workbench` and then returns a custom implementation of a `Tab` control
`tileFactory()` | Requires a `Callback` function which takes a `Workbench` and then returns a custom implementation of a `Tile` control

### `Workbench`
After the `build()` call on the builder, the `Workbench` is created.
Following useful calls might be of interest:

Method (Workbench) | Description
------------------ | -----------
`getNavigationDrawer()`      | Returns the `Navigation drawer`
`getNavigationDrawerItems()` | Returns the `ObservableList` of the drawers `ToolbarItems`
`show...Dialog()`            | Shows a [predefined dialog](#predefined-dialogs)
`showDialog()`               | Shows a [custom dialog](#custom-dialog)
`showDrawer()`               | Shows a [custom drawer](#drawer)
`getToolbarControlsLeft()`   | Grants access to the items on the left of the `Toolbar`
`getToolbarControlsRight()`  | Grants access to the items on the right of the `Toolbar`
`showOverlay()`              | Shows a [custom overlay](#custom-overlay)

### `WorkbenchModule`
The `WorkbenchModule` also provides useful functionality.
It is possible to add `ToolbarItems` to the toolbar of the module (just like in the workbench):

Method (WorkbenchModule)    | Description
--------------------------- | -----------
`getToolbarControlsLeft()`  | Calling this method returns an `ObservableList` of `ToolbarItems`. Adding items to the list will automatically create a toolbar between the `Tab` and the module content and adds the items on the left side
`getToolbarControlsRight()` | Calling this method returns an `ObservableList` of `ToolbarItems`. Adding items to the list will automatically create a toolbar between the `Tab` and the module content and adds the items on the right side
`close()`                   | Will immediately close the module, ignoring the [Module Lifecycle](#module-lifecycle)
`getWorkbench()`            | In the `init()` call, the `Workbench` is stored in the module. Calling this returns it

# Using the Components
## ToolbarItem
The `Toolbar items` which can be set in the toolbars of either the workbench or the module are styled and behave differently based on their content.
If for example the item contains a `String` as text and a `MenuItem` it is automatically assumed that the styling and behaviour of a `MenuButton` is needed.
If on the other hand only an `IconView` is defined, it is assumed, the behaviour of a `Label` is desired.

Adding different attributes to the `ToolbarItem` results in different outcomes: 
They can also be seen in the `toolbar` of the `CustomDemo`

<table>
    <tr>
        <th>Syntax</th>
        <th>Outcome</th>
    </tr>
    <tr>
        <td><pre lang="java">
// Label with text
ToolbarItem toolbarItem = new ToolbarItem("Hello World");</td>
        <td><img src="docs/images/toolbarItems/label_text.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Label with graphic
ToolbarItem toolbarItem = new ToolbarItem(
    new MaterialDesignIconView(MaterialDesignIcon.THUMB_UP)
);</td>
        <td><img src="docs/images/toolbarItems/label_icon.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Label with text and graphic
ToolbarItem toolbarItem = new ToolbarItem(
    "Hello World",
    new MaterialDesignIconView(MaterialDesignIcon.THUMB_UP)
);</td>
        <td><img src="docs/images/toolbarItems/label_text_icon.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Button with text
ToolbarItem toolbarItem = new ToolbarItem(
    "Hello World", event -> System.out.println("Hello World")
);</td>
        <td><img src="docs/images/toolbarItems/button_text.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Button with graphic
ToolbarItem toolbarItem = new ToolbarItem(
    new MaterialDesignIconView(MaterialDesignIcon.THUMB_UP),
    event -> System.out.println("Hello World")
);</td>
        <td><img src="docs/images/toolbarItems/button_icon.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Button with text and graphic
ToolbarItem toolbarItem = new ToolbarItem(
    "Hello World",
    new MaterialDesignIconView(MaterialDesignIcon.THUMB_UP),
    event -> System.out.println("Hello World")
);</td>
        <td><img src="docs/images/toolbarItems/button_text_icon.png"></td>
    </tr>
    <tr>
        <td><pre lang="java">
// MenuButton with text
ToolbarItem toolbarItem = new ToolbarItem(
    "Hello World",
    new MenuItem("Content 1"), new MenuItem("Content 2")
);</td>
        <td><img src="docs/images/toolbarItems/menuButton_text.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// MenuButton with graphic
ToolbarItem toolbarItem = new ToolbarItem(
    new MaterialDesignIconView(MaterialDesignIcon.THUMB_UP),
    new MenuItem("Content 1"), new MenuItem("Content 2")
);</td>
        <td><img src="docs/images/toolbarItems/menuButton_icon.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// MenuButton with text and graphic
ToolbarItem toolbarItem = new ToolbarItem(
    "Hello World",
    new MaterialDesignIconView(MaterialDesignIcon.THUMB_UP),
    new MenuItem("Content 1"), new MenuItem("Content 2")
);</td>
        <td><img src="docs/images/toolbarItems/menuButton_text_icon.png"/></td>
    </tr>
    <tr>
    <tr>
        <td><pre lang="java">
// MenuButton with a MenuItem containing custom content
ToolbarItem toolbarItem = new ToolbarItem(
    "Account",
    new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT),
    new MenuItem("",
        new HBox(
            new Label("Login: "),
            new TextField(),
            new Button("", new MaterialDesignIconView(
                MaterialDesignIcon.PLUS))
        )
    )
);</td>
        <td><img src="docs/images/toolbarItems/custom_content.png"/></td>
    </tr>
    <tr>
</table>

## Dialog
A demo of the dialogs can be found in the `DialogTestModule` of the [Custom Demo](#demos) 

### Predefined Dialogs
`WorkbenchFX` comes with a lot of predefined dialogs.
Using them is as simple as calling `(Workbench).show...Dialog()` with the desired dialog type.
Every dialog type returns after clicking on a `Button` the corresponding `ButtonType` as a result.
Therefore it is required to define a `Consumer<ButtonType>` for every dialog to validate the answer.
A few examples on how to use them are listed below: 

```Java
// Precondition
Workbench workbench = Workbench.builder(...).build; // Creating the workbench
Button dialogBtn = new Button("Show Dialog"); // Assuming the button is used in a module
```

<table>
    <tr>
        <th>Syntax</th>
        <th>Outcome</th>
    </tr>
    <tr>
        <td><pre lang="java">
// Confirmation Dialog
dialogBtn.setOnAction(event ->
    workbench.showConfirmationDialog(
       "Continue without saving?",
       "Are you sure you want to continue without saving your document?",
       buttonType -> { // Proceed and validate the result }
    )
);</td>
        <td><img src="docs/images/dialogs/confirmation.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Error Dialog
dialogBtn.setOnAction(event ->
    workbench.showErrorDialog(
       "Button click failed!",
       "During the click of this button, something went horribly wrong.",
       buttonType -> { // Proceed and validate the result }
    )
);</td>
        <td><img src="docs/images/dialogs/error.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Error Dialog on exception
dialogBtn = null; // Provokes an exception
try {
  dialogBtn.setOnAction(event -> System.out.println("NPE alert!"));
} catch (NullPointerException exception) {
  workbench.showErrorDialog(
     "Button click failed!",
     "During the click of this button, something went horribly +
     wrong. Please forward the content below to anyone but the +
     WorkbenchFX developers to track down the issue:",
     exception // Could also be just a String
     buttonType -> { // Proceed and validate the result }
  );
}</td>
        <td><img src="docs/images/dialogs/exception.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Warning Dialog
dialogBtn.setOnAction(event ->
    workbench.showWarningDialog(
       "Reset settings?",
       "This will reset your device to its default factory settings.",
       buttonType -> { // Proceed and validate the result }
    )
);</td>
        <td><img src="docs/images/dialogs/warning.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Information Dialog
dialogBtn.setOnAction(event ->
    workbench.showInformationDialog(
       "Just to let you know",
       "(This is an information dialog)",
       buttonType -> { // Proceed and validate the result }
    )
);</td>
        <td><img src="docs/images/dialogs/information.png"/></td>
    </tr>
</table>

### Custom Dialog
Sometimes just using the default dialog types are not enough.
For such special cases, the `showDialog()` method can be used.
With a `WorkbenchDialog.builder()` a custom dialog can be created.
The builder provides some useful methods which can be used:

WorkbenchDialog.builder(*Parameters*) | Description
------------------------------------- | -----------
`String title`                        | Required and defines the `title` of the dialog
`String message`                      | Optional either a message or a content can be added. The `message` is located below the `title`
`Node content`                        | A `Node` as custom content
`Type type`                           | Defines one of the default dialog types like `error`, `information`, etc. The corresponding buttons will automatically be set
`ButtonType... buttonTypes`           | All the defined button types will be set (eg. `OK`, `CANCEL` and `APPLY` for a preferences dialog)

Note: 
- Defining a `content` will prevent any further definition of `messages` or `exceptions`

WorkbenchDialog.builder().*Parameters* | Description
-------------------------------------- | -----------
`blocking(boolean)`                    | Defines whether clicking on the `Glasspane` closes the dialog or not (eg. forcing a decision)
`onResult(Consumer<ButtonType>)`       | Clicking on a dialog, the clicked `ButtonType` is returned. On result proceeds the answer
`details(String)`                      | Adding details (@see the note on top)
`exception(Exception)`                 | Adding an `Exception` (@see the note on top)
`maximized(boolean)`                   | Defines whether the dialogs size should take the full screen or only as much as it fits the content
`showButtonsBar(boolean)`              | Defines whether the dialogs buttons should be shown or not
`onShown(EventHandler<Event>)`         | The `EventHandler` which is called when the dialog is showing
`onHidden(EventHandler<Event>)`        | The `EventHandler` which is called when the dialog is hidden
`dialogControl(DialogControl)`         | It is possible to set a custom `DialogControl`
`build()`                              | Builds the `WorkbenchDialog`

WorkbenchDialog                    | Description
---------------------------------- | -----------
`getButton(ButtonType buttonType)` | Returns an `Optional<Button>` of the dialog. Useful when accessing the buttons of the dialog is needed

Using the builder it is possible to write some interesting custom dialogs:

```Java
// Precondition
Workbench workbench = Workbench.builder(...).build; // Creating the workbench
Button dialogBtn = new Button("Show Dialog"); // Assuming the button is used in a module
```

<table>
    <tr>
        <th>Syntax</th>
        <th>Outcome</th>
    </tr>
    <tr>
        <td><pre lang="java">
// Dialog which requires input to proceed
// Create a CheckBox which will be set as content
CheckBox checkBox = new CheckBox("I accept the Terms and Conditions");
dialogBtn.setOnAction(event -> {
  // Building the dialog with the CheckBox as content
  WorkbenchDialog dialog = WorkbenchDialog.builder(
  "Check the box to continue", checkBox, ButtonType.OK)
      .blocking(true)
      .build();
  // Bind the OK button to the CheckBox
  dialog.setOnShown(event1 -> {
    dialog.getButton(ButtonType.OK).ifPresent(button -> {
      button.disableProperty().bind(checkBox.selectedProperty().not());
    });
  });
  getWorkbench().showDialog(dialog);
});</td>
        <td><img src="docs/images/dialogs/conditional.png"/></td>
    </tr>
</table>

Other examples can be found in the `DialogTestModule` of the [Custom Demo](#demos) 

## Prevent module from closing
In some cases it is necessary to prevent a module from closing.
For example following dialog asks for saving before closing:

![Image of a dialog which asks for saving before closing the module](docs/images/dialogs/save.png)

In the [Module Lifecycle](#module-lifecycle) it is stated, that the `destroy()` method will be called when closing the module.
The module will be closed as soon as the `destroy()` method returns `true`.
If someone wants to prevent the module from closing he has to return `false` and then close the module manually as soon as he is ready to.
This is done by calling the method `close()`.

The code snippet below results in the dialog displayed in the image on top:

```Java
@Override
public boolean destroy() {
  
  // Do some asynchronous task (in our case showing a dialog and waiting for input)
  getWorkbench().showDialog(WorkbenchDialog.builder(
      "Save before closing?",
      "Do you want to save your progress? Otherwise it will be lost.",
      ButtonType.YES, ButtonType.NO, ButtonType.CANCEL)
      .blocking(true)
      .onResult(buttonType -> {
        if (ButtonType.YES.equals(buttonType)) {
          // YES was pressed -> Proceed with saving
          ...
          close(); // At the end of saving, close the module 
        }
      })
      .build());
  
  return false; // return false, because we're closing manually
}
```

## Drawer
// Chapter about using the Drawers

## Custom Overlay
The foundation of [Dialogs](#dialog) and [Drawers](#drawer) are `Overlays`.
It is possible to define a custom one using the method `showOverlay()` in the `workbench`.
The defined overlay will be stacked on a `Glass pane`.

    (Workbench).showOverlay(
        Region overlay,  // The overlay to show
        boolean blocking // true, if the overlay should not be closed when clicking on the glasspane
    );
    
The overlay can essentially be any `Region` (for example a `Custom Control`).
As default, the defined content will be displayed in the top-left corner of the window.
If it is desired to center the content the following call in the overlay is needed: 

```Java
public class MyOverlay extends GridPane { // Example implementation of an overlay
  public MyOverlay() {
    StackPane.setAlignment(this, Pos.CENTER); // This call is needed to center the overlay on screen
  }
}
```

# Restyling
First of all:
`WorkbenchFX` does not interrupt with the styles of the developer.
This way each module can be styled independently and one does not have to worry about the workbench taking influence on the styling.

But it is possible to alter the styles of the workbench itself.
`WorkbenchFX` comes with an out of the box styling.
It is strongly inspired by the [material design standards](https://material.io/).

In the [CustomDemo.java](#demos) file a stylesheet is referenced:
      
```Java
workbench.getStylesheets().add(CustomDemo.class.getResource("customTheme.css").toExternalForm());
```

This way, the workbench styling can be altered.
In the `customTheme.css` file are some default colors referenced:

```css
* {
  -primary-color: #6200EE;
  -primary-variant-color: #3700b3;
  -secondary-color: #6300ff;
  -secondary-variant-color: #1e005f;
  -background-color: #FFFFFF;
  -surface-color: #FFFFFF;
  -error-color: #B00020;
  -on-primary-color: #FFFFFF;
  -on-secondary-color: #FFFFFF;
  -on-background-color: #000000;
  -on-surface-color: #000000;
  -on-error-color: #FFFFFF;
}

.logo {
  /* Reference to the applications logo */
  -fx-graphic: url("logo.png");
}
```

Their naming and the way they are used is directly taken from the [material design standards](https://material.io/design/color/the-color-system.html#color-theme-creation).
Changing those colors leads to a complete restyling of the application.
For example A `darkTheme.css` is also referenced in the demo:

![screenshot of the workbenches darkTheme version](docs/images/workbenchFX_in_use_dark.png)

Sometimes just changing the colors is not enough.
Every component in the workbench has its own `class` or `id`.
This way, the components can be restyled if needed.

For example every generated `Tab` and `Tile` has its own unique `id`.
the naming convention for the `id` is set to:
- Prefix: `tab/tile` (depending on the component)
- body: the defined name of the module
  - with any special letters removed
  - all ' ' spaces replaced with '-' hyphens
  - uppercase letters converted to lowercase

for further information we refer to the Javadoc `WorkbenchUtils.convertToId()`

`id` example:

    Module name:
        Pokémon Game
        
    Results to:
        tab-pokmon-game // for the Tab
        tile-pokmon-game // for the Tile

Referring to the custom workbench in the chapter [Getting Started](#getting-started):

If someone would like to change the colors of the application he creates a new `css` file `customTheme.css` and refers to it in the workbench:

```Java
customWorkbench.getStylesheets().add(CustomDemo.class.getResource("customTheme.css").toExternalForm());
``` 

In context the code looks like this:

```Java
public class CustomDemo extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    
    // Creating the Workbench
    Workbench customWorkbench = Workbench.builder( // Accessing the WorkbenchBuilder
        new CustomModule() // Adding the CustomModule
    ).build(); // Building the Workbench
    
    // Adding the stylesheet to the workbench to restyle it
    customWorkbench.getStylesheets().add(CustomDemo.class.getResource("customTheme.css").toExternalForm());
    
    Scene myScene = new Scene(customWorkbench);
    primaryStage.setScene(myScene);
    primaryStage.setWidth(700);
    primaryStage.setHeight(450);
    primaryStage.show();
  }
}
``` 

Changing the colors in the `css` file to something like this:

```css
* {
  -primary-color: #c9deee;
    -primary-variant-color: #97aaba;
    -secondary-color: #c9deee;
    -secondary-variant-color: #97aaba;
    -background-color: #FFFFFF;
    -surface-color: #FFFFFF;
    -error-color: #B00020;
    -on-primary-color: #333333;
    -on-secondary-color: #333333;
    -on-background-color: #333333;
    -on-surface-color: #333333;
    -on-error-color: #FFFFFF;
}
```

Leads to following design:

![screenshot of the custom css](docs/images/custom_css_1.png)

Assuming the `Tab` and `Tile` are needed to be restyled: Adding following code snippet to the `customTheme.css` 

```css
/* Styling the Tile */
#tile-my-first-workbench-module .tile-box {
  -fx-background-color: -primary-color !important; /* The background of the Tile */
}

#tile-my-first-workbench-module .tile-box .text, /* The icon and the text */
#tile-my-first-workbench-module .tile-box .glyph-icon {
  -fx-fill: -on-primary-color !important;
}

/* Styling the Tab */
#tab-my-first-workbench-module:selected {
  -fx-background-color: #333333 !important; /* The background of the Tab */
  -fx-background-radius: 5px 5px 0 0 !important;
}

#tab-my-first-workbench-module:selected .text, /* The icon and the text */
#tab-my-first-workbench-module:selected .glyph-icon {
  -fx-fill: white !important;
}

#tab-my-first-workbench-module:selected .shape {
  -fx-background-color: white !important; /* The close icon */
}
```

Leads to following styling:

![screenshot of the custom css](docs/images/custom_css_2.png)

Note:
- The `color-variables` can still be used in the `customTheme.css` file
- Since the styling of the workbench is superior, the `!important` tag is required when restyling the workbench
- A tool like [ScenicView](http://fxexperience.com/scenic-view/) works well to determine the style classes

# Team
- Marco Sanfratello
  - marco.sanfratello@students.fhnw.ch
  - Skype: sanfratello.m@gmail.com 
  - GitHub: Genron

- François Martin
  - francois.martin@students.fhnw.ch 
  - Skype: francoisamimartin
  - GitHub: martinfrancois
  
- Dirk Lemmermann
  - dlemmermann@gmail.com
  - Skype: dlemmermann
  - GitHub: dlemmermann
  
- Dieter Holz
  - dieter.holz@fhnw.ch
  - Skype: dieter.holz.canoo.com
  - GitHub: DieterHolz
