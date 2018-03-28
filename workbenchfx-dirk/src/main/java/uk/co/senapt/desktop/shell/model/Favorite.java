package uk.co.senapt.desktop.shell.model;

/**
 * Created by gdiaz on 24/08/2017.
 */
public class Favorite {

    public static final String DEFAULT_STYLE = "favorite";

    private String name;
    private String iconStyleClass;

    public Favorite() {
        this(null);
    }

    public Favorite(String name) {
        this(name, DEFAULT_STYLE);
    }

    public Favorite(String name, String iconStyleClass) {
        super();
        this.name = name;
        this.iconStyleClass = iconStyleClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconStyleClass() {
        return iconStyleClass;
    }

    public void setIconStyleClass(String iconStyleClass) {
        this.iconStyleClass = iconStyleClass;
    }
}
