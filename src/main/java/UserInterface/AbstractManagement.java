package UserInterface;

import Domain.Entities.Entity;
import Domain.Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * An abstract class that is extendent by the classes which have graphic interactions with the different users,
 * for entities CRUD management
 */
public abstract class AbstractManagement {
    protected ObservableList<Entity> entities = FXCollections.observableArrayList();

    User currentUser;

    /**
     * the initialisation of the current scene
     */

    @FXML
    protected abstract void initialize();

    /**
     * @param actionEvent The event of clicking the "add" button.
     */
    public abstract void addEntity(ActionEvent actionEvent);

    /**
     * @param mouseEvent the event of selecting a entity from the table.
     */
    public abstract void showSelected(MouseEvent mouseEvent);

    /**
     * @param actionEvent the event of clicking the "delete" button.
     */
    public abstract void deleteEntity(ActionEvent actionEvent);

    /**
     * @param actionEvent the event of clicking the "update" button.
     */
    public abstract void updateEntity(ActionEvent actionEvent);

    /**
     * @param actionEvent the event of clicking the "refresh" button
     */
    public abstract void refresh(ActionEvent actionEvent);
}
