package uk.co.senapt.desktop.shell.skins;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import uk.co.senapt.desktop.shell.model.User;
import uk.co.senapt.desktop.shell.UserAvatar;

/**
 * Created by lemmi on 21.08.17.
 */
public class UserAvatarSkin extends SkinBase<UserAvatar> {

    private Label label = new Label();
    private ImageView avatarView = new ImageView();

    public UserAvatarSkin(UserAvatar userAvatar) {
        super(userAvatar);

        Circle circle = new Circle();
        circle.setFill(Color.WHITE);
        circle.centerXProperty().bind(userAvatar.widthProperty().divide(2));
        circle.centerYProperty().bind(userAvatar.heightProperty().divide(2));
        circle.radiusProperty().bind(userAvatar.widthProperty().divide(2));

        userAvatar.setClip(circle);

        avatarView.fitWidthProperty().bind(userAvatar.widthProperty());
        avatarView.setPreserveRatio(true);

        userAvatar.userProperty().addListener(it -> updateView());
        updateView();
    }

    private void updateView() {
        User user = getSkinnable().getUser();
        if (user != null) {
            if (user.getAvatar() != null) {
                getChildren().setAll(avatarView);
                avatarView.setImage(user.getAvatar());
            } else {
                getChildren().setAll(label);
                label.setText(user.getFullName().substring(0,1));
                label.setTextFill(user.getInitialsForeground());
                getSkinnable().setBackground(new Background(new BackgroundFill(user.getInitialsBackground(), new CornerRadii(Double.MAX_VALUE), Insets.EMPTY)));
            }
        }
    }
}
