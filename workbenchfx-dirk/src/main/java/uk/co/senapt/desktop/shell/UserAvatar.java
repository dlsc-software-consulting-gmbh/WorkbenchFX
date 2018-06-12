package uk.co.senapt.desktop.shell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import uk.co.senapt.desktop.shell.model.User;
import uk.co.senapt.desktop.shell.skins.UserAvatarSkin;

/**
 * Created by lemmi on 21.08.17.
 */
public class UserAvatar extends Control {

    public UserAvatar() {
        getStyleClass().add("user-avatar");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new UserAvatarSkin(this);
    }

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
}
