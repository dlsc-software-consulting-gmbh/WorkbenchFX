# WorkbenchFX
[![Build Status](https://travis-ci.com/FHNW-IP5-IP6/WorkbenchFX.svg?token=8WqsSGJvE4SAqmHHx2Z7&branch=develop)](https://travis-ci.com/FHNW-IP5-IP6/WorkbenchFX)

**The one and only library to build large JavaFX Applications!**

![screenshot of an application created with WorkbenchFX](docs/images/workbenchFX_in_use.png) 

## Table of Contents
- [WorkbenchFX](#workbenchfx)
- [Table of Contents](#table-of-contents)
- [What is WorkbenchFX](#what-is-workbenchfx)
- [Advantages](#advantages)
- [Main Features](#main-features)
- [Documentation](#documentation)
- [Structure](#structure)
- [Demos](#demos)
- [Getting Started](#getting-started)
  - [Extending the `WorkbenchModule`](#extending-the-workbenchmodule)
  - [Creating the `Workbench`](#creating-the-workbench)
  - [Optionals](#optionals)

## What is WorkbenchFX?
TODO: description

## Advantages
- Less error-prone
- Less code needed
- Easy to learn
- Easy to understand
- Easy to use, especially for developers which have not much experience in working with JavaFX
- A well designed UI, inspired by the material design standards

## Main Features
- Simple and understandable API
- Encapsulating multiple, independent `WorkbenchModules`, and display them in Tabs
- A predefined stylesheet which allows the user to change the styles as he needs to
- The most important features are noted in the picture and the corresponding table below:

![screenshot of the most important features WorkbenchFX provides](docs/images/features.png)

Nr. | Feature | Description
--- | ------- | -----------
1 | `WorkbenchModule` | what is a module? extend from wbmod, put different mods into the wb 
2 | `Pagination` | mods displayed in addpage, pagination, multiple sites
3 | `Tabs` | opening modules in multiple tabs, open/closeable
4 | `NavigationDrawer` | automatically blend in when items
5 | `Toolbar` | 
6 | `ToolbarItems` | when defining custom -> just use the node ctor
7 | `ModuleToolbar` | automatically blend in and out as you like 
8 | `WorkbenchDialog` | providing various default dialog types

## Documentation
This project uses the `asciidoctor` plugin to generate the necessary documentation. Run the following *maven* task:
```Maven
process-resources
```
Afterwards, you will find the documentation in the `target/generated-docs/` subdirectory.

## Structure
WorkbenchFX uses the builder pattern to create the application, since one can use plenty of optional features.
The minimal usage requires only the definition of a custom extension from the `WorkbenchModule`.
Afterwards one can define further functionality calling the equivalent method.

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

Notes:
- The result of the `build()` call is a `Control` which can be set in a scene.
- It is also possible to use the default constructor `new Workbench()` and add the `WorkbenchModules` and features afterwards.

## Demos
We created several demos to visualize the capabilities of `WorkbenchFX` in the `workbench-fx-demo` folder:

File | Description
---- | -----------
`CustomDemo.java` | A workbench application which uses all features, to demonstrate the full capability of `WorkbenchFX`
`ExtendedDemo.java` | To demonstrate an application with a lot of `WorkbenchModules`
`SceneBuilderDemo.java` | A proof of concept, if the API also works with `SceneBuilder`
`StadardDemo.java` | Shows the simplest usage of `WorkbenchFX` with only three modules and no optional features  

## Getting started
### Extending the `WorkbenchModule`
An extension of the abstract class `WorkbenchModule` is required in order to create an application:

```Java
public class CustomModule extends WorkbenchModule {
  
}
```

It is then required to call the `super()` constructor and hand over a `String` as name and either an `Image`, `FontawesomeIcon` or `MaterialDesignIcon` as icon for the module:

```Java
public class CustomModule extends WorkbenchModule {
  public CustomModule() {
      super("My first Workbench module", MaterialDesignIcon.THUMB_UP); // A name and an icon is required
  }
}
```

Furthermore, overriding the `activate()` method is also required.
This method will be called when clicking on the `Tile` to open the module:

```Java
public class CustomModule extends WorkbenchModule {
  public CustomModule() {
      super("My first Workbench module", MaterialDesignIcon.THUMB_UP); // A name and an icon is required
  }
  @Override
  public Node activate() {
      return new Label("Hello World"); // return here your actual content to display
  }
}
```

This is a minimal implementation of a custom `WorkbenchModule`.
For further information we refer to the `Javadoc`.

### Creating the `Workbench`
After extending the `WorkbenchModule`, the application can be created.
To do this, one can access the `WorkbenchBuilder` by calling `Workbench.builder()`, setting the previously written module and build the `Workbench` by calling the `build()` method:

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
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
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

### Optionals
The optionals are called after adding the custom modules to the builder:

```Java
Workbench workbench = Workbench.builder(...)
.modulesPerPage(6) // call the optionals
.build();
```

The following parameters are optionally available to further configure the application:

Method (Workbench) | Description
------------------ | -----------
`modulesPerPage()` | Define the amount of `Tiles` which are displayed at the `AddModulePage`
`navigationDrawerItems()` | Allows setting multiple `MenuItems` which are then displayed in the `NavigationDrawer`. The button to open and close the drawer appears alway in the top-left corner
`toolbarLeft()` | Allows setting multiple `ToolbarItems` on the left side of the toolbar on top of the `Tabs`
`toolbarRight()` | Allows setting multiple `ToolbarItems` on the right side of the toolbar on top of the `Tabs`

When the default layout of `Page`, `Tab`, `Tile` or `NavigationDrawer` doesn't fulfill the desired requirements, it is possible to replace them:

Method (Workbench) | Description
------------------ | -----------
`navigationDrawer()` | Allows setting a custom implementation of the `NavigationDrawer` control, which will then be used
`pageFactory()` | Requires a `Callback` function which takes a `Workbench` and then returns a custom implementation of a `Page` control
`tabFactory()` | Requires a `Callback` function which takes a `Workbench` and then returns a custom implementation of a `Tab` control
`tileFactory()` | Requires a `Callback` function which takes a `Workbench` and then returns a custom implementation of a `Tile` control

The `WorkbenchModule` provides also optional functionality. It is possible to add `ToolbarItems` to the toolbar of the module (just like in the workbench):

Method (WorkbenchModule) | Description
------------------------ | -----------
`getToolbarControlsLeft()` | Calling this method returns an `ObservableList` of `ToolbarItems`. Adding items to the list will automatically create a toolbar between the `Tab` and the module content and adds the items on the left side
`getToolbarControlsRight()` | Calling this method returns an `ObservableList` of `ToolbarItems`. Adding items to the list will automatically create a toolbar between the `Tab` and the module content and adds the items on the right side

#### Setting types
The following table shows how to create `Settings` using the predefined controls and how they look like:

<table>
    <tr>
        <th>Syntax</th>
        <th>Outcome</th>
    </tr>
    <tr>
        <td><pre lang="java">
// Integer
IntegerProperty brightness = new SimpleIntegerProperty(50);
Setting.of("Brightness", brightness);</pre>
        </td>
        <td><img src="./docs/images/settings/integer_setting.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Integer Range
IntegerProperty fontSize = new SimpleIntegerProperty(12);
Setting.of("Font Size", fontSize, 6, 36);</pre>
        </td>
        <td><img src="./docs/images/settings/integerSlider_setting.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Double
DoubleProperty scaling = new SimpleDoubleProperty(1);
Setting.of("Scaling", scaling);</pre>
        </td>
        <td><img src="./docs/images/settings/double_setting.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Double Range
DoubleProperty lineSpacing = new SimpleDoubleProperty(1.5);
Setting.of("Line Spacing", lineSpacing, 0, 3, 1);</pre>
        </td>
        <td><img src="./docs/images/settings/doubleSlider_setting.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Boolean
BooleanProperty nightMode = new SimpleBooleanProperty(true);
Setting.of("Night Mode", nightMode);</pre>
        </td>
        <td><img src="./docs/images/settings/boolean_setting.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// String
StringProperty welcomeText = new SimpleStringProperty("Hello World");
Setting.of("Welcome Text", welcomeText);</pre>
        </td>
        <td><img src="./docs/images/settings/string_setting.png"></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Combobox, Single Selection, with ObservableList
ObservableList<String> resolutionItems = FXCollections.observableArrayList(Arrays.asList(
  "1024x768", "1280x1024", "1440x900", "1920x1080")
);
ObjectProperty<String> resolutionSelection = new SimpleObjectProperty<>("1024x768");
Setting.of("Resolution", resolutionItems, resolutionSelection);</pre>
        </td>
        <td><img src="./docs/images/settings/observableList_setting.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Combobox, Single Selection, with ListProperty
ListProperty<String> orientationItems = new SimpleListProperty<>(
  FXCollections.observableArrayList(Arrays.asList("Vertical", "Horizontal"))
);
ObjectProperty<String> orientationSelection = new SimpleObjectProperty<>("Vertical");
Setting.of("Orientation", orientationItems, orientationSelection);</pre>
        </td>
        <td><img src="./docs/images/settings/listProperty_setting.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Combobox, Multi Selection
ListProperty<String> favoritesItems = new SimpleListProperty<>(
  FXCollections.observableArrayList(Arrays.asList(
      "eMovie", "Eboda Phot-O-Shop", "Mikesoft Text",
      "Mikesoft Numbers", "Mikesoft Present", "IntelliG"
      )
  )
);
ListProperty<String> favoritesSelection = new SimpleListProperty<>(
  FXCollections.observableArrayList(Arrays.asList(
      "Eboda Phot-O-Shop", "Mikesoft Text"))
);
Setting.of("Favorites", favoritesItems, favoritesSelection);</pre>
        </td>
        <td><img src="./docs/images/settings/favourites_setting.png"/></td>
    </tr>
    <tr>
        <td><pre lang="java">
// Custom Control
IntegerProperty customControlProperty = new SimpleIntegerProperty(42);
IntegerField customControl = Field.ofIntegerType(customControlProperty).render(
  new IntegerSliderControl(0, 42));
Setting.of("Favorite Number", customControl, customControlProperty);</pre>
        </td>
        <td><img src="./docs/images/settings/custom_setting.png"/></td>
    </tr>
</table>

## Localisation
All displayed strings can be internationalized. You can use [resource bundles](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html) to define different locales and use the key instead of the descriptions. Adding i18n support is simply done by calling the method `.i18n()` at the end when creating the preferences:

```java
private ResourceBundle rbDE = ResourceBundle.getBundle("demo.demo-locale", new Locale("de", "CH"));
private ResourceBundle rbEN = ResourceBundle.getBundle("demo.demo-locale", new Locale("en", "UK"));

private ResourceBundleService rbs = new ResourceBundleService(rbEN);

PreferencesFx.of(…)
             .i18n(rbs);
```

## Validation
It is possible to optionally add a [Validator](http://dlsc.com/wp-content/html/formsfx/apidocs/com/dlsc/formsfx/model/validators/Validator.html) to settings. PreferencesFX uses the [implementation of FormsFX for the validation](http://dlsc.com/wp-content/html/formsfx/apidocs/com/dlsc/formsfx/model/validators/Validator.html). FormsFX offers a wide range of pre-defined validators, but also includes support for custom validators using the `CustomValidator.forPredicate()` method. The following table lists the supported validators:

| Validator | Description |
| --------- | ----------- |
| `CustomValidator` | Defines a predicate that returns whether the field is valid or not. |
| `DoubleRangeValidator` | Defines a number range which is considered valid. This range can be limited in either one direction or in both directions. |
| `IntegerRangeValidator` | Defines a number range which is considered valid. This range can be limited in either one direction or in both directions. |
| `RegexValidator` | Valiates text against a regular expression. This validator offers pre-defined expressions for common use cases, such as email addresses.
| `SelectionLengthValidator` | Defines a length interval which is considered valid. This range can be limited in either one direction or in both directions. |
| `StringLengthValidator` | Defines a length interval which is considered valid. This range can be limited in either one direction or in both directions. |

## Version Management
To change the version, set the property `workbenchfx.version` in the parent `pom.xml` file to the next version.
Then, run: `mvn process-resources`
It will then automatically proceed by updating all versions in all pom files automatically.

## Team
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
