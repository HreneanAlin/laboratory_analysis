package UserInterface;

import Domain.Entities.Entity;
import Domain.Entities.Patient;
import Domain.Entities.User;
import Service.PatientService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;

import java.io.*;
import java.time.LocalDate;

/**
 * the class used to search in the patient database.
 */
public class GraphicSearchResults {
    public TextField txtCnp;
    public TextField txtFistName;
    public TextField txtLastName;
    public DatePicker datePickerStart;
    public DatePicker datePickerStop;
    public TableView patientTable;
    public TextField txtId;
    public TableColumn colFirstName;
    public TableColumn colLastName;
    private User user;
    private PatientService patientService;
    private ObservableList<Entity> patients = FXCollections.observableArrayList();

    /**
     * @param patientService The service that has functionality for patients.
     * @param user           The current user who uses the aplication.
     */
    public void setAll(PatientService patientService, User user) {
        this.patientService = patientService;
        this.user = user;
    }

    /**
     * the initialisation of the current scene.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            refreshPacients();
            clearAndSetAll();

        });

    }

    /**
     * the refresh of all the Observable Lists and tables.
     */
    private void clearAndSetAll() {
        patients.clear();
        patientTable.refresh();
        ColorChanger.colorFinishPatients(colFirstName);
        ColorChanger.colorFinishPatients(colLastName);

    }

    /**
     * the external call of initialisation
     */
    public void continueRefresh() {
        initialize();
    }

    /**
     * Updates changes made in the analyses Repository to analyses referances from a patient object.
     */
    private void refreshPacients() {
        for (Patient patient : patientService.getAll()) {
            patientService.updateAnalysis(patient);
        }
    }

    /**
     * @param actionEvent the event of clicking on the "search" button.
     */
    public void search(ActionEvent actionEvent) {
        clearAndSetAll();
        Integer id = null;
        if (!txtId.getText().isEmpty()) id = Integer.parseInt(txtId.getText());
        String cnp = txtCnp.getText();
        String firstName = txtFistName.getText();
        String lastName = txtLastName.getText();
        LocalDate start = datePickerStart.getValue();
        LocalDate stop = datePickerStop.getValue();
        if (id != null && !txtId.getText().isEmpty()) {
            patients.addAll(patientService.searchPatients(id, cnp, firstName, lastName, start, stop));

        } else {
            patients.addAll(patientService.searchPatients(-1, cnp, firstName, lastName, start, stop));
        }
        patientTable.setItems(patients);
    }

    /**
     * @param actionEvent the event of clicking on the "view result" button.
     * @throws IOException in case GraphicOneResult.fxml is not found.
     */
    public void goToResult(ActionEvent actionEvent) throws IOException {
        TableSelectionModel<Entity> tableSelectionModel = patientTable.getSelectionModel();
        Patient patient = (Patient) tableSelectionModel.getSelectedItem();
        if (patient != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GraphicOneResult.fxml"));
            Parent root = fxmlLoader.load();
            GraphicOneResult graphicOneResult = fxmlLoader.getController();
            graphicOneResult.setAll(patient, user);
            Stage stage = new Stage();
            stage.setTitle(patient.getLastName() + "" + patient.getFirstName());
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        }else Alerts.showAlert("Error","Please select a patient", Alert.AlertType.ERROR);
    }

    /**
     * Exports patients information as a Jasper Report as PDF.
     *
     * @param actionEvent The event of clicking "export to PDF" button.
     * @throws IOException in case the export cannot be done.
     * @throws JRException in case the export cannot be done.
     */
    public void exportToPdf(ActionEvent actionEvent) throws IOException, JRException {
        TableSelectionModel<Entity> tableSelectionModel = patientTable.getSelectionModel();
        Patient patient = (Patient) tableSelectionModel.getSelectedItem();
        if (patient != null) {
            ExportingJasperReports.exportReportPDF(patient, user);

        }else Alerts.showAlert("Error","Please select a patient", Alert.AlertType.ERROR);
    }


    /**
     * Exports patients information as a Jasper Report to html and opens it in default broswer.
     *
     * @param actionEvent The event of clicking "export to WEB" button.
     * @throws IOException in case the export cannot be done.
     * @throws JRException in case the export cannot be done.
     */

    public void exportToWeb(ActionEvent actionEvent) throws IOException, JRException {
        TableSelectionModel<Entity> tableSelectionModel = patientTable.getSelectionModel();
        Patient patient = (Patient) tableSelectionModel.getSelectedItem();
        if (patient != null) {
            ExportingJasperReports.exportReportWeb(patient, user);
        }else Alerts.showAlert("Error","Please select a patient", Alert.AlertType.ERROR);
    }


}
