package uk.co.senapt.desktop.shell.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by lemmi on 21.08.17.
 */
public class User {

    private String username;
    private String fullName;
    private String title;
    private Image avatar;
    private Color initialsBackground = Color.CORNFLOWERBLUE;
    private Color initialsForeground = Color.WHITE;

    public User() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setInitialsBackground(Color initialsBackground) {
        this.initialsBackground = initialsBackground;
    }

    public Color getInitialsBackground() {
        return initialsBackground;
    }

    public void setInitialsForeground(Color initialsForeground) {
        this.initialsForeground = initialsForeground;
    }

    public Color getInitialsForeground() {
        return initialsForeground;
    }
}
