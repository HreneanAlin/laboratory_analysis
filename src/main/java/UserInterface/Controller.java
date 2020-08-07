package UserInterface;

import Domain.Entities.User;
import Service.AnalysisService;
import Service.PatientService;
import Service.SampleService;
import Service.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.bind.annotation.XmlAnyAttribute;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The main menu of the aplication.
 */
public class Controller {
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button btnLogOut;
    public Button btnLogIn;
    public MenuItem menuAddUser;
    public MenuItem menuSamples;
    public MenuItem menuAnalyses;
    public MenuBar menuBar;
    public Button btnRegisterPacients;
    public Button btnEnterResults;
    public Button btnEnterDiagnostic;
    public Button btnSearchPatients;
    public VBox vbox;
    UserService userService;
    SampleService sampleService;
    AnalysisService analysisService;
    PatientService patientService;
    User currentUser;


    /**
     * @param userService     The service that has functionality for users.
     * @param sampleService   The service that has functionality for samples.
     * @param analysisService The service that has functionality for analysis.
     * @param patientService  The service that has functionality for patients.
     */

    public void setSetServices(UserService userService, SampleService sampleService,
                               AnalysisService analysisService, PatientService patientService) {
        this.userService = userService;
        this.sampleService = sampleService;
        this.analysisService = analysisService;
        this.patientService = patientService;
    }

    /**
     * @param actionEvent the event of clicking on the "Log in" button.
     */
    public void logIn(ActionEvent actionEvent) {
        String userName = txtUserName.getText();
        String passWord = txtPassword.getText();
        if (userService.checkLogIn(userName, passWord)) {
            Alerts.showAlert("Password Confirmed", "Wellcome " + userName, Alert.AlertType.INFORMATION);
            currentUser = userService.getUser(userName, passWord);
            btnLogOut.setVisible(currentUser != null);
            txtUserName.setEditable(false);
            txtPassword.setVisible(false);
            btnLogIn.setVisible(false);
            menuBar.setDisable(false);
            menuAddUser.setDisable(!currentUser.isAdmin());
            menuSamples.setDisable(!currentUser.isAdmin());
            menuAnalyses.setDisable(!currentUser.isAdmin());
            btnRegisterPacients.setDisable(currentUser == null);
            btnEnterResults.setDisable(!currentUser.isAdmin() && !currentUser.isAcceptToValidate());
            btnEnterDiagnostic.setDisable(!currentUser.isAdmin() && !currentUser.isAcceptToValidate());
            btnSearchPatients.setDisable(false);

        } else {
            Alerts.showAlert("Error", "Wrong Username or Password", Alert.AlertType.ERROR);

        }

    }

    /**
     * @param actionEvent the event of click on the "Log out" button.
     */

    public void logOut(ActionEvent actionEvent) {
        this.currentUser = null;
        txtUserName.setEditable(true);
        txtPassword.setVisible(true);
        txtUserName.clear();
        txtPassword.clear();
        btnLogIn.setVisible(true);
        btnLogOut.setVisible(false);
        menuBar.setDisable(true);
        btnRegisterPacients.setDisable(true);
        btnEnterResults.setDisable(true);
        btnEnterDiagnostic.setDisable(true);
        btnSearchPatients.setDisable(true);
    }

    /**
     * @param actionEvent the event on clicking the"User management" menu item.
     * @throws IOException in case that GraphicUserManagement.fxml is not found.
     */
    public void goToUserManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicUserManagement.fxml"));
        Parent root = fxmlLoader.load();
        GraphicUserManagement graphicUserManagement = fxmlLoader.getController();
        graphicUserManagement.setUserService(userService);
        Stage stage = new Stage();
        stage.setTitle("Users Management");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * @param actionEvent the event on clicking the"Sample management" menu item.
     * @throws IOException in case that GraphicSamplesManagement.fxml is not found.
     */
    public void goToSamplesManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicSamplesManagement.fxml"));
        Parent root = fxmlLoader.load();
        GraphicSamplesManagement graphicSamplesManagement = fxmlLoader.getController();
        graphicSamplesManagement.setAll(sampleService, currentUser);
        Stage stage = new Stage();
        stage.setTitle("Samples Management");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * @param actionEvent the event on clicking the"analysis management" menu item.
     * @throws IOException in case that GraphicAnalysesManagement.fxml is not found.
     */

    public void goToAnalysesManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicAnalysesManagement.fxml"));
        Parent root = fxmlLoader.load();
        GraphicAnalysesManagement graphicAnalysesManagement = fxmlLoader.getController();
        graphicAnalysesManagement.setAll(analysisService, sampleService, currentUser);
        graphicAnalysesManagement.continueRefresh();
        Stage stage = new Stage();
        stage.setTitle("Analyses Management");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * @param actionEvent the event on clicking the"Patient registration" button.
     * @throws IOException in case that GraphicPatientsManagement.fxml is not found.
     */
    public void goToPacientsManagement(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicPacientsManagement.fxml"));


        Parent root = fxmlLoader.load();
        GraphicPacientsManagement graphicPacientsManagement = fxmlLoader.getController();
        graphicPacientsManagement.setAll(patientService, analysisService, currentUser);
        graphicPacientsManagement.continueRefresh();
        Stage stage = new Stage();
        stage.setTitle("Register Pacients");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * @param actionEvent the event on clicking the"Enter results" button.
     * @throws IOException in case that GraphicResults.fxml is not found.
     */
    public void goToResults(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicResults.fxml"));
        Parent root = fxmlLoader.load();
        GraphicResults graphicResults = fxmlLoader.getController();
        graphicResults.setAll(patientService, currentUser);
        graphicResults.continueRefresh();
        Stage stage = new Stage();
        stage.setTitle("Enter Results");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * @param actionEvent the event on clicking the"Enter diagnostic" button.
     * @throws IOException in case that GraphicDiagnose.fxml is not found.
     */
    public void goToDiagnostic(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicDiagnose.fxml"));
        Parent root = fxmlLoader.load();
        GraphicDiagnose graphicDiagnose = fxmlLoader.getController();
        graphicDiagnose.setAll(patientService, currentUser);
        graphicDiagnose.continueRefresh();
        Stage stage = new Stage();
        stage.setTitle("Enter Diagnostic");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();

    }

    /**
     * @param actionEvent the event on clicking the"Search patients" button.
     * @throws IOException in case that GraphicSearchResults.fxml is not found.
     */

    public void goToSearchPatients(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicSearchResults.fxml"));
        Parent root = fxmlLoader.load();
        GraphicSearchResults graphicSearchResults = fxmlLoader.getController();
        graphicSearchResults.setAll(patientService, currentUser);
        graphicSearchResults.continueRefresh();
        Stage stage = new Stage();
        stage.setTitle("Search Patients");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * @param actionEvent the event on clicking the "Statistic" menu item.
     * @throws IOException in case that GraphicStatistics.fxml is not found.
     */

    public void goToStatistics(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicStatistics.fxml"));
        Parent root = fxmlLoader.load();
        GraphicStatistics graphicStatistics = fxmlLoader.getController();
        graphicStatistics.setAll(patientService);
        Stage stage = new Stage();
        stage.setTitle("Statistics");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * @param actionEvent the event on clicking the "About" menu item.
     */
    public void showAbout(ActionEvent actionEvent) {
        String about = "This aplication was made to demonstrate my OOP Java skills \n" +
                "It should not be used for practical reasons\n" +
                "All rights reserved.\n" +
                "contact:hreneanalin@gmail.com ";
        Alerts.showAlert("About", about, Alert.AlertType.INFORMATION);
    }
}
