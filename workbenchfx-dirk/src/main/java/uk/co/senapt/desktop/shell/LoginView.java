package uk.co.senapt.desktop.shell;

import com.google.inject.Inject;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javax.inject.Provider;
import javax.inject.Singleton;
import uk.co.senapt.desktop.shell.skins.LoginViewSkin;

@Singleton
public class LoginView extends Control {

    private LoginSuccessCallback callback;

    @Inject
    private Provider<LoginViewSkin> skin;

    public LoginView() {
        getStyleClass().add("login-view");
        getStylesheets().add(LoginView.class.getResource("login.css").toExternalForm());
    }

    public void setLoginSuccessCallback(LoginSuccessCallback callback) {
        this.callback = callback;
    }

    public LoginSuccessCallback getLoginSuccessCallback() {
        return callback;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return skin.get();
    }

    public interface LoginSuccessCallback {
        void loginSuccessful();
    }

}
