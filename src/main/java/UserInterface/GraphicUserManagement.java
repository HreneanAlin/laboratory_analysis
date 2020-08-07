package UserInterface;

import Domain.Entities.Entity;
import Domain.Entities.User;
import Domain.Exceptions.InvalidUserException;
import Service.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * The class that manages CRUD functionality for User entities.
 */
public class GraphicUserManagement extends AbstractManagement {
    public TableView userTable;
    public TextField txtUserName;
    public TextField txtPassword;
    public TextField txtJobTitle;
    public CheckBox checkRegister;
    public CheckBox checkValidate;
    public CheckBox checkIsAdmin;
    UserService userService;

    /**
     * @param userService The service that has functionality for users.
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * the initialisation of the current scene.
     */
    @Override
    @FXML
    protected void initialize() {

        Platform.runLater(() -> {
            entities.clear();
            userTable.refresh();
            entities.addAll(userService.getAll());
            userTable.setItems(entities);
        });
    }

    /**
     * @param actionEvent The event of clicking the "add user" button.
     */
    @Override
    public void addEntity(ActionEvent actionEvent) {
        try {
            String name = txtUserName.getText();
            String password = txtPassword.getText();
            String jobTitle = txtJobTitle.getText();
            boolean register = checkRegister.isSelected();
            boolean validate = checkValidate.isSelected();
            boolean admin = checkIsAdmin.isSelected();
            userService.addUser(name, password, jobTitle, register, validate, admin);
            entities.clear();
            entities.addAll(userService.getAll());
        } catch (InvalidUserException e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * @param mouseEvent the event of selecting a user from the table.
     */
    @Override
    public void showSelected(MouseEvent mouseEvent) {
        TableSelectionModel<Entity> tableSelectionModel = userTable.getSelectionModel();
        User user = (User) tableSelectionModel.getSelectedItem();
        txtUserName.setText(user.getName());
        txtPassword.setText(user.getPassword());
        txtJobTitle.setText(user.getJobTitle());
        checkRegister.setSelected(user.isAcceptToRegister());
        checkValidate.setSelected(user.isAcceptToValidate());
        checkIsAdmin.setSelected(user.isAdmin());

    }

    /**
     * @param actionEvent the event of clicking the "refresh" button
     */
    @Override
    public void refresh(ActionEvent actionEvent) {
        initialize();
    }

    /**
     * @param actionEvent the event of clicking the "delete user" button.
     */
    @Override
    public void deleteEntity(ActionEvent actionEvent) {
        if (Alerts.showQuestion("The user will be deleted!", "Delete?").get()) {
            TableSelectionModel<Entity> tableSelectionModel = userTable.getSelectionModel();
            Entity abstractEntity = tableSelectionModel.getSelectedItem();
            userService.deleteOne(abstractEntity.getId());
            entities.clear();
            userTable.refresh();
            entities.setAll(userService.getAll());
            userTable.setItems(entities);
            txtUserName.clear();
            txtPassword.clear();
            txtJobTitle.clear();
            checkRegister.setSelected(false);
            checkValidate.setSelected(false);
            checkIsAdmin.setSelected(false);
        }
    }

    /**
     * @param actionEvent the event of clicking the "update user" button.
     */
    @Override
    public void updateEntity(ActionEvent actionEvent) {
        try {
            if (Alerts.showQuestion("The user will be Updated!", "Update?").get()) {
                TableSelectionModel<Entity> tableSelectionModel = userTable.getSelectionModel();
                User user = (User) tableSelectionModel.getSelectedItem();
                String name = txtUserName.getText();
                String password = txtPassword.getText();
                String jobTitle = txtJobTitle.getText();
                boolean register = checkRegister.isSelected();
                boolean validate = checkValidate.isSelected();
                boolean admin = checkIsAdmin.isSelected();
                userService.updateUser(user.getId(), name, password, jobTitle, register, validate, admin);
                entities.clear();
                userTable.refresh();
                entities.addAll(userService.getAll());
                userTable.setItems(entities);

            }
        } catch (InvalidUserException e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
