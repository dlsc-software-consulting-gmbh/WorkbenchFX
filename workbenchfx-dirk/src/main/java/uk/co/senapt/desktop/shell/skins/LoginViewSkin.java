package uk.co.senapt.desktop.shell.skins;

import com.google.inject.Inject;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.CustomPasswordField;
import uk.co.senapt.desktop.shell.LoginView;

public class LoginViewSkin extends SkinBase<LoginView> {

    @Inject
    public LoginViewSkin(LoginView view) {
        super(view);

        ImageView senaptLogo = new ImageView();
        senaptLogo.setFitHeight(50);
        senaptLogo.setPreserveRatio(true);
        senaptLogo.getStyleClass().add("logo");

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        ImageView customerLogo = new ImageView();
        customerLogo.setFitHeight(50);
        customerLogo.setPreserveRatio(true);
        customerLogo.getStyleClass().add("customer-logo");

        HBox header = new HBox(senaptLogo, separator, customerLogo);
        header.getStyleClass().add("header");
        VBox.setMargin(header, new Insets(0, 0, 30, 0));

        TextField userNameField = new TextField();
        userNameField.setPromptText("Username");

        Button forgotPassword = new Button("Forgot Password");
        forgotPassword.setOnAction(evt -> System.out.println("not implemented, yet"));

        CustomPasswordField passwordField = new CustomPasswordField();
        passwordField.setPromptText("Password");
        passwordField.setRight(forgotPassword);

        Button loginButton = new Button("LOGIN");
        loginButton.setOnAction(evt -> view.getLoginSuccessCallback().loginSuccessful());
        loginButton.getStyleClass().add("login-button");
        VBox.setMargin(loginButton, new Insets(20, 0, 0, 0));

        VBox vBox = new VBox(header, userNameField, passwordField, loginButton);
        vBox.getStyleClass().add("container");

        getChildren().add(vBox);
    }
}
