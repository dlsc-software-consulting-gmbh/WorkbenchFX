package com.dlsc.workbenchfx.modules.webview;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import com.google.common.base.Strings;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebModule extends WorkbenchModule {

  private static final double FONT_SCALE_INCREMENT = 0.1;
  private final String url;
  private final WebView browser;
  private final TextField browserUrl;
  private WebEngine webEngine;

  public WebModule(String name, MaterialDesignIcon icon, String url) {
    super(name, icon);
    this.url = url;

    // setup webview
    browser = new WebView();
    webEngine = browser.getEngine();

    // setup textfield for URL
    browserUrl = new TextField("Loading...");
    HBox.setHgrow(browserUrl, Priority.ALWAYS);
    browserUrl.setPrefColumnCount(Integer.MAX_VALUE); // make sure textfield takes up rest of space
    browserUrl.setEditable(false);

    // setup toolbar
    ToolbarItem home = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.HOME),
        event -> webEngine.load(url));
    ToolbarItem increaseSize = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.PLUS),
        event -> browser.setFontScale(browser.getFontScale() + FONT_SCALE_INCREMENT));
    ToolbarItem decreaseSize = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.MINUS),
        event -> browser.setFontScale(browser.getFontScale() - FONT_SCALE_INCREMENT));
    getToolbarControlsLeft().addAll(home, increaseSize, decreaseSize, new ToolbarItem(browserUrl));

    // update textfield with url every time the url of the webview changes
    webEngine.documentProperty().addListener(
        observable -> {
          String currentUrl = webEngine.getDocument().getDocumentURI();
          if (!Strings.isNullOrEmpty(currentUrl)) {
            browserUrl.setText(currentUrl);
          }
        });
  }

  @Override
  public Node activate() {
    Platform.runLater(() -> webEngine.load(url));
    return browser;
  }

}
