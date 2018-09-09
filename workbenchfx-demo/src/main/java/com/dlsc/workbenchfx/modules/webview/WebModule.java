package com.dlsc.workbenchfx.modules.webview;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import com.google.common.base.Strings;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.util.Objects;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

public class WebModule extends WorkbenchModule {

  // Custom icons as SVG
  private final SVGPath decreaseFontIcon = new SVGPath();
  private final SVGPath increaseFontIcon = new SVGPath();
  {
    // materialdesignicons.com format-font-size-increase
    increaseFontIcon.setContent("M5.12,14L7.5,7.67L9.87,14M6.5,5L1,19H3.25L4.37,16H10.62L11.75,19H14L8.5,5H6.5M18,7L13,12.07L14.41,13.5L17,10.9V17H19V10.9L21.59,13.5L23,12.07L18,7Z");
    // materialdesignicons.com format-font-size-decrease
    decreaseFontIcon.setContent("M5.12,14L7.5,7.67L9.87,14M6.5,5L1,19H3.25L4.37,16H10.62L11.75,19H14L8.5,5H6.5M18,17L23,11.93L21.59,10.5L19,13.1V7H17V13.1L14.41,10.5L13,11.93L18,17Z");
  }

  private static final double FONT_SCALE_INCREMENT = 0.1;
  private final String url;
  private final WebView browser;
  private final TextField browserUrl;
  private final WebEngine webEngine;

  public WebModule(String name, MaterialDesignIcon icon, String url) {
    super(name, icon);
    this.url = url;

    // setup webview
    browser = new WebView();
    webEngine = browser.getEngine();

    // workaround since HTTP headers related to CORS, are restricted, see: https://bugs.openjdk.java.net/browse/JDK-8096797
    System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

    // setup textfield for URL
    browserUrl = new TextField("Loading...");
    HBox.setHgrow(browserUrl, Priority.ALWAYS);
    browserUrl.setPrefColumnCount(Integer.MAX_VALUE); // make sure textfield takes up rest of space
    browserUrl.setEditable(false);

    // setup toolbar
    ToolbarItem back = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.CHEVRON_LEFT),
        event -> webEngine.executeScript("history.back()"));
    ToolbarItem forward = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.CHEVRON_RIGHT),
        event -> webEngine.executeScript("history.forward()"));
    ToolbarItem home = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.HOME),
        event -> webEngine.load(url));
    ToolbarItem reload = new ToolbarItem(new MaterialDesignIconView(MaterialDesignIcon.REFRESH),
        event -> webEngine.reload());
    getToolbarControlsLeft().addAll(back, forward, home, reload, new ToolbarItem(browserUrl));

    ToolbarItem increaseSize = new ToolbarItem(new Group(increaseFontIcon),
        event -> browser.setFontScale(browser.getFontScale() + FONT_SCALE_INCREMENT));
    ToolbarItem decreaseSize = new ToolbarItem(new Group(decreaseFontIcon),
        event -> browser.setFontScale(browser.getFontScale() - FONT_SCALE_INCREMENT));
    getToolbarControlsRight().addAll(increaseSize, decreaseSize);

    // update textfield with url every time the url of the webview changes
    webEngine.documentProperty().addListener(
        observable -> {
          Document document = webEngine.getDocument();
          if (!Objects.isNull(document) &&
              !Strings.isNullOrEmpty(document.getDocumentURI())) {
            browserUrl.setText(document.getDocumentURI());
          }
        });
  }

  @Override
  public Node activate() {
    Platform.runLater(() -> webEngine.load(url));
    return browser;
  }

}
