package UserInterface;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * a class that offers alerts for display.
 */
public class Alerts {
    /**
     * @param title the title of the alert
     * @param text  the text of the alert
     * @param type  the type of the alert
     */
    public static void showAlert(String title, String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("");
        alert.setContentText(text);
        alert.setHeaderText(title);
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(okButton);
        alert.show();
    }

    /**
     * @param title    the title of the alert
     * @param question the question asked.
     * @return whether the "yes" button was clicked as AtomicBoolean value.
     */
    public static AtomicBoolean showQuestion(String title, String question) {
        AtomicBoolean ok = new AtomicBoolean(false);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText(title);
        alert.setContentText(question);
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                ok.set(true);
            }
        });
        return ok;
    }
}
